(ns clopengl.engine.opengl.window
  (:use [clopengl.engine.utilities.misc])
  (:require [clopengl.engine.glfw.controls.keyboard :as keyboard]
	          [clojure.core.matrix :as m]
            [clopengl.engine.opengl.buffers :as buffer]
            [clopengl.engine.utilities.transformations :as transformation]
            [clopengl.engine.state.global :as state]
	          [clopengl.engine.glfw.controls.mouse :as mouse])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback GLFWErrorCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL)))

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
  "Return the window dimensions"
  (let [width (buffer/create-int-buffer [0])
        height (buffer/create-int-buffer [0])]
    (GLFW/glfwGetWindowSize window width height)
    {:width (.get width 0) :height (.get height 0)}))

(defn get-center [window]
  "Return the coordinates of window center"
  (let [size (get-size window)
        x (/ (:width size) 2.0)
        y (/ (:height size) 2.0)]
    {:x x :y y}))

(defn configure [window]
  (GLFW/glfwMakeContextCurrent window)
  (GLFW/glfwSwapInterval 1)
  ;;  Init keyboard controls
  (GLFW/glfwSetKeyCallback window keyboard/key-callback)
  (GLFW/glfwSetInputMode window GLFW/GLFW_STICKY_KEYS 1)
  ;; Hide mouse cursor and capture its position.
  (mouse/center window (get-center window))
  (GLFW/glfwSetInputMode window GLFW/GLFW_CURSOR GLFW/GLFW_CURSOR_DISABLED)
  (GLFW/glfwSetCursorPosCallback window mouse/fps-camera)

  (GL/createCapabilities)
  (GL11/glEnable GL11/GL_DEPTH_TEST)
  (GLFW/glfwShowWindow window)

  (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))
  window)

(defn create [params]
  (let [window (initialize params)]
    (configure window)
    (swap! (state/get-atom) assoc :window window)
    window))


(defn render [window to-render-functions]
  "Draw everything needed in the GLFW window. to-render-functions is a vector of functions that contains OpenGL instructions to draw shapes from corresponding VAO"
  (let [cam (buffer/create-float-buffer (transformation/make "look-at" [(state/get-data :camera)]))
        cam-position (buffer/create-float-buffer (state/cam :front))]
  (GL11/glClearColor 0.0 0.0 0.0 1.0)
  (GL11/glClear (bit-or GL11/GL_COLOR_BUFFER_BIT GL11/GL_DEPTH_BUFFER_BIT))
  (doseq [f to-render-functions] (f cam cam-position))

  (GLFW/glfwSwapBuffers window)
  (GLFW/glfwPollEvents)))
