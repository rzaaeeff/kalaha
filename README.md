<div style="text-align: center;"><h1> Kalaha by Heydar Rzayev </h1></div> <br>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [API documentation](ms-kalaha-api/README.md#api)
- [How to build & How to run](#how-to-build--how-to-run)
- [Dependencies](#dependencies)
- [Support](#support)
- [Future goals](#future-goals)

## Introduction

Kalaha is a game in the mancala family invented in the United States by William Julius Champion, Jr. in 1940. This game is sometimes also called "Kalahari", possibly by false etymology from the Kalahari desert in Namibia.

Rules are as below:
1. At the beginning of the game, six seeds are placed in each house. This is the traditional method.
2. Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.
3. Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.
4. If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.
5. If the last sown seed lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
6. When one player no longer has any seeds in any of their houses, the game ends. The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.

## Features

This application features a UI where two players can take their turns and play. The player doesn't have to care about losing the state of the game, because application saves state after every move. So, by just having game ID, player can return and continue their game.

## Architecture

![Solution Design image](docs/Current%20solution%20design.png)

The application consists of two main and one auxiliary module:
- [kalaha-core](kalaha-core) - Core library to provide gameplay experience (auxiliary)
- [ms-kalaha-api](ms-kalaha-api) - MicroService to provide backend/API
- [mf-kalaha-ui](mf-kalaha-ui) - MicroFrontend to provide frontend/UI

kalaha-core library is the main provider of the gameplay logic. It's a complete package with logic and tests. 
The implementation is done using Java. The tests are written using Spock Framework on Groovy.

ms-kalaha-api is the main provider for the backend operations. It is designed to use any core library and database.
In other words, it's not coupled with the core library used inside. In this case, the implementation 
uses kalaha-core library and MongoDB. Because of loose coupling any other core library can be easily integrated without 
needing much refactor. The microservice also provides presentation layer using REST API.
The implementation is done using Spring Boot on Java. The tests are written using Spock Framework on Groovy.

mf-kalaha-ui is the main provider for the frontend operations. The implementation is done using HTML, CSS and vanilla Javascript.

> :information_source: UI could have been implemented using other technologies such as Angular, React, Vue or even Vaadin with Spring.
> However, as I'm a backend engineer and not an expert in the frontend area, I decided to keep it simple and easy to code.

## API Documentation



## How to build & How to run

> :warning: You need **Java 17**, **Docker** and **Docker Compose** installed to run the all-in-one setup.

1. Start:
```shell script
$ ./start.sh
```

2. Access:
- UI: http://localhost/index.html
- API: http://localhost:8080/swagger-ui.html

3. Stop:
```shell script
$ ./stop.sh
```

4. Cleanup:

> :warning: Do this step only if you want to reset application data. 
> This script is designed to delete all docker volumes and images created in Docker,
> so it will delete all the previous data and you'll start from scratch.

```shell script
$ ./reset.sh
```

## Dependencies

Projects:
- [Kalaha Core Library by Heydar Rzayev](kalaha-core)

Technologies:
- [Java 17](https://openjdk.org/projects/jdk/17/)
- [MongoDB](https://www.mongodb.com/)
- [Gradle](https://gradle.org/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Support

If you need support, please, get in touch with **[rzaaeeff](https://github.com/rzaaeeff)**.

## Future Goals
- Introduce auth mechanism (a separate auth adapter of some IDP) and use it to determine Player IDs for authorization.
- When the load increases, to save up on the DB ops, introduce distributed caching layer with Redis (using write-back strategy).
- Refactor frontend to use a better alternative, possible one these frameworks: React, Angular, Vue.

After future goals have been reached, solution design would look like this:
![Future Solution Design image](docs/Future%20solution%20design.png)