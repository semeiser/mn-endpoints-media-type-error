FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY build/libs/*-all.jar mn-endpoints-media-type-error-groovy.jar
CMD java ${JAVA_OPTS} -jar mn-endpoints-media-type-error-groovy.jar