# Fall16-Team16: ZeroSum
# Waffle.io: https://waffle.io/SJSU272Lab/Fall16-Team16

# Abstract:
With the rapidly expanding of machine learning nowadays, there is a trend to build a powerful machine to  challenge smartest human beings in all fields especially in gaming like chess with AlphaGo from Google or Jeopardy with Watson from IBM.  Likewise, our team will take up this trend to build a machine powered by either machine learning platform such as TensorFlow from Google or Spark Analytic Services from IBM to challenge human being in one of the childhood game called Rock Paper Scissors. 


When playing rock paper and scissor games, people always have their own strategies to play such as making “rock” throw frequently or making the same throw twice in a row. The idea of this project is to build a rock paper scissor powered by Apache Spark with a pattern-recognition engine to find out next coming winning move against human players.

# Requirements:

- As a registered user, I should have
   + A feature in the app so that I can play unlimited against machine and,
   + A feature to show the history of each game
- As administrator, I should have:
   + All features of a registered user,
   + A feature in the app so that I can see statistic of all games played
   + A feature to show the throw decision of the machine
- As a guest, I can only play maximum 3 games.
    
# App personas:
- Registered user(s)
- Administrator 
- Guest(s)

# Architecture:
- Using Node.js as middleware
- Using AngularJS and jQuery to build front-end for the application
- Spark Streaming will collect human’s throw while playing and save it as archive in the object store for machine’s deep learning to make a smart throw.
- Backend will be powered either by Spark Core or by TensorFlow to help suggest which throw is good to make against human’s throw.
- Cloudant DB will be used to save data
![snip20161026_24](https://cloud.githubusercontent.com/assets/12701069/19754643/33584b7c-9bc4-11e6-8064-787a93919664.png)
