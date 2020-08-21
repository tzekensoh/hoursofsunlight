(ns hoursofsunlight.calc)

(require '[clj-time.core :as t])

(defn ndays [year month day]
  (t/in-days (t/interval  (t/date-time year 1 1)  (t/date-time year month day))))


(defn declin [year month day]
  (* -1
     (Math/asin
       (* 0.39779
          (Math/cos (Math/toRadians (+ (* 0.98565 (+ (ndays year month day) 10))
                                       (* 1.914 (Math/sin (Math/toRadians (* 0.98565 (+ (ndays  year month day) 2))))))))))
     )
  )

(defn ha [lat year month day]
  (let [d (declin year month day)]
    (Math/acos
      (- (/ (Math/cos (Math/toRadians 90.833)) (* (Math/cos (Math/toRadians lat)) (Math/cos d)))
         (* (Math/tan (Math/toRadians lat)) (Math/tan d )))))

  )

(defn hoursofsunlight [lat year month day]
  (let [h (ha lat year month day)]
    (/ (* 2 (Math/toDegrees h)) 15 )
    )
  )