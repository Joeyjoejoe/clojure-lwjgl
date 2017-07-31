(ns test-lwjgl.buffers
  (:import (org.lwjgl BufferUtils)
           (org.lwjgl.opengl GL11 GL15 GL20)))

(defn create [datas]
  ;; Store the vertices vector into a FloatBuffer to make it available for OpenGL.
  ;; .put write the data, .flip set the position of the FloatBuffer to 0 (that is,
  ;; we say that we've finished writing datas).
  (let [datas (float-array datas)]
    (-> (BufferUtils/createFloatBuffer (count datas))
      (.put datas)
      (.flip))))

(defn vertex-buffer-object [vertices-buffer]
  ;; Naïve implementation, need some more configuration options
  (let [vbo-id (GL15/glGenBuffers)]
    ;; Copy datas in a buffer for OpenGL to use
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo-id) 
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer 0 3 GL11/GL_FLOAT false (* 3 java.lang.Float/BYTES) 0)
    vbo-id))


