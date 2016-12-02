package rps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
	
	private int getNumberOfConcurrency(String serieOfMoves){
		if(!statsMove.containsKey(serieOfMoves)){
			return 0;
		}else{
			int numberOfConcurrency = statsMove.get(serieOfMoves);
			return numberOfConcurrency;
		}
	}
	
	public void loadData()
	{
		if(controller!=null){
			controller.nthCurrentMove.add("RP");
			controller.nthCurrentMove.add("PPRP");
			controller.statsMove.put("RRP", 1);
			controller.statsMove.put("PPS", 1);
			controller.statsMove.put("SSR", 1);
		}
	}
	public static int getMaxCurr (int concurr1, int concurr2, int concurr3)
	{
		return 1; // will return either 0,1, or 2 corresponding to Rock, Paper, Scissor
	}
	
	public int getMaxFreqIndex(Object[] integers)
	{
		int max = (Integer)integers[0];
		int index = 0 ;
		for(int i = 0 ; i < integers.length ; i++){
			if(max < (Integer)integers[i]){
				max = (Integer)integers[i];
				index = i;
			}
		}
		return index;
	}
	
	public MoveType getSmartMove(String match)
	{
		
		if(match.length() < 2)
		{
			//pick a random between 3 moves Rock, Paper, or Scissors
			Random rand = new Random();
			rand.setSeed(System.currentTimeMillis());
			int upperBound = MoveType.values().length -1;
			int  n = rand.nextInt(upperBound  + 1);
			return MoveType.values()[n];
		}else{
			
			ArrayList<Integer> frequencies = new ArrayList<Integer>();
			ArrayList<String> keys = new ArrayList<String>();
			//new int[nthCurrentMove.size() * MoveType.values().length];
			//String[] keys = new String[nthCurrentMove.size() * MoveType.values().length];
			int start = 2;
			for(int i = 0 ; i < nthCurrentMove.size() ; i++){
				if(match.length() < start)
					break;
				//split match into 2,4,6 depending on nthCurrentMoveSize
				String split = match.substring( match.length() - start ,  match.length()); // --> need to fix the last index
				for(int j = 0 ; j < MoveType.values().length; j++){
					//frequencies[i*MoveType.values().length + j] = getNumberOfConcurrency(split + MoveType.values()[j].getSymbol());
					frequencies.add(getNumberOfConcurrency(split + MoveType.values()[j].getSymbol()));
					keys.add(split + MoveType.values()[j].getSymbol());
				}
				start = start * 2;
			}
			
			int maxIndex = getMaxFreqIndex(frequencies.toArray());
			//need to update in hash table
			if(!statsMove.containsKey(keys.get(maxIndex))){
				statsMove.put(keys.get(maxIndex), 1);
			}else{
				int numberOfConcurrency = statsMove.get(keys.get(maxIndex));
				statsMove.put(keys.get(maxIndex), numberOfConcurrency+1);
			}
			//statsMove.put(keys.get(maxIndex), frequencies.get(maxIndex));
			return MoveType.values()[maxIndex % MoveType.values().length];
		}
	}
	
	public void printHash()
	{
		System.out.println("========================");
		for (String key: statsMove.keySet()){
            Integer value = statsMove.get(key);  
            System.out.println(key + " " + value);  
		} 
		System.out.println("========================");
	}
	
}
