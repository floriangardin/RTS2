package menu;

import java.net.InetAddress;
import java.util.Vector;

import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.Options;
import model.Player;
import ressources.GraphicElements;
import ressources.Map;

public class Lobby {
	
	public Vector<Menu_Player> players;
	
	public Vector<String> maps;
	
	public Vector<InetAddress> addressesInvites = new Vector<InetAddress>();
	
	public boolean multiplayer = false;
	public boolean host = false;
	
	public int idCurrentPlayer;
	
	public String idCurrentMap;
	
	
	public Lobby(){
		this.players = new Vector<Menu_Player>();
		this.maps = Map.maps();
		this.idCurrentMap = maps.get(0);
		this.idCurrentPlayer = 0;
	}
	
	public void initMulti(){
		multiplayer = true;
		host = true;
		this.players = new Vector<Menu_Player>();
		this.players.add(new Menu_Player(0,0,Options.nickname));
	}
	
	public void initSingle(){
		multiplayer = false;
		this.players = new Vector<Menu_Player>();
		this.players.add(new Menu_Player(0,1,Options.nickname));
		this.players.add(new Menu_Player(1,2,"IA"));
	}
	
	public void initMulti(Lobby l){
		multiplayer = true;
		host = false;
		this.players = new Vector<Menu_Player>();
		this.players = l.players;
		this.maps = l.maps;
		this.idCurrentMap = l.idCurrentMap;
		for(Menu_Player mp : this.players){
			if(mp.name.equals(Options.nickname)){
				this.idCurrentPlayer = mp.id;
			}
		}
	}
	
	public boolean checkStartGame(){
		boolean toGame = true;
		if(multiplayer){
			if(host){
				// checking if all players are ready
				for(int j=1;j<this.players.size(); j++){
					if(!this.players.get(j).isReady){
						toGame = false;
					}
				}
			} else {
				toGame = false;
			}
		}
		if(!toGame){
			return false;
		}
		// Checking if at least one player is present by team
		boolean present1 = false;
		boolean present2 = false;
		if(toGame){
			for(Menu_Player mp : this.players){
				if(mp.team==1){
					present1 = true;
				}
				if(mp.team==2){
					present2 = true;
				}
			}
			if (present1 && present2){
				// Launch Game
				return true;
			}
		}
		return false;
	}

}
