package testing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import rps.Defeat;
import rps.MetaStrategy;
import rps.MoveType;
import rps.Strategy;



public class Test {
	public static int getRandInt(int max)
	{
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		int upperBound = max -1;
		int  n = rand.nextInt(upperBound  + 1);
		return n;
	}

	public static int verifyWinning(MoveType humanMove, MoveType machineMove)
	{
		Defeat dhuman = Defeat.valueOf(humanMove.toString());
		Defeat dmachine = Defeat.valueOf(machineMove.toString());
		if(dhuman == dmachine)
			return 0;
		else if(dhuman.getDefeating().toString().equals(dmachine.toString()))
			return -1;
		else
			return 1;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stubSystem.out.println(MoveType.ROCK);

		String[] possibleMoves = {"R","P","S" };
		String match = "";
		int winning = 0;
		int tie = 0 ;
		int losing = 0;

		MetaStrategy mStrat = new MetaStrategy();

		Strategy strategyPrime = new Strategy(true);
		MoveType prevMachineMove = null;

		System.out.println("===============PLEASE CHOOSE YOUR MOVE AGAINST MACHINE MOVE TO EXPERIMENT==================");
		System.out.println("       1. Type 1: playing the same move");
		System.out.println("       2. Type 2: playing against previous machine move");
		System.out.println("       3. Type 3: playing history sequence move");
		System.out.println("       4. Type 4: playing random move");
		System.out.println("===========================================================================================");

		Scanner scanner = new Scanner(System.in);
		int selector = scanner.nextInt();

		for(int i = 0 ; i < 500 ; i++){
			MoveType userMove = null;

			switch(selector){
			case 1: userMove = MoveType.R;break;
			case 2: {
				if(i == 0 ){
					userMove = MoveType.valueOf(possibleMoves[getRandInt(3)]);
				}else{
					userMove = Strategy.rotate(1, prevMachineMove);
				}
			}break;
			case 3: userMove = strategyPrime.getSmartThrow();break;
			default: userMove = MoveType.valueOf(possibleMoves[getRandInt(3)]);break;
			}
			Thread.sleep(500);
			MoveType machineMove = mStrat.getBestMove();
			if(selector == 2)
				prevMachineMove = machineMove;

			System.out.print("Your move : " + userMove + "  ");
			System.out.println("Machine thrown : " + machineMove);
			int temp = verifyWinning(userMove,machineMove);
			if(temp == 0 )
				tie++;
			else if(temp == 1)
				winning++;
			else
				losing++;
			System.out.println("Score: " + winning + "-" + tie+"-"+losing);
			//strategy.updateStrategy(userMove, machineMove);
			if(selector == 3)
				strategyPrime.updateStrategy(userMove, machineMove);
			mStrat.updatePredictors(userMove);
			mStrat.updateScore(userMove, machineMove);
			match += userMove;
			match += machineMove;
			System.out.println(match);
		}
	}
}
