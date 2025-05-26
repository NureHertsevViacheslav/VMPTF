import React, { useState, useEffect } from 'react';
import io from 'socket.io-client';
import './App.css';

const socket = io('http://localhost:3001');

function App() {
  const [name, setName] = useState('');
  const [guess, setGuess] = useState('');
  const [gameLog, setGameLog] = useState([]);
  const [leaderboard, setLeaderboard] = useState([]);

  useEffect(() => {
    socket.on('gameLog', (log) => setGameLog(log));
    socket.on('leaderboard', (data) => setLeaderboard(data));

    return () => {
      socket.off('gameLog');
      socket.off('leaderboard');
    };
  }, []);

  const submitGuess = () => {
    if (!name || !guess) return;
    socket.emit('guess', {
      playerName: name,
      guess: parseInt(guess),
    });
    setGuess('');
  };

  return (
    <div className="App">
      <h1>Guess the Number</h1>

      <input
        type="text"
        placeholder="Your name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <input
        type="number"
        placeholder="Guess number (1-100)"
        value={guess}
        onChange={(e) => setGuess(e.target.value)}
      />
      <button onClick={submitGuess}>Submit</button>

      <h2>Game Log</h2>
      <ul>
        {gameLog.map((entry, i) => (
          <li key={i}>{entry}</li>
        ))}
      </ul>

      <h2>Leaderboard</h2>
      <ol>
        {leaderboard.map((entry, i) => (
  <li key={i}>
    {entry.name} — {entry.score} guessed
  </li>
))}
      </ol>
    </div>
  );
}

export default App;
