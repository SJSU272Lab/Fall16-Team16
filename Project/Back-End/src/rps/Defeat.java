package rps;

public enum Defeat{
	R(MoveType.P),
	P(MoveType.S),
	S(MoveType.R);
	
	private MoveType defeating;
	
	Defeat(MoveType defeating)
	{
		this.defeating = defeating;
	}
	
	public MoveType getDefeating()
	{
		return defeating;
	}
}