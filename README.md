# Fall16-Team16: ZeroSum
# Waffle.io: https://waffle.io/SJSU272Lab/Fall16-Team16
# App URL: http://rpssm.mybluemix.net/
- To have a good experience, please use this app with Firefox.

# Slack App URL: https://cryptic-ridge-87698.herokuapp.com/login
- That URL will lets our app request authorization to private details in your Slack account without getting your password.
- Use the same URL to wake up the "botrps2" if its status is changed to AWAY.

# Abstract:
With the rapidly expanding of machine learning nowadays, there is a trend to build a powerful machine to  challenge smartest human beings in all fields especially in gaming like chess with AlphaGo from Google or Jeopardy with Watson from IBM.  Likewise, our team will take up this trend to build a machine powered by either machine learning platform such as TensorFlow from Google or Spark Analytic Services from IBM to challenge human being in one of the childhood game called Rock Paper Scissors.


When playing rock paper and scissor games, people always have their own strategies to play such as making “rock” throw frequently or making the same throw twice in a row. The idea of this project is to build a rock paper scissor powered by Apache Spark with a pattern-recognition engine to find out next coming winning move against human players.

# Algorithm
- The strategy that we use in this game is history matching frequency. It will records all the move made so far including our move and the opponent’s. In order to determine the next move to play, it use a substring matching algorithm to determine the most frequently match substring played most of the time. From the frequency, it will guess what opponent will throw next to decide which move it will throw against it.
- Here is an example record of choice sequence with a length of N past moves such as 3, 5, or 7, etc. In this algorithm, we decide to choose N=3. Assume that an existing record of choice sequences saved so far in the game are (opponent throws are underscored): PRP, RPR, PSR, PRP, SPS, PSS, PRS. Supposed the last game played are PR, so we need to guess what the opponent will throw based on our history matching frequency and the last game (due to N=3). With this assumption, we will try to find what the frequencies are with the following pattern SRR, SRP, SRS . We notice that the pattern SRP exists twice in our history of records; therefore, the chance the opponent will throw in his next move will be P. In order to defeat him, we have to throw S which cuts P paper.
Assume that the opponent’s throw will be non-random and he or she does not use dice to determine his choice, so the longer we play, the more record of sequences that we record, and the more chance machine will win over the human. There will be a lot of variant of this strategy such as finding the latest match or all history of matches, etc…
- However, any strategy has its own counter. With this strategy, the counter will be when one record of sequences has a very high frequency then it leads machine to choose same throw for few times until other pattern picks up their own frequencies.  Or another counter of this algorithm is that the opponent will use the same strategy to play against you. Therefore, we need to build a heuristically machine learning algorithm by using a method called meta-strategy. With a strategy you choose, there will be 6 possibilities of choices in this game
      + P0: we will play the move that suggested by your strategy.
      + P1: Assume that your move gets beaten by opponent. You will throw the move that against to your opponent move that beats P0
      + P2: Similarly, you will throw the move that against to the move from the opponent that beats P1
      + P’0: Assume the opponent uses the same strategy P to play. This will have to rotate P0 by 1
      + P’1: try to defeat P’0. Therefore, you can rotate P’0 by 1
      + P’2:  rotate the output from P’1 by 1

- Surely, the output of P’2 will be defeated by P0. A question raised will be among these possibilities called selectors, which choice is the best choice to make against the opponent. We decide to use a method called naïve scoring meaning if the last opponent move is the same as such a selector, we will increase the selector score by 1, decrement its score if it is not matching, and not updating its score if it is a tie. Based on naïve scoring, whose score is the highest will be selected to make a throw against in the next game.

# Experiment
- We experiment by having different strategies to play against our strategy to see how good the algorithm is. Each opponent strategy we have will play in 500 games against our strategy. We try with 4 different strategies such as throwing the same move, throwing the move that beat a previous move, using the same strategy, and play random. The following is the table of our experiment
![snip20161214_1](https://cloud.githubusercontent.com/assets/12701069/21208873/d1f68c6c-c225-11e6-80b4-ce81913f8bf3.png)

# Architecture:
- Using Node.js as middleware
- Using AngularJS and less to build front-end for the application
- Implement a bot plugin for Slack using nodejs
- Back-end is developed by Java Core, Restlet, and Jave Socket
- Cloudant DB will be used to save data
<img width="766" alt="screen shot 2016-12-12 at 4 40 57 pm" src="https://cloud.githubusercontent.com/assets/8787114/21122878/ce9cab48-c089-11e6-95c4-ae3dca621c5e.png">

# Testing Algorithms Meta-Strategy
- Please make sure that you have java in your class path. Then cd into the folder "Back-end > algorithmtesting", then run "make test"
- In order to recompile and run from begining, you can run the following command:
  + cd into the folder "Back-end > algorithmtesting"
  + make clean
  + make test
- Note that when running the test, you will see some delay deliberatedly since we put some sleep time for observation between each game. But you can remove sleep time in Test.java and recompile.
# Info
  - The icons in this app were acquired from the [Noun Project](http://www.thenounproject.com)

# Contact us:
- stevenztruong@yahoo.com
- hoang.luong@sjsu.edu
- tuanungquoc@gmail.com
- xuanquangpham@gmail.com
