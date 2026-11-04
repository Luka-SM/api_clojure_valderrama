FROM clojure:lein

WORKDIR /app

# Copia o arquivo de dependências
COPY project.clj ./

# Baixa as dependências
RUN lein deps

# Copia o código-fonte e recursos
COPY src ./src
COPY resources ./resources

# Compila e gera o uberjar
RUN lein uberjar

# Porta da aplicação
EXPOSE 10000

# Inicia a aplicação
CMD ["java", "-jar", "target/uberjar/valderrama_api-0.1.0-SNAPSHOT-standalone.jar"]
