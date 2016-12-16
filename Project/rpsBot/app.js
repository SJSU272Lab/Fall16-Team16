var Botkit = require('botkit')
var port = process.env.PORT;
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var id = "80357499185.114610453777";
var secret = "a3f800b5751b45df83c84b9c9109cc2c";
var localbot, localmessage;
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

// if (!id || !secret || !port)
// {
//   console.log('Error: Specify clientId clientSecret and port in environment');
//   process.exit(1);
// }

var controller = Botkit.slackbot({
  // interactive_replies: true, // tells botkit to send button clicks into conversations
  interactive_replies: true,
  retry: Infinity,
  json_file_store: './db_slackbutton_bot/',
  // rtm_receive_messages: false, // disable rtm_receive_messages if you enable events api
}).configureSlackApp(
  {
    clientId: id,
    clientSecret: secret,
    scopes: ['bot'],
  }
);

controller.setupWebserver(port,function(err,webserver) {
  startGame(webserver);
  controller.createWebhookEndpoints(controller.webserver);

  controller.createOauthEndpoints(controller.webserver,function(err,req,res) {
    if (err) {
      res.status(500).send('ERROR: ' + err);
    } else {
      res.send('Success!');
    }
  });
});

function startGame(app)
{
  app.post('/game', function(req, res) {
    var payload = JSON.parse(req.body.payload);
    var userName = payload.user.name;
    var userID = payload.user.id;
    var userMove = payload.actions[0].value;
    var responseText;
  	var xhr = new XMLHttpRequest();

    if (userMove === END)
    {
      //console.log(JSON.parse(xhr.responseText));
      xhr.open("POST", "http://169.44.10.31:8080/end", true);
      xhr.send(JSON.stringify({ user_id:userID }));
      return res.status(200).send("Goodbye. Thanks for playing!");
    }
      // Call GetBestMove API with userName
      // Body: { user_id: "12345" }

      // Call Cloudant API with user_id, user_move, bot_move
      // Body: { user_id: "12345", user_move: "R", bot_move: "P" }
    xhr.open("POST", "http://169.44.10.31:8080/rps", false);
    //xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  	xhr.send(JSON.stringify({ user_id:userID }));
    //xhr.send(JSON.stringify({user_id:userID, user_move:userMove, bot_move:botMove}));
  	const responseJSON = JSON.parse(xhr.responseText);
  	const botMove = responseJSON.smartMove[0];
    responseText = compare(userMove, botMove);

  	// Update smartMove hashmap
  	xhr.open("PUT", "http://169.44.10.31:8080/rps", false);
  	const updateBody = { user_id: userID, userMove: userMove.toUpperCase(), smartMove: botMove.toUpperCase() };
  	// console.log(updateBody);
  	xhr.send(JSON.stringify(updateBody));

    var botPayload = {
      text : responseText
    };

    if (userName !== 'slackbot') {
      console.log(localmessage);
      console.log(typeof localmessage);
      displayGame(localbot,localmessage);
      return res.status(200).json(botPayload);
    } else {
      return res.status(200).end();
    }
  });
}

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
// just a simple way to make sure we don't
// connect to the RTM twice for the same team
var _bots = {};
function trackBot(bot) {
  _bots[bot.config.token] = bot;
}

controller.hears(['^rps$'],['direct_message','direct_mention','mention'], function(bot,message) {
  displayGame(bot, message);
});

function displayGame(bot, message)
{
  localbot = bot;
  localmessage = message;
  var reply_with_attachments = {
    'attachments': [
      {
        'fallback': 'Not able to pick.',
        'text': "Please pick one:",
        'callback_id':'myGameRPS',
        'color': '#9C1A22',
        "actions": [
          {"name": "rock",
          "text": ":fist:",
          "type": "button",
          "value": "r"},
          {"name": "paper",
          "text": ":hand:",
          "type": "button",
          "value": "p"},
          {"name": "scissors",
          "text": ":v:",
          "type": "button",
          "value": "s"},
          {"name": "end",
          "text": "End",
          "type": "button",
          "value": "end"}
        ]
      }
    ]
  };
  bot.reply(message, reply_with_attachments);
}

controller.on('create_bot',function(bot,config) {

  if (_bots[bot.config.token]) {
    // already online! do nothing.
  } else {
    bot.startRTM(function(err) {

      if (!err) {
        trackBot(bot);
      }

      bot.startPrivateConversation({user: config.createdBy},function(err,convo) {
        if (err) {
          console.log(err);
        } else {
          convo.say('I am a bot that has just joined your team');
          convo.say('You must now /invite me to a channel so that I can be of use!');
        }
      });

    });
  }

});

// Handle events related to the websocket connection to Slack
controller.on('rtm_open',function(bot) {
  console.log('** The RTM api just connected!');
});

controller.on('rtm_close',function(bot) {
  console.log('** The RTM api just closed');
  // you may want to attempt to re-open
});

controller.on(['direct_message','mention','direct_mention'],function(bot,message) {
  bot.reply(message,'I cannot understand. Valid input is "rps"');
});

controller.storage.teams.all(function(err,teams) {

  if (err) {
    throw new Error(err);
  }

  // connect all teams with bots up to slack!
  for (var t  in teams) {
    if (teams[t].bot) {
      controller.spawn(teams[t]).startRTM(function(err, bot) {
        if (err) {
          console.log('Error connecting bot to Slack:',err);
        } else {
          trackBot(bot);
        }
      });
    }
  }

});
