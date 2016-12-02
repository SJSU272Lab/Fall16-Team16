package rps;

public enum MoveType {
	ROCK(0,"R"),
	PAPER(1,"P"),
	SCISSOR(2,"S");
	
	private int rank;
	private String symbol;
	
	MoveType(int rank,String symbol) 
    {
       this.rank= rank;
       this.symbol = symbol;
    }

    public int getRank() 
    {
       return rank;
    }
    
    public String getSymbol()
    {
    	return symbol;
    }
}
