package buildings;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import model.ActionObjet;
import model.Checkpoint;
import model.Game;
import model.Objet;
import model.Plateau;
import multiplaying.OutputModel.OutputBuilding;
import units.UnitsList;
import weapon.Weapon;

public class Building extends ActionObjet{
	public Game g;
	public float sizeX;
	public float sizeY;
	public int teamCapturing;
	public float maxLifePoints;
	public float constructionPoints;
	public float animation;
	public int potentialTeam;
	public int type;
	public Objet rallyPoint;
	public BuildingHeadQuarters hq;

	public float charge;
	public boolean isProducing;


	public Building(){}

	public Building(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
		teamCapturing = 0;
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=0;
		this.lifePoints = 1f;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		this.sight = 300f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	public void collision(Weapon w){
		if(this.potentialTeam!=w.owner.team){
			if(this.constructionPoints<=0f){
				this.potentialTeam = w.owner.team;
				this.hq = this.p.g.players.get(w.owner.team).hq;
			}
			this.constructionPoints-=0.1f;
		}
		else if(this.constructionPoints<this.maxLifePoints){
			this.constructionPoints+=0.1f;
		}
		else{
			if(this.potentialTeam!=this.team){
				this.team = this.potentialTeam;
				if(this instanceof BuildingProduction){
					((BuildingProduction)this).queue.clear();
					((BuildingProduction)this).charge = 0f;					
				}
				if(this instanceof BuildingTech){
					((BuildingTech)this).queue=null;
					((BuildingTech)this).charge = 0f;
					this.hq = this.p.g.players.get(this.team).hq;
					((BuildingTech)this).updateProductionList();
				}
				if(this instanceof BuildingMine){
					((BuildingMine) this).bonusProd = 0;
				}
				if(this instanceof BuildingMill){
					((BuildingMill) this).bonusProd = 0;
				}
				this.updateImage();
			}
		}
	}

	public void change(OutputBuilding ocb) {
		this.lifePoints = ocb.lifepoints;
		this.team = ocb.team;	
		this.maxLifePoints = ocb.maxlifepoints;
		this.constructionPoints = ocb.constrpoints;
		this.animation = ocb.animation;
		this.sight = ocb.sight;
		if(ocb.team==2){
			if(this instanceof BuildingProduction){
				((BuildingProduction) this).changeQueue(ocb);
			} else if(this instanceof BuildingTech){
				if(ocb.queue[0]!=-1){
					((BuildingTech)this).queue = ((BuildingTech)this).productionList.get(ocb.queue[0]);
					this.charge = ocb.charge;
					System.out.println(((BuildingTech)this).queue);
				} else {

				}
			}
		}
		this.updateImage();
	}

	public Building(OutputBuilding ocb, Plateau p){
		Building b;
		switch(ocb.typeBuilding){
		case 0: b = new BuildingMine(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		case 1: b = new BuildingMill(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		case 2: b = new BuildingStable(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		case 3: b = new BuildingBarrack(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		case 4: b = new BuildingAcademy(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		case 5: b = new BuildingHeadQuarters(p,p.g,ocb.x,ocb.y,ocb.team); b.id = ocb.id;break;
		case 6: b = new BuildingUniversity(p,p.g,ocb.x,ocb.y); b.id = ocb.id;break;
		default:
		}
	}

	public void drawIsSelected(Graphics g){


		g.drawImage(this.selection_circle,this.getX()-5f-this.collisionBox.getWidth()/2,this.getY()-this.collisionBox.getHeight()/2-5f,this.getX()+this.collisionBox.getWidth()/2+5f,this.getY()+this.collisionBox.getHeight()/2+5f,0,0,this.selection_circle.getWidth(),this.selection_circle.getHeight());
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));

	}	

	public void updateImage(){
		if(this instanceof BuildingBarrack){
			if(team==1){
				this.image = this.p.images.buildingBarrackBlue;
			} else if(team==2){
				this.image = this.p.images.buildingBarrackRed;
			} else {
				this.image = this.p.images.buildingBarrackNeutral;
			}
		}

		else if(this instanceof BuildingStable){
			if(team==1){
				this.image = this.p.images.buildingStableBlue;
			} else if(team==2){
				this.image = this.p.images.buildingStableRed;
			} else {
				this.image = this.p.images.buildingStableNeutral;
			}
		}
		else if(this instanceof BuildingAcademy){
			if(team==1){
				this.image = this.p.images.buildingAcademyBlue;
			} else if(team==2){
				this.image = this.p.images.buildingAcademyRed;
			} else {
				this.image = this.p.images.buildingAcademyNeutral;
			}
		}
		else if(this instanceof BuildingMill){
			if(team==1){
				this.image = this.p.images.buildingMillBlue;
			} else if(team==2){
				this.image = this.p.images.buildingMillRed;
			} else {
				this.image = this.p.images.buildingMillNeutral;
			}
		}
		else if(this instanceof BuildingMine){
			if(team==1){
				this.image = this.p.images.buildingMineBlue;
			} else if(team==2){
				this.image = this.p.images.buildingMineRed;
			} else {
				this.image = this.p.images.buildingMineNeutral;
			}
		}
	}


}
