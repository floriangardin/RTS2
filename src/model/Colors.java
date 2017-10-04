package model;

import org.newdawn.slick.Color;

public strictfp class Colors {

	public static Color team0 = new Color(31,31,31);
	public static Color team1= new Color(0,0,155);
	public static Color team2 = new Color(200,0,0);
	public static Color selection = new Color(0,250,0);
	public static Color selectionFirst = new Color(200,250,0);
	public static Color mouseOver = new Color(100,250,150);
	public static Color buildingTaking = new Color(204,153,0);
	public static Color aggressive = new Color(153,0,0);
	
	public static Color getTeamColor(int team){
		switch(team){
		case 0 : return team0;
		case 1 : return team1;
		case 2 : return team2;
		default:
			return new Color(0,0,0);
		}
	}
	
}
