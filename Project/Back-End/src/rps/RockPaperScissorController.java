package rps;

import java.util.ArrayList;
import java.util.HashMap;




public class RockPaperScissorController {
	
	private HashMap<String, Integer> statsMove = new HashMap<String, Integer>();
	private ArrayList<String> nthCurrentMove = new ArrayList<String>();
	
	private static RockPaperScissorController controller ;
	
	
	private RockPaperScissorController(){};
	
	public static RockPaperScissorController getInstance(){
		if (controller == null) {
			controller = new RockPaperScissorController() ;
			return controller ;
		}
		else {
			return controller ;
		}
	}
}
