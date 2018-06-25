(ns test-lwjgl.engine.opengl.uniforms
  (:import (org.lwjgl.opengl GL20)))

(defn get-location [program-id uniform-name]
  (GL20/glGetUniformLocation ^Long program-id ^String uniform-name))

