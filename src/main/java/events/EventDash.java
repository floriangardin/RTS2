package events;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import display.Camera;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public class EventDash extends Event{
	
	public EventDash(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
		if(plateau.isVisibleByTeam(Player.team, parent) && parent.getTarget(plateau)!=null && parent.getTarget(plateau) instanceof Character && parent.getTarget(plateau).getTeam()!=parent.getTeam()){
			Sounds.playSoundAt("dash_shot", parent.x, parent.y);
		}else if(plateau.isVisibleByTeam(Player.team, parent)){
			Sounds.playSound("dash");
		}
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		// On fait des lignes blanches derrière le mec
		// Get direction vector
		if(plateau.isVisibleByTeam(Player.team, parent)){			
			g.setColor(new Color(256, 256, 256, 0.5f));
			if(parent.vx>0){
				g.rotate(parent.x, parent.y-50f, (float)(180f/Math.PI*Math.atan(parent.vy/parent.vx)));
				float signe = ((float)Math.signum(parent.vx));
				for(int i=0; i<5; i++){
					if(toDraw){
						g.fillRect(parent.x-150*signe+50*Math.abs(i-2)*signe+((float)Math.random()*50f)*signe, parent.y-50f-(i-2)*12+((float)Math.random()*20f), -60*signe, 2);
					}
					//g.fillRect(parent.x+100*signe-50*Math.abs(i-2)*signe+((float)Math.random()*100f), parent.y-(i-2)*12, -20*signe, 3);
				}
				g.rotate(parent.x, parent.y-50f, (float)(-180f/Math.PI*Math.atan(parent.vy/parent.vx)));
			}
		}
		return parent.inDash>0f && parent.isAlive() && plateau.getObjets().containsKey(parent.id);
	}


}
