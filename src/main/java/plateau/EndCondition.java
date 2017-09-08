package plateau;

import java.io.Serializable;

public abstract strictfp class EndCondition implements Serializable{
	
	public enum EndConditions{
		normal,
		units;
	};
	
	public final int LOST = 0;
	public final int WIN = 1;
	public final int NO = 2;
	public abstract boolean hasLost(Plateau p, Team t );
	
	
}
