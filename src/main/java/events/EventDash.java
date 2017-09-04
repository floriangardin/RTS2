package events;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import display.Camera;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public class EventDash extends Event{
	
	public EventDash(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
		if(parent.getTarget(plateau)!=null && parent.getTarget(plateau) instanceof Character && parent.getTarget(plateau).getTeam()!=parent.getTeam()){
			Sounds.playSound("dash_shot");
		}else{
			Sounds.playSound("dash");
		}
		
	}

	@Override
	public boolean play(Graphics g, Plateau plateau) {
		// TODO Auto-generated method stub
		// On fait des lignes blanches derrière le mec
		// Get direction vector
		
		g.setColor(new Color(256, 256, 256, 0.5f));
		if(parent.vx>0){
			g.rotate(parent.x, parent.y-50f, (float)(180f/Math.PI*Math.atan(parent.vy/parent.vx)));
			float signe = ((float)Math.signum(parent.vx));
			for(int i=0; i<5; i++){
				g.fillRect(parent.x-150*signe+50*Math.abs(i-2)*signe+((float)Math.random()*50f)*signe, parent.y-50f-(i-2)*12+((float)Math.random()*20f), -60*signe, 2);
				//g.fillRect(parent.x+100*signe-50*Math.abs(i-2)*signe+((float)Math.random()*100f), parent.y-(i-2)*12, -20*signe, 3);
			}
			
			g.rotate(parent.x, parent.y-50f, (float)(-180f/Math.PI*Math.atan(parent.vy/parent.vx)));
		}
		return parent.inDash>0f;
	}


}
