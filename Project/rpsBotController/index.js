var Botkit = require('botkit')
var express = require('express');
var bodyParser = require('body-parser');
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

var app = express();
var port = 3000;
//var token = process.env.SLACK_TOKEN;
var id = "80357499185.114610453777";
var secret = "a3f800b5751b45df83c84b9c9109cc2c";


if (!id || !secret || !port)
{
  console.log('Error: Specify clientId clientSecret and port in environment');
  process.exit(1);
}

var controller = Botkit.slackbot({
  // interactive_replies: true, // tells botkit to send button clicks into conversations
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
  controller.createWebhookEndpoints(controller.webserver);

  controller.createOauthEndpoints(controller.webserver,function(err,req,res) {
    if (err) {
      res.status(500).send('ERROR: ' + err);
    } else {
      res.send('Success!');
    }
  });
});

// just a simple way to make sure we don't
// connect to the RTM twice for the same team
var _bots = {};
function trackBot(bot) {
  _bots[bot.config.token] = bot;
}

// var controller = Botkit.slackbot({
//   // reconnect to Slack RTM when connection goes bad
//   retry: Infinity,
//   debug: false
// })

// Assume single team mode if we have a SLACK_TOKEN
// if (token) {
//   console.log('Starting in single-team mode')
//   controller.spawn({
//     token: token
//   }).startRTM(function (err, bot, payload) {
//     if (err) {
//       throw new Error(err)
//     }
//
//     console.log('Connected to Slack RTM')
//   })
// }

// controller.on('bot_channel_join', function (bot, message) {
//   bot.reply(message, "I'm here!")
// })
//
// controller.hears(['hi'], ['ambient', 'direct_message','direct_mention','mention'], function (bot, message) {
//   bot.reply(message, 'Hello.')
// })

controller.hears(['startrps'],['direct_message','direct_mention','mention'], function(bot,message) {
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
          "value": "s"}
          // {"name": "end",
          //  "text": "End",
          //  "type": "button",
          //  "value": "end"}
        ]
      }
    ]
  }
  bot.reply(message, reply_with_attachments)
})

controller.hears(['end'],['direct_message','direct_mention','mention'], function(bot,message) {
  bot.reply(message, "Game Ended.")
})
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

controller.hears('^stop','direct_message',function(bot,message) {
  bot.reply(message,'Goodbye');
  bot.rtm.close();
});

controller.on(['direct_message','mention','direct_mention'],function(bot,message) {
  bot.api.reactions.add({
    timestamp: message.ts,
    channel: message.channel,
    name: 'robot_face',
  },function(err) {
    if (err) { console.log(err) }
    bot.reply(message,'I heard you loud and clear boss.');
  });
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
