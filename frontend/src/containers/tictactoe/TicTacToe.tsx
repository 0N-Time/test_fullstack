import { useState, useEffect } from 'react';


interface GameState {
    game: any;
    xIsNext: boolean;
}

    const TicTacToe = () => {
    const [gameState, setGameState] = useState<GameState>({
        game: null,
        xIsNext: true,
    });

    useEffect(() => {
        if (gameState.game.id) {
            const fetchData = async () => {
                const response = await fetch(`/api/games/game-progress/${gameState.game.id}`);
                const result = await response.json();
                setGameState({
                    game: result,
                    xIsNext: result.playerOne.id === Number(localStorage.getItem('userId')),
                });
            };

            fetchData();

            const ws = new WebSocket(`ws://${window.location.host}/api/games/ws/${gameState.game.id}`);

            ws.onmessage = (event) => {
                const result = JSON.parse(event.data);
                setGameState({
                    game: result,
                    xIsNext: result.playerOne.id === Number(localStorage.getItem('userId')),
                });
            };

            return () => {
                ws.close();
            };
        }
    }, [gameState]);

    const createGame = async () => {
        const jwt = localStorage.getItem('jwt');

        if (jwt) {
            const response = await fetch('/api/games/create-game', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`,
                },
            });

            const result = await response.json();
            setGameState({
                game: result,
                xIsNext: result.playerOne.id === Number(localStorage.getItem('userId')),
            });
        }
    };

    const joinRandomGame = async () => {
        const jwt = localStorage.getItem('jwt');

        if (jwt) {
            const response = await fetch('/api/games/connect/random', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${jwt}`,
                },
            });

            const result = await response.json();
            setGameState({
                game: result,
                xIsNext: result.playerOne.id === Number(localStorage.getItem('userId')),
            });
        }
    };

    return (
        <div>
            <h1>Tic-Tac-Toe</h1>
            {!gameState.game.id && (
                <>
                    <button onClick={createGame}>Create Game</button>
                    <button onClick={joinRandomGame}>Join Random Game</button>
                </>
            )}
            {gameState.game.id && (
                <>
                    <h2>Tic-Tac-Toe Game {gameState.game.id}</h2>
                    <table>
                        <tbody>
                        {gameState.game.gameBoard.split('').map((cell: string, index: number) => (
                            <td key={index}>
                                {cell === '0' ? '' : (
                                    cell === '1' ? 'X' : 'O'
                                )}
                            </td>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
};

export default TicTacToe;