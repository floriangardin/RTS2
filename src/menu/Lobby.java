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
		for(int j=1;j<Lobby.players.size(); j++){
			if(!Lobby.players.get(j).isReady){
				toGame = false;
			}
		}
		if(!toGame){
			return false;
		}
		return true;
	}

}
