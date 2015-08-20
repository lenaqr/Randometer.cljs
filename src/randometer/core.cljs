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

(defn board-element [board]
  [:div.board {:style {:display "inline-block"
                       :vertical-align "middle"
                       :margin "0.5em"}}
   (for [row board]
     [:div.row (apply str (map show-trial row))])])

(defn board-chooser [{:keys [on-guess on-reset flavortext answertext]} boards keymap guess]
  [:fieldset {:on-click #(when (some? guess) (on-reset))}
   [:legend flavortext]
   (for [ix (range (count boards))]
     ^{:key ix}
     [:div {:style {:background-color (when (some? guess)
                                        (cond
                                         (= 0 (keymap ix)) "#efe"
                                         (= guess ix) "#fee"))}}
      [:label
       [:input {:type :radio
                :name "guess"
                :checked (= ix guess)
                :disabled (some? guess)
                :on-click #(on-guess ix)
                :style {:display "inline-block"
                        :vertical-align "middle"}}]
       [board-element (nth boards (keymap ix))]
       [:span {:style {:display (when (nil? guess) "none")}}
        (nth answertext (keymap ix))]]])
   (when (some? guess)
     [:button {:on-click #(on-reset) :style {:margin-top "0.5em"}}
      "play again"])])

(defn choose-one [x xs]
  (let [game-state (atom {})
        init-game! (fn []
                     (let [boards (map #(puzzle muted-generator %) (cons x xs))
                           keymap (shuffle-boards boards)]
                      (swap! game-state assoc
                             :boards boards
                             :keymap keymap
                             :guess nil)))
        guess! (fn [ix]
                 (swap! game-state assoc :guess ix))]
    (init-game!)
    (fn []
      (let [{:keys [boards keymap guess]} @game-state]
        [board-chooser {:on-guess #(guess! %)
                        :on-reset #(init-game!)
                        :flavortext "Which is truly random?"
                        :answertext (map #(str % " switch") (cons x xs))}
         boards keymap guess]))))

(defn main-page []
  [:div
   [:h1 "Randometer"]
   [choose-one 0.5 [0.25 0.33 0.67 0.75]]])

(reagent/render-component [main-page]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

