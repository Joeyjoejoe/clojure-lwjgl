(ns test-lwjgl.engine.utilities.shapes.basic)

(defn rectangle [length width]
  (let [hw (/ width 2.0)
        hl (/ length 2.0)
        -hl (- hl)
        -hw (- hw)]
  {:vertices [{:coordinates [-hw 0.0 -hl] :color [0.0 0.0 1.0] :texture [0.0 0.0]}
              {:coordinates [ hw 0.0 -hl] :color [0.0 0.0 1.0] :texture [0.0 0.0]}
              {:coordinates [ hw 0.0  hl] :color [0.0 0.0 1.0] :texture [0.0 0.0]}
              {:coordinates [-hw 0.0  hl] :color [0.0 0.0 1.0] :texture [0.0 0.0]}]
   :indices  [0 1 2 2 3 0]}))

(defn cube
  ([] {:vertices [{:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}

                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}

                  {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}

                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}

                  {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [ 0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}

                  {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}
                  {:coordinates [ 0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [-0.5  0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [-0.5  0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 1.0]}]
       :indices []})

  ([with_indices?] {:vertices [{:coordinates [-0.5 -0.5 0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                               {:coordinates [ 0.5 -0.5 0.5] :color [0.0 1.0 0.0] :texture [1.0 0.0]}
                               {:coordinates [-0.5  0.5 0.5] :color [0.0 1.0 0.0] :texture [0.0 1.0]}
                               {:coordinates [ 0.5  0.5 0.5] :color [0.0 0.0 1.0] :texture [1.0 1.0]}

                               {:coordinates [-0.5 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                               {:coordinates [ 0.5 -0.5 -0.5] :color [0.0 1.0 0.0] :texture [1.0 0.0]}
                               {:coordinates [-0.5  0.5 -0.5] :color [0.0 1.0 0.0] :texture [0.0 1.0]}
                               {:coordinates [ 0.5  0.5 -0.5] :color [0.0 0.0 1.0] :texture [1.0 1.0]}]
                    :indices [3 2 1 0 1 2 7 6 5 4 5 6 2 6 0 0 4 6 3 7 5 1 5 3 2 6 3 7 6 3 0 1 4 5 1 4]}))

(defn triangle
  ([] {:vertices [{:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.0  0.5  0.0] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [ 0.0  0.5  0.0] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.0 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.0  0.5  0.0] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.0 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                  {:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                  {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                  {:coordinates [ 0.0 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}]
       :indices []})

   ([with_indeces?] {:vertices [{:coordinates [ 0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [0.0 0.0]}
                                {:coordinates [-0.5 -0.5  0.5] :color [1.0 0.0 0.0] :texture [1.0 0.0]}
                                {:coordinates [ 0.0  0.5  0.0] :color [1.0 0.0 0.0] :texture [1.0 1.0]}
                                {:coordinates [ 0.0 -0.5 -0.5] :color [1.0 0.0 0.0] :texture [1.0 1.0]}]
                    :indices [0 1 2 0 2 3 1 2 3 0 1 3]}))
