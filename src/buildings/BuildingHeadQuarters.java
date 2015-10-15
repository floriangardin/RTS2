package buildings;

import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import model.Player;
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
	public Player player;
	public BuildingHeadQuarters(Plateau plateau, Game g, float f, float h,int team) {
		// Init ProductionList
		this.hq = this;
		this.p = plateau ;
		this.player = this.p.g.players.get(team);
		this.productionList = new Vector<Technologie>();
		this.allTechs = new Vector<Technologie>();
		this.p.g.players.get(team).hq = this;
		if(this.p.g.players.get(team).civ==0){
			
			// AGING
			DualistAge2 d2 = new DualistAge2(this.p,this.player);
			this.allTechs.addElement(d2);
			DualistAge3 d3 = new DualistAge3(this.p,this.player);
			this.allTechs.addElement(d3);
			d3.techRequired=d2;
			
			// SIGHT TECH
			DualistEagleView ev =new DualistEagleView(this.p,this.player);
			this.allTechs.addElement(ev);
			ev.techRequired = d2;
			
			// RESSOURCES BONUS
			DualistBonusFood d4 = new DualistBonusFood(this.p,this.player);
			this.allTechs.addElement(d4);
			DualistBonusGold bg = new DualistBonusGold(this.p,this.player);
			this.allTechs.addElement(bg);
			
			// EXPLOSION TECH
			DualistExplosion ex = new DualistExplosion(this.p,this.player);
			ex.techRequired=d3;
			
			// SHIELD TECH
			DualistShield2 s2 = new DualistShield2(this.p,this.player);
			this.allTechs.addElement(s2);
			DualistShield3 s3 = new DualistShield3(this.p,this.player);
			s3.techRequired = s2;
			this.allTechs.addElement(s3);
			
			// HEALTH TECH
			DualistHealth2 h2 = new DualistHealth2(this.p,this.player);
			this.allTechs.addElement(h2);
			DualistHealth3 h3 = new DualistHealth3(this.p,this.player);
			h3.techRequired = h2;
			this.allTechs.addElement(h3);
			
			// CONTACT WEAPON TECH
			DualistContact2 c2 = new DualistContact2(this.p,this.player);
			this.allTechs.addElement(c2);
			DualistContact3 c3 = new DualistContact3(this.p,this.player);
			c3.techRequired = c2;
			this.allTechs.addElement(c3);
			
			
			// RANGE WEAPON TECH
			DualistRangeAttack2 r2 = new DualistRangeAttack2(this.p,this.player);
			this.allTechs.addElement(r2);
			DualistRangeAttack3 r3 = new DualistRangeAttack3(this.p,this.player);
			r3.techRequired = r2;
			this.allTechs.addElement(r3);
			
	
		}
		else if(this.p.g.players.get(team).civ==1){
			this.productionList = new Vector<Technologie>();
		}
		else{
			this.productionList = new Vector<Technologie>();
		}
		this.queue = null;
		teamCapturing= team;

		this.team = team;
		this.sizeX = this.player.data.headQuartersSizeX; 
		this.sizeY = this.player.data.headQuartersSizeY;
		this.sight = this.player.data.headQuartersSight;
		maxLifePoints = player.data.headQuartersLifePoints;
		this.name = "headquarters";
		this.x = f;
		this.y = h;
		p.addBuilding(this);
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 5;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(this.team == 1){
			this.image = this.p.g.images.buildingHeadQuartersBlue;
		}
		else if(this.team == 2){
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
		this.potentialTeam = this.team;
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
				this.techTerminate(Technologie.technologie(q, p, player));
			}
		}
	}

	public void action(){

		//Do the action of Barrack
		//Product, increase state of the queue
		// If enough faith create archange
		
		if(this.player.civ==0 && this.player.special>=UnitsList.Archange.specialPrice){
			
			this.player.data.create(UnitsList.Archange, this.x, this.y+this.sizeY/2);
			this.player.special=0;
		}
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				animation = 0;
			this.charge+=0.1f;
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
