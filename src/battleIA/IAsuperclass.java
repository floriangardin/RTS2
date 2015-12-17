package battleIA;

public class IAsuperclass {
	
	public int currentTeam;
	public IAfunctions functions;
	public IAStateOfGame plateau;
	
	public IAsuperclass(int currentTeam){
		this.currentTeam = currentTeam;
	}
	
	public void update(){
		//TODO overide this function
	}
	
	public final void start(IAStateOfGame plateau){
		/**
		 * method to call during the main loop of the game
		 */
		this.plateau = plateau;
		this.functions.plateau = plateau;
		this.functions.update();
		this.update();
	}

}
