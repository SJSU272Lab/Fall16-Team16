package rps;

public class MetaStrategy {

	private int[] predictScores = new int[6];
	private MoveType[]predict = new MoveType[6];
	private Strategy strategy = new Strategy(false);
	private Strategy strategyPrime = new Strategy(true);
	private String match = "" ;
	public MetaStrategy()
	{
		for(int i = 0 ; i < 6 ; i++)
			predictScores[i] = 0;
	}
	
	public void loadHistory(String history)
	{
		strategy.loadData(history);
	}
	
	public void updatePredictors(MoveType userMove)
	{
		for(int i = 0 ; i < predict.length ; i++)
		{
			if(userMove == predict[i]){
			}else if(Defeat.valueOf(userMove.toString()).getDefeating() == predict[i]){
				//increate predictScore at i by 1
				//predictScores[i] = predictScores[i] .8
				predictScores[i]++;
			}else{
				// decrease predictScore at i by -1
				predictScores[i]--;
			}
		}
	}
	
	public void getMoveForAllPredictors()
	{
		predict[0] = strategy.getSmartThrow();
		predict[1] = Strategy.rotate(2, predict[0]);
		predict[2] = Strategy.rotate(1, predict[0]);
		predict[3] = Strategy.rotate(1, strategyPrime.getSmartThrow());
		predict[4] = Strategy.rotate(1, predict[3]);
		predict[5] = Strategy.rotate(1, predict[4]);
	}
	
	public void updateScore(MoveType userMove, MoveType robotMove)
	{
		strategy.updateStrategy(userMove, robotMove);
		strategyPrime.updateStrategy(userMove, robotMove);
		match = match + userMove + robotMove;
	}
	
	public String getMatch()
	{
		return match;
	}
	
	private int getBestPredictor()
	{
		int bestScore = predictScores[0];
		int maxIndex = 0;
		for(int i = 1 ; i < 6 ; i++){
			if(bestScore < predictScores[i])
			{
				maxIndex = i;
				bestScore = predictScores[i];
			}
		}
		return maxIndex;
	}
	
	public MoveType getBestMove()
	{
		getMoveForAllPredictors();
		int bestIndex = getBestPredictor();
		return predict[bestIndex];
	}
}
