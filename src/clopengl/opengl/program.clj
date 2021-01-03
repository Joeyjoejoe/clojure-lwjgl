(ns clopengl.opengl.program
  (:import (org.lwjgl.opengl GL20))
  (:require [clopengl.opengl.data.glsl-objects :refer :all]
            [clopengl.opengl.abstract.interface :as interface]
            [clojure.java.io :as io]))


;;{
;;  :id nil,
;;  :type :program,
;;  :uniforms {
;;    "project" {
;;      :name "project",
;;      :type "Matrix4f",
;;      :value [[0 0 0 0] [0 0 0 0] [0 0 0 0] [0 0 0 0]],
;;      :location nil
;;    }
;;  },
;;  :shaders [
;;    {:path "path.vert", :type :shader, :stage "GL20/GL_VERTEX_SHADER"}
;;    {:path "path.frag", :type :shader, :stage "GL20/GL_FRAGMENT_SHADER"}
;;  ]
;;}

(def default-prog
  (-> (program)
      (+shader  "shaders/vertices/default.vert" GL20/GL_VERTEX_SHADER)
      (+shader  "shaders/fragments/lightnings/default.frag" GL20/GL_FRAGMENT_SHADER)
      (+uniform "view" "mat4")
      (+uniform "projection" "mat4")
      (+uniform "positionTransformation" "mat4")))

(def light-source
  (-> (program)
      (+shader  "shaders/vertices/light-source.vert" GL20/GL_VERTEX_SHADER)
      (+shader  "shaders/fragments/default.frag" GL20/GL_FRAGMENT_SHADER)
      (+uniform "view" "mat4")
      (+uniform "projection" "mat4")
      (+uniform "positionTransformation" "mat4")))

(defmethod interface/data->opengl! :program
  [h & _]
  (let [program-id (GL20/glCreateProgram)
        shader-ids (map interface/data->opengl! (:shaders h))
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
                              (fn [[k v]] [k (interface/data->opengl! v program-id)])
                              (:uniforms h)))]
    ;; Delete shaders
    (doseq [sid shader-ids]
      (GL20/glDeleteShader sid))

    (-> h
        (assoc :id program-id)
        (assoc :uniforms uniforms))))

(defmethod interface/data->opengl! :shader
  [h & _]
  (let [stage (:stage h)
        path (:path h)
        id    (GL20/glCreateShader stage)
        code  (-> path (io/resource) (io/file) (slurp))]
    (when (= 0 id)
      (throw (Exception. (str "Error creating shader of type: " stage))))
    (GL20/glShaderSource id code)
    (GL20/glCompileShader id)
    (when (= 0 (GL20/glGetShaderi id GL20/GL_COMPILE_STATUS))
      (throw (Exception. (str "Error compiling shader: " (GL20/glGetShaderInfoLog id 1024) " in " path))))
    id))

(defmethod interface/data->opengl! :uniform
  [h & opts]
  (let [uname      (:name h)
        program-id (first opts)
        location   (GL20/glGetUniformLocation ^Long program-id ^String uname)]
    (assoc h :location location)))
