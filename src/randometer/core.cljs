(ns ^:figwheel-always randometer.core
    (:require
              [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def size [24 6])

(defn show-trial [x]
  (if x "●" "○"))

(defn trial []
  (> 0.5 (rand)))

(defn biased-trial [propensity]
  (> propensity (rand)))

(defn muted-trial [propensity memory]
  (if (> propensity (rand))
    (not memory)
    memory))

(defn biased-generator [n p]
  (repeatedly n #(biased-trial p)))

(defn muted-generator [n p]
  (letfn [(helper [previous]
            (lazy-seq
             (cons previous
                   (helper (muted-trial p previous)))))]
    (take n (helper (trial)))))

(defn puzzle [generator propensity]
  (partition (first size) (generator (apply * size) propensity)))

(defn shuffle-boards [boards]
  (zipmap (range) (shuffle (range (count boards)))))

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn hello-world []
  [:h1 (:text @app-state)])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

