package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Images {
	
	public Image water, dirt;
	public Image arrow;
	public Image fireball;
	public Image explosion;
	public Image bible, sword, magicwand, bow;
	public Image blue, red, corps;
	public Image horseBlue, horseRed;
	public Image lightArmor, mediumArmor, heavyArmor;
	public Image tree01, tree02, tree03, tree04;
	public Image tent;
	public Image cursor;
	public Image selection_circle;
	public Image selection_rectangle;
	public Image fog;
	public Image fire;
	public Image fountain;
	public Image smoke;
	public Image windmill;
	public Image windmillarms;
	
	//Building
	public Image buildingBarrackBlue;
	public Image buildingBarrackRed;
	public Image buildingBarrackNeutral;
	public Image buildingStableBlue;
	public Image buildingStableRed;
	public Image buildingStableNeutral;
	public Image buildingAcademyBlue;
	public Image buildingAcademyRed;
	public Image buildingAcademyNeutral;
	public Image buildingMillBlue;
	public Image buildingMillRed;
	public Image buildingMillNeutral;
	public Image buildingMineBlue;
	public Image buildingMineRed;
	public Image buildingMineNeutral;
	public Image buildingHeadQuartersBlue;
	public Image buildingHeadQuartersRed;
	
	//Icones
	public Image iconeSpearman;
	public Image iconeBowman;
	public Image iconePriest;
	public Image iconeWizard;
	public Image iconeKnight;
	
	//Tech
	public Image techEagleView;
	
	//Spell
	public Image spellFirewall;
	
	public Images(){
		try {
			this.selection_circle = new Image("pics/ring.svg").getScaledCopy(1f/20f);
			this.selection_rectangle = new Image("pics/rectSelect.png").getScaledCopy(1f/20f);
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
			this.fountain = new Image("pics/fountain.png");
			this.fire = new Image("pics/fire.png");
			this.smoke = new Image("pics/smoke.png");
			this.windmill = new Image("pics/windmill.png");
			this.windmillarms = new Image("pics/windmillarms.png");
			
			//Building
			buildingBarrackBlue = new Image("pics/Building/BarrackBlue.png");
			buildingBarrackRed = new Image("pics/Building/BarrackRed.png");
			buildingBarrackNeutral = new Image("pics/Building/BarrackNeutral.png");
			buildingStableBlue = new Image("pics/Building/StableBlue.png");
			buildingStableRed = new Image("pics/Building/StableRed.png");
			buildingStableNeutral = new Image("pics/Building/StableNeutral.png");
			buildingAcademyBlue = new Image("pics/Building/AcademyNeutral.png");
			buildingAcademyRed = new Image("pics/Building/AcademyRed.png");
			buildingAcademyNeutral = new Image("pics/Building/AcademyBlue.png");
			buildingMillBlue = new Image("pics/Building/MillBlue.png");
			buildingMillRed = new Image("pics/Building/MillRed.png");
			buildingMillNeutral = new Image("pics/Building/MillNeutral.png");
			buildingMineBlue = new Image("pics/Building/MineBlue.png");
			buildingMineRed = new Image("pics/Building/MineRed.png");
			buildingMineNeutral = new Image("pics/Building/MineNeutral.png");
			buildingHeadQuartersBlue = new Image("pics/Building/HeadQuartersBlue.png");
			buildingHeadQuartersRed = new Image("pics/Building/HeadQuartersRed.png");
			
			//Icones
			this.iconeBowman = new Image("pics/iconBow.png");
			this.iconeSpearman = new Image("pics/iconSpear.png");
			this.iconeKnight = new Image("pics/iconHorse.png");
			this.iconePriest = new Image("pics/iconBible.png");
			this.iconeWizard = new Image("pics/iconFireball.png");
			
			//Icones Techs
			this.techEagleView = new Image("pics/Tech/EagleView.png");
			
			//Icone spell
			this.spellFirewall = new Image("pics/Spell/firewall.png");
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public Image getIconByName(String name){
		switch(name){
		case "spearman": return this.iconeSpearman;
		case "crossbowman": return this.iconeBowman;
		case "knight": return this.iconeKnight;
		case "priest": return this.iconePriest;
		case "inquisitor": return this.iconeWizard;
		default: return null;
		}
	}
	
	public Image getIconeTech(String name){
		switch(name){
		case "EagleView": return this.techEagleView;
		default: return null;
		}
		
	}

}
