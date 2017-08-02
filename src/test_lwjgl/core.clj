(ns test-lwjgl.core
  (:require [test-lwjgl.window :as window]
            [test-lwjgl.config.controls :as controls]
            [test-lwjgl.buffers :as buffer]
            [clojure.tools.logging :as log])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback) 
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL20 GLCapabilities GL)))

(defn -main 
  "Start the game"
  [] 
  
   (def globals (atom {:program-id 0 :triangle-color 0 :vao-id 0 :indices-count 0}))
    ;;  Create window
    (def window (window/create {:width 1280 :height 960 :title "My Shitty Game"}))

    ;;  Init keyboard controls
    (GLFW/glfwSetKeyCallback window controls/key-callback)
    
    (GL/createCapabilities)
    (println "OpenGL version:" (GL11/glGetString GL11/GL_VERSION))

    (window/vertex-setup [{:coordinates [-0.5 -0.5 1.0]}
                           {:coordinates [0.5 -0.5 1.0]} 
                           {:coordinates [-0.5 0.5 1.0]} 
                           {:coordinates [0.5 0.5 1.0]}
                           {:coordinates [0.8 0.0 1.0]}] 
			   
			 [0 1 2
			  2 3 1
			  1 3 4] globals)

    ;;  Start game loop
    (loop [curr (.getTime (new java.util.Date))
           prev (.getTime (new java.util.Date))
           lag (atom 0.0)]

      (swap! lag #(+ % (- curr prev)))
    
      ;;  (handle-inputs)

      ;;  (log/info "previous: " (new java.util.Date prev))
      ;;  (log/info "current: " (new java.util.Date curr))
      ;;  (log/info "elapsed: " (- curr prev))

      (while (>= @lag 0.1)
        ;;  (update)
        (swap! lag #(- % 0.1))
      )

      ;; (render (/ lag 0.1))
      (window/render window globals)

      (if (zero? (GLFW/glfwWindowShouldClose window))
        (recur (.getTime (new java.util.Date)) curr lag)
      )
    )

    (GLFW/glfwDestroyWindow window)
    (GLFW/glfwTerminate)
  
)
