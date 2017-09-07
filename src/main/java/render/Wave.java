package render;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import pathfinding.Case;
import plateau.Plateau;
import ressources.Images;

public strictfp class Wave{
	public float x, y;
	public float vx;
	public float alpha;
	public float t = 0f;
	public int type;
	
	
	public Wave(float x, float y){
		this.x = x;
		this.y = y;
		init();
	}
	
	public void init(){
		type = 0;
		alpha = 0;
		this.vx = (float) (0.5f+StrictMath.random()*0.2f);
		while(StrictMath.random()>0.7){
			if(Images.exists("wave"+(type+1))){
				type++;
			}
		}
	}
	
	public boolean play(Graphics g, Plateau plateau){
		Image im = Images.get("wave"+type);
		t+=1f;
		alpha = -t*(t-80f)/1200f;
		x += vx;
		im.setAlpha(alpha);
		g.drawImage(im, this.x-im.getWidth()/2, this.y-im.getHeight()/2);
		im.setAlpha(1f);
		return alpha>0;
	}
	
}