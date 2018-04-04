(ns test-lwjgl.shader
  (:require [clojure.java.io :as io])
  (:import (org.lwjgl.opengl GL20)))

(defn load-and-compile [file-path shader-type]
  "Compile a shader file and return its id"
  (let [shader-id   (GL20/glCreateShader shader-type)
        file        (io/file (io/resource file-path))
        shader-code (slurp file)]
    (when (= 0 shader-id) (throw (Exception. (str "Error creating shader of type: " shader-type))))
    (GL20/glShaderSource shader-id shader-code)
    (GL20/glCompileShader shader-id)
    (when (= 0 (GL20/glGetShaderi shader-id GL20/GL_COMPILE_STATUS))
      (throw (Exception. (str "Error compiling shader: " (GL20/glGetShaderInfoLog shader-id 1024) " in " file-path))))
    shader-id))

(defn init-defaults []
  (def default-vertex
    (load-and-compile "default.vert" GL20/GL_VERTEX_SHADER))

  (def default-fragment
    (load-and-compile "default.frag" GL20/GL_FRAGMENT_SHADER)))
