package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import display.Camera;
import plateau.BurningArea;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;

public strictfp class EventBurningArea extends Event{
	
	public float size;

	public EventBurningArea(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
		this.size = ((BurningArea)parent).size;
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		Image im = Images.get("magma").getScaledCopy((int)(2*size), (int)(2*size));
		float alpha = (((BurningArea)parent).totalTime-((BurningArea)parent).remainingTime)/(((BurningArea)parent).startTime*((BurningArea)parent).remainingTime);
		alpha = (float)StrictMath.min(alpha, ((BurningArea)parent).remainingTime/(((BurningArea)parent).endTime*((BurningArea)parent).totalTime));
		alpha = (float)StrictMath.min(alpha, 1f);
		alpha = (float)StrictMath.max(alpha, 0f);
		im.setAlpha(alpha);
		if(toDraw){
			g.drawImage(im, ((BurningArea)parent).getX()-im.getWidth()/2f, ((BurningArea)parent).getY()-im.getHeight()/2f);
			g.setColor(new Color(1f,0.1f,0f,0.2f));
			g.setLineWidth(1f);
			g.draw(((BurningArea)parent).getCollisionBox());
		}
		return parent.isAlive() && plateau.getObjets().containsKey(parent.getId());
	}

}
