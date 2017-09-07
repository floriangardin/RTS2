package events;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Colors;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;

public strictfp class EventBuildingTakingGlobal extends Event{
	
	public HashMap<Integer, Integer> numberOfAttackers;
	
	public EventBuildingTakingGlobal(Objet parent, Plateau plateau) {
		super(parent, plateau);
		this.topLayer = true;
		numberOfAttackers = new HashMap<Integer, Integer>();
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		int value1 = 0, value2 = 0;
		if(!toDraw){
			return true;
		}
		if(numberOfAttackers.containsKey(plateau.teams.get(1).id)){
			value1 = numberOfAttackers.get(plateau.teams.get(1).id);
		}
		if(numberOfAttackers.containsKey(plateau.teams.get(2).id)){
			value2 = numberOfAttackers.get(plateau.teams.get(2).id);
		}
		Building b = (Building)parent;
		if(value1!=value2){
			// taking
			Color c = new Color(0f,0f,0f,0f);
			if(value1>value2){
				c = Colors.getTeamColor(1);
			} else {
				c = Colors.getTeamColor(2);
			}
			int nbLine = 5;
			float x = b.constructionPoints/b.getAttribut(Attributs.maxLifepoints);
			g.setLineWidth(1f);
			for(int i=0; i<nbLine; i++){
				g.setColor(new Color(c.r,c.g,c.b,(float) (StrictMath.sin(StrictMath.PI*x*x*50f)*(nbLine-i)/nbLine)));
				//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
				g.drawRect(b.getX()-b.getAttribut(Attributs.sizeX)/4-i,b.getY()-3*b.getAttribut(Attributs.sizeY)/4-i,b.getAttribut(Attributs.sizeX)/2+2*i,10f+2*i);
			}	
		} else if (value1>0){
			// contested
			g.setColor(new Color(1f,1f,1f,(float) (StrictMath.sin((plateau.round%60)*StrictMath.PI/30))));
			String s = "OPPOSITION";
			g.drawString(s, b.getX()-g.getFont().getWidth(s)/2, b.getY()-3*b.getAttribut(Attributs.sizeY)/4-1.5f*g.getFont().getHeight(s));
		}
		return true;
	}

	public void addAttacker(int team){
		if(!numberOfAttackers.containsKey(team)){
			numberOfAttackers.put(team, 1);
		} else {
			numberOfAttackers.put(team, numberOfAttackers.get(team)+1);
		}
	}
	
	public void removeAttacker(int team){
		if(!numberOfAttackers.containsKey(team)){
		} else {
			numberOfAttackers.put(team, numberOfAttackers.get(team)-1);
		}
	}
	
	

}
