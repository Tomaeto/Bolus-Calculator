package gui;

import java.time.LocalDateTime;

public abstract class Handler  {
	public abstract int getCurrentCF();
	public abstract int getCurrentIC();
	public abstract int getUpperTarget();
	public abstract void writeBolus(int bg, double bolus, int carbs, LocalDateTime timestamp);
	
}
