FROM openjdk:8-jre-alpine

ENV APP_NAME=vap-restdocs-app
ENV APP_VERSION=1.0.0

ENV IMAGE_APP_DIR="/opt/ncs/vap/$APP_NAME"

WORKDIR $IMAGE_APP_DIR

COPY ["build/install/$APP_NAME", "$IMAGE_APP_DIR"]

ENTRYPOINT ["java", "-jar", "vap-restdocs-app-1.0.0.jar"]
