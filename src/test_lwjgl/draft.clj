;; Macro to describe and load 3D datas into GC memory
;;  Use case:
;;    (load-data [{:coordinates [x y z] :color [red green blue alpha]}
;;                {:coordinates [x y z] :color [red green blue alpha]}
;;                {:coordinates [x y z] :color [red green blue alpha]}]) 
;;
;;  Behavior:
;;    - Determine number of points to draw
;;    - Determine the data pattern (size, stride and offset):
;;        * size is the number of elements that compose de vertex (here it's 3: x, y and z)
;;        * the stride is the space between consecutive vertex attribute sets. Here, the next vertex is separated by 3 floats (x, y and z) and 4 floats (red, green, vlue, alpha). So the stride is 7 time the bytes size of a float: (* 7 java.lang.Float/BYTES)
(defmacro load-data [datas]
  (println "0")
  `(let [size#   (count (:coordinates datas)) 
        stride# (* (+ size# (count (:color datas))) java.lang.Float/BYTES)
        vertex-position# 0
        offset# 1
        data-type# GL11/GL_FLOAT
        normalize-datas?# false
        formated-datas# (vec (concat (:coordinates datas) (:color datas)))
        vbo-id# (GL15/glGenBuffers)
        vertices-buffer# (create formated-datas#)]

  (println "1")
      ;; Copy datas in a buffer for OpenGL to use
      (GL15/glBindBuffer GL15/GL_ARRAY_BUFFER vbo-id#) 
      (GL15/glBufferData GL15/GL_ARRAY_BUFFER vertices-buffer# GL15/GL_STATIC_DRAW)
      (GL20/glVertexAttribPointer ~vertex-position# ~size# ~data-type# ~normalize-datas?# ~stride# ~offset#)
      vbo-id#))
