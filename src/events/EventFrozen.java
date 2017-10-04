package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import plateau.Objet;
import plateau.Plateau;
import plateau.Character;
import ressources.Images;
import ressources.Sounds;

public strictfp class EventFrozen extends Event {

	public float totalTimeFrozenAnimation = 0.25f;
	public float timeFrozenAnimation = totalTimeFrozenAnimation;
	
	public EventFrozen(Objet parent, Plateau plateau) {
		super(parent, plateau);
		this.topLayer = true;
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		Sounds.playSoundAt("ice_spell", parent.getX(), parent.getY());
		timeFrozenAnimation -= Main.increment;
		int direction = (parent.orientation/2-1);
		if(direction==1 || direction==2){
			direction = ((direction-1)*(-1)+2);
		}
		Image im;
		im = Images.getUnit(parent.getName(), direction, parent.animation, parent.getTeam().id, ((Character)parent).isAttacking).getScaledCopy(1.0f+0.5f*timeFrozenAnimation/totalTimeFrozenAnimation);
		im.drawFlash(parent.getX()-im.getWidth()/2,parent.getY()-3*im.getHeight()/4,im.getWidth(),im.getHeight(),new Color(20,150,250,200+50*timeFrozenAnimation/totalTimeFrozenAnimation));
		return timeFrozenAnimation>0;
	}

}
