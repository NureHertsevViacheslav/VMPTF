const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: '*',
  },
});

app.get('/', (req, res) => {
  res.send('Socket.IO server is running');
});

let targetNumber = Math.floor(Math.random() * 100) + 1;
let gameLog = [];
let scores = {}; 

io.on('connection', (socket) => {
  console.log('User connected:', socket.id);

  socket.emit('gameLog', gameLog);
  socket.emit('leaderboard', getTopLeaderboard());

  socket.on('guess', ({ playerName, guess }) => {
    let result = '';

    if (guess == targetNumber) {
      result = `${playerName} guessed ${guess} —  Correct!`;
  
      if (!scores[playerName]) scores[playerName] = 0;
      scores[playerName] += 1;

      targetNumber = Math.floor(Math.random() * 100) + 1;

    } else if (guess < targetNumber) {
      result = `${playerName} guessed ${guess} — too low.`;
    } else {
      result = `${playerName} guessed ${guess} — too high.`;
    }

    gameLog.push(result);
    if (gameLog.length > 15) gameLog.shift();

    io.emit('gameLog', gameLog);
    io.emit('leaderboard', getTopLeaderboard());
  });

  socket.on('disconnect', () => {
    console.log('User disconnected:', socket.id);
  });
});

function getTopLeaderboard() {
  return Object.entries(scores)
    .map(([name, score]) => ({ name, score }))
    .sort((a, b) => b.score - a.score)
    .slice(0, 10);
}

server.listen(3001, () => {
  console.log('Server is running on http://localhost:3001');
});
