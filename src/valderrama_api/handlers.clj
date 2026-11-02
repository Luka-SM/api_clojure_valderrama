;; Tratamento das requisições e respostas

(ns valderrama_api.handlers
  (:require [valderrama_api.services.table-service :as table-service]))

(defn processar-handler [request]

  (let [scale-value (get-in request [:body :scale-value])]

    (if (and (number? scale-value)
             (pos? scale-value))

      {:status 200
       :body (table-service/calcular-tabela scale-value)}

      {:status 400
       :body {:erro "Informe uma escala numérica positiva."}})))
