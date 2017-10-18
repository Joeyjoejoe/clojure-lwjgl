(ns test-lwjgl.transformations
  (:use [test-lwjgl.utility])
  (:require [clojure.core.matrix :as m]
	    [clojure.core.matrix.operators :as mo]
	    [thi.ng.math.macros :as mm]))


;; Overide core.matrix with vectorz-clj implementation
(m/set-current-implementation :vectorz) 

(defn make 
  ([transformation args] 
	(m/as-vector (apply (resolve (symbol (str "test-lwjgl.transformations/" transformation))) args)))
  ([transformation args vectorz] 
	(apply (resolve (symbol (str "test-lwjgl.transformations/" transformation))) args)))

(defn scale-matrix [Sx Sy Sz vector]
  (m/diagonal-matrix [Sx Sy Sz 1]))

(defn translate-matrix [Tx Ty Tz]
  (let [translation-matrix (m/mutable (m/identity-matrix 4))
	_ (m/mset! translation-matrix 3 0 Tx)
	_ (m/mset! translation-matrix 3 1 Ty)
	_ (m/mset! translation-matrix 3 2 Tz)]
    translation-matrix))

(defn rotate-x [angle]
  (let [angle (Math/toRadians angle)
	rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 1 1 cosinus)
	_ (m/mset! rotation-matrix 1 2 (- sinus))
	_ (m/mset! rotation-matrix 2 1 sinus)
	_ (m/mset! rotation-matrix 2 2 cosinus)]
    rotation-matrix))

(defn rotate-y [angle]
  (let [angle (Math/toRadians angle)
	rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 0 0 cosinus)
	_ (m/mset! rotation-matrix 0 2 sinus)
	_ (m/mset! rotation-matrix 2 0 (- sinus))
	_ (m/mset! rotation-matrix 2 2 cosinus)]
    rotation-matrix))

(defn rotate-z [angle]
  (let [angle (Math/toRadians angle)
	rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 0 0 cosinus)
	_ (m/mset! rotation-matrix 0 1 (- sinus))
	_ (m/mset! rotation-matrix 1 0 sinus)
	_ (m/mset! rotation-matrix 1 1 cosinus)]
    rotation-matrix))

(defn perspective-projection
  "Returns a perspective transform matrix, which makes far away objects appear
  smaller than nearby objects. The `aspect` argument should be the width
  divided by the height of your viewport and `fov` is the vertical angle
  of the field of view in degrees."
  [fovy aspect near far]
  (let [perspective-matrix (m/mutable (m/identity-matrix 4))
	f (/ (Math/tan (* 0.5 (Math/toRadians fovy))))
	near (- near)
	far (- far)
        nf (/ (- near far))
	_ (m/mset! perspective-matrix 0 0 (/ f aspect))
	_ (m/mset! perspective-matrix 1 1 f)
	_ (m/mset! perspective-matrix 2 2 (mm/addm near far nf))
	_ (m/mset! perspective-matrix 2 3 -1.0)
	_ (m/mset! perspective-matrix 3 2 (mm/mul 2.0 near far nf))]
    perspective-matrix))

(defn look-at
  ([camera] (let [front (:front camera)
                  eye (:position camera)
                  up (:up camera)
                  center (mo/+ front (:position camera))]
              (look-at eye center up)))
  ([eye center up] (let [eyex (nth eye 0)
                         eyey (nth eye 1)
                         eyez (nth eye 2)
                         upx (nth up 0)
                         upy (nth up 1)
                         upz (nth up 2)
                         z0 (- eyex (m/mget center 0))
                         z1 (- eyey (m/mget center 1))
                         z2 (- eyez (m/mget center 2))
                         len (/ 1 (Math/sqrt (+ (* z0 z0) (* z1 z1) (* z2 z2))))
                         z0 (* z0 len)
                         z1 (* z1 len)
                         z2 (* z2 len)
                         x0 (- (* upy z2) (* upz z1))
                         x1 (- (* upz z0) (* upx z2))
                         x2 (- (* upx z1) (* upy z0))
                         len (Math/sqrt (+ (* x0 x0) (* x1 x1) (* x2 x2)))
                         x0 (cond (= len 0) 0 :else (/ x0 len))
                         x1 (cond (= len 0) 0 :else (/ x1 len))
                         x2 (cond (= len 0) 0 :else (/ x2 len))
                         y0 (- (* z1 x2) (* z2 x1))
                         y1 (- (* z2 x0) (* z0 x2))
                         y2 (- (* z0 x1) (* z1 x0))
                         len (Math/sqrt (+ (* y0 y0) (* y1 y1) (* y2 y2)))
                         y0 (cond (= len 0) 0 :else (/ y0 len))
                         y1 (cond (= len 0) 0 :else (/ y1 len))
                         y2 (cond (= len 0) 0 :else (/ y2 len))
                         look (m/mutable (m/identity-matrix 4))
                         _ (m/set-row! look 0 [x0 y0 z0 0.0])
                         _ (m/set-row! look 1 [x1 y1 z1 0.0])
                         _ (m/set-row! look 2 [x2 y2 z2 0.0])
                         _ (m/set-row! look 3 [(- (+ (* x0 eyex) (* x1 eyey) (* x2 eyez))) (- (+ (* y0 eyex) (* y1 eyey) (* y2 eyez))) (- (+ (* z0 eyex) (* z1 eyey) (* z2 eyez))) 1.0])]
                        look)))


