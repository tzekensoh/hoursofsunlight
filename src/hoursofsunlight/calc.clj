(ns hoursofsunlight.calc)

(require '[clj-time.core :as t]
         '[clj-time.format :as f])

(defn ndays
  "Calculate the number of days since Jan 1st of the same year"
  [year month day]
  (t/in-days (t/interval  (t/date-time year 1 1)  (t/date-time year month day))))


(defn declin
  "Calculate the declination of the Sun, perpendicular to the Earth's orbit."
  [year month day]
  (* -1
     (Math/asin
       (* 0.39779
          (Math/cos (Math/toRadians (+ (* 0.98565 (+ (ndays year month day) 10))
                                       (* 1.914 (Math/sin (Math/toRadians (* 0.98565 (+ (ndays  year month day) 2))))))))))
     )
  )

(defn ha
  "Calculate the hour angle"
  [lat year month day]
  (let [d (declin year month day)]
    (Math/acos
      (- (/ (Math/cos (Math/toRadians 90.833)) (* (Math/cos (Math/toRadians lat)) (Math/cos d)))
         (* (Math/tan (Math/toRadians lat)) (Math/tan d )))))

  )

(defn hoursofsunlight
  "Calculate the hours of sunlight for the location on the given latitude and date"
  [lat year month day]
  (let [h (ha lat year month day)]
    (/ (* 2.0 (Math/toDegrees h)) 15.0 )
    )
  )

(defn toHMS
  "Convert hours from decimal to HMS format"
  [hr]
  (let [h (Math/floor hr)
        mi (* 60 (- hr h))
        m (Math/floor mi)
        ss (* 60 (- mi m))
        s (Math/round ss)]
    (str (int h)  ":" (int m)  ":" (int s) )))

(defn toMonthName
  "Convert from number to English month name"
  [m]
  (f/unparse (f/formatter "MMM") (t/date-time 1972 m 1))
  )
