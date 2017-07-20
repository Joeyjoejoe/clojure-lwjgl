(ns test-lwjgl.core
  (:require [test-lwjgl.window :as window]
            [test-lwjgl.images :as images]
            [test-lwjgl.config.controls :as controls]
            [clojure.tools.logging :as log]))
  
;;  Import les classes nécessaires
;;  (import (org.lwjgl.glfw GLFW) (org.lwjgl.system MemoryUtil))
;;
;;  Initialize GLFW pour pouvoir utiliser les classe/methodes
;;  (GLFW/glfwInit)
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;  
;;
;;  Cree une nouvelle fenetre 
;;  (def window (GLFW/glfwCreateWindow 100 100 "dsqds" (MemoryUtil/NULL) (MemoryUtil/NULL)))
;;  (GLFW/glfwSetWindowSize window 900 600)
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;  
;;
;;  Set Current context (window) for opengl
;;  (GLFW/glfwMakeContextCurrent window)
;;  (GLFW/glfwGetCurrentContext)
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Set a mouse pointer
;;  (GLFW/glfwSetCursor window (GLFW/glfwCreateStandardCursor GLFW/GLFW_CROSSHAIR_CURSOR))
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;;  Destroy the window
;;  (GLFW/glfwDestroyWindow window)
(import (org.lwjgl.glfw GLFW GLFWKeyCallback) 
        (org.lwjgl.system MemoryUtil)
        (org.lwjgl.opengl GL11 GL20 GLCapabilities GL))

(defn -main 
  "Start the game"
  [] 
  
  
    ;;  Create window
    (def window (window/create {:width 1280 :height 960 :title "My Shitty Game"}))

    ;;  Init keyboard controls
    (GLFW/glfwSetKeyCallback window controls/key-callback)
    
    (GL/createCapabilities)
    (-> [-0.5 -0.5 0.0 0.5 -0.5 0.0 0.0 0.5 0.0]
      (window/create-vertices-buffer)
      (window/vertex-setup)
    )

    ;;(GL20/glEnableVertexAttribArray 0)



    ;;  Start game loop
    (loop [curr (.getTime (new java.util.Date))
           prev (.getTime (new java.util.Date))
           lag (atom 0.0)]

      (swap! lag #(+ % (- curr prev)))
    
      ;;  (handle-inputs)
      (GLFW/glfwPollEvents)

      ;;  (log/info "previous: " (new java.util.Date prev))
      ;;  (log/info "current: " (new java.util.Date curr))
      ;;  (log/info "elapsed: " (- curr prev))

      (while (>= @lag 0.1)
        ;;  (update)
        (swap! lag #(- % 0.1))
      )

      ;; (render (/ lag 0.1))
      (window/render window)

      (if (zero? (GLFW/glfwWindowShouldClose window))
        (recur (.getTime (new java.util.Date)) curr lag)
      )
    )

    (GLFW/glfwDestroyWindow window)
    (GLFW/glfwTerminate)
  
)
