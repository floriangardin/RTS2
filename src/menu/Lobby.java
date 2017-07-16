package menu;

import java.net.InetAddress;
import java.util.Vector;

import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.Options;
import ressources.GraphicElements;
import ressources.Map;

public class Lobby {
	
	public static Vector<Menu_Player> players;
	public static Vector<String> maps;
	public static Vector<InetAddress> addressesInvites = new Vector<InetAddress>();
	public static boolean multiplayer = false;
	public static boolean host = false;
	public static String idCurrentMap;
	
	
	public static void init(){
		players = new Vector<Menu_Player>();
		maps = Map.maps();
		idCurrentMap = maps.get(0);
	}
		
	public static boolean checkStartGame(){
		boolean toGame = true;
		if(multiplayer){
			if(host){
				// checking if all players are ready
				for(int j=1;j<Lobby.players.size(); j++){
					if(!Lobby.players.get(j).isReady){
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
			for(Menu_Player mp : Lobby.players){
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
