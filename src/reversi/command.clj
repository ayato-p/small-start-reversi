(ns reversi.command
  (:require [reversi.store :as store]))

(defn line-of-stones
  [stones [x y :as pos] [x' y' :as direction]]
  (loop [x (+ x x')
         y (+ y y')
         collected []]
    (if-let [stone (get stones [x y])]
      (recur (+ x x')
             (+ y y')
             (conj collected [[x y] stone]))
      collected)))

(def ^:private directions
  (->> (for [y [-1 0 1] x [-1 0 1]] [x y])
       (remove (partial every? zero?))))

(defn reverse-targets [stones pos color]
  (let [reversed-color? (partial = (if (= :b color) :w :b))]
    (reduce (fn [targets direction]
              (let [[sandwiched opposite]
                    (->> (line-of-stones stones pos direction)
                         (split-with #(-> % second reversed-color?)))]
                (cond-> targets
                  (seq opposite) (into (map first sandwiched)))))
            []
            directions)))

(defn put-stone [[x y :as pos] color]
  (reset! store/stones
          (reduce (fn [stones pos]
                    (assoc stones pos color))
                  @store/stones
                  (conj (reverse-targets @store/stones pos color) pos))))
