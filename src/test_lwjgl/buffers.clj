(ns test-lwjgl.buffers
  (:use [test-lwjgl.utility])
  (:require [test-lwjgl.utility :as program])
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.opengl GL11 GL15 GL20)))


(defn create-float-buffer [datas]
  "Create an float array buffer from datas"
  (let [datas (float-array datas)]
    (-> (BufferUtils/createFloatBuffer (count datas))
      (.put datas)
      (.flip))))

(defn create-int-buffer [datas]
  "Create an integer array buffer from datas"
  (let [datas (int-array datas)]
    (-> (BufferUtils/createIntBuffer (count datas))
      (.put datas)
      (.flip))))


(defn create-vbo [datas]
   (let [coordinates (vec (mapcat #(select-values %1 [:coordinates]) datas))
         colors (vec (mapcat #(select-values % [:color]) datas))
         size 3
         stride (* (+ size (count colors)) java.lang.Float/BYTES)
         vertex-position 0
         offset 0
         data-type GL11/GL_FLOAT
         normalize-datas? false
         formated-datas (vec (mapcat concat coordinates colors))
         vertices-buffer (create-float-buffer formated-datas)
         vbo-id (GL15/glGenBuffers)]

    (println (str "Handling data..."))
    (println vbo-id)
    (println (str "size: " size))
    (println (str "stride: " stride))
    (println (str "vertex-position: " vertex-position))
    (println (str "offset: " offset))
    (println (str "data-type: " data-type))
    (println (str "normalize-datas? :" normalize-datas?))
    (println (str "formated-datas :" formated-datas))
    (println (str "vertices-buffer :" vertices-buffer))


    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo-id) 
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer vertex-position size data-type normalize-datas? 12 offset)
    (GL20/glEnableVertexAttribArray 0)
    ;; note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)))


(defn create-ebo [indices]
  (let [ebo-id (GL15/glGenBuffers)
	indices-buffer (create-int-buffer indices)]
  (GL15/glBindBuffer GL15/GL_ELEMENT_ARRAY_BUFFER ebo-id) 
  (GL15/glBufferData GL15/GL_ELEMENT_ARRAY_BUFFER indices-buffer GL15/GL_STATIC_DRAW)
))
