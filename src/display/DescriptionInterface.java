package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import buildings.Building;
import main.Main;
import model.ActionObjet;
import units.Character;

public class DescriptionInterface extends Bar {

	BottomBar parent;
	float x, y, sizeX, sizeY;
	ActionObjet c0;

	public DescriptionInterface(BottomBar parent){
		this.parent = parent;
		this.x = parent.x;
		this.y = parent.y;
		this.c0 = null;
		this.sizeX = parent.sizeX/3f;
		this.sizeY = parent.sizeY;
	}

	public Graphics draw(Graphics g){
		if(this.parent.player.selection.size()>0){
			this.c0 = this.parent.player.selection.get(0);
			g.setColor(Color.white);
			g.drawString(c0.name, x+30f, y+15f);
			if(this.c0 instanceof Building){
				Building c0 = (Building) this.c0;
			}


			if(this.c0 instanceof Character){
				Character c0 =(Character) this.c0;
//				if(c0.horse!=null)
//					g.drawImage(c0.image, x+25f, y+40f, x+this.sizeX/3f-25f, y+sizeY*2f/3f+15f,0f,0f,c0.image.getWidth()/3f,c0.image.getHeight()/4f);
//				else
//					g.drawImage(c0.image, x+25f, y+40f, x+this.sizeX/3f-25f, y+sizeY*2f/3f,0f,0f,c0.image.getWidth()/3f,c0.image.getHeight()/4f);
				g.setColor(Color.red);
				g.fillRect(x+10f, y+this.sizeY-25f, this.sizeX/3f-20f, 20f);
				g.setColor(Color.green);
				g.fillRect(x+10f, y+this.sizeY-25f, (this.sizeX/3f-20f)*c0.lifePoints/c0.maxLifePoints, 20f);
				g.setColor(Color.white);
				String s = "HP: " + (int)c0.lifePoints + " / " + (int)c0.maxLifePoints;
				g.drawString(s, x+sizeX/3f+10f, y+10f);
				
				if(c0.armor!=0f)
					s = "Armor: " + (int)c0.armor*10;
				else
					s = "No Armor";
				g.drawString(s, x+sizeX/3f+10f, y+45f);
				//s = "Velocity: " + (int)c0.maxVelocity;
				s = "Speed: " + (int)c0.maxVelocity;
				g.drawString(s, x+sizeX/3f+10f, y+80f);
				if(c0.weapon!=null){
					float aux = Main.framerate/(c0.chargeTime*10f);
					int a = (int)(aux*100f)/100;
					int b = (int)(aux*100f)-a*100;
					s = "Att.Rate: " + a+"."+b;
					g.drawString(s, x+2f*sizeX/3f, y+10f);
					float aux1 = Math.abs(c0.damage);
					a = (int)(aux1*100f)/100;
					b = (int)(aux1*100f)-a*100;
					if(c0.damage<0)
						s = "Healing: ";
					else
						s = "Damage: "; 
					s+= a +"." +b;
					g.drawString(s, x+2f*sizeX/3f, y+45f);
					a = (int)(aux1*100f*aux)/100;
					b = (int)(aux1*100f*aux)-a*100;
					s = "DPS: " + a+"."+b;
					g.drawString(s, x+2f*sizeX/3f, y+80f);
				}else{
					s = "No";
					g.drawString(s, x+2f*sizeX/3f+40f, y+30f);
					s = "Weapon";
					g.drawString(s, x+2f*sizeX/3f+20f, y+60f);

				}
			} else {
				this.c0 = null;
			}
		}
		return g;
	}

}
