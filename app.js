var express = require('express');
var bodyParser = require('body-parser');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

var app = express();
var port = process.env.PORT || 1337;

const START = "start";
const ROCK = "r";
const PAPER = "p";
const SCISSORS = "s";
const MaxInputLen = 10;

// body parser middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json())

app.post('/startGame', function(req, res) {
  var userName = req.body.user_name;
  var userID = req.body.user_id;
  var input = req.body.text.trim();
  var userMove = input[input.length-1];
  var responseText;

  if ((input.length != MaxInputLen) ||
      (userMove != ROCK && userMove != PAPER && userMove != SCISSORS))
  {
    responseText = "Invalid input! Please enter [r]ock, [p]aper or [s]cissors.";
  }
  else {
    // Call GetBestMove API with userName
    // Body: { user_id: "12345" }
    var botMove = 's';
    responseText = compare(userMove, botMove);
    // Call Cloudant API with user_id, user_move, bot_move
    // Body: { user_id: "12345", user_move: "R", bot_move: "P" }
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "https://rps-cloudant-db.mybluemix.net/api/update");
    xhr.send(JSON.stringify({user_id:userID, user_move:userMove, bot_move:botMove}));
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

function postMessageToSlack(){
    var xmlhttp = new XMLHttpRequest(),
        webhook_url = url-you-saved-from-before,
        myJSONStr= json-string-from-above;
    xmlhttp.open('POST', webhook_url, false);
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlhttp.send(myJSONStr);
}

// test route
app.get('/', function (req, res) { res.status(200).send('Hello world!'); });

app.listen(port, function () {
  console.log('Listening on port ' + port);
});
app.post('/hello', function (req, res) {
  var userName = req.body.user_name;
  var userID = req.body.user_id;
  var botPayload = {
    text : 'Hello ' + userID + ', welcome to Devdactic Slack channel! I\'ll be your guide.'
  };
  // Loop otherwise..
  if (userName !== 'slackbot') {
    return res.status(200).json(botPayload);
  } else {
    return res.status(200).end();
  }
});
