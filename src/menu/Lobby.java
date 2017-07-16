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
	
	private final static Vector<Menu_Player> players = new Vector<Menu_Player>();
	public static Vector<String> maps;
	public static Vector<InetAddress> addressesInvites = new Vector<InetAddress>();
	public static boolean multiplayer = false;
	public static boolean host = false;
	public static String idCurrentMap;
	
	
	public static void init(){
		//players = new Vector<Menu_Player>();
		maps = Map.maps();
		idCurrentMap = maps.get(0);
	}
	
	public static Vector<Menu_Player> getPlayers(){
		synchronized(players){
			return players;
		}
	}
	public static boolean checkStartGame(){
		boolean toGame = true;
		for(Menu_Player player : Lobby.getPlayers()){
			if(!player.isReady){
				toGame = false;
			}
		}
		if(!toGame || Lobby.getPlayers().size()==0){
			return false;
		}
		return true;
	}

	public static void addPlayer(Menu_Player mpMessage) {
		synchronized(players){			
			Lobby.players.add(mpMessage);
		}
	}

}
