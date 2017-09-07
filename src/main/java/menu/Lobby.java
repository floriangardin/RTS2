package menu;

import java.net.InetAddress;
import java.util.Vector;

import menuutils.Menu_Map;
import menuutils.Menu_Player;
import model.Game;
import model.Options;
import ressources.GraphicElements;
import ressources.Map;

public strictfp class Lobby {
	
	public static Vector<Menu_Player> players = new Vector<Menu_Player>();
	public static Vector<String> maps;
	public static Vector<InetAddress> addressesInvites = new Vector<InetAddress>();
	public static boolean multiplayer = false;
	public static boolean host = false;
	public static String idCurrentMap;
	private static boolean isInit = false;	
	
	public static void init(){
		players = new Vector<Menu_Player>();
		maps = Map.maps();
		idCurrentMap = maps.get(0);
		isInit = true;
	}
	
	public static boolean checkStartGame(){
		boolean toGame = true;
		synchronized (players) {
			for(Menu_Player player : players){
				if(!player.isReady){
					toGame = false;
				}
			}
		}
		if(!toGame || players.size()==0){
			return false;
		}
		return true;
	}

	public static void addPlayer(Menu_Player mpMessage) {
		synchronized(players){
			Menu_Player mp = new Menu_Player(mpMessage.id, mpMessage.team, mpMessage.nickname);
			Lobby.players.add(mp);
		}
	}

	public static boolean isInit() {
		// TODO Auto-generated method stub
		return isInit;
	}

	public static void removePlayer(int id){
		int index = 0;
		for(Menu_Player mp :players){
			if(id == mp.id){
				break;
			}
			index+=1;
		}
		if(index<players.size()){
			players.removeElementAt(index);
		}
	}
}
