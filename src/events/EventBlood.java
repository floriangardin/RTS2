package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import main.Main;
import plateau.Objet;
import plateau.Plateau;

public strictfp class EventBlood extends Event{

	public EventBlood(Objet parent, Plateau plateau) {
		super(parent, plateau);
		this.duration = 20f;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		g.setColor(new Color(256-(int)(100f*StrictMath.random()),0,0,0.5f+(float)StrictMath.random()*0.5f));
		g.fillOval(parent.getX()+100f*((float)StrictMath.random()-0.5f), parent.getY()+100f*((float)StrictMath.random()-0.5f), 3f, 3f);
		this.duration -= Main.increment;
		return parent.isAlive() && this.duration>0f;
	}



}
