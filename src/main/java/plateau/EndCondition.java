package plateau;

import java.io.Serializable;

public abstract class EndCondition implements Serializable{
	public final int LOST = 0;
	public final int WIN = 1;
	public final int NO = 2;
	public abstract boolean hasLost(Plateau p, Team t );
	
	
}
