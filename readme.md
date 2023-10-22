## Requirements:
* Docker


## Set up:

Start containers:
```
$ docker build -t chatexu-server:0.1.0 .
$ docker comose up
```

## Cleaning up:

Check if containers are running. If they are, stop them:
```
$ docker ps
$ docker stop chatexu-db
$ docker stop chatexu-server
```

\
Remove containers:
```
$ docker rm chatexu-db
$ docker rm chatexu-server
```
\
Remove `ChatexU Server` image:
```
$ docker rmi chatexu-server:0.1.0
```
\
Remove other images, if unwanted:
```
$ docker rmi mongo
$ docker rmi openjdk:17-jdk
```

