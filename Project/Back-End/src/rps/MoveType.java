package rps;

public enum MoveType {
	R(0,"Rock"),
	P(1,"Paper"),
	S(2,"Scissor");
	
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


