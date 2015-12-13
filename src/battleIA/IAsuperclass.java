package battleIA;

public class IAsuperclass {
	
	public int currentTeam;
	public IAfunctions functions;
	
	public IAsuperclass(int currentTeam){
		this.currentTeam = currentTeam;
	}
	
	public void update(IAStateOfGame plateau){
		//TODO overide this function
	}

}
