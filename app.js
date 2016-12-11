var express = require('express');
var bodyParser = require('body-parser');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

var app = express();
var port = process.env.PORT || 1337;

const START = "start";
const ROCK = "r";
const PAPER = "p";
const SCISSORS = "s";

// body parser middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json())

app.post('/startGame', function(req, res) {
  var userName = req.body.user_name;
  var userID = req.body.user_id;
  var input = req.body.text;
  var responseText;
  if (input != ROCK && input != PAPER && input != SCISSORS)
  {
    responseText = "Invalid input! Please enter [r]ock, [p]aper or [s]cissors.";
  }
  else {
    // Call GetBestMove API with userName
    // Body: { user_id: "12345" }
    var botMove = 's';
    responseText = compare(input, botMove);
    // Call Cloudant API with userName, Usermove, Botmove
    // var xhr = new XMLHttpRequest();
    // xhr.open("GET", "https://rps-cloudant-db.mybluemix.net/api/match", false)
    // xhr.send();

    // Body: { user_id: "12345", user_move: "R", botMove: "P" }
  }
  var botPayload = {
    text : responseText
  };

  if (userName !== 'slackbot') {
    return res.status(200).json(botPayload);
  } else {
    return res.status(200).end();
  }
});

function compare (userMove, botMove)
{
  if ((userMove === ROCK && botMove === SCISSORS) ||
      (userMove === PAPER && botMove === ROCK) ||
      (userMove === SCISSORS && botMove === PAPER))
  {
    return "You win!";
  } else if (userMove === botMove) {
    return "Tie!";
  } else {
    return "You lose!";
  }
}
// test route
app.get('/', function (req, res) { res.status(200).send('Hello world!'); });

app.listen(port, function () {
  console.log('Listening on port ' + port);
});
app.post('/hello', function (req, res) {
  var userName = req.body.user_name;
  var botPayload = {
    text : 'Hello ' + userName + ', welcome to Devdactic Slack channel! I\'ll be your guide.'
  };
  // Loop otherwise..
  if (userName !== 'slackbot') {
    return res.status(200).json(botPayload);
  } else {
    return res.status(200).end();
  }
});
