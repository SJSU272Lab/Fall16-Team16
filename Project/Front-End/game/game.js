'use strict';

var rpslsAppGame = angular.module('rpslsApp.game', [])

   /**
    * Game Manager
    */

   .service( 'gameManager', ['$filter','$timeout', function($filter,$timeout) {
      //machine Move
      var machineMove = {};
      //connection
      var connection = new WebSocket('ws://169.44.126.139:8990');
      connection.onopen = function () {
          // first we want users to enter their names

      };

      connection.onerror = function (error) {
          // just in there were some problems with conenction...

      };

      connection.onmessage = function (message ) {
          connection.token = message.data;
      };


      //save history of match
      var match = [];
      // Create the players
      var players = [
         { id: 'human', name: 'You', chosen: null },
         { id: 'robot', name: 'Robot', chosen: null }
      ];

      // Declare the moves
      var moves = {
         'rock' : { id: 'rock', name: 'Rock', sym:'R', chosen: false, defeats: [
            { verb: 'crushes', id: 'scissors' }
         ]},
         'paper' : { id: 'paper', name: 'Paper', sym:'P',chosen: false, defeats: [
            { verb: 'covers', id: 'rock' }
         ]},
         'scissors' : { id: 'scissors', name: 'Scissors',sym:'S', chosen: false, defeats: [
            { verb: 'cut', id: 'paper' }
         ]}
      };

      // Set the initial scores
      var scores = {
         humanWins: 0,
         ties: 0,
         robotWins: 0
      };
      this.sleep = function(ms)
      {
          return new Promise(resolve => setTimeout(resolve, ms));
      }
      this.getConnection = function(){
        return connection;
      }
      this.getMatch = function(){
        return match;
      }
      // Get the array of player data
      this.getPlayerData = function() {
         return players;
      }

      // Get the array of moves data
      this.getMoveData = function() {
         return moves;
      }

      // Get the array of moves data
      this.getScoreData = function() {
         return scores;
      }

      // Get the human player's selected move
      this.humanChoice = null;

      this.getHumanMove = function() {
         this.moveData = this.getMoveData();
         this.humanChoiceObject = this.moveData[this.humanChoice];

         return this.humanChoiceObject;
      }

      // Pick a random move for the robot player

      this.updateMatch = function(userMove, smartMove)
      {
        var socket = this.getConnection();
        var resthttp = new XMLHttpRequest();
        var url = "http://169.44.13.218:8080/rps";
        resthttp.open("PUT", url, false);
        resthttp.setRequestHeader("Content-type", "application/json");
        resthttp.smartMove = {};
        resthttp.onreadystatechange = function () { //Call a function when the state changes.
          if (resthttp.readyState == 4 && resthttp.status == 200) {

            }
        }
         var socket = this.getConnection();
         var parameters = {
         };
        parameters.user_id = socket.token;
        parameters.userMove = userMove;
        parameters.smartMove = smartMove;
        resthttp.send(JSON.stringify(parameters));
      }

      this.getRobotMove = function() {
         var resthttp = new XMLHttpRequest();
         var url = "http://169.44.13.218:8080/rps";
         resthttp.open("POST", url, false);
         resthttp.setRequestHeader("Content-type", "application/json");
         resthttp.smartMove = {};
         resthttp.onreadystatechange = function () { //Call a function when the state changes.
           if (resthttp.readyState == 4 && resthttp.status == 200) {
                resthttp.smartMove = JSON.parse(resthttp.responseText);
             }
         }
          var socket = this.getConnection();
          var parameters = {
          };
         parameters.user_id = socket.token;
         parameters.match = this.getMatch().join('');
         resthttp.send(JSON.stringify(parameters));
         parameters.match = this.getMatch().join('');
         this.possibleMoves = this.getMoveData();
         this.robotMoveObject = this.possibleMoves[resthttp.smartMove["smartMove"]];
         return this.robotMoveObject;
      }

      // Figure out who won the round and update score
      this.getWinner = function( humanMove, robotMove ) {
         this.moveData = this.getMoveData();
         this.roundWinner = null;
         this.infoText = "Select your first move below:";
         this.match = this.getMatch();
         this.match.push(humanMove.id.substring(0,1).toUpperCase() );
         this.match.push(robotMove.id.substring(0,1).toUpperCase());

         if ( humanMove.id == robotMove.id ) {
            this.roundWinner = 'tie';
            this.infoText = 'That\'s a draw!';
         } else {
            var humanWin = $filter('arrayContains')(humanMove.defeats, robotMove.id);

            if ( humanWin ) {
               this.roundWinner = 'human';
               this.infoText = 'You win! ' + humanMove.name + ' ' + humanWin.verb + ' ' + humanWin.id + '.';
            } else {
               var robotWin = $filter('arrayContains')(robotMove.defeats, humanMove.id);
               this.roundWinner = 'robot';
               this.infoText = 'Robot wins! ' + robotMove.name + ' ' + robotWin.verb + ' ' + robotWin.id + '.';
            }
         }

         return { winner: this.roundWinner, text: this.infoText };
      }
   }])

   // Create a general purpose filter for finding keys in objects
   .filter('arrayContains', function() {
      return function(input, id) {
         for (var i=0; i<input.length; i++) {
            if (input[i].id == id) {
               return input[i];
            }
         }
         return false;
      }
   })

   /**
    * Game
    */

   .controller('gameController', ['gameManager', '$scope', '$timeout', function(gameManager, $scope, $timeout) {
      this.game = gameManager;
      $scope.instructions = "Select your first move below:";
      $scope.firstMove = true;
      $scope.isActive = false;



      this.playMove = function(element){

         // Get player array
         $scope.players = this.game.getPlayerData();

         // Get score data
         $scope.scoreData = this.game.getScoreData();

         // Get each players' chosen move
         $scope.robotMove = this.game.getRobotMove();
         $scope.humanMove = this.game.getHumanMove();
         this.game.updateMatch($scope.humanMove.sym,$scope.robotMove.sym);
         $scope.results = this.game.getWinner( $scope.humanMove, $scope.robotMove );

         // Clear the players' previous move choices
         angular.forEach( $scope.players, function( value, index ) {
            value.chosen = null;
         })

         // Trigger animation, get the winner, and update the score
         $scope.instructions = "One, two, three..." ;
         $scope.firstMove = false;
         $scope.isActive = true;

         $timeout( function(){
            $scope.isActive = false;

            angular.forEach( $scope.players, function( value, index ) {
               if ( value.id == 'human' ) {
                  value.chosen = $scope.humanMove.id;
               } else if ( value.id == 'robot' ) {
                  value.chosen = $scope.robotMove.id;
               }
            })

            if ( $scope.results.winner == 'tie' ) {
               $scope.scoreData.ties += 1;
            } else {
               if ( $scope.results.winner == 'human' ) {
                  $scope.scoreData.humanWins += 1;
               } else {
                  $scope.scoreData.robotWins += 1;
               }
            }
            $scope.instructions = $scope.results.text;

         }, 3000 );
      }
   }])

   /**
    * Players
    */

   .controller('playerController', ['gameManager', function(gameManager) {
      this.players = gameManager.getPlayerData();
   }])

   /**
    * Moves
    */

   .controller('moveController', ['gameManager', function(gameManager) {
      this.moves = gameManager.getMoveData();
   }])

   .directive('playerMoves', function() {
      return {
         restrict: 'E',
         templateUrl: 'game/moves.html',
         scope: true
      };
   })

   /**
    * Scores
    */

   .controller('scoreController', ['gameManager', function(gameManager) {
      this.scores = gameManager.getScoreData();
   }]);
