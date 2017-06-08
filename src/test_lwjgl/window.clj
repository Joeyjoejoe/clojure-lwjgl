(ns test-lwjgl.window
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL15 GL20 GL30 GLCapabilities GL)))

;; set key callback to stop game
;; (defn key-callback [window key_code scan_code action mode]
;;   (if (and (= key GLFW/GLFW_KEY_ESCAPE) (= action GLFW/GLFW_PRESS))
;;     (GLFW/glfwSetWindowShouldClose window GL11/GL_TRUE)
;;   )
;; )


(defn create [params]
  (let [{:keys [title width height]} params]
    (GLFW/glfwInit)
    (GLFW/glfwDefaultWindowHints)
    (GLFW/glfwWindowHint GLFW/GLFW_VISIBLE GLFW/GLFW_FALSE)
    (GLFW/glfwWindowHint GLFW/GLFW_RESIZABLE GL11/GL_TRUE)
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

;; (slurp "src/test_lwjgl/shaders/default.vert")

(defn shader-compile [source shader-type]
  ;; Load a shader file at source (ex: "~/opengl/shader.vert")
  ;; Create a shader object of shader-type (ex: GL20/GL_VERTEX_SHADER...)
  ;; Compile it
  (let [shader-id  (GL20/glCreateShader shader-type)
        shader-str (slurp source)]

    (GL20/glShaderSource shader-id shader-str)
    (GL20/glCompileShader shader-id)
  )
)

(defn create-vertices-buffer [vertices]
  (let [vertices (float-array vertices)]
    (-> (BufferUtils/createFloatBuffer (count vertices))
      (.put vertices)
      (.flip)
    )
  )
)

(defn vertex-setup [vertices-buffer]
  (let [VBO (GL15/glGenBuffers)
        vertex-id (shader-compile "src/test_lwjgl/shaders/default.vert" GL20/GL_VERTEX_SHADER)
        fragment-id (shader-compile "src/test_lwjgl/shaders/default.frag" GL20/GL_FRAGMENT_SHADER)
        program-id (GL20/glCreateProgram)]
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER VBO) 
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT GL11/GL_FALSE 0 0)
    (GL20/glEnableVertexAttribArray 0)
    (GL20/glAttachShader program-id vertex-id)
    (GL20/glAttachShader program-id fragment-id)
    (GL20/glLinkProgram program-id)
    (GL20/glUseProgram program-id)
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;(def triangle (float-array 
;;                [-0.5 -0.5 0.0
;;                  0.5 -0.5 0.0
;;                  0.0  0.5 0.0]))
;;
;;(def vertices-buffer (.flip (.put (BufferUtils/createFloatBuffer (count triangle)) triangle)))
;;(def vao-id (GL30/glGenVertexArrays))
;;(GL30/glBindVertexArray vao-id)
;;
;;(def vbo-id (GL15/glGenBuffers))
;;(GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo-id)
;;(GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer GL15/GL_STATIC_DRAW)
;;;; Describe data structure for shader
;;(GL15/glVertexAttribPointer 0 3 GL11/GL_FLOAT GL11/GL_FALSE 0 0)
;;(GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)
;;(GL30/glBindVertexArray 0)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn render [w]
  (GL/createCapabilities)
  (GL11/glClearColor (randcc 2) (randcc 2) (randcc 2) 1.0)
  (GL11/glClear GL11/GL_COLOR_BUFFER_BIT)

  (-> [-0.5 -0.5 0.0 0.5 -0.5 0.0 0.0 0.5 0.0]
    (create-vertices-buffer)
    (vertex-setup)
  )

  (GL11/glDrawArrays GL11/GL_TRIANGLES 0 3)
  (GLFW/glfwSwapBuffers w)
  )


