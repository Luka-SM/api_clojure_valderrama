;; Inicialização do servidor


(ns valderrama_api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [valderrama_api.routes :refer [app-routes]]))

;; CONFIGURAÇÃO CORS

(defn wrap-cors [handler]

  (fn [request]

    (if (= :options (:request-method request))

      {:status 204
       :headers {
         "Access-Control-Allow-Origin" "http://localhost:5173"
         "Access-Control-Allow-Methods" "POST, OPTIONS"
         "Access-Control-Allow-Headers" "Content-Type"
       }
       :body ""}

      (let [response (handler request)]

        (-> response
            (assoc-in [:headers "Access-Control-Allow-Origin"]
                      "http://localhost:5173")
            (assoc-in [:headers "Access-Control-Allow-Methods"]
                      "POST, OPTIONS")
            (assoc-in [:headers "Access-Control-Allow-Headers"]
                      "Content-Type"))))))

;; INICIALIZAÇÃO DO SERVIDOR

(defn -main []

  (run-jetty

    (wrap-cors
      (-> app-routes
          (wrap-json-body {:keywords? true})
          wrap-json-response))

    {:port 3000
     :join? false}))
