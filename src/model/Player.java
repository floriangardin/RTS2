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
	public int civ;
	public String civName;
	public Vector<ActionObjet> selection;
	public Vector<Vector<ActionObjet>> groups;
	public Plateau p;
	
	public int id;
	public int team;
	public String nickname;
	public int groupSelection;
	public int ennemiesKilled;
	public Data data;
	public int food;
	public int gold;
	public int special;
	public int pop;
	public BottomBar bottomBar;
	public TopBar topBar;
	public BuildingHeadQuarters hq ;
	public Player(Plateau p ,int id,String name, int team,int civ) {
		this.civ  = civ;
		civName = "Dualists";
		this.id = id;
		this.nickname = name;
		this.p = p;
		this.selection = new Vector<ActionObjet>();
		this.groups = new Vector<Vector<ActionObjet>>();
		for(int i=0; i<10; i++)
			this.groups.add(new Vector<ActionObjet>());
		this.team = team;
		food = 0;
		gold = 0;
		pop = 0;
		special = 0;
		groupSelection = -1;
		ennemiesKilled = 0 ;
		this.data = new Data(this.p,this,Main.framerate);
	}
	
	
	public Character create(UnitsList u,float x , float y){
		Character c = this.data.create(u,x,y);
		return c;
	}
	
	public String toString(){
		String s ="";
		s+="team:"+team+";";
		s+="gold:"+gold+";";
		s+="food:"+food+";";
		s+="special:"+special+";";
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
			food=Integer.parseInt(hs.get("food"));
		}
		if(hs.containsKey("gold")){
			gold=Integer.parseInt(hs.get("gold"));
		}
		if(hs.containsKey("special")){
			special=Integer.parseInt(hs.get("special"));
		}
		if(hs.containsKey("team")){
			team=Integer.parseInt(hs.get("team"));
		}
	}


	public void drawMapChoice(Graphics g, float f, float h) {
		g.setColor(Color.black);
		g.drawString("Player "+this.id+" : "+this.nickname, f, h);
		g.fillRect(f+298f, h-2f, 94f,4f+g.getFont().getHeight("W"));
		switch(this.team){
		case 1 : g.setColor(Color.blue);break;
		case 2 : g.setColor(Color.red);break;
		default : g.setColor(Color.black);
		}
		g.fillRect(f+302f, h+2f, 86f, g.getFont().getHeight("W")-4f);
		g.setColor(Color.black);
		g.drawString(civName, f+450f, h);
		
	}
}
