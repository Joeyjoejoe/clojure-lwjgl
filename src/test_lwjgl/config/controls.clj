(ns test-lwjgl.config.controls
  (:require [clojure.tools.logging :as log])
  (:import (org.lwjgl.glfw GLFW GLFWKeyCallback)
           (org.lwjgl.opengl GL11)))

(defn default-release [win]
  (log/info "key released") 
)

(defn default-press [win]
  (log/info "key pressed") 
)

(defn default-repeat [win]
  (log/info "key repeated") 
)

(defn close-window [win]
  (GLFW/glfwSetWindowShouldClose win GL11/GL_TRUE)
)

(def key-bindings
  "Bind key codes with a vector of three actions indexed as default key events:
  press, released, repeat"
  ;;  key-bindings is a map of key codes associated with a 3 element vector.
  ;;  whose indexes represent the key actions:
  ;;
  ;;  key_code: integer     0: GLWF_RELEASE  1: GLWF_PRESS  2: GLFW_REPEAT 
  {
      GLFW/GLFW_KEY_ESCAPE [default-release close-window  default-repeat]
      GLFW/GLFW_KEY_ENTER  [default-release default-press default-repeat]
  }
)

(def key-callback (proxy [GLFWKeyCallback] []
  (invoke [win key scancode action mods]
    (let [key-actions (key-bindings key)]
      (if (not (nil? key-actions)) ((key-actions action) win))
    )
  )                 
))
