package data;

public class AttributsChange implements java.io.Serializable {
	
	public Attributs attribut;
	public Change change;
	public float value;
	public float remainingTime;
	public boolean endless;
	public boolean usageUnique;
	
	public AttributsChange(Attributs attribut, Change change, float value, float remainingTime) {
		this.attribut = attribut;
		this.change = change;
		this.value = value;
		this.remainingTime = remainingTime;
		this.endless = remainingTime==0;
	}
	public AttributsChange(Attributs attribut, Change change, float value, boolean usageUnique) {
		this.attribut = attribut;
		this.change = change;
		this.value = value;
		this.usageUnique = usageUnique;
		this.endless = true;
	}
	
	
	public float apply(float value){
		switch(change){
		case ADD : return value + this.value;
		case MUL : return value * this.value;
		case SET : return this.value;
		default : return value;
		}
	}


	public enum Change{
		ADD,
		MUL,
		SET;
	}

}
