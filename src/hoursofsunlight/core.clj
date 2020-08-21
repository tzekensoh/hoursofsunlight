(ns hoursofsunlight.core)

(require '[hoursofsunlight.calc :as hc]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(defn solstice
  "Try 20 21 22 of the month and return the largest for Summer and smallest for Winter"
  [city lat year season]
  (let [seasonMonths {:summer 6 :winter 12}
        seasonIndexes {:summer 0 :winter 2}
        days (range 20 23)
        seasonMonth (seasonMonths (keyword season))
        ; create list of hours of sunlight corresponding to 20, 21 and 22 of the month
        daysWithHOS (map #(hc/hoursofsunlight lat year seasonMonth %) days)
        ; create list of ([day, hours])
        mappedDaysWithHOS (map vector days daysWithHOS)
        ; sort the list from largest to smallest based on the hour
        sortedDaysWithHOS (sort (fn [p1 p2] (let [[v11 v12] p1
                                                  [v21 v22] p2] (compare v22 v12))) mappedDaysWithHOS)
        ; get the first or last element depending on Summer or Winter
        [day hours] (nth sortedDaysWithHOS (seasonIndexes (keyword season)))
        ]
    [(str season " solstice day") (str year "/" seasonMonth "/" day) hours (hc/toHMS hours) ]
    )
  )

(defn createCsvForCity
  "Create a CSV with hours of sunlight data for a city"
  [city lat year]
  (let [months (range 1 13)
        hoursindecimals (map #(/ (Math/round (* (hc/hoursofsunlight lat year % 1) 1000.0 ) ) 1000.0)  months)
        hms (map hc/toHMS hoursindecimals) ]
    (with-open [writer (io/writer (str city ".csv"))]
      (csv/write-csv writer
                     [(map hc/toMonthName months)
                      hoursindecimals
                      hms
                      (solstice city lat year "summer")
                      (solstice city lat year "winter")
                      ])))

  )

(defn -main []
  (createCsvForCity "PaloAlto" 37.4419 2021)
  (createCsvForCity "Edinburgh" 55.95 2020)
  (createCsvForCity "HongKong" 22.3193 2020)
  )

(-main)
(hc/toMonthName 8)
(hc/toHMS (hc/hoursofsunlight 37.4419 2020 12 21))
(hc/hoursofsunlight 55.95 2020 12 20)
(hc/hoursofsunlight 55.95 2020 12 21)
(hc/hoursofsunlight 55.95 2020 12 22)
