(ns test-lwjgl.window
  (:require [test-lwjgl.shader-program :as program]
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
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_PROFILE GLFW/GLFW_OPENGL_CORE_PROFILE)
    (GLFW/glfwWindowHint GLFW/GLFW_OPENGL_FORWARD_COMPAT GL11/GL_TRUE)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MAJOR 3)
    (GLFW/glfwWindowHint GLFW/GLFW_CONTEXT_VERSION_MINOR 2)
    (def window (GLFW/glfwCreateWindow width height title (MemoryUtil/NULL) (MemoryUtil/NULL)))
    (GLFW/glfwSwapInterval 1)
    (GLFW/glfwMakeContextCurrent window)
    (GLFW/glfwShowWindow window)
    window
  )
)

(defn randcc [n]
  (float (rand-int n))
)

(defn vertex-setup [vertices-buffer globals]
  (let [vao-id (GL30/glBindVertexArray (GL30/glGenVertexArrays))
      ;;  vco (GL15/glGenBuffers)
     ;;   color (create-vertices-buffer [0.5 0.5 0.5])
        vertex-id (shader/create "src/test_lwjgl/shaders/default.vert" GL20/GL_VERTEX_SHADER)
        fragment-id (shader/create "src/test_lwjgl/shaders/default.frag" GL20/GL_FRAGMENT_SHADER)
        program-id (program/create)
        ]
    (println (str "program:" program-id))
    (println (str "vert:" vertex-id))
    (println (str "frag:" fragment-id))
    (println vertices-buffer)

    (buffer/vertex-buffer-object vertices-buffer)
    ;; Vert
    ;;(GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0) 
    ;; Frag
   ;; (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vco) 
   ;; (GL15/glBufferData GL15/GL_ARRAY_BUFFER color GL15/GL_STATIC_DRAW)
   ;; (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT false 0 0)
   ;; (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0) 

   


    (program/attach-shader program-id vertex-id)
    (program/attach-shader program-id fragment-id)

    (program/link program-id)
   ;; (program/detach-shader vertex-id program-id)
   ;; (program/detach-shader fragment-id program-id)

    ;; To remove for production
    (program/validate program-id)

   ;; (GL20/glDeleteShader vertex-id)
   ;; (GL20/glDeleteShader fragment-id)

    (program/bind program-id)

    (GL20/glEnableVertexAttribArray 0)
    (def triangle-color (GL20/glGetUniformLocation program-id "triangleColor"))
    (swap! globals assoc :program-id program-id :triangle-color triangle-color)
  )
)

(defn render [w globals]

  (GL11/glClearColor 0.0 0.0 0.0 1.0)
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)

  (GL20/glUniform3f (:triangle-color @globals) (randcc 2) (randcc 2) (randcc 2))
  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 6)

  (GLFW/glfwSwapBuffers w)
)


