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
    winner: "X" | "O" | undefined
    turn: boolean | undefined
}

const TicTacToe = () => {
    const [game, setGame] = useState<Game>({gameBoard: "000000000", id: 0, status: "NEW", winner: undefined , turn: undefined});
    const client = new Client({
        brokerURL: "ws://localhost:8080/ws",
        debug: function (str: string) {
            console.log(str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    });

    useEffect(() => {
        client.activate();
        client.onConnect = () => {
            console.log("Connected to WebSocket Server");
            consumeGameUpdates();
        };
        client.onDisconnect = () => {
            console.log("Disconnected from WebSocket Server");
        };
        return () => {
            client.deactivate();
        };
    }, []);

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const response = await fetch("/api/games/create-game", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        });

        const data = await response.json();
        setGame(data);
        consumeGameUpdates()
    };

    const consumeGameUpdates = () => {
        client.subscribe("/topic/game/" + game.id, (message) => {
            const gameBoard: string = JSON.parse(message.body);
            setGame({...game, gameBoard: gameBoard});
        });
    };

    const handleCellClick = (coordinateX: number, coordinateY: number) => {
        if (game.status === "FINISHED") {
            return;
        }
        const move: Move = {
            coordinateX,
            coordinateY,
        };
        fetch()
    };

    const renderBoard = (gameRoom: Game) => {
        return (
            <table id="board" className="table table-bordered">
                <tbody>
                {gameRoom.board.map((row, rowIndex) => (
                    <tr key={rowIndex}>
                        {row.map((cell, colIndex) => (
                            <td key={colIndex} onClick={() => handleCellClick(rowIndex, colIndex)}>
                                {cell}
                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    const renderGameOver = (gameRoom: Game) => {
        if (!gameRoom.winner) {
            return null;
        }
        return (
            <div className="alert alert-info">
                {gameRoom.winner === "X" ? "Player X" : "Player O"} wins!
            </div>
        );
    };

    const render = () => {
        if (!gameRoomId) {
            return (
                <div>
                    <button onClick={handleNewGame} className="btn btn-primary">
                        New Game
                    </button>
                </div>
            );
        }
        if (!gameRoom) {
            return <div>Loading...</div>;
        }
        return (
            <>
                {renderBoard(gameRoom)}
                {renderGameOver(gameRoom)}
            </>
        );
    };

    return <div>{render()}</div>;
};

export default TicTacToe;