(ns clopengl.engine.opengl.shaders.program
  (:require [clopengl.engine.opengl.shaders.shader :as shader])
  (:import (org.lwjgl.opengl GL20)))

(defn create []
  (let [program-id (GL20/glCreateProgram)]
    (cond (= 0 program-id)
      (throw (Exception. "Could not create shader program"))
      :else program-id)))

(defn attach-shader [program-id shader-id]
  (GL20/glAttachShader program-id shader-id))

(defn link [program-id]
  (GL20/glLinkProgram program-id)
  (when (= 0 (GL20/glGetProgrami program-id GL20/GL_LINK_STATUS))
    (throw (Exception. (str "Error linking shader to program: " (GL20/glGetProgramInfoLog program-id 1024))))))

(defn detach-shader [program-id shader-id]
  (when (= 0 shader-id)
    (throw (Exception. (str "Shader " shader-id " does not exist"))))
  (GL20/glDetachShader program-id shader-id))

(defn validate [program-id]
  "Debug concerns, do not use it in production"
  (GL20/glValidateProgram program-id)
  (when (= 0 (GL20/glGetProgrami program-id GL20/GL_VALIDATE_STATUS))
    (throw (Exception. (str "Error creating shader of type: " (GL20/glGetProgramInfoLog program-id 1024))))))

(defn bind [program-id]
  (GL20/glUseProgram program-id))

(defn unbind []
  (bind 0))

(defn cleanup [program-id]
  (unbind)
  (when (not= 0 program-id)
    (GL20/glDeleteProgram program-id)))

(defn defprogram [vertex-shader fragment-shader]
  (let [program-id (create)
        vertex-shader-id (shader/add-vertex vertex-shader)
        fragment-shader-id (shader/add-fragment fragment-shader)]
    (attach-shader program-id vertex-shader-id)
    (attach-shader program-id fragment-shader-id)
    (link program-id)
    (GL20/glDeleteShader vertex-shader-id)
    (GL20/glDeleteShader fragment-shader-id)
    ;; To remove for production
    ;;(validate program-id)

    program-id))

