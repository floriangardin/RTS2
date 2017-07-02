package model;
import java.net.InetAddress;

import bot.IA;
import control.Selection;
import menuutils.Menu_Player;
import plateau.Plateau;
import plateau.Team;

public class Player {

	
	public int id;
	private Team gameteam;
	public String nickname;
	
//	public BottomBar bottomBar;
	public boolean isReady;
	//Network
	public InetAddress address;
	// IA
	public IA ia;
	
	// Selection (to be used if current Player)
	public Selection selection;
	
	
	public Player(int id,String name, Team gameteam, Plateau plateau) {
		this.initialize(id, name, gameteam, plateau);
	}

	public Player(Menu_Player mp, Team team, Plateau plateau) {
		// TODO Auto-generated constructor stub
		this.initialize(mp.id, mp.name, team, plateau);
	}

	public void initialize(int id , String name , Team gameteam, Plateau plateau){
		this.id = id;
		this.nickname = name;
		this.gameteam = gameteam;
		this.selection = new Selection(id, plateau);
		
	}
	public int getTeam(){
		return gameteam.id;
	}
	public Team getGameTeam(){
		return gameteam;
	}
	
	public boolean hasRectangleSelection(){
		return this.selection.rectangleSelection!=null;
	}
	
	public void action(){
		if(ia!=null){			
			this.ia.action();
		}	
		
		
//		// Handle madness objectives
//		Vector<Objective> toRemove = new Vector<ObjectiveMadness>();
//		for(Objective m : this.getGameTeam().getObjectiveMadness()){
//			m.action();
//			
//			if(m.isCompleted()){
//				// Add all tech choices 
//				this.getGameTeam().civ.cardSelection.addAll(m.techAllowed);
//				toRemove.add(m);
//			}
//		}
//
//		this.getGameTeam().successObjectives(toRemove);
	}
	
	public void initIA(IA ia){
		this.ia = ia;
		
	}
	public String toString(){
		String s ="";
		s+="id:"+id+";";
		s+="food:"+gameteam.food+";";
		return s;
		
	}
	

	public boolean equals(Player p){
		return this.id==p.id;
	}

	
}
