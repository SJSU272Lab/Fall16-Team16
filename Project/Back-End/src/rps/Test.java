package rps;

import java.util.Random;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		RockPaperScissorController controller = RockPaperScissorController.getInstance();
		controller.loadData();
		Scanner in = new Scanner(System.in);
		String match = "";
		        
		for(int i = 0 ; i < 100 ; i++){
			System.out.print("Please enter your move : ");
			String usrInput  = in.nextLine();
			MoveType userMove = MoveType.valueOf(usrInput.toUpperCase());
			//get smartMove 
			MoveType machineMove = controller.getSmartMove(match);
			System.out.println("Machine thrown : " + machineMove);
			match += userMove.getSymbol();
			match += machineMove.getSymbol();
			controller.printHash();
		}
	}

}
