(ns clopengl.interpret.glsl-objects
  (:import (org.lwjgl.opengl GL20))
  (:require [clopengl.interpret.interface :as interface]
            [clojure.java.io :as io]))

(defmethod interface/data->opengl! :glsl/program
  [_ h & _]
  (let [program-id (GL20/glCreateProgram)
        shader-ids (map #(interface/data->opengl! :glsl/shader %) (:program/shaders h))
                   ;; Attach compiled shaders code to program
        _          (doseq [sid shader-ids] (GL20/glAttachShader program-id sid))
                   ;; Link program
        _          (doseq []
                     (GL20/glLinkProgram program-id)
                     (when (= 0 (GL20/glGetProgrami program-id GL20/GL_LINK_STATUS))
                       (throw (Exception. (str
                                            "Error linking shader to program: "
                                            (GL20/glGetProgramInfoLog program-id 1024))))))
                   ;; Get uniforms locations
        uniforms   (into {} (map
                              (fn [[k v]] [k (interface/data->opengl! :glsl/uniform v program-id)])
                              (:program/uniforms h)))]
    ;; Delete shaders
    (doseq [sid shader-ids]
      (GL20/glDeleteShader sid))

    (-> h
        (assoc :program/id program-id)
        (assoc :program/uniforms uniforms))))

(defmethod interface/data->opengl! :glsl/shader
  [_ h & _]
  (let [stage (:shader/stage h)
        path (:shader/path h)
        id    (GL20/glCreateShader stage)
        code  (-> path (io/resource) (io/file) (slurp))]
    (when (= 0 id)
      (throw (Exception. (str "Error creating shader of type: " stage))))
    (GL20/glShaderSource id code)
    (GL20/glCompileShader id)
    (when (= 0 (GL20/glGetShaderi id GL20/GL_COMPILE_STATUS))
      (throw (Exception. (str "Error compiling shader: " (GL20/glGetShaderInfoLog id 1024) " in " path))))
    id))

(defmethod interface/data->opengl! :glsl/uniform
  [_ h & opts]
  (let [uname      (:uniform/name h)
        program-id (first opts)
        location   (GL20/glGetUniformLocation ^Long program-id ^String uname)]
    (assoc h :uniform/location location)))
