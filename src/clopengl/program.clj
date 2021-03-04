(ns clopengl.program
  (:import (org.lwjgl.opengl GL20))
  (:require [clopengl.data.glsl-objects :as data]
            [clopengl.interpret.glsl-objects :as _]
            [clojure.java.io :as io]))

(defonce build-program data/blank-program)

(defn +shader
  "Creates a shader datastructure and place it in the provided program (if any)."
  ([path stage]
    (let [shader-source (-> path (io/resource) (io/file) (slurp))
          u (re-seq #"uniform\s+(.+?)\s+(.+);" shader-source)
          a (re-seq #"layout.+?(\d).+?in\s+(.+?)\s+(.+?);" shader-source)
          uniforms (reduce #(assoc %1 (nth %2 2) (nth %2 1)) {} u)
          attribs (reduce #(assoc %1 (nth %2 3) {:location (nth %2 1) :type (nth %2 2)}) {} a)]
      (-> data/blank-shader
        (assoc :shader/path path)
        (assoc :shader/stage stage)
        (assoc :shader/attribs attribs)
        (assoc :shader/uniforms uniforms))))
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
