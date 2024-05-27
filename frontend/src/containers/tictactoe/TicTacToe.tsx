import './TicTacToe.css';
import React, { useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";

type Move = {
    coordinateX: number
    coordinateY: number
}

type Game = {
    id: number
    status: "NEW" | "IN_PROGRESS" | "FINISHED"
    gameBoard: string
    winner: "X" | "O" | "TIE" | null
    currentPlayerTurn: "X" | "O" | undefined
    playerX: string
    playerO: string
}

const TicTacToe = () => {
    const [gameId, setGameId] = useState<number>(0);
    const [gameInProgress, setGameInProgress] = useState<boolean>(false);
    const [game, setGame] = useState<Game>({gameBoard: "000000000", id: 0, status: "NEW", winner: null , currentPlayerTurn: undefined, playerX: "Searching...", playerO: "Searching..."});
    const [client] = useState( new Client({
        brokerURL: "ws://localhost:8080/ws",
        debug: function (str: string) {
            console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    }));

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch("/api/games/game/check-for-game", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("jwt"),
                },
            });

            const data = await response.json();
            console.log(data);
            setGameInProgress(data);
        };

        fetchData();
    }, []);

    useEffect(() => {
        client.activate();
        client.onConnect = () => {
            console.log("Connected to WebSocket Server");
        };
        client.onDisconnect = () => {
            console.log("Disconnected from WebSocket Server");
        };
        return () => {
            client.deactivate();
        };
    }, []);

    if (game.id) {
        console.log(game.id)
        client.subscribe("/topic/game/" + game.id, (message) => {
            const newGame: Game = JSON.parse(message.body);
            setGame(newGame);
        });
    }

    const handleSubmitCreateGame = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        const response = await fetch("/api/games/create-game", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
        });

        const data = await response.json();
        console.log(data);
        setGame(data);
    };

    const handleSubmitJoinRandomGame = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        const response = await fetch("/api/games/connect/random", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
        });

        const data = await response.json();
        console.log(data);
        setGame(data);
    }

    const handleSubmitJoinGame = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const response = await fetch(`/api/games/connect/${gameId}`, {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
        });
        const data = await response.json();
        console.log(data);
        setGame(data);
    };

    const handleSubmitReconnect = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        const response = await fetch("/api/games/game-reconnect", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
        });

        const data = await response.json();
        console.log(data);
        setGame(data);
    }

    const handleSubmitSurrender = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        fetch("/api/games/game-surrender", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
        });

    }

    const handleResetGame = () => {
        setTimeout(() => {
            setGame({gameBoard: "000000000", id: 0, status: "NEW", winner: null , currentPlayerTurn: undefined, playerX: "Searching...", playerO: "Searching..."})
            setGameInProgress(false);
        }, 4000);
    };

    const handleCellClick = async (coordinateX: number, coordinateY: number) => {
        if (game.status === "FINISHED") {
            return;
        }

        const move: Move = {
            coordinateX: coordinateX,
            coordinateY: coordinateY,
        };

        await fetch("/api/games/gameLoop", {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwt"),
                "Content-Type": "application/json",
            },
            body: JSON.stringify(move),
        });
    };

    const renderBoard = (game: Game) => {
            return (
                <div>
                    <div>
                        {game.currentPlayerTurn} turn
                    </div>
                    <div id="board" className="table table-bordered">
                        {game.gameBoard.split("").map((cell, index) => (
                            <div key={index} className="cell" onClick={() => {
                                const row = Math.floor(index / 3);
                                const col = index % 3;
                                handleCellClick(row, col);
                            }}>
                                {cell === "0" ? "" : cell === "1" ? "X" : "O"}
                            </div>
                        ))}
                    </div>
                </div>

            );
    };

    const renderSurrenderButton = (game: Game) => {
        if (game.id && game.status != "FINISHED") {
            return (
                <div className="button-container">
                    <button onClick={handleSubmitSurrender}>
                        Leave game
                    </button>
                </div>
            );
        }
    }

    const renderGameOver = (game: Game) => {
        if (!game.winner) {
            return null;
        }
        if (game.winner === "TIE") {
            return (
                <div className="alert alert-info">
                    <span>Its a tie!</span>
                </div>
            );
        } else {
            return (
                <div className="alert alert-info">
                {game.winner === "X" ? "Player X" : "Player O"} wins!
                </div>
            );
        }
    };

    const render = () => {
        if (!game.id && gameInProgress) {
            return (
                <div>
                    <button onClick={handleSubmitCreateGame} className="btn btn-create-game">
                        New Game
                    </button>
                    <form onSubmit={handleSubmitJoinGame}>
                        <label htmlFor="gameId">Game ID:</label>
                        <input
                            type="number"
                            id="gameId"
                            value={gameId}
                            onChange={(event) => setGameId(event.target.valueAsNumber)}
                        />
                        <button type="submit">Join Game</button>
                    </form>
                    <button onClick={handleSubmitJoinRandomGame} className="btn btn-join-random-game">
                        Join Random Game
                    </button>
                    <button onClick={handleSubmitReconnect} className="btn btn-reconnect-game">
                        Reconnect
                    </button>
                </div>
            );
        }
        if (!game.id) {
            return (
                <div>
                    <button onClick={handleSubmitCreateGame} className="btn btn-create-game">
                        New Game
                    </button>
                    <form onSubmit={handleSubmitJoinGame}>
                        <label htmlFor="gameId">Game ID:</label>
                        <input
                            type="number"
                            id="gameId"
                            value={gameId}
                            onChange={(event) => setGameId(event.target.valueAsNumber)}
                        />
                        <button type="submit">Join Game</button>
                    </form>
                    <button onClick={handleSubmitJoinRandomGame} className="btn btn-join-random-game">
                        Join Random Game
                    </button>
                </div>
            );
        }
        if (!game.id) {
            return <div>Loading...</div>;
        }
        if (game.status == "FINISHED") {
            {
                handleResetGame()
            }
        }
        return (
            <>
                <div className="game-information-container">
                    <div id="game-id">
                        Game ID: {game.id}
                    </div>
                    <div id="playerX">
                        X: {game.playerX}
                    </div>
                    <div id="playerO">
                        O: {game.playerO}
                    </div>
                </div>
                {game.id && renderBoard(game)}
                {renderSurrenderButton(game)}
                {renderGameOver(game)}
            </>
        );
    };

    return <div>{render()}</div>;
};

export default TicTacToe;