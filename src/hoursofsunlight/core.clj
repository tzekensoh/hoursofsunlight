(ns hoursofsunlight.core)

(require '[hoursofsunlight.calc :as hc]
         '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(defn summerSolstice
  "Try 20 21 22 of the month and return the largest"
  [city lat year]
  (let [days (range 20 23)
        daysWithHOS (map #(hc/hoursofsunlight lat year 6 %) days)]
    daysWithHOS

    )
  ; [(str year "-June-" 20) ]

  )

(defn createCsvForCity
  "Create a CSV with hours of sunlight data for a city"
  [city lat year]
  (let [months (range 1 13)
        hoursindecimals (map #(hc/hoursofsunlight lat year % 1) months)
        hms (map hc/toHMS hoursindecimals) ]
    (with-open [writer (io/writer (str city ".csv"))]
      (csv/write-csv writer
                     [months
                      hoursindecimals
                      hms
                      (summerSolstice city lat year)])))
  )

(defn -main []
  (createCsvForCity "PaloAlto" 37.4419 2020)
  (createCsvForCity "Edinburgh" 55.95 2020)
  (createCsvForCity "HongKong" 22.3193 2020)
  )

(-main)
(hc/toHMS (hc/hoursofsunlight 37.4419 2020 8 21))
(hc/hoursofsunlight 55.95 2019 7 1)
