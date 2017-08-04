(ns test-lwjgl.transformations
  (:require [clojure.core.matrix :as m]))


;; Overide core.matrix with vectorz-clj implementation
(m/set-current-implementation :vectorz) 

(defn scale-matrix [Sx Sy Sz vector]
  (vec (flatten (map vec (m/diagonal-matrix [Sx Sy Sz 1])))))

(defn translate-matrix [Tx Ty Tz]
  (let [translation-matrix (m/mutable (m/identity-matrix 4))
	_ (m/mset! translation-matrix 0 3 Tx)
	_ (m/mset! translation-matrix 1 3 Ty)
	_ (m/mset! translation-matrix 2 3 Tz)]
	(vec (flatten (map vec translation-matrix)))))

(defn rotate-x [angle]
  (let [rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 1 1 cosinus)
	_ (m/mset! rotation-matrix 1 2 (- sinus))
	_ (m/mset! rotation-matrix 2 1 sinus)
	_ (m/mset! rotation-matrix 2 2 cosinus)]
    (vec (flatten (map vec rotation-matrix)))))

(defn rotate-y [angle]
  (let [rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 0 0 cosinus)
	_ (m/mset! rotation-matrix 0 2 sinus)
	_ (m/mset! rotation-matrix 2 0 (- sinus))
	_ (m/mset! rotation-matrix 2 2 cosinus)]
    (vec (flatten (map vec rotation-matrix)))))

(defn rotate-z [angle]
  (let [rotation-matrix (m/mutable (m/identity-matrix 4))
	sinus (Math/sin angle)
	cosinus (Math/cos angle)
	_ (m/mset! rotation-matrix 0 0 cosinus)
	_ (m/mset! rotation-matrix 0 1 (- sinus))
	_ (m/mset! rotation-matrix 1 0 sinus)
	_ (m/mset! rotation-matrix 1 1 cosinus)]
    (vec (flatten (map vec rotation-matrix)))))
