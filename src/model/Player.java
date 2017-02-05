package model;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import bot.IA;
import control.InputObject;
import madness.Objective;

public class Player {
	public Vector<Objet> selection;

	
	//INPUTS TO BE APPLIED ON THIS ROUND
	public Vector<InputObject> inputs;
	
	public int id;
	private GameTeam gameteam;
	public String nickname;
	
//	public BottomBar bottomBar;
	public boolean isReady;
	//Network
	public InetAddress address;
	// IA
	public IA ia;
	public Player(int id,String name, GameTeam gameteam) {
		this.initialize(id, name, gameteam);
		
	
	}

	public void initialize(int id , String name , GameTeam gameteam){
		this.id = id;
		this.nickname = name;
		this.selection = new Vector<Objet>();
		this.gameteam = gameteam;
		
		this.inputs = new Vector<InputObject>();
	}
	public int getTeam(){
		return gameteam.id;
	}
	public GameTeam getGameTeam(){
		return gameteam;
	}
	public void setTeam(int team){
		
		this.gameteam = Game.g.teams.get(team);
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
	
	public void addToPlay(InputObject in){
		this.inputs.addElement(in);
	}

	public boolean equals(Player p){
		return this.id==p.id;
	}

	
}
