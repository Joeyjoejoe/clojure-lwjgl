(ns clopengl.engine.utilities.grid)

(defprotocol GridCoordinates
  (cell [entity index])
  (cells [entity]))

(defrecord Grid2D [fpr fpc fh fw fc]
  ;; fpc: frames per column
  ;; fpr: frames per row
  ;; fh: frame height
  ;; fw: frame width
  ;; fc: frame count
  GridCoordinates
  (cell [self index]
    "return coordinates of the frame in the sprite"
    (let [rpos (mod index (:fpr self))
          cpos (mod index (:fpc self))
          fw (:fw self)
          fh (:fh self)]
      [[(* fw cpos)       (* fh rpos)]
       [(* fw (inc cpos)) (* fh rpos)]
       [(* fw (inc cpos)) (* fh (inc rpos))]
       [(* fw cpos)       (* fh (inc rpos))]]))

  (cells [self]
    (let [frame-indexes (range (:fc self))]
      (vec (map #(cell self %) frame-indexes)))))


