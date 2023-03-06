<div style="text-align: center;"><h1> ms-kalaha-api </h1></div> <br>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [API](api)
- [How to build & How to run](#how-to-build--how-to-run)

## Introduction

ms-kalaha-api is an API for a Kalaha game.

If you need support, please, get in touch with **[rzaaeeff](https://github.com/rzaaeeff)**.

## Features

This microservice serves two resources:

- Game - Current game state
- Game Move - Move in the game

For the Game resource, a consumer can use this API to:

- Create - POST /games
- Read - GET /games/{id}
- Delete - DELETE /games/{id}

For the Game Move resource, a consumer can use this API to:

- Create - POST /games/{gameId}/moves

Below you can find API definition.

## API

<details>
<summary>Endpoints</summary>
<br/>
Create a new game
<details>
<summary>/v1/games - HTTP POST</summary>
<br/>

Try yourself:

```shell script
curl -X POST "http://localhost:8080/v1/games" -H "accept: application/json"
```

Request body: N/A

Response:

1. Success response<br/>
   Status code: 201 (Created)<br/>
   Response body:
    ```json
    {
        "id": "631223842204cb730598a5f7",
        "houses": [6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6],
        "stores": [0, 0],
        "activePlayerId": "PLAYER_1",
        "status": "ONGOING",
        "createdAt": "2022-09-02T15:38:44.832812108"
    }
    ```
2. Unknown error<br/>
   Status code: 500 (Internal Server Error)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.unexpected",
        "message": null
    }
    ```

</details>

<br/>
Read the game
<details>
<summary>/v1/games/{id} - HTTP GET</summary>
<br/>

Try yourself:

```shell script
curl -X GET "http://localhost:8080/v1/games/631223842204cb730598a5f7" -H "accept: application/json"
```

Request body: N/A

Response:

1. Success response<br/>
   Status code: 200 (Ok)<br/>
   Response body:
    ```json
    {
        "id": "631223842204cb730598a5f7",
        "houses": [6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6],
        "stores": [0, 0],
        "activePlayerId": "PLAYER_1",
        "status": "ONGOING",
        "createdAt": "2022-09-02T15:38:44.832812108"
    }
    ```
2. Game not found<br/>
   Status code: 404 (Not Found)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.game-not-found",
        "message": "Game not found by ID=631223842204cb730598a5f"
    }
    ```
3. Unknown error<br/>
   Status code: 500 (Internal Server Error)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.unexpected",
        "message": null
    }
    ```

</details>

<br/>
Delete the game
<details>
<summary>/v1/games/{id} - HTTP DELETE</summary>
<br/>

Try yourself:

```shell script
curl -X DELETE "http://localhost:8080/v1/games/631223842204cb730598a5f7" -H "accept: application/json"
```

Request body: N/A

Response:

1. Success response<br/>
   Status code: 204 (No Content)<br/>
   Response body: N/A
2. Game not found<br/>
   Status code: 404 (Not Found)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.game-not-found",
        "message": "Game not found by ID=631223842204cb730598a5f"
    }
    ```
3. Unknown error<br/>
   Status code: 500 (Internal Server Error)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.unexpected",
        "message": null
    }
    ```

</details>

<br/>
Make a move (by creating the move resource)
<details>
<summary>/v1/games/{id}/moves - HTTP POST</summary>
<br/>

Try yourself:

```shell script
curl -X POST "http://localhost:8080/v1/games/631241482204cb730598a5f8/moves" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"pitId\": 1, \"playerId\": \"PLAYER_1\"}"
```

Request body:

```json
{
  "pitId": 1,
  "playerId": "PLAYER_1"
}
```

Response:

1. Success response<br/>
   Status code: 201 (Created)<br/>
   Response body:
    ```json
    {
        "id": "631241482204cb730598a5f8",
        "houses": [0, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6],
        "stores": [1, 0],
        "activePlayerId": "PLAYER_1",
        "status": "ONGOING",
        "createdAt": "2022-09-02T17:45:44.866"
    }
    ```
2. Game not found<br/>
   Status code: 404 (Not Found)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.game-not-found",
        "message": "Game not found by ID=631223842204cb730598a5f"
    }
    ```
3. Illegal move<br/>
   Status code: 400 (Bad Request)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.illegal-move",
        "message": "Player P2 cannot take their turn yet"
    }
    ```
4. Unknown error<br/>
   Status code: 500 (Internal Server Error)<br/>
   Response body:
    ```json
    {
        "code": "kalaha.exception.unexpected",
        "message": null
    }
    ```

</details>

</details>

<br/>
<details>
<summary>Dictionary</summary>
<details>
<summary>Status</summary>

| Status       | Description |
|--------------|-------------|
| ONGOING      | N/A         |
| DRAW         | N/A         |
| PLAYER_1_WIN | N/A         |
| PLAYER_2_WIN | N/A         |

</details>
<details>
<summary>Player</summary>

| Player       | Description |
|--------------|-------------|
| PLAYER_1     | N/A         |
| PLAYER_2     | N/A         |

</details>
</details>

## How to build & How to run

1. Build:

```shell script
$ ./gradlew build
```

Run:

```shell script
$ java -jar /build/libs/ms-kalaha-api.jar
```