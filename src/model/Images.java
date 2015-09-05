package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Images {
	
	Image water, dirt;
	Image arrow, fireball, explosion;
	Image bible, sword, magicwand, bow;
	Image blue, red, corps;
	Image horseBlue, horseRed;
	Image lightArmor, mediumArmor, heavyArmor;
	Image tree01, tree02, tree03, tree04;
	Image tent;
	Image cursor;
	Image selection_circle;
	Image fog;
	Image windmill;
	Image windmillarms;
	
	//Icones
	Image iconeSpearman;
	Image iconeBowman;
	Image iconePriest;
	Image iconeWizard;
	Image iconeKnight;
	
	public Images(){
		try {
			this.selection_circle = new Image("pics/ring.svg").getScaledCopy(1f/20f);
			this.arrow = new Image("pics/arrow.png");
			this.fireball = new Image("pics/fireball.png");
			this.explosion = new Image("pics/explosion.png");
			this.bible = new Image("pics/bible.png");
			this.sword = new Image("pics/sword.png");
			this.bow = new Image("pics/bow.png");
			this.magicwand = new Image("pics/magicwand.png");
			this.lightArmor = new Image("pics/lightArmor.png");
			this.mediumArmor = new Image("pics/mediumArmor.png");
			this.heavyArmor = new Image("pics/heavyArmor.png");
			this.corps = new Image("pics/corps.png");
			this.red = new Image("pics/red.png");
			this.blue = new Image("pics/blue.png");
			this.horseRed = new Image("pics/horseRed.png");
			this.horseBlue = new Image("pics/horseBlue.png");
			this.tree01 = new Image("pics/tree01.png");
			this.tree02 = new Image("pics/tree02.png");
			this.tree03 = new Image("pics/tree03.png");
			this.tree04 = new Image("pics/tree04.png");
			this.tent = new Image("pics/tent.png");
			this.dirt = new Image("pics/dirt.png");
			this.water = new Image("pics/water.jpg");
			this.cursor = new Image("pics/cursor.png");
			this.fog = new Image("pics/fog.png");
			this.windmill = new Image("pics/windmill.png");
			this.windmillarms = new Image("pics/windmillarms.png");
			
			
			//Icones
			this.iconeBowman = new Image("pics/iconBow.png").getSubImage(0, 0, 512, 512);
			this.iconeSpearman = new Image("pics/iconSpear.png").getSubImage(0, 0, 512, 512);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public Image getIconByName(String name){
		switch(name){
		case "Spearman": return this.iconeSpearman;
		case "Bowman": return this.iconeBowman;
		case "Knight": return this.iconeKnight;
		case "Priest": return this.iconePriest;
		case "Wizard": return this.iconeWizard;
		default: return null;
		}
	}

}
