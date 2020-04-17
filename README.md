# Proxy Geoip Service
* Proxy developed for caching some geoip service

## How to use
* This image has been uploaded to [docker hub](https://hub.docker.com/repository/docker/txuselo/geoip-proxy)
* To use, you need to pass your service geoip api key
```
docker run -d -p 8080:8080 --name geoip-proxy -e GEOIP_API_KEY="yourapikey" txuselo/geoip-proxy
```
* You can get your api key singing up here [ipgeolocation](https://app.ipgeolocation.io/)

### Configuration
* You can configure geoip service and cache service with this environment variables

|          Property Name           |         Env var          |                    Description                     |                     Default Value                      |
|----------------------------------|--------------------------|----------------------------------------------------|--------------------------------------------------------|
| `geoip-service.url`              | `GEOIP_URL`              | Url preformatted of remote geoip service.          | `"https://api.ipgeolocation.io/ipgeo?apiKey=%s&ip=%s"` |
| `geoip-service.apy-key`          | `GEOIP_API_KEY`          | Api key used for authenticate remote geoip service | -                                                      |
| `geoip-service.cache.evict-time` | `GEOIP_CACHE_EVICT_TIME` | Evict time in seconds for each row consulted.      | `3600 # Evict after one hour`                          |
| `geoip-service.cache.evict-cron` | `GEOIP_CACHE_EVICT_CRON` | Cron expression to run evict cache task            | `"0 0 * * * ?" # Once per hour`                        |

```
docker run -d -p 8080:8080 --name geoip-proxy -e GEOIP_API_KEY="yourapikey" -e GEOIP_URL="url" -e GEOIP_CACHE_EVICT_TIME="seconds" GEOIP_CACHE_EVICT_CRON="cron" txuselo/geoip-proxy
```

* You also can use externalized configuration file running this
```
docker run -d -p 8080:8080 --name geoip-proxy -v /path/to/your.yml:/config/application.yml txuselo/geoip-proxy
```

* Default yml config file
```yml
geoip-service:
  url: "https://api.ipgeolocation.io/ipgeo?apiKey=%s&ip=%s"
  api-key: ${GEOIP_API_KEY}
  cache:
    evict-time: 3600 # Evict after one hour
    evict-cron: "0 0 * * * ?" # Once per hour
```

### Use in logstash filter
* You can connect this proxy with logstash filter for retrieve geoip info about your clients

#### Logstash filter
```
filter {
    rest {
      request => {
        url => "http://geoip-proxy/%{client}"
        method => "get"
      }
      json => true
      target => "geoip"
    }

}
```

#### Connect logstash container with geoip-proxy container
* Connect via `docker-compose` (Recomended)
```
version: "3.7"
services:
  geoip-proxy:
    image: txuselo/geoip-proxy:${TAG}
    ports:
      - "8080:8080"
    environment:
      - GEOIP_API_KEY="yourapikey"
  logstash:
    ...
    depends_on: geoip-proxy

```
* Connect via docker link (Legacy option)
```
docker run ... --link geoip-proxy geoip-proxy docker.elastic.co/logstash/logstash:${LOGSTASH_VERSION}
```

## Build from scratch
* [Source Code](https://github.com/txuselo/geoip-proxy)

### Dependencies
* [Java Runtime +1.8.0](https://openjdk.java.net/install/)
* [Maven +3.6.3](https://maven.apache.org/download.cgi)
* [Docker Engine +19.03.8](https://docs.docker.com/get-docker/)

### Build
* For build this proxy you should exec
```
git clone https://github.com/txuselo/geoip-proxy.git
cd geoip-proxy
mvn clean install
java -jar target/*.jar
```
* Server start in default spring boot port `8080`

### Build Docker
* This project count with Dockerfile for run in docker environments
* For build docker you can exec
```
mvn clean install
docker build -t repo/image:tag .
docker run -d -p 8080:8080 --name geoip-proxy repo/image/tag
```


