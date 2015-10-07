package units;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import model.Data;
import model.Plateau;
import model.Player;

public class UnitTest extends Character {
	public UnitTest(Plateau p, Player player, Data data) {
		super(p, player);
		this.name = "test";
		this.type = UnitsList.Test;
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 60f;
		this.armor = 0f;
		this.damage = 5f;
		this.chargeTime = 15f;
		this.weapon = "wand";
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 200f;
		


		//Load animations
		try {
			for(int i=0;i<1;i++){
				for(int j = 0; j<4;j++){
					for(int k=0;k<4;k++){
						this.animations[i][j][k] = new Image("pics/unit/test/"+i+"-"+j+"-"+k+".png");
					
				}
			}
		}

		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.updateImage();
	}

	public UnitTest(UnitTest spearman, float x, float y) {
		super(spearman,x,y);
	}

	public Graphics draw(Graphics g){
		int direction = (int)(orientation/2-1);
		g.drawImage(this.animations[this.mode][direction][this.increment],x,y);
		return g;
	}
}
