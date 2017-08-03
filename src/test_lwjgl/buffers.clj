(ns test-lwjgl.buffers
  (:use [test-lwjgl.utility])
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
  "TODO macroize it so the readable datas (DSL?) are processed at compile time, and only remains the OpenGL state update at runtime. vbo-id should be extract from the first let, because he needs an OpenGL context who do not exist at compile time."
   (let [coordinates (vec (mapcat #(select-values %1 [:coordinates]) datas))
         colors (vec (mapcat #(select-values % [:color]) datas))
         textures (vec (mapcat #(select-values % [:texture]) datas))
         vertex-size (count (:coordinates (first datas)))
         color-size (count (:color (first datas)))
         texture-size (count (:texture (first datas)))
         stride (* (+ vertex-size color-size texture-size) java.lang.Float/BYTES)
         vertex-position 0
         color-position 1
         texture-position 2
         color-offset (* vertex-size java.lang.Float/BYTES)
         texture-offset (* (+ vertex-size color-size) java.lang.Float/BYTES)
         data-type GL11/GL_FLOAT
         normalize-datas? false
         formated-datas (vec (mapcat concat coordinates colors textures))
         vertices-buffer (create-float-buffer formated-datas)
         vbo-id (GL15/glGenBuffers)]

    ;; Describe vertices
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo-id) 
    (GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer GL15/GL_STATIC_DRAW)
    (GL20/glVertexAttribPointer vertex-position vertex-size data-type normalize-datas? stride 0)
    (GL20/glEnableVertexAttribArray vertex-position)
    
    ;; Describe colors
    (GL20/glVertexAttribPointer color-position color-size data-type normalize-datas? stride color-offset)
    (GL20/glEnableVertexAttribArray color-position)

    ;; Decribe texture
    (GL20/glVertexAttribPointer texture-position texture-size data-type normalize-datas? stride texture-offset)
    (GL20/glEnableVertexAttribArray texture-position)

    ;; note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
    (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER 0)))


(defn create-ebo [indices]
  "Creates an Elements buffer object containing a list of indices corresponding to vbo vertices index. Theses indices are used to define triangles without loading identical points multiple times"
  (let [ebo-id (GL15/glGenBuffers)
	indices-buffer (create-int-buffer indices)]
  (GL15/glBindBuffer GL15/GL_ELEMENT_ARRAY_BUFFER ebo-id) 
  (GL15/glBufferData GL15/GL_ELEMENT_ARRAY_BUFFER indices-buffer GL15/GL_STATIC_DRAW)))

(defn create-tbo [texture-coordinates]
  "Create a texture buffer object"
  )
