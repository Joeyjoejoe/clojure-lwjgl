(ns clopengl.opengl.program
  (:import (org.lwjgl.opengl GL20))
  (:require [clopengl.opengl.data.glsl-objects :as data]
            [clopengl.opengl.abstract.interface :as interface]
            [clojure.java.io :as io]))



(defonce build-program data/blank-program)

(defn +shader
  "Creates a shader datastructure and place it in the provided program (if any)."
  ([path stage]
    (-> data/blank-shader
        (assoc :shader/path path)
        (assoc :shader/stage stage)))
  ([program path stage]
   (let [shader (+shader path stage)]
     (update program :program/shaders conj shader))))

(defn +uniform
  "Creates a uniform datastructure with or without value and store it in the provided programe (if any)."
  ([uname vtype]
   (-> data/blank-uniform
       (assoc :uniform/name uname)
       (assoc :uniform/type vtype)))
  ([program uname vtype]
   (let [uniform (+uniform uname vtype)]
     (assoc-in program [:program/uniforms uname] uniform))))

(defn uniform=
  "Set program's uniform `uname` value"
  ([program uname value]
   (assoc-in program [:program/uniforms uname :value] value)))




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




;; DSL usage examples
(def default-prog
  (-> build-program
      (+shader  "shaders/vertices/default.vert" GL20/GL_VERTEX_SHADER)
      (+shader  "shaders/fragments/lightnings/default.frag" GL20/GL_FRAGMENT_SHADER)
      (+uniform "view" "mat4")
      (+uniform "projection" "mat4")
      (+uniform "positionTransformation" "mat4")))

(def light-source
  (-> build-program
      (+shader  "shaders/vertices/light-source.vert" GL20/GL_VERTEX_SHADER)
      (+shader  "shaders/fragments/default.frag" GL20/GL_FRAGMENT_SHADER)
      (+uniform "view" "mat4")
      (+uniform "projection" "mat4")
      (+uniform "positionTransformation" "mat4")))
