;; Regras de negócio relacionadas à tabela

(ns valderrama_api.services.table-service)

;; FUNÇÕES AUXILIARES

(defn funcao-espacamento-traste [current-dist prev-dist]
  (- current-dist prev-dist))

(defn funcao-espacamento-rastilho [scale current-pestana]
  (- scale current-pestana))

(defn funcao-distancia-pestana [scale fret]
  (* scale
     (- 1
        (/ 1 (Math/pow 2 (/ fret 12.0))))))

;; FUNÇÃO PRINCIPAL
(defn calcular-tabela [scale-value]

    (loop [fret 1
            distancia-anterior nil
            vetor-espacamento-traste []
            vetor-distancia-rastilho []
            vetor-distancia-pestana []]

        (if (< fret 13)

            (let [distancia-atual
                (funcao-distancia-pestana scale-value fret)

                distancia-rastilho
                (funcao-espacamento-rastilho scale-value distancia-atual)

                espacamento-traste
                (if (= fret 1)
                    distancia-atual
                    (funcao-espacamento-traste
                    distancia-atual
                    distancia-anterior))]

            (recur
                (inc fret)
                distancia-atual
                (conj vetor-espacamento-traste
                    (format "%.2f" espacamento-traste))
                (conj vetor-distancia-rastilho
                    (format "%.2f" distancia-rastilho))
                (conj vetor-distancia-pestana
                    (format "%.2f" distancia-atual))
            ))

            ;; RETORNO DOS TRÊS VETORES
            {
                :vetor-espacamento-traste vetor-espacamento-traste
                :vetor-distancia-rastilho vetor-distancia-rastilho
                :vetor-distancia-pestana vetor-distancia-pestana
            }
        )
    )
)
