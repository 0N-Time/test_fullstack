import {useState} from "react";


interface GameState {
    game: any;
}

    const TicTacToe = () => {
    const [message, setMessage] = useState<string>('');
    const [gameState, setGameState] = useState<GameState>({
        game: null
    });

        const socket = new WebSocket('ws://localhost:8080/game-progress');

        socket.onopen = () => {
            console.log('WebSocket connection established.');
        };

        socket.onmessage = (event) => {
            console.log('Received message:', event.data);
        };

        socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        socket.onclose = () => {
            console.log('WebSocket connection closed.');
        };


    const sendMessage = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const formData = new FormData(event.currentTarget);
        let msg =  formData.get("message");
        console.log(msg);
        socket.send(message);
    };



    return (
        <div>
            <h1>Tic-Tac-Toe</h1>

                <>
                    <form onSubmit={sendMessage}>
                        <label>Test</label>
                        <input type="text" name="message"/>
                        <button type="submit">Send</button>
                    </form>
                </>

            {gameState.game?.id && (
                <>
                    <h2>Tic-Tac-Toe Game {gameState.game?.id}</h2>
                    <table>
                        <tbody>
                        {gameState.game?.gameBoard.split('').map((cell: string, index: number) => (
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