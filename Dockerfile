FROM clojure:lein

WORKDIR /app

# Copia os arquivos do projeto
COPY project.clj .
COPY src ./src
COPY resources ./resources

# Instala dependências e gera o JAR
RUN lein uberjar

# Porta utilizada pelo Render
EXPOSE 10000

# Executa a aplicação
CMD ["java", "-jar", "target/valderrama_api-0.1.0-SNAPSHOT-standalone.jar"]