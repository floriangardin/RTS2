package model;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import multiplaying.InputObject;
import display.BottomBar;
import display.TopBar;

public class Player {
	public Vector<ActionObjet> selection;
	public Vector<Vector<ActionObjet>> groups;
	public Plateau p;
	
	//FLO INPUTS TO BE APPLIED ON THIS ROUND
	public Vector<InputObject> inputs;
	
	public int id;
	private GameTeam gameteam;
	private int team;
	public String nickname;
	public int groupSelection;
	public BottomBar bottomBar;
	public TopBar topBar;
	public Data data;
	public boolean isReady;
	//Network
	public InetAddress address;
	
	
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
		this.inputs = new Vector<InputObject>();
	}
	
	public int getTeam(){
		return team;
	}
	public GameTeam getGameTeam(){
		return gameteam;
	}
	public void setTeam(int team){
		this.team = team;
		for(GameTeam g : this.p.teams)
			if(g.id == team)
				this.gameteam = g;
	}
	
	
	public String toString(){
		String s ="";
		s+="id:"+id+";";
		s+="team:"+team+";";
		s+="gold:"+gameteam.gold+";";
		s+="food:"+gameteam.food+";";
		s+="special:"+gameteam.special+";";
		return s;
		
	}
	public void parsePlayer(String s){
		String[] u1 = s.split("\\|");
		
		// Find the right player :
		String result = "";
		for(int i = 0;i<u1.length;i++){
			HashMap<String,String> hs = new HashMap<String,String>();
			String[] inter = u1[i].split(";");
			for(int j = 0; j<inter.length;j++){
				String[] r = inter[j].split(":");
				hs.put(r[0], r[1]);
			}
			if(hs.containsKey("id")){
				if(Integer.parseInt(hs.get("id"))==this.id){
					result = u1[i];
					break;
				}
			}
		}
		//SEPARATION BETWEEN KEYS
		
		String[] u = result.split(";");
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
	
	public void addToPlay(InputObject in){
		this.inputs.addElement(in);
	}

	public boolean equals(Player p){
		return this.id==p.id;
	}

	
}
