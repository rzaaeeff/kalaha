init();

var game = null;

// ================== UI operations ==================

function init() {
    let url = new URL(window.location.href);
    let id = url.searchParams.get("game-id");

    if (id != null && id != undefined && id.trim().length > 0 && id != "undefined") {
        document.getElementById("game-id").value = id;
        loadGame(id);
    }
}

function showResult(text) {
    document.getElementById("game-container").style.display = 'none';
    document.getElementById("result-container").style.display = 'block';
    document.getElementById("result-text").innerHTML = text;
}

function decorate() {
    switch (game["status"]) {
        case "ONGOING":
            document.getElementById("game-container").style.display = "block";
            
            // decorate houses
            for (let i = 0; i < game["houses"].length; i++) {
                document.getElementById("h" + i).innerHTML = game["houses"][i];
            }

            // decorate stores
            for (let i = 0; i < game["stores"].length; i++) {
                document.getElementById("s" + i).innerHTML = game["stores"][i];
            }

            // show active player
            var activePlayer = document.getElementById("p" + 1);
            var nonActivePlayer = document.getElementById("p" + 2);

            if (game["activePlayerId"] == "PLAYER_2") {
                var temp = activePlayer;
                activePlayer = nonActivePlayer;
                nonActivePlayer = temp;
            }

            activePlayer.style.color = "seagreen";
            activePlayer.style.fontWeight = "bold";
            nonActivePlayer.style.color = "black";
            nonActivePlayer.style.fontWeight = "normal";
            break;

        case "DRAW":
            showResult("The game is finished with a draw.");
            break;

        case "PLAYER_1_WIN":
            showResult("The game is finished with a victory of Player 1.");
            break;
        
        case "PLAYER_2_WIN":
            showResult("The game is finished with a victory of Player 2.");
            break;
    }
}

function showUknownError() {
    alert("Unknown error occurred! Please, try again later!");
}

function showNotFoundError() {
    alert("Game could not be found!");
}

function showIllegalMoveError(msg) {
    alert(msg);
}

// ================== Client calls ==================

function createNewGame() {
    fetch(`http://localhost:8080/v1/games`, {
        method: "POST",
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: null
    }).then(res => {
        if (res.status == 201) {
            res.json().then(json => {
                let id = json["id"];
                if (id != null && id != undefined) {
                    window.location.href = `../index.html?game-id=${id}`;
                } else {
                    showUknownError();
                }
            });
        } else {
            showUknownError();
        }
    });
}

function loadGame(id) {
    fetch(`http://localhost:8080/v1/games/${id}`, {
        method: "GET",
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: null
    }).then(res => {
        if (res.status == 200) {
            res.json().then(json => {
                game = json;
                decorate(json);
            });
        } else if (res.status == 404) {
            showNotFoundError();
        } else {
            showUknownError();
        }
    });
}

function move(pitId) {
    let activePlayerId =  game['activePlayerId'];
    if (activePlayerId == "PLAYER_2") pitId -= 6;
    let body = { pitId: pitId, playerId: activePlayerId};
    fetch(`http://localhost:8080/v1/games/${game["id"]}/moves`, {
        method: "POST",
        mode: 'cors',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    }).then(res => {
        if (res.status == 201) {
            res.json().then(json => {
                game = json;
                decorate(json);
            });
        } else if (res.status == 400) {
            res.json().then(json => {
                showIllegalMoveError(json["message"]);
            });
        } else if (res.status == 404) {
            showNotFoundError();
        } else {
            showUknownError();
        }
    });
}