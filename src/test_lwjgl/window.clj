(ns test-lwjgl.window
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.shader-program :as program]
            [test-lwjgl.config.controls :as controls]
            [test-lwjgl.buffers :as buffer]
            [test-lwjgl.shader :as shader])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL15 GL20 GL30 GLCapabilities GL)))

(defn create [params]
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
      (GLFW/glfwShowWindow window)
    
      (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))

    window)))

(defn vertex-setup [vertices indices]

  ;; TODO should return the number of elements to draw

  (println "Start vertex setup")
  (let [vao-id (GL30/glGenVertexArrays)
        _ (GL30/glBindVertexArray vao-id)
        vertex-id (shader/create "src/test_lwjgl/shaders/default.vert" GL20/GL_VERTEX_SHADER)
        fragment-id (shader/create "src/test_lwjgl/shaders/default.frag" GL20/GL_FRAGMENT_SHADER)
        program-id (program/create)]


    (program/attach-shader program-id vertex-id)
    (program/attach-shader program-id fragment-id)
    (program/link program-id)

    (GL20/glDeleteShader vertex-id)
    (GL20/glDeleteShader fragment-id)

    ;; To remove for production
    (program/validate program-id)

    (buffer/create-vbo vertices)
    (buffer/create-ebo indices)

    ;; You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
    (GL30/glBindVertexArray 0) 

    (def triangle-color (GL20/glGetUniformLocation program-id "uniformColor"))
    ;;(GL11/glPolygonMode GL11/GL_FRONT_AND_BACK GL11/GL_LINE)

    (fn []
      (program/bind program-id)
      (GL30/glBindVertexArray vao-id)
      (GL20/glUniform4f triangle-color 0.0 (Math/sin (GLFW/glfwGetTime)) 0.0 1.0)
      (GL11/glDrawElements GL11/GL_TRIANGLES (count indices) GL11/GL_UNSIGNED_INT 0))))

(defn render [window to-render-functions]

  (GL11/glClearColor 0.0 0.0 0.0 1.0)
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)

  (doseq [f to-render-functions] (f))

  (GLFW/glfwSwapBuffers window)
  (GLFW/glfwPollEvents)
)


