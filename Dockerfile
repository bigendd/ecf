FROM maven AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8091


ENTRYPOINT ["java", "-jar", "app.jar"]