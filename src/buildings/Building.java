package buildings;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import model.ActionObjet;
import model.Checkpoint;
import model.Game;
import model.Objet;
import model.Plateau;
import units.Character;
import multiplaying.OutputModel.OutputBuilding;
import technologies.Technologie;

public class Building extends ActionObjet{
	public Game g;
	public float sizeX;
	public float sizeY;
	public int teamCapturing;
	public float constructionPoints;
	
	public int potentialTeam;
	public int type;
	public Objet rallyPoint;
	public BuildingHeadQuarters hq;
	public Image imageNeutre;

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
		this.sizeX = 220f; 
		this.sizeY = 220f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.g.images.tent;
		this.sight = 300f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	public void collisionWeapon(Character c){
		if( c.weapon== "bow" || c.weapon== "wand" || c.weapon=="bible")
			return;
		if(this instanceof BuildingStable && c.player.hq.age<2){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this instanceof BuildingAcademy && c.player.hq.age<3){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this.potentialTeam!=c.team){
			if(this.constructionPoints<=0f){
				this.potentialTeam = c.team;
				this.hq = this.p.g.players.get(c.team).hq;
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

	public void drawIsSelected(Graphics g){


		g.drawImage(this.selection_circle,this.getX()-5f-this.collisionBox.getWidth()/2,this.getY()-this.collisionBox.getHeight()/2-5f,this.getX()+this.collisionBox.getWidth()/2+5f,this.getY()+this.collisionBox.getHeight()/2+5f,0,0,this.selection_circle.getWidth(),this.selection_circle.getHeight());
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));

	}	

	public void updateImage(){
		if(this instanceof BuildingBarrack){
			this.imageNeutre = this.p.g.images.buildingBarrackNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingBarrackBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingBarrackRed;
			} else {
				this.image = this.p.g.images.buildingBarrackNeutral;
			}
		}

		else if(this instanceof BuildingStable){
			this.imageNeutre = this.p.g.images.buildingStableNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingStableBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingStableRed;
			} else {
				this.image = this.p.g.images.buildingStableNeutral;
			}
		}
		else if(this instanceof BuildingAcademy){
			this.imageNeutre = this.p.g.images.buildingAcademyNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingAcademyBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingAcademyRed;
			} else {
				this.image = this.p.g.images.buildingAcademyNeutral;
			}
		}
		else if(this instanceof BuildingMill){
			this.imageNeutre = this.p.g.images.buildingMillNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingMillBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingMillRed;
			} else {
				this.image = this.p.g.images.buildingMillNeutral;
			}
		}
		else if(this instanceof BuildingMine){
			this.imageNeutre = this.p.g.images.buildingMineNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingMineBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingMineRed;
			} else {
				this.image = this.p.g.images.buildingMineNeutral;
			}
		}
		else if(this instanceof BuildingUniversity){
			this.imageNeutre = this.p.g.images.buildingUniversityNeutral;
			if(team==1){
				this.image = this.p.g.images.buildingUniversityBlue;
			} else if(team==2){
				this.image = this.p.g.images.buildingUniversityRed;
			} else {
				this.image = this.p.g.images.buildingUniversityNeutral;
			}
		}
	}

	
	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		if(visibleByCurrentPlayer || this instanceof BuildingHeadQuarters)
			g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, this.image.getWidth(), this.image.getHeight());
		else
			g.drawImage(this.imageNeutre, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, this.imageNeutre.getWidth(), this.imageNeutre.getHeight());
		if(visibleByCurrentPlayer)
			this.drawAnimation(g);
		//g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));

		}
		// Construction points
				if(this.constructionPoints<this.maxLifePoints && this.visibleByCurrentPlayer && this.constructionPoints>0){
					g.setColor(Color.white);
					g.fill(new Rectangle(this.getX()-r,this.getY()-r-50f,2*r,4f));
					float x = this.constructionPoints*2f*r/this.maxLifePoints;
					if(this.potentialTeam==1)
						g.setColor(Color.blue);
					else
						g.setColor(Color.red);
					g.fill(new Rectangle(this.getX()-r,this.getY()-r-50f,x,4f));
				}
		return g;
	}
	
	public void drawAnimation(Graphics g){
		
	}
	
	
	public String toStringBuilding(){
		String s = toStringObjet();
		s+=toStringActionObjet();
		
		if(changes.sizeX){
			s+="sizeX:"+sizeX+";";
			changes.sizeX = false;
		}
		if(changes.sizeY){
			s+="sizeY:"+sizeY+";";
			changes.sizeY = false;
		}
		if(changes.potentialTeam){
			s+="potentialTeam:"+potentialTeam+";";
			changes.potentialTeam = false;
		}
		if(changes.constructionPoints){
			s+="constructionPoints:"+constructionPoints+";";
			changes.constructionPoints= false;
		}
		if(changes.rallyPoint){
			if(this.rallyPoint!=null){
				s+="rallyPointX:"+this.rallyPoint.x+";";
				s+="rallyPointY:"+this.rallyPoint.y+";";
			}
			changes.rallyPoint = false;

		}
		if(changes.sizeX){
			s+="sizeX:"+sizeX+";";
			changes.sizeX = false;
		}
		return s;
	}
	
	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringBuilding();
		return s;
	}

	public void parseBuilding(HashMap<String, String> hs) {
		if(hs.containsKey("sizeX")){
			this.sizeX = Float.parseFloat(hs.get("sizeX"));
		}
		if(hs.containsKey("sizeY")){
			this.sizeX = Float.parseFloat(hs.get("sizeX"));
		}
		if(hs.containsKey("rallyPointX")){
			this.rallyPoint.x = Float.parseFloat(hs.get("rallyPointX"));
		}
		if(hs.containsKey("rallyPointY")){
			this.rallyPoint.y = Float.parseFloat(hs.get("rallyPointY"));
		}
		if(hs.containsKey("constructionPoints")){
			this.constructionPoints = Float.parseFloat(hs.get("constructionPoints"));
		}
		if(hs.containsKey("potentialTeam")){
			this.potentialTeam = Integer.parseInt(hs.get("potentialTeam"));
		}
		if(hs.containsKey("constructionPoints")){
			this.sizeX = Float.parseFloat(hs.get("sizeX"));
		}

	}

	public void parse(HashMap<String, String> hs) {
		
		
	}

	public Technologie getTechnologieById(int id){
		Technologie tec = null;
		for(Technologie t : this.hq.allTechs){
			if(t.id==id){
				tec = t;
			}
		}
		return tec;
	}
	
	public void setCharge(float charge){
		this.charge = charge;
		this.changes.charge = true;
		if(charge==0f){
			this.changes.isFinished=true;
		}
		
	}
	
	public void setTeam(int team){
		for(Building b : this.p.buildings){
			if(b.team==team){
				this.hq = b.hq;
				
			}
		}
	this.team = team;
	}

}
