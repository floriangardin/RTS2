package buildings;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.ActionObjet;
import model.Checkpoint;
import model.Game;
import model.Objet;
import model.Plateau;
import technologies.Technologie;
import units.Character;

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

	public boolean underAttack;
	public float underAttackRemaining=0;

	public Building(){}

	public Building(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
		teamCapturing = 0;
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.setTeam(0);
		this.lifePoints = 1f;
		this.sizeX = 220f; 
		this.sizeY = 220f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.selectionBox = this.collisionBox;
		this.image = this.p.g.images.tent;
		this.sight = 300f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	public void collisionWeapon(Character c){
		if( c.weapon== "bow" || c.weapon== "wand" || c.weapon=="bible")
			return;
		if(this instanceof BuildingStable && c.getGameTeam().hq.age<2){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this instanceof BuildingAcademy && c.getGameTeam().hq.age<3){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this.potentialTeam!=c.getTeam() && c.mode==Character.TAKE_BUILDING){
			this.underAttack = true;
			this.underAttackRemaining =20f;

			if(this.constructionPoints<=0f){
				this.potentialTeam = c.getTeam();
				this.hq = this.getGameTeam().hq;
			}
			this.constructionPoints-=Main.increment;
		}
		else if(this.constructionPoints<this.maxLifePoints && c.mode==Character.TAKE_BUILDING){
			this.constructionPoints+=Main.increment;
		}
		else if(c.mode==Character.TAKE_BUILDING){
			if(this.potentialTeam!=this.getTeam() && (this.g.plateau.teams.get(potentialTeam).pop+1)<this.g.plateau.teams.get(potentialTeam).maxPop){
				this.getGameTeam().pop-=2;
				this.setTeam(this.potentialTeam);
				this.getGameTeam().pop+=2;
				if(this instanceof BuildingHeadQuarters){
					this.p.g.endGame = true;
					if(this.getTeam()==this.p.currentPlayer.id){
						this.p.g.victory = true;
					}
					else{
						this.p.g.victory = false;
					}
				}
				this.hq = this.getGameTeam().hq;
				if(this instanceof BuildingProduction){
					((BuildingProduction)this).queue.clear();
					((BuildingProduction)this).charge = 0f;					
				}
				if(this instanceof BuildingTech){
					((BuildingTech)this).queue=null;
					((BuildingTech)this).charge = 0f;
					this.hq = this.getGameTeam().hq;
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
			if(getTeam()==1){
				this.image = this.p.g.images.buildingBarrackBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingBarrackRed;
			} else {
				this.image = this.p.g.images.buildingBarrackNeutral;
			}
		}

		else if(this instanceof BuildingStable){
			this.imageNeutre = this.p.g.images.buildingStableNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingStableBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingStableRed;
			} else {
				this.image = this.p.g.images.buildingStableNeutral;
			}
		}
		else if(this instanceof BuildingAcademy){
			this.imageNeutre = this.p.g.images.buildingAcademyNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingAcademyBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingAcademyRed;
			} else {
				this.image = this.p.g.images.buildingAcademyNeutral;
			}
		}
		else if(this instanceof BuildingMill){
			this.imageNeutre = this.p.g.images.buildingMillNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingMillBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingMillRed;
			} else {
				this.image = this.p.g.images.buildingMillNeutral;
			}
		}
		else if(this instanceof BuildingMine){
			this.imageNeutre = this.p.g.images.buildingMineNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingMineBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingMineRed;
			} else {
				this.image = this.p.g.images.buildingMineNeutral;
			}
		}
		else if(this instanceof BuildingUniversity){
			this.imageNeutre = this.p.g.images.buildingUniversityNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingUniversityBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingUniversityRed;
			} else {
				this.image = this.p.g.images.buildingUniversityNeutral;
			}
		}
	}



	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		if(visibleByCurrentPlayer || this instanceof BuildingHeadQuarters)
			g.drawImage(this.image, this.x-this.sizeX/1.8f, this.y-this.sizeY, this.x+this.sizeX/1.8f, this.y+this.sizeY/2f, 0, 0, this.image.getWidth(), this.image.getHeight());
		else
			g.drawImage(this.imageNeutre, this.x-this.sizeX/1.8f, this.y-this.sizeY, this.x+this.sizeX/1.8f, this.y+this.sizeY/1.8f, 0, 0, this.imageNeutre.getWidth(), this.imageNeutre.getHeight());
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
		String s = "";

		if(changes.sizeX){
			s+="sizeX:"+sizeX+";";
			changes.sizeX = true;
		}
		if(changes.sizeY){
			s+="sizeY:"+sizeY+";";
			changes.sizeY = true;
		}
		if(changes.potentialTeam){
			s+="potentialTeam:"+potentialTeam+";";
			changes.potentialTeam = true;
		}
		if(changes.constructionPoints){
			s+="constructionPoints:"+constructionPoints+";";
			changes.constructionPoints= true;
		}
		if(changes.rallyPoint){
			if(this.rallyPoint!=null){
				s+="rallyPointX:"+this.rallyPoint.x+";";
				s+="rallyPointY:"+this.rallyPoint.y+";";
			}
			changes.rallyPoint = true;
		}
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
			this.hq = this.p.getTeamById(potentialTeam).hq;
		}
		if(hs.containsKey("constructionPoints")){
			this.sizeX = Float.parseFloat(hs.get("sizeX"));
		}
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
	}

}
