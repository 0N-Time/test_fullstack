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
    winner: "X" | "O" | null
    currentPlayerTurn: "X" | "O" | undefined
}

const TicTacToe = () => {
    const [game, setGame] = useState<Game>({gameBoard: "000000000", id: 0, status: "NEW", winner: null , currentPlayerTurn: undefined});
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

    const renderGameOver = (game: Game) => {
        if (!game.winner) {
            return null;
        }
        return (
            <div className="alert alert-info">
                {game.winner === "X" ? "Player X" : "Player O"} wins!
            </div>
        );
    };

    const render = () => {
        if (!game.id) {
            return (
                <div>
                    <button onClick={handleSubmitCreateGame} className="btn btn-create-game">
                        New Game
                    </button>
                    <button onClick={handleSubmitJoinRandomGame} className="btn btn-join-random-game">
                        Join Random Game
                    </button>
                </div>
            );
        }
        if (!game.id) {
            return <div>Loading...</div>;
        }
        return (
            <>
                {game.id && renderBoard(game)}
                {renderGameOver(game)}
            </>
        );
    };

    return <div>{render()}</div>;
};

export default TicTacToe;