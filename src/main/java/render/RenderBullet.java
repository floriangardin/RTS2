package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import main.Main;
import plateau.Arrow;
import plateau.Bullet;
import plateau.Fireball;
import plateau.Plateau;
import ressources.Images;

public strictfp class RenderBullet {
	
	
	public static void render(Bullet b, Graphics g, Plateau plateau){
		
		if(b instanceof Arrow){
			Images.get("arrow").rotate(((Arrow)b).angle);
			g.drawImage(Images.get("arrow"),b.getX()-5f*Main.ratioSpace,b.getY()-75f*Main.ratioSpace);
			Images.get("arrow").rotate(-((Arrow)b).angle);
			Image shadow = Images.get("arrow").getScaledCopy(Main.ratioSpace);
			shadow.rotate(((Arrow)b).angle);
			shadow.drawFlash(b.getX()-5f*Main.ratioSpace,b.getY()-5f*Main.ratioSpace,shadow.getWidth(),shadow.getHeight(),new Color(0,0,0,0.3f));
		} else if (b instanceof Fireball){
			Fireball b1 = (Fireball)b;
			if(b1.explosion){
				Image boom = Images.get("explosion").getScaledCopy(Main.ratioSpace);
				float r = boom.getWidth()/5f;
				if(b1.getLifePoints()>=24f)
					g.drawImage(boom, b1.getX()-40f*Main.ratioSpace, b1.getY()-40f*Main.ratioSpace, b1.getX()+40f*Main.ratioSpace, b1.getY()+40f*Main.ratioSpace,0f,0f,r,r);
				else if(b1.getLifePoints()>=18f)
					g.drawImage(boom, b1.getX()-40f*Main.ratioSpace, b1.getY()-40f*Main.ratioSpace, b1.getX()+40f*Main.ratioSpace, b1.getY()+40f*Main.ratioSpace,r,0f,2*r,r);
				else if(b1.getLifePoints()>=12f)
					g.drawImage(boom, b1.getX()-40f*Main.ratioSpace, b1.getY()-40f*Main.ratioSpace, b1.getX()+40f*Main.ratioSpace, b1.getY()+40f*Main.ratioSpace,2*r,0f,3*r,r);
				else if(b1.getLifePoints()>=6f)
					g.drawImage(boom, b1.getX()-40f*Main.ratioSpace, b1.getY()-40f*Main.ratioSpace, b1.getX()+40f*Main.ratioSpace, b1.getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
				else 
					g.drawImage(boom, b1.getX()-40f*Main.ratioSpace, b1.getY()-40f*Main.ratioSpace, b1.getX()+40f*Main.ratioSpace, b1.getY()+40f*Main.ratioSpace,4*r,0f,5*r,r);

			} else {
				if(b1.animation<3)	{
					Image image = (Images.get("fireball")).getSubImage(0, 150, 75, 75).getScaledCopy(Main.ratioSpace);
					image.rotate(b1.angle);
					g.drawImage(image, b1.getX()-28*Main.ratioSpace, b1.getY()-28*Main.ratioSpace, b1.getX()+28*Main.ratioSpace, b1.getY()+28*Main.ratioSpace,0f,0f,image.getWidth(),image.getHeight());
				}else if(b1.animation<6)	{
					Image image1 = (Images.get("fireball")).getSubImage(75, 150, 75, 75).getScaledCopy(Main.ratioSpace);
					image1.rotate(b1.angle);
					g.drawImage(image1, b1.getX()-28*Main.ratioSpace, b1.getY()-28*Main.ratioSpace, b1.getX()+28*Main.ratioSpace, b1.getY()+28*Main.ratioSpace,0f,0f,image1.getWidth(),image1.getHeight());
				}else	{
					Image image2 = (Images.get("fireball")).getSubImage(150, 150, 75, 75).getScaledCopy(Main.ratioSpace);
					image2.rotate(b1.angle);
					g.drawImage(image2, b1.getX()-28*Main.ratioSpace, b1.getY()-28*Main.ratioSpace, b1.getX()+28*Main.ratioSpace, b1.getY()+28*Main.ratioSpace,0f,0f,image2.getWidth(),image2.getHeight());
				}
			}
		}
	}
}
