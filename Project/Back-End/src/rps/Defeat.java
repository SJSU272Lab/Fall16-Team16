package rps;

public class Defeat {
	private String verb;
	private String defeatedID;
	
	public Defeat(String verb, String defeatedID){
		this.verb = verb;
		this.defeatedID = defeatedID;
	}
	
	public String getVerb()
	{
		return verb;
	}
	
	public String getDefeatID()
	{
		return defeatedID;
	}
}
