(ns test-lwjgl.config.controls
  (:require [clojure.tools.logging :as log]
	          [clojure.core.matrix :as m]
	          [clojure.core.matrix.operators :as mo]
	          [test-lwjgl.state :as state]
            [test-lwjgl.camera :as camera])
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

(defn move-forward [win]
  (let [camera (camera/get-atom)
        cam @camera
        front (:front cam)
        position (:position cam)]
	    (swap! camera assoc :position (mo/+ position (mo/* (state/camera-speed) front)))))

(defn move-backward [win]
  (let [camera (camera/get-atom)
        front (:front @camera)
        position (:position @camera)]
	    (swap! camera assoc :position (mo/- position (mo/* (state/camera-speed) front)))))

(defn move-left [win]
  (let [camera (camera/get-atom)
        front (:front @camera)
        up (:up @camera)
        position (:position @camera)]
	    (swap! camera assoc :position (mo/- position (mo/* (m/normalise (m/cross front up)) (state/camera-speed))))))

(defn move-right [win]
  (let [camera (camera/get-atom)
        front (:front @camera)
        up (:up @camera)
        position (:position @camera)]
	    (swap! camera assoc :position (mo/+ position (mo/* (m/normalise (m/cross front up)) (state/camera-speed))))))

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
      GLFW/GLFW_KEY_W  [default-release move-forward move-forward]
      GLFW/GLFW_KEY_S  [default-release move-backward move-backward]
      GLFW/GLFW_KEY_A  [default-release move-left move-left]
      GLFW/GLFW_KEY_D  [default-release move-right move-right]
  }
)

(def key-callback (proxy [GLFWKeyCallback] []
  (invoke [win key scancode action mods]
    (let [key-actions (key-bindings key)]
      (if (not (nil? key-actions)) 
        ((key-actions action) win))))))
