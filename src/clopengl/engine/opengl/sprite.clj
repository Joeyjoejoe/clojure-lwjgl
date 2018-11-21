
;;  _____________________________
;; |     |     |     |     |     |  Here is a sprite sheet composed of 25 frames numeroted from 0 to 24.
;; |  0  |  1  |  2  |  3  |  4  |  The spirte sheet is composed of:
;; |_____|_____|_____|_____|_____|    * 5 frames horizontal: FH = 5
;; |     |     |     |     |     |    * 5 frames vertical:   FV = 5
;; |  5  |  6  |  7  |  8  |  9  |
;; |_____|_____|_____|_____|_____|  A single frame is composed of four points (@0, @1, @2, @3):
;; |     |     |     |     |     |   __________
;; |  10 |  11 |  12 |  13 |  14 |  @0         @1
;; |_____|_____|_____|_____|_____|  |          |
;; |     |     |     |     |     |  |    12    |
;; |  15 |  16 |  17 |  18 |  19 |  |          |
;; |_____|_____|_____|_____|_____|  @3_________@2
;; |     |     |     |     |     |
;; |  20 |  21 |  22 |  23 |  24 |
;; |_____|_____|_____|_____|_____|

(ns clopengl.engine.opengl.sprite
  (:require [clopengl.engine.utilities.grid :as grid]
            [clopengl.engine.opengl.textures :as textures]))

(defprotocol Activable
  (current [this] "Return the collection active element")
  (next [this] "Set element after active one to active"))

(deftype RelayVector [collection active]
  Activable
  (current [this] (nth collection active))
  (next [this]
    (let [max-index (- (count collection) 1)
          next-index (+ active 1)
          next-index (if (< max-index next-index) 0 next-index)]
      (set! (.active this) next-index)
      next-index))
  clojure.lang.Counted
  (count [_] (count collection))
  clojure.lang.Indexed
  (nth [_ i] (nth collection i))
  (nth [_ i else] (nth collection i else)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol Animate
  (defanim [self ident start end] "Define a new animation"))

(defrecord Animation [ident               ; Name of animation
                      ^RelayVector frames ; RelayVector of SpriteFrames
                      next-at])           ; Time at which the next frame will be activated

(defrecord Frame [index coordinates])

(defrecord Sprite [frames texture-id]
  ;; Attributes:
  ;;   - frames: list of Frame objects
  ;;   - texture-id: the opengl texture id where the sprite is loaded.
  ;; Functions:
  ;;   - defanim => Define a new animation composed of frame from this sprite (defanim ident frames-indexes-range) => SpriteAnimation
  ;;   -
  Animate
  (defanim [sprite ident start end]
    (let [frames (:frames sprite)
          anim-frames (->RelayVector (subvec frames start end) 0)]
      (->Animation ident anim-frames 0))))

(defn make-sprite [path fpc fc]
  ;; save sprite as texture
  ;; Get frames coordinates
  (let [full-rows (/ fc fpc)
        fpr (if (= 0 (mod fc fpc)) full-rows (+ 1 full-rows))
        fh (/ 1.0 fpr)
        fw (/ 1.0 fpc)
        grid2d (grid/->Grid2D fpr fpc fh fw fc)
        frames (vec (map-indexed #(->Frame %1 %2) (grid/cells grid2d)))
        texture-id 0 ;;(textures/setup (.getAbsolutePath (io/file (io/resource path))))
        ]
      (->Sprite frames texture-id)))

;; OBJECTIVES
;; - Save to gpu groups of 4 vertices (forming a rectangle) bounded to textures coordinates corresponding to a sprite's frame.
;;   |-> return the render function which take as parameter a frame index (to only draw the corresponding group of 4 vertex)
;;       with (GL11/glDrawArrays GL11/GL_TRIANGLES first-vertex last-vertex)
;; - Bind a serie of frames to an event state (moving, running, jumping etc...)
;; - Render different frames based on clock time, inputs and state.

