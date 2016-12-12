package rps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Strategy implements StrategyInterface {
	public static MoveType rotate(int time, MoveType type){
		int numberOfMoveTypes = MoveType.values().length;
		return MoveType.values()[(type.getRank()+time)% numberOfMoveTypes];
	}

	private HashMap<String, Integer> statsMove ;
	private ArrayList<String> nthCurrentMove;
	private int[] scores;
	private String history;
	private boolean isPrime = false;
	
	public Strategy(boolean isPrime){
		statsMove = new HashMap<String, Integer>();
		nthCurrentMove = new ArrayList<String>();
		nthCurrentMove.add("RP");
		scores = new int[3];
		history = "";
		this.isPrime = isPrime;
	}
	
	private int getNumberOfConcurrency(String serieOfMoves){
		if(!statsMove.containsKey(serieOfMoves)){
			return 0;
		}else{
			int numberOfConcurrency = statsMove.get(serieOfMoves);
			return numberOfConcurrency;
		}
	}
	
	
	public void loadData(String initialRecords)
	{
		
		for(int i = 0 ; i < nthCurrentMove.size() ; i++)
		{
			for(int j = 0 ; j<initialRecords.length() ; j=j+2){
				if(j == (initialRecords.length() - nthCurrentMove.get(i).length()))
						break;
				else{
					//build a key
					String key = initialRecords.substring(j, j+nthCurrentMove.get(i).length()+1);
					if(!statsMove.containsKey(key)){
						statsMove.put(key, 1);
					}else{
						int numberOfConcurrency = statsMove.get(key);
						statsMove.put(key, numberOfConcurrency+1);
					}
				}
			}
		}
		
		printHash();
	}
	

	public void updateStrategy(MoveType userMove, MoveType robotMove)
	{
		if(userMove == robotMove)
			scores[1]++;
		else if(Defeat.valueOf(userMove.toString()).getDefeating() == robotMove)
			scores[2]++;
		else
			scores[0]++;
		//update history of game
		if(!isPrime)
			history = history + userMove + robotMove;
		else
			history =  history + robotMove + userMove;
		//updateHashMap
		updateHashMap(history);
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
	//return index
	public int getMax(int[]integers){
		//check if all values are equal. if so, will return a random index
		int temp = integers[0];
		boolean isTheSame = true;
		for(int i=0 ; i < integers.length ; i++){
			if(temp!= integers[i])
				isTheSame = false;
				
		}
		
		if(isTheSame){
			return getRandInt(MoveType.values().length);
		}
		
		int max = integers[0];
		int index = 0 ;
		for(int i = 0 ; i < integers.length ; i++){
			if(max < integers[i]){
				max = integers[i];
				index = i;
			}
		}
		return index;
	}
	
	public static int getRandInt(int max)
	{
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		int upperBound = max -1;
		int  n = rand.nextInt(upperBound  + 1);
		return n;
	}
	
	private void updateHashMap(String match)
	{
		for(int i = 0 ; i<nthCurrentMove.size() ; i++){
			if(match.length() >= nthCurrentMove.get(i).length() + 2){
				String theLastNthSet = match.substring(match.length() - (nthCurrentMove.get(i).length()+2));
				String key = theLastNthSet.substring(0, (nthCurrentMove.get(i).length() + 1));
				if(!statsMove.containsKey(key)){
					statsMove.put(key, 1);
				}else{
					int numberOfConcurrency = statsMove.get(key);
					statsMove.put(key, numberOfConcurrency+1);
				}
			}
		}
	}
	
	private MoveType guessNextHumanThrow()
	{
		if(history.length() < 2)
		{
			return MoveType.valueOf("R");
		}else{
			//update to the hash map
			ArrayList<int[]> frequencies = new ArrayList<int[]>();
			ArrayList<String[]> keys = new ArrayList<String[]>();
			
			int start = 2;
			for(int i = 0 ; i < nthCurrentMove.size() ; i++){
				if(history.length() < start)
					break;
				//split match into 2,4,6 depending on nthCurrentMoveSize
				int[] frequency = new int[MoveType.values().length];
				String[] key = new String[MoveType.values().length];
				String split = history.substring( history.length() - nthCurrentMove.get(i).length()); // --> need to fix the last index
				for(int j = 0 ; j < MoveType.values().length; j++){
					//frequencies[i*MoveType.values().length + j] = getNumberOfConcurrency(split + MoveType.values()[j].getSymbol());
					frequency[j] = getNumberOfConcurrency(split + MoveType.values()[j]);
					key[j] = split + MoveType.values()[j];
				}
				frequencies.add(frequency);
				keys.add(key);
				start = start * 2;
			}
			//check the maxIndex of each frequency list then save to hashmap
			int maxFreq = -1;
			int whichlist = -1;
			int subIndex = -1;
			for(int k=0;k<frequencies.size();k++)
			{
				int index = getMax(frequencies.get(k));
				if(maxFreq <= frequencies.get(k)[index]){
					maxFreq = frequencies.get(k)[index];
					whichlist = k;
					subIndex = index;
				}
				
			}
			
			//statsMove.put(keys.get(maxIndex), frequencies.get(maxIndex));
			return MoveType.values()[subIndex];
		}
	}
	
	
	public MoveType getSmartThrow()
	{
		MoveType guessedNextHumanThrow = guessNextHumanThrow();
		return Defeat.valueOf(guessedNextHumanThrow.toString()).getDefeating();
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
