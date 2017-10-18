(ns test-lwjgl.core
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.window :as window]
	    [clojure.core.matrix :as m]
	    [clojure.core.matrix.operators :as mo]
            [test-lwjgl.buffers :as buffer]
            [test-lwjgl.transformations :as transformation]
            [test-lwjgl.camera :as camera]
            [clojure.tools.logging :as log])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback) 
	   (org.lwjgl BufferUtils)
           (org.lwjgl.system MemoryUtil)
           (org.lwjgl.opengl GL11 GL20 GLCapabilities GL))
  (:gen-class))

(defn -main [] 
  "Start the game"


  (def window (window/create {:width 1280 :height 960 :title "My Shitty Game"}))



  (def init (window/vertex-setup 
;;    [{:coordinates [-0.5 -0.5 0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
;;     {:coordinates [0.5 -0.5 0.5] :color [0.0 1.0 0.0] :texture [1.0 0.0]}
;;     {:coordinates [-0.5 0.5 0.5] :color [0.0 1.0 0.0] :texture [0.0 1.0]}
;;     {:coordinates [0.5 0.5 0.5] :color [0.0 0.0 1.0] :texture [1.0 1.0]}
;;
;;     {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
;;     {:coordinates [0.5 -0.5 -0.5] :color [0.0 1.0 0.0] :texture [1.0 0.0]}
;;     {:coordinates [-0.5 0.5 -0.5] :color [0.0 1.0 0.0] :texture [0.0 1.0]}
;;     {:coordinates [0.5 0.5 -0.5] :color [0.0 0.0 1.0] :texture [1.0 1.0]}
;;]
;;		
;;		[3 2 1 ;; Front face
;;		 0 1 2
;;
;;		 7 6 5 ;; Back face
;;		 4 5 6
;;
;;		 2 6 0 ;; Left face
;;		 0 4 6
;;
;;		 3 7 5 ;; Right face
;;		 1 5 3
;;
;;		 2 6 3 ;; Top face
;;		 7 6 3
;;
;;		 0 1 4 ;; Bottom face
;;		 5 1 4]

		
		[
       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 

       {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 

       {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 

       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 

       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 

       {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
       {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]} 
       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]} 
       {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]} 
       {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]} 
] 
[]
))

(def fps (atom [0 0]))
  ;;  Start game loop
  (loop [to-render-functions [init]
         curr (GLFW/glfwGetTime)
         prev (GLFW/glfwGetTime)
         lag (atom 0.0)]

    (swap! lag #(+ % (- curr prev)))
  
    ;;  (handle-inputs)

    ;;  (log/info "previous: " (new java.util.Date prev))
    ;;  (log/info "current: " (new java.util.Date curr))
    ;;  (log/info "elapsed: " (- curr prev))

    (while (>= @lag 0.1)
      ;;  (update)
      (swap! lag #(- % 0.1)))

    ;; (render (/ lag 0.1))
    (window/render window to-render-functions)

    
    (if (>= (- (GLFW/glfwGetTime) (@fps 1)) 1.0)
	(do (println (str "FPS: " (+ 1 (@fps 0))))
	    (swap! fps update-in [1] inc)
	    (swap! fps assoc 0 0))
 	(swap! fps update-in [0] inc))

    (if (zero? (GLFW/glfwWindowShouldClose window))
      (recur to-render-functions (GLFW/glfwGetTime) curr lag)))

  (GLFW/glfwDestroyWindow window)
  (GLFW/glfwTerminate))
