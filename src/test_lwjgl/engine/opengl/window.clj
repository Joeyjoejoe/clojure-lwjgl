(ns test-lwjgl.engine.opengl.window
  (:use [test-lwjgl.engine.utilities.misc])
  (:require [test-lwjgl.engine.opengl.shader-program :as program]
            [test-lwjgl.engine.utilities.transformations :as transformation]
            [test-lwjgl.engine.glfw.controls.keyboard :as keyboard]
	          [clojure.core.matrix :as m]
	          [test-lwjgl.engine.opengl.uniforms :as uniform]
            [test-lwjgl.engine.opengl.textures :as textures]
            [test-lwjgl.engine.opengl.buffers :as buffer]
	          [test-lwjgl.engine.glfw.controls.mouse :as mouse]
	          [test-lwjgl.engine.state.global :as state])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback GLFWErrorCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL13 GL15 GL20 GL30 GL31 GLCapabilities GL)))

(defn initialize [params]
  "Create the game window and set the OpenGl context where everything will be draw"
  (let [{:keys [title width height]} params]
    (GLFW/glfwSetErrorCallback (GLFWErrorCallback/createPrint System/err))

    (when-not (GLFW/glfwInit)
	(throw (IllegalStateException. "Unable to initialize GLFW")))
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GL11/GL_TRUE)

    ;; Cross plateform compatiblity for OpenGL and GLSL (declaration order matters)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GL11/GL_TRUE)

    (GLFW/glfwCreateWindow ^Long width ^Long height ^String title (MemoryUtil/NULL) (MemoryUtil/NULL))))

(defn get-size [window]
  (let [width (buffer/create-int-buffer [0])
        height (buffer/create-int-buffer [0])]
    (GLFW/glfwGetWindowSize window width height)
    {:width (.get width 0) :height (.get height 0)}))

(defn center-cursor [window]
  (let [window-size (get-size window)
        x (/ (:width window-size) 2)
        y (/ (:height window-size) 2)]
    (GLFW/glfwSetCursorPos window x y)))

(defn configure [window]
  (GLFW/glfwMakeContextCurrent window)
  (GLFW/glfwSwapInterval 1)
  ;;  Init keyboard controls
  (GLFW/glfwSetKeyCallback window keyboard/key-callback)
  (GLFW/glfwSetInputMode window GLFW/GLFW_STICKY_KEYS 1)
  ;; Hide mouse cursor and capture its position.
  (center-cursor window)
  (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_DISABLED)
  (GLFW/glfwSetCursorPosCallback window mouse/fps-camera)

  (GL/createCapabilities)
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  (GLFW/glfwShowWindow window)

  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  window)

(defn create [params]
  (let [window (initialize params)]
    (configure window)))

(defn vertex-setup [shape instances]
  "Take a list of vertices (coordinates [x y z] of a pixel and optionaly its color), indices is a list of index from vertices, used to describe triangles."
  (let [vertices (:vertices shape)
        indices (:indices shape)
        vao-id (GL30/glGenVertexArrays)
        _ (GL30/glBindVertexArray vao-id)
	      ;;texture1-id (textures/setup "src/test_lwjgl/assets/textures/container.jpg")
	      ;;texture2-id (textures/setup "src/test_lwjgl/assets/textures/awesomeface.png")
        program-id (program/init)
	      instances-coords (rand-positions instances)
	      points-count (if (= 0 (count indices)) (count vertices) (count indices) )]

    (buffer/create-vbo vertices)
    (buffer/create-pbo instances-coords)
    (if (< 0 (count indices))
        (buffer/create-ebo indices))
    ;; You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
    (GL30/glBindVertexArray 0)

    ;;(GL11/glPolygonMode GL11/GL_FRONT_AND_BACK GL11/GL_LINE)

    ;; Bind Texture to uniform in shader
    (program/bind program-id)
    ;;(GL20/glUniform1i (uniform/get-location program-id, "texture1") 0)
    ;;(GL20/glUniform1i (uniform/get-location program-id, "texture2") 1)

    ;; projection matrix (perspective)
    (GL20/glUniformMatrix4fv (uniform/get-location program-id "projection") false (buffer/create-float-buffer (transformation/make "perspective-projection" [45.0 (/ 1280.0 960.0) 0.1 100.0])))
    (def model-position (uniform/get-location program-id "model"))
    (def view-position (uniform/get-location program-id "view"))

    (program/unbind)

    ;; Return a function with draw code
    (fn []
      (program/bind program-id)
      (GL20/glUniformMatrix4fv view-position false (buffer/create-float-buffer (transformation/make "look-at" [(state/get-data :camera)])))
      ;; Texture
      ;;(GL13/glActiveTexture GL13/GL_TEXTURE0)
      ;;(GL11/glBindTexture GL11/GL_TEXTURE_2D texture1-id)
      ;;(GL13/glActiveTexture GL13/GL_TEXTURE1)
      ;;(GL11/glBindTexture GL11/GL_TEXTURE_2D texture2-id)

      (GL30/glBindVertexArray vao-id)

      (if (> instances 1)
        ;; Draw multiple instances
        (if (= 0 (count indices))
    	    (GL31/glDrawArraysInstanced GL11/GL_TRIANGLES 0 points-count instances) ;; Normal draw
    	    (GL31/glDrawElementsInstanced GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0 instances)) ;; Draw with indices
        ;; Draw single shape
        (if (= 0 (count indices))
    	    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 points-count) ;; Normal draw
          (GL11/glDrawElements GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0)))))) ;; Draw with indices

(defn render [window to-render-functions]
  "Draw everything needed in the GLFW window. to-render-functions is a vector of functions that contains OpenGL instructions to draw shapes from corresponding VAO"
  (GL11/glClearColor 0.5 0.2 0.0 1.0)
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))

  (doseq [f to-render-functions] (f))

  (GLFW/glfwSwapBuffers window)
  (GLFW/glfwPollEvents))
