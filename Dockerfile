FROM openjdk:8-jdk-alpine as builder
# ----
# Install Maven
# RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk add --no-cache curl tar bash
ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
RUN mkdir -p /usr/share/maven && \
curl -fsSL https://ftp.cixug.es/apache/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
# speed up Maven JVM a bit
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
ENTRYPOINT ["/usr/bin/mvn"]
# ----
# Install project dependencies and keep sources
# make source folder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# install maven dependency packages (keep in image)
COPY pom.xml /usr/src/app
COPY src ./src
# RUN ls -ltr
RUN mvn install
RUN ls target/
RUN ls target/classes/


FROM openjdk:8-jdk-alpine
ARG JAR_FILE=/usr/src/app/target/*.jar
ARG CONFIG_FILE=/usr/src/app/target/classes/application.yml

COPY --from=builder ${JAR_FILE} app.jar 
COPY --from=builder ${CONFIG_FILE} /config/application.yml
ENV CONFIG_LOCATION=file:///config/

# Application env vars
ENV GEOIP_URL "https://api.ipgeolocation.io/ipgeo?apiKey=%s&ip=%s"
ENV GEOIP_CACHE_EVICT_TIME 3600
ENV GEOIP_CACHE_EVICT_CRON "0 0 * * * ?"

# If you not use volume, you must override this environment value with -e GEOIP_API_KEY "yourapikey"
ENV GEOIP_API_KEY ""

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]