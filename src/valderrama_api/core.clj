
(ns valderrama_api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [valderrama_api.routes :refer [app-routes]]))

;; CONFIGURAÇÃO CORS

(def frontend-url
  (or (System/getenv "FRONTEND_URL")
      "http://localhost:5173"))

(defn wrap-cors [handler]
  (fn [request]
    (if (= :options (:request-method request))
      {:status 204
       :headers {
         "Access-Control-Allow-Origin" frontend-url
         "Access-Control-Allow-Methods" "GET, POST, PUT, DELETE, OPTIONS"
         "Access-Control-Allow-Headers" "Content-Type, Authorization"
       }
       :body ""}
      (let [response (handler request)]
        (-> response
            (assoc-in [:headers "Access-Control-Allow-Origin"]
                      frontend-url)
            (assoc-in [:headers "Access-Control-Allow-Methods"]
                      "GET, POST, PUT, DELETE, OPTIONS")
            (assoc-in [:headers "Access-Control-Allow-Headers"]
                      "Content-Type, Authorization"))))))

;; INICIALIZAÇÃO DO SERVIDOR

(defn -main []
  (run-jetty
    (wrap-cors
      (-> app-routes
          (wrap-json-body {:keywords? true})
          wrap-json-response))
    {:port (Integer/parseInt
             (or (System/getenv "PORT") "3000"))
     :join? false}))
