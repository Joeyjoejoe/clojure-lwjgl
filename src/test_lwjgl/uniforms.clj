(ns test-lwjgl.uniforms
  (:import (org.lwjgl.opengl GL20)))

(defn get-location [program-id uniform-name]
  (GL20/glGetUniformLocation program-id uniform-name))

