package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import main.Main;
import plateau.Objet;

public class EventBlood extends Event{

	public EventBlood(Objet parent) {
		super(parent);
		this.duration = 20f;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g) {
		g.setColor(new Color(256-(int)(100f*Math.random()),0,0,0.5f+(float)Math.random()*0.5f));
		g.fillOval(parent.x+100f*((float)Math.random()-0.5f), parent.y+100f*((float)Math.random()-0.5f), 3f, 3f);
		this.duration -= Main.increment;
		return parent.isAlive() && this.duration>0f;
	}



}
