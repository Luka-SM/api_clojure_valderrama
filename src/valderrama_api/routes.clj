;; Definição das rotas HTTP

(ns valderrama_api.routes
  (:require [compojure.core :refer [defroutes POST]]
            [valderrama_api.handlers :as handlers]))

(defroutes app-routes

  (POST "/api/calcular-tabela" request
    (handlers/processar-handler request)))
    