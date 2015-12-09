package buildings;

import java.util.Vector;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.Plateau;
import technologies.DualistAge2;
import technologies.DualistAge3;
import technologies.DualistBonusFood;
import technologies.DualistBonusGold;
import technologies.DualistContact2;
import technologies.DualistContact3;
import technologies.DualistEagleView;
import technologies.DualistExplosion;
import technologies.DualistHealth2;
import technologies.DualistHealth3;
import technologies.DualistRangeAttack2;
import technologies.DualistRangeAttack3;
import technologies.DualistShield2;
import technologies.DualistShield3;
import technologies.Technologie;
import units.UnitsList;

public class BuildingHeadQuarters extends BuildingTech {


	public int age=1;
	boolean isProducing;
	public Vector<Technologie> techsDiscovered;
	public Vector<Technologie> allTechs;
	public BuildingHeadQuarters(Plateau plateau, Game g, float f, float h, int team) {
		// Init ProductionList
		this.hq = this;
		this.p = plateau ;
		this.g = g;
		this.productionList = new Vector<Technologie>();
		this.allTechs = new Vector<Technologie>();
		this.gameteam = g.teams.get(team);
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.headQuartersSound);
		
		this.getGameTeam().hq = this;
		if(getGameTeam().civ==0){
			// AGING
			DualistAge2 d2 = new DualistAge2(this.p,this.getGameTeam());
			this.allTechs.addElement(d2);
			DualistAge3 d3 = new DualistAge3(this.p,this.getGameTeam());
			this.allTechs.addElement(d3);
			d3.techRequired=d2;
			
			// SIGHT TECH
			DualistEagleView ev =new DualistEagleView(this.p,this.getGameTeam());
			this.allTechs.addElement(ev);
			ev.techRequired = d2;
			
			// RESSOURCES BONUS
			DualistBonusFood d4 = new DualistBonusFood(this.p,this.getGameTeam());
			this.allTechs.addElement(d4);
			DualistBonusGold bg = new DualistBonusGold(this.p,this.getGameTeam());
			this.allTechs.addElement(bg);
			
			// EXPLOSION TECH
			DualistExplosion ex = new DualistExplosion(this.p,this.getGameTeam());
			this.allTechs.addElement(ex);
			ex.techRequired=d2;
			
			// SHIELD TECH
			DualistShield2 s2 = new DualistShield2(this.p,this.getGameTeam());
			this.allTechs.addElement(s2);
			DualistShield3 s3 = new DualistShield3(this.p,this.getGameTeam());
			s3.techRequired = s2;
			this.allTechs.addElement(s3);
			
			// HEALTH TECH
			DualistHealth2 h2 = new DualistHealth2(this.p,this.getGameTeam());
			this.allTechs.addElement(h2);
			DualistHealth3 h3 = new DualistHealth3(this.p,this.getGameTeam());
			h3.techRequired = h2;
			this.allTechs.addElement(h3);
			
			// CONTACT WEAPON TECH
			DualistContact2 c2 = new DualistContact2(this.p,this.getGameTeam());
			this.allTechs.addElement(c2);
			DualistContact3 c3 = new DualistContact3(this.p,this.getGameTeam());
			c3.techRequired = c2;
			this.allTechs.addElement(c3);
			
			
			// RANGE WEAPON TECH
			DualistRangeAttack2 r2 = new DualistRangeAttack2(this.p,this.getGameTeam());
			this.allTechs.addElement(r2);
			DualistRangeAttack3 r3 = new DualistRangeAttack3(this.p,this.getGameTeam());
			r3.techRequired = r2;
			this.allTechs.addElement(r3);
			
	
		}
		else if(getGameTeam().civ==1){
			this.productionList = new Vector<Technologie>();
		}
		else{
			this.productionList = new Vector<Technologie>();
		}
		this.queue = null;
		teamCapturing= getTeam();
		this.sizeX = this.getGameTeam().data.headQuartersSizeX; 
		this.sizeY = this.getGameTeam().data.headQuartersSizeY;
		this.sight = this.getGameTeam().data.headQuartersSight;
		maxLifePoints = getGameTeam().data.headQuartersLifePoints;
		this.name = "headquarters";
		this.x = f;
		this.y = h;
		p.addBuilding(this);
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 5;
		this.lifePoints = this.maxLifePoints;
		
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		this.selectionBox = this.collisionBox;
		if(this.getTeam() == 1){
			this.image = this.p.g.images.buildingHeadQuartersBlue;
		}
		else if(this.getTeam() == 2){
			this.image = this.p.g.images.buildingHeadQuartersRed;
		}
		else {
			this.image = this.p.g.images.tent;
		}
		// List of potential production 
		this.techsDiscovered = new Vector<Technologie>();
		this.updateProductionList();
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.constructionPoints = this.maxLifePoints;
		this.potentialTeam = this.getTeam();
		this.updateImage();
		

	}

	public void changeTech(Vector<Integer> techs){
		for(Integer q : techs){
			boolean useful = true;
			for(Technologie t : this.techsDiscovered){
				if(t.id==q){
					useful = false;
				}
			}
			if(useful){
				this.techTerminate(Technologie.technologie(q, p, getGameTeam()));
			}
		}
	}

	public void action(){
		giveUpProcess();
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		//Do the action of Barrack
		//Product, increase state of the queue
		// If enough faith create archange
		
		if(this.getGameTeam().civ==0 && this.getGameTeam().special>=UnitsList.Archange.specialPrice){
			
			this.getGameTeam().data.create(UnitsList.Archange, this.x, this.y+this.sizeY/2);
			this.getGameTeam().special=0;
		}
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				animation = 0;
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			this.animation = 0;
		}


	}
	

	
	

	
}
