(ns test-lwjgl.engine.utilities.parser_3d.ply
  (:use [test-lwjgl.engine.utilities.misc])
  (:require [test-lwjgl.engine.opengl.window :as window]
            [clojure.java.io :as io]))

(defn triangulate-indice [indice]
  (let [[t a b c d] indice]
    (if (= t 4) [[a b c] [c d a]] [[a b c]])))

(defn coords-n-colors [data]
  (let [[x y z nx ny nz _ _ r g b] data]
    {:coordinates [x y z] :color [r g b] :texture [0.0 0.0]}))

(defn parse-ply [file-path]
  (let [file  (slurp (io/file (io/resource file-path)))
        lines (clojure.string/split-lines file)
        file-config (atom {:header-offset 0 :vertices-count 0 :indices-count 0 :vertices [] :indices []})]
    (doseq-indexed [line lines index]
                   (cond
                     (clojure.string/starts-with? line "element vertex") (swap! file-config assoc :vertices-count (Integer. (last (clojure.string/split line #" "))))
                     (= line "end_header") (do
                                             (swap! file-config assoc :header-offset index)
                                             (swap! file-config assoc :indices-count (- (count lines) (+ (:header-offset @file-config) (:vertices-count @file-config)))))
                     (and (not (zero? (:header-offset @file-config))) (<= (:header-offset @file-config) index (+ (:vertices-count @file-config) (:header-offset @file-config)))) (swap! file-config update-in [:vertices] #(conj % (coords-n-colors (map (fn [i] (Float. i)) (clojure.string/split line #" ")))))
                     (and (not (zero? (:header-offset @file-config))) (<= (+ (:vertices-count @file-config) (:header-offset @file-config)) index)) (swap! file-config update-in [:indices] #(conj % (map (fn [i] (Integer. i)) (clojure.string/split line #" "))))))
    (swap! file-config update-in [:indices] #(vec (flatten (vec (mapcat triangulate-indice %)))))
    @file-config))


