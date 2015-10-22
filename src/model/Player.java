package model;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import buildings.BuildingHeadQuarters;
import display.BottomBar;
import display.TopBar;
import main.Main;
import units.Character;
import units.UnitsList;

public class Player {
	public Vector<ActionObjet> selection;
	public Vector<Vector<ActionObjet>> groups;
	public Plateau p;
	
	public int id;
	public GameTeam gameteam;
	public int team;
	public String nickname;
	public int groupSelection;
	public BottomBar bottomBar;
	public TopBar topBar;
	public Data data;
	public Player(Plateau p ,int id,String name, GameTeam gameteam) {
		this.id = id;
		this.nickname = name;
		this.p = p;
		this.selection = new Vector<ActionObjet>();
		this.groups = new Vector<Vector<ActionObjet>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<ActionObjet>());
		groupSelection = -1;
		this.gameteam = gameteam;
		this.team = gameteam.id;
		this.data = gameteam.data;
	}
	
	
	
	
	public String toString(){
		String s ="";
		s+="team:"+team+";";
		s+="gold:"+gameteam.gold+";";
		s+="food:"+gameteam.food+";";
		s+="special:"+gameteam.special+";";
		return s;
		
	}
	public void parsePlayer(String s){

		//SEPARATION BETWEEN KEYS
		
		String[] u = s.split(";");
		HashMap<String,String> hs = new HashMap<String,String>();
		for(int i=0;i<u.length;i++){
			String[] r = u[i].split(":");
			hs.put(r[0], r[1]);
		}
		if(hs.containsKey("food")){
			gameteam.food=Integer.parseInt(hs.get("food"));
		}
		if(hs.containsKey("gold")){
			gameteam.gold=Integer.parseInt(hs.get("gold"));
		}
		if(hs.containsKey("special")){
			gameteam.special=Integer.parseInt(hs.get("special"));
		}
		if(hs.containsKey("team")){
			for(GameTeam t : this.p.teams){
				if(t.id == Integer.parseInt(hs.get("team"))){
					this.gameteam = t;
					this.team = t.id;
				}
			}
		}
	}


	
}
