(ns test-lwjgl.window
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.shader-program :as program]
            [test-lwjgl.transformations :as transformation]
            [test-lwjgl.config.controls :as controls]
            [test-lwjgl.textures :as textures]
            [test-lwjgl.buffers :as buffer]
            [test-lwjgl.shader :as shader])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL13 GL15 GL20 GL30 GLCapabilities GL)))


(defn create [params]
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

    (let [window (GLFW/glfwCreateWindow width height title (MemoryUtil/NULL) (MemoryUtil/NULL))]
      (GLFW/glfwSwapInterval 1)
      (GLFW/glfwMakeContextCurrent window)
      ;;  Init keyboard controls
      (GLFW/glfwSetKeyCallback window controls/key-callback)
      (GL/createCapabilities)
      (GL11/glEnable GL11/GL_DEPTH_TEST)
      (GLFW/glfwShowWindow window)
    
      (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
      window)))


(defn vertex-setup [vertices indices]
  "Take a list of vertices (coordinates [x y z] of a pixel and optionaly its color), indices is a list of index from vertices, used to describe triangles."
  (let [vao-id (GL30/glGenVertexArrays)
        _ (GL30/glBindVertexArray vao-id)
        vertex-id (shader/create "src/test_lwjgl/shaders/default.vert" GL20/GL_VERTEX_SHADER)
        fragment-id (shader/create "src/test_lwjgl/shaders/default.frag" GL20/GL_FRAGMENT_SHADER)
	texture1-id (textures/setup "src/test_lwjgl/assets/textures/container.jpg")
	texture2-id (textures/setup "src/test_lwjgl/assets/textures/awesomeface.png")
        program-id (program/create)
	points-count (if (= 0 (count indices)) (count vertices) (count indices) )]

    (program/attach-shader program-id vertex-id)
    (program/attach-shader program-id fragment-id)
    (program/link program-id)


    (GL20/glDeleteShader vertex-id)
    (GL20/glDeleteShader fragment-id)

    ;; To remove for production
    (program/validate program-id)

    (buffer/create-vbo vertices)
    
    (if (< 0 (count indices))
    (buffer/create-ebo indices))
    ;; You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
    (GL30/glBindVertexArray 0) 

    (def triangle-color (GL20/glGetUniformLocation program-id "uniformColor"))
    ;;(GL11/glPolygonMode GL11/GL_FRONT_AND_BACK GL11/GL_LINE)

    ;; Bind Texture to uniform in shader
    (program/bind program-id)
    (GL20/glUniform1i (GL20/glGetUniformLocation program-id, "texture1") 0)
    (GL20/glUniform1i (GL20/glGetUniformLocation program-id, "texture2") 1)
    (program/unbind)

    (fn [uniform-rotate]
	
      (program/bind program-id)
      
(println "fdsfdsfds")
      (GL20/glUniformMatrix4fv (GL20/glGetUniformLocation program-id "rotate") false (buffer/create-float-buffer uniform-rotate))
      ;; view matrix
      (GL20/glUniformMatrix4fv (GL20/glGetUniformLocation program-id "view") false (buffer/create-float-buffer (transformation/make "translate-matrix" [0.0 0.0 -3.0])))
      ;; projection matrix (perspective)	
      (GL20/glUniformMatrix4fv (GL20/glGetUniformLocation program-id "projection") false (buffer/create-float-buffer (transformation/make "perspective-projection" [45.0 (/ 1280.0 960.0) 0.1 100.0])))


      ;; Texture
      (GL13/glActiveTexture GL13/GL_TEXTURE0)
      (GL11/glBindTexture GL11/GL_TEXTURE_2D texture1-id)

      (GL13/glActiveTexture GL13/GL_TEXTURE1)
      (GL11/glBindTexture GL11/GL_TEXTURE_2D texture2-id)


      (GL30/glBindVertexArray vao-id)
      (GL20/glUniform4f triangle-color 0.0 (Math/sin (GLFW/glfwGetTime)) 0.0 1.0)

      ;; model matrix
 (doseq [t [[ 0.0  0.0  0.0 ] 
 [ 2.0  5.0 -15.0]
 [-1.5 -2.2 -2.5 ]
 [-3.8 -2.0 -12.3]
 [ 2.4 -0.4 -3.5 ] 
 [-1.7  3.0 -7.5 ] 
 [ 1.3 -2.0 -2.5 ] 
 [ 1.5  2.0 -2.5 ] 
 [ 1.5  0.2 -1.5 ] 
 [-1.3  1.0 -1.5 ]]] 
(println t)
(println "-----")
      (GL20/glUniformMatrix4fv (GL20/glGetUniformLocation program-id "model") false (buffer/create-float-buffer (clojure.core.matrix/as-vector (clojure.core.matrix/mmul (transformation/make "translate-matrix" t true) (transformation/make "rotate-x" [55.0] true)))))

      (if (= 0 (count indices))
	;; Draw points without indices
	(GL11/glDrawArrays GL11/GL_TRIANGLES 0 points-count)

	;; Draw with indices
	(GL11/glDrawElements GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0))
)
)))

(defn render [window to-render-functions]
  "Draw everything needed in the GLFW window. to-render-functions is a vector of functions that contains OpenGL instructions to draw shapes from corresponding VAO"


  (GL11/glClearColor 0.0 0.0 0.0 1.0)
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))

  (doseq [f to-render-functions] (f (transformation/make "rotate-z" [(* 25 (GLFW/glfwGetTime))])))

  (GLFW/glfwSwapBuffers window)
  (GLFW/glfwPollEvents))


