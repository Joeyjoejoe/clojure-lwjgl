(ns test-lwjgl.window
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.shader-program :as program]
            [test-lwjgl.transformations :as transformation]
            [test-lwjgl.config.controls :as controls]
	          [clojure.core.matrix :as m]
	          [test-lwjgl.uniforms :as uniform]
            [test-lwjgl.textures :as textures]
            [test-lwjgl.buffers :as buffer]
            [test-lwjgl.camera :as camera]
            [test-lwjgl.shader :as shader])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL13 GL15 GL20 GL30 GL31 GLCapabilities GL)))

(defn initialize [params]
  "Create the game window and set the OpenGl context where everything will be draw"
  (let [{:keys [title width height]} params]
    (GLFW/glfwInit)
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GL11/GL_TRUE)

    ;; Cross plateform compatiblity for OpenGL and GLSL (declaration order matters)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GL11/GL_TRUE)

    (GLFW/glfwCreateWindow ^Long width ^Long height ^String title (MemoryUtil/NULL) (MemoryUtil/NULL))))

(defn configure [window]
  (GLFW/glfwSwapInterval 1)
  (GLFW/glfwMakeContextCurrent window)
  ;;  Init keyboard controls
  (GLFW/glfwSetKeyCallback window controls/key-callback)
  (GLFW/glfwSetInputMode window GLFW/GLFW_STICKY_KEYS 1)
  (GL/createCapabilities)
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  (GLFW/glfwShowWindow window)

  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  window)

(defn create [params]
  (let [window (initialize params)]
    (configure window)))

(defn vertex-setup [vertices indices]
  "Take a list of vertices (coordinates [x y z] of a pixel and optionaly its color), indices is a list of index from vertices, used to describe triangles."
  (let [vao-id (GL30/glGenVertexArrays)
        _ (GL30/glBindVertexArray vao-id)
        vertex-id (shader/create "src/test_lwjgl/shaders/default.vert" GL20/GL_VERTEX_SHADER)
        fragment-id (shader/create "src/test_lwjgl/shaders/default.frag" GL20/GL_FRAGMENT_SHADER)
	      texture1-id (textures/setup "src/test_lwjgl/assets/textures/container.jpg")
	      texture2-id (textures/setup "src/test_lwjgl/assets/textures/awesomeface.png")
        program-id (program/create)
	      cubes-pos (map #(transformation/make "translate-matrix" %) (map (fn [x] (vector (+ (rand -15) (rand 15)) (+ (rand -15) (rand 15)) (+ (rand -15) (rand 15)))) (vec (repeat 400 nil))))
	      points-count (if (= 0 (count indices)) (count vertices) (count indices) )]

    (program/attach-shader program-id vertex-id)
    (program/attach-shader program-id fragment-id)
    (program/link program-id)


    (GL20/glDeleteShader vertex-id)
    (GL20/glDeleteShader fragment-id)

    ;; To remove for production
    (program/validate program-id)

    (buffer/create-vbo vertices)
    (buffer/create-pbo cubes-pos)
    
    (if (< 0 (count indices))
    (buffer/create-ebo indices))
    ;; You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
    (GL30/glBindVertexArray 0) 

    (def triangle-color (uniform/get-location program-id "uniformColor"))
    ;;(GL11/glPolygonMode GL11/GL_FRONT_AND_BACK GL11/GL_LINE)

    ;; Bind Texture to uniform in shader
    (program/bind program-id)
    (GL20/glUniform1i (uniform/get-location program-id, "texture1") 0)
    (GL20/glUniform1i (uniform/get-location program-id, "texture2") 1)


    ;; projection matrix (perspective)	
    (GL20/glUniformMatrix4fv (uniform/get-location program-id "projection") false (buffer/create-float-buffer (transformation/make "perspective-projection" [45.0 (/ 1280.0 960.0) 0.1 100.0])))
    (def model-position (uniform/get-location program-id "model"))
    (def view-position (uniform/get-location program-id "view"))


    (program/unbind)

    (fn []
	
      (program/bind program-id)
      
      ;;(GL20/glUniformMatrix4fv (uniform/get-location program-id "rotate") false (buffer/create-float-buffer uniform-rotate))
      ;; view matrix
      ;;(GL20/glUniformMatrix4fv (uniform/get-location program-id "view") false (buffer/create-float-buffer (transformation/make "translate-matrix" [0.0 0.0 -3.0])))

    ;;  (let [radius 10.0
	  ;;  camX (* (Math/sin (GLFW/glfwGetTime)) radius)
	  ;;  camZ (* (Math/cos (GLFW/glfwGetTime)) radius)]
	    ;;(swap! (camera/get-atom) assoc :position [camX 0.0 camZ]))
      (GL20/glUniformMatrix4fv view-position false (buffer/create-float-buffer (transformation/make "look-at" [(camera/get-raw)])))

      ;; Texture
      (GL13/glActiveTexture GL13/GL_TEXTURE0)
      (GL11/glBindTexture GL11/GL_TEXTURE_2D texture1-id)

      (GL13/glActiveTexture GL13/GL_TEXTURE1)
      (GL11/glBindTexture GL11/GL_TEXTURE_2D texture2-id)


      (GL30/glBindVertexArray vao-id)
      ;;(GL20/glUniform4f triangle-color 0.0 (Math/sin (GLFW/glfwGetTime)) 0.0 1.0)

	(GL31/glDrawArraysInstanced GL11/GL_TRIANGLES 0 points-count 400)

  ;; (if (= 0 (count indices))
	;; Draw points without indices
	;;     (GL11/glDrawArrays GL11/GL_TRIANGLES 0 points-count)
	;; Draw with indices
  ;;     (GL11/glDrawElements GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0))
)))

(defn render [window to-render-functions]
  "Draw everything needed in the GLFW window. to-render-functions is a vector of functions that contains OpenGL instructions to draw shapes from corresponding VAO"


  (GL11/glClearColor 0.0 0.0 0.0 1.0)
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))

  (doseq [f to-render-functions] (f))

  (GLFW/glfwSwapBuffers window)
  (GLFW/glfwPollEvents)
  )
