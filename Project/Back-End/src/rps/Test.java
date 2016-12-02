package rps;

import java.util.Random;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stubSystem.out.println(MoveType.ROCK);
	
		RockPaperScissorController controller = RockPaperScissorController.getInstance();
		controller.loadData();
		//controller.printHash();
		
		Scanner in = new Scanner(System.in);
		String match = "";
		        
		for(int i = 0 ; i < 100 ; i++){
			System.out.print("Please enter your move : ");
			String usrInput  = in.nextLine();
			MoveType userMove = MoveType.valueOf(usrInput.toUpperCase());
			//get smartMove 
			MoveType machineMove = controller.getSmartThrow(match);
			System.out.println("Machine thrown : " + machineMove);
			match += userMove;
			match += machineMove;
			System.out.println(match);
			controller.printHash();
		}
		
	}
	


}
