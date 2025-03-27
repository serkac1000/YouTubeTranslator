const express = require('express');
const path = require('path');

const app = express();
const PORT = 5000;

// Serve static files
app.use(express.static(path.join(__dirname)));

// All routes should serve the index.html
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

// Start the server
app.listen(PORT, '0.0.0.0', () => {
  console.log(`YouTube Translator Simulator running at http://localhost:${PORT}`);
});