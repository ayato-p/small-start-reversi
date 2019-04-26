(ns reversi.store)

(def initial-stones
  {[3 3] :w [4 3] :b
   [3 4] :b [4 4] :w})

(defonce stones
  (atom initial-stones))

(defn reset-stones! []
  (reset! stones initial-stones))
