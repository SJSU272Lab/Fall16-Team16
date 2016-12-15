package rps;

public interface StrategyInterface {
	public MoveType getSmartThrow();
	public void updateStrategy(MoveType userMove, MoveType machineMove);
}
