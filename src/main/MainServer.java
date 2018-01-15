package main;

import model.GameServer;


public strictfp class MainServer {
	// A REGLER \\
	public static float ratioSpace = 1f;
	public static int framerate = 60;
	//public static int nDelay = 0;
	///////\\\\\\\\\
	public static float increment = 0.05f;
	public static boolean fullscreen=false;
	
	public static void main(String[] args) {
//		Log.setLogSystem(new NullLogSystem()); 
		GameServer.init();
		
		
	}
}
