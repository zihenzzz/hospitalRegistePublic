const express = require('express');
const compression = require('compression');
const app = express();
const port = 80;

app.use(compression());
app.use(express.static('./dist'));

module.exports = app.listen(port, function (err) {
  if (err) {
    console.log(err);
    return;
  }
  console.log('http://localhost:' + port + '\n');
});


