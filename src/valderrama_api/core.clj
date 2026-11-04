
(ns valderrama_api.core
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [valderrama_api.routes :refer [app-routes]]))

;; ORIGENS AUTORIZADAS

(def frontend-urls
  (let [urls (or (System/getenv "FRONTEND_URL")
                 "http://localhost:5173")]
    (set (map clojure.string/trim
              (clojure.string/split urls #",")))))

;; MIDDLEWARE CORS

(defn wrap-cors [handler]
  (fn [request]
    (let [origin (get-in request [:headers "origin"])
          allowed? (contains? frontend-urls origin)]
      (if (= :options (:request-method request))
        {:status 204
         :headers
         (cond-> {"Access-Control-Allow-Methods"
                  "GET, POST, PUT, DELETE, OPTIONS"
                  "Access-Control-Allow-Headers"
                  "Content-Type, Authorization"}
           allowed?
           (assoc "Access-Control-Allow-Origin" origin
                  "Vary" "Origin"))
         :body ""}
        (let [response (handler request)]
          (if allowed?
            (-> response
                (assoc-in [:headers "Access-Control-Allow-Origin"] origin)
                (assoc-in [:headers "Access-Control-Allow-Methods"]
                          "GET, POST, PUT, DELETE, OPTIONS")
                (assoc-in [:headers "Access-Control-Allow-Headers"]
                          "Content-Type, Authorization")
                (assoc-in [:headers "Vary"] "Origin"))
            response))))))

;; INICIALIZAÇÃO DO SERVIDOR

(defn -main [& args]
  (run-jetty
    (wrap-cors
      (-> app-routes
          (wrap-json-body {:keywords? true})
          wrap-json-response))
    {:port (Integer/parseInt
             (or (System/getenv "PORT") "3000"))
     :join? false}))
