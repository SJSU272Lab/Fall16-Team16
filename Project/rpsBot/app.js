var express = require('express');
var bodyParser = require('body-parser');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var app = express();
var port = process.env.PORT || 1337;
var myMap = new Map();
const START = "start";
const END = "end";
const ROCK = "r";
const PAPER = "p";
const SCISSORS = "s";
const MaxInputLen = 10;
myMap.set(ROCK, ":fist:");
myMap.set(PAPER, ":hand:");
myMap.set(SCISSORS, ":v:");

// body parser middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json())

app.post('/start', function(req, res) {
  postMessageToSlack();
});
app.post('/game', function(req, res) {
  // var userID = req.body.user.id;
  // var userName = req.body.user.name;
  // var value = req.body.actions[0].value;
  var payload = JSON.parse(req.body.payload);
  var userName = payload.user.name;
  var userID = payload.user.id;
  var userMove = payload.actions[0].value;
  var responseText;
	var xhr = new XMLHttpRequest();

    // Call GetBestMove API with userName
    // Body: { user_id: "12345" }

    // Call Cloudant API with user_id, user_move, bot_move
    // Body: { user_id: "12345", user_move: "R", bot_move: "P" }
  xhr.open("POST", "http://169.44.10.31:8080/rps", false);
  //xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	xhr.send(JSON.stringify({ user_id:userID }));
  //xhr.send(JSON.stringify({user_id:userID, user_move:userMove, bot_move:botMove}));
  console.log(xhr.responseText);
  console.log(typeof xhr.responseText);
	const responseJSON = JSON.parse(xhr.responseText);
	const botMove = responseJSON.smartMove[0];
  responseText = compare(userMove, botMove);

	// Update smartMove hashmap
	xhr.open("PUT", "http://169.44.10.31:8080/rps", false);
	const updateBody = { user_id: userID, userMove: userMove.toUpperCase(), smartMove: botMove.toUpperCase() };
	// console.log(updateBody);
	xhr.send(JSON.stringify(updateBody));

	//console.log(JSON.parse(xhr.responseText));
	// xhr.open("POST", "http://169.44.10.31:8080/end", true);
	// xhr.send(JSON.stringify({ user_id:userID }));

  var botPayload = {
    text : responseText
  };

  if (userName !== 'slackbot') {
    return res.status(200).json(botPayload);
  } else {
    return res.status(200).end();
  }
//   var myJSONStr = '{"test":"success"}';
//
// xmlhttp.open('POST', webhook_url, false);
// xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
// xmlhttp.send(myJSONStr);
});

function compare (userMove, botMove)
{
  var result;
  if ((userMove === ROCK && botMove === SCISSORS) ||
      (userMove === PAPER && botMove === ROCK) ||
      (userMove === SCISSORS && botMove === PAPER))
  {
     result = ". You win!";
  } else if (userMove === botMove) {
    result = ". Tie!";
  } else {
    result =  ". You lose!";
  }
  return myMap.get(userMove) + " vs " + myMap.get(botMove) + result;
}

function postMessageToSlack(){
    var xmlhttp = new XMLHttpRequest(),
        webhook_url = "https://hooks.slack.com/services/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",
        // myJSONStr = 'payload= {'+
        //             '"username": "botrps",' +
        //             '"icon_url": "example.com/img/icon.jpg",' +
        // myJSONStr = '{"text": "My testing rps" , "attachments": [{' +
        //             '"fallback": "This attachement isnt supported.",' +
        //             '"title": "VALENTINE DAY OFFER",' +
        //             '"color": "#9C1A22",' +
        //             '"pretext": "Todays list of awesome offers picked for you",' +
        //             '"author_name": "Preethi",' +
        //             '"author_link": "http://www.hongkiat.com/blog/author/preethi/",' +
        //             '"author_icon": "http://media05.hongkiat.com/author/preethi.jpg",' +
        //             '"fields": [{' +
        //             '"title": "Sites",' +
        //             '"value": "_<http://www.amazon.com|Amazon>_\n_<http://www.ebay.com|Ebay>_",' +
        //             '"short": true' +
        //             '}, {' +
        //             '"title": "Offer Code",' +
        //             '"value": "UI90O22\n-",' +
        //             '"short": true' +
        //             '}],' +
        //             '"mrkdwn_in": ["text", "fields"],' +
        //             '"text": "Just click the site names and start buying. Get *extra reduction with the offer code*, if provided.",' +
        //             '"thumb_url": "http://example.com/thumbnail.jpg" }]}';
        myJSONStr = '{"attachments": [' +
                     '{"text": "Please pick one:",' +
                      '"fallback": "You are unable to pick",' +
                      '"color": "#9C1A22",' +
                    //  '"attachment_type": "default",' +
                      '"actions": [' +
                       '{"name": "rock",' +
                        '"text": ":fist:",' +
                        '"type": "button",' +
                        '"value": "r"},' +
                        '{"name": "paper",' +
                         '"text": ":hand:",' +
                         '"type": "button",' +
                         '"value": "p"},' +
                         '{"name": "scissors",' +
                          '"text": ":v:",' +
                          '"type": "button",' +
                          '"value": "s"}]}]}';
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
  var message = JSON.stringify(req.body);
  // var userName = req.body.user_name;
  // var userID = req.body.user_id;
  var botPayload = {
    text : "hello " + message
  };
  // Loop otherwise..
  if (userName !== 'slackbot') {
    return res.status(200).json(botPayload);
  } else {
    return res.status(200).end();
  }
});
