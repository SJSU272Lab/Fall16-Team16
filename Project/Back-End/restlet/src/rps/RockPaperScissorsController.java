package rps;

import java.io.IOException;
import java.util.HashMap;

import rps.LoadDB.RPSdata;


public class RockPaperScissorsController {
	
	private HashMap<String, MetaStrategy> games = new HashMap<String, MetaStrategy>();
	private static RockPaperScissorsController controller ;
	
	
	private RockPaperScissorsController(){};
	
	public static RockPaperScissorsController getInstance(){
		if (controller == null) {
			controller = new RockPaperScissorsController() ;
			return controller ;
		}
		else {
			return controller ;
		}
	}
	
	public MoveType getSmartMove(String user_id) throws IOException
	{
		//check to see if user_id exist
		MetaStrategy mStrategy ;
		if(games.containsKey(user_id)){
			//get that object
			mStrategy = (MetaStrategy) games.get(user_id);
			
		}else{
			//get the match from cloudant to load data
			mStrategy = new MetaStrategy();
			//check to get history of player
			LoadDB cloudantData = new LoadDB();
			cloudantData.connect();	
			RPSdata data = cloudantData.readRPSDB(user_id);
			if(data != null){
				mStrategy.loadHistory(data.getMatch());
			}
			games.put(user_id, mStrategy);
		
		}
		MoveType machineMove = mStrategy.getBestMove();
		return machineMove;
	}
	
	
	public void updateMove(String user_id,String userMove, String smartMove)
	{
		if(games.containsKey(user_id)){
			//get that object
			
			MetaStrategy mStrategy = (MetaStrategy) games.get(user_id);
			mStrategy.updatePredictors(MoveType.valueOf(userMove));
			mStrategy.updateScore(MoveType.valueOf(userMove), MoveType.valueOf(smartMove));
		}
	}
	public void removeInstance(String user_id)
	{
		if(games.containsKey(user_id))
			games.remove(user_id);
	}
	
	public String getMatch(String user_id)
	{
		return ((MetaStrategy)games.get(user_id)).getMatch();
	}
	
}
