package buildings;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.ActionObjet;
import model.Checkpoint;
import model.Colors;
import model.Game;
import model.Map;
import model.Objet;
import model.Plateau;
import multiplaying.ChatMessage;
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
	public boolean giveUpProcess = false;
	public boolean underAttack;
	public float underAttackRemaining=0;
	
	
	public Vector<Circle> corners=new Vector<Circle>();
	
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

	public void initialize(float f, float h){
		this.x = f*Map.stepGrid+sizeX/2f;
		this.y = h*Map.stepGrid+sizeY/2f;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		this.selectionBox = this.collisionBox;
		this.setXY(x, y);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.constructionPoints = 0f;
		this.potentialTeam = this.getTeam();
		this.updateImage();
		corners.add(new Circle(x-sizeX/2f,y-sizeY/2f,20f));
		corners.add(new Circle(x+sizeX/2f,y-sizeY/2f,20f));
		corners.add(new Circle(x+sizeX/2f,y+sizeY/2f,20f));
		corners.add(new Circle(x-sizeX/2f,y+sizeY/2f,20f));
	}
	
	
	public void giveUpProcess(){
		if(giveUpProcess){
			constructionPoints-=Main.increment;
			if(constructionPoints<=0f){
				this.setTeam(0);
			}
		}
	}
	public void collisionWeapon(Character c){
		if(this instanceof Bonus && !((Bonus)this).bonusPresent){
			return;
		}
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
		if(this.potentialTeam!=c.getTeam() && c.mode==Character.TAKE_BUILDING && c.target==this){
			this.underAttack = true;
			this.underAttackRemaining =20f;

			if(this.constructionPoints<=0f){
				this.potentialTeam = c.getTeam();
				if(!(this instanceof BuildingHeadQuarters)){
					this.setTeam(0);
				}
				this.hq = this.getGameTeam().hq;
			}
			this.constructionPoints-=Main.increment;
		}
		if(this.potentialTeam==c.getTeam() && this.constructionPoints<this.maxLifePoints && c.mode==Character.TAKE_BUILDING && c.target==this){
			this.constructionPoints+=Main.increment;
		}
		
		if(this.constructionPoints>=this.maxLifePoints && this.potentialTeam==c.getTeam() && c.mode==Character.TAKE_BUILDING && c.target==this){
			if(this.potentialTeam!=this.getTeam()  ){
				if(((this.g.teams.get(potentialTeam).pop+2)<=this.g.teams.get(potentialTeam).maxPop)||(this instanceof BuildingHeadQuarters)){
					
					this.setTeam(this.potentialTeam);
					if(this instanceof BuildingHeadQuarters){
						this.p.g.endGame = true;
						if(this.getTeam()==this.p.g.currentPlayer.getTeam()){
							this.p.g.victory = true;
						}
						else{
							this.p.g.victory = false;
						}
					}
				}else{
					this.g.sendMessage(ChatMessage.getById("pop"));
				}
				
			}
		}
	}

	public void drawIsSelected(Graphics g){
		g.setColor(Colors.selection);
		g.setLineWidth(2f);
		g.draw(this.collisionBox);
		this.drawRallyPoint(g);
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

		else if(this instanceof BuildingTower){
			this.imageNeutre = this.p.g.images.buildingTowerNeutral;
			if(getTeam()==1){
				this.image = this.p.g.images.buildingTowerBlue;
			} else if(getTeam()==2){
				this.image = this.p.g.images.buildingTowerRed;
			} else {
				this.image = this.p.g.images.buildingTowerNeutral;
			}

		}
	}



	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		

		
//		//TEST
//		Image i = this.image;
//		g.drawImage(i,x-i.getWidth()/2,y-i.getHeight()/2);
//		if(mouseOver){
//			i.drawFlash(this.x-this.sizeX/1.8f, this.y-this.sizeY,i.getWidth(),i.getHeight(),color);
//			g.setColor(new Color(250,0,0,0.8f));
//		}
//		//
		
		if(visibleByCurrentPlayer || this instanceof BuildingHeadQuarters){
			g.drawImage(this.image, this.x-this.sizeX/1.8f, this.y-this.sizeY, this.x+this.sizeX/1.8f, this.y+this.sizeY/2f, 0, 0, this.image.getWidth(), this.image.getHeight());
			if(mouseOver){
				Color color = new Color(this.gameteam.color.getRed(),this.gameteam.color.getGreen(),this.gameteam.color.getBlue(),0.1f);
				this.image.drawFlash(this.x-this.sizeX/1.8f, this.y-this.sizeY, 2f*sizeX/1.8f,1.5f*sizeY, color);
			}
		}
			else
			g.drawImage(this.imageNeutre, this.x-this.sizeX/1.8f, this.y-this.sizeY, this.x+this.sizeX/1.8f, this.y+this.sizeY/1.8f, 0, 0, this.imageNeutre.getWidth(), this.imageNeutre.getHeight());
		if(visibleByCurrentPlayer)
			this.drawAnimation(g);
		//g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		
		
		g.setAntiAlias(false);
		g.setLineWidth(25f);
		// Construction points
		if(this.constructionPoints<this.maxLifePoints && this.visibleByCurrentPlayer && this.constructionPoints>0){
			g.setColor(new Color(255,255,255,1f));
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fill(new Rectangle(this.getX()-sizeX/4,this.getY()-3*this.sizeY/4,sizeX/2,10f));
			float x = this.constructionPoints/this.maxLifePoints;
			if(this.potentialTeam==1)
				g.setColor(Colors.team1);
			else if(this.potentialTeam==2)
				g.setColor(Colors.team2);
			else if(this.potentialTeam==0){
				g.setColor(Colors.team0);
			}
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
			g.fill(new Rectangle(this.getX()-sizeX/4,this.getY()-3*this.sizeY/4,x*sizeX/2,10f));
		}
		g.setAntiAlias(false);
		g.setLineWidth(2f);
		
		
		return g;
	}

	
	public Graphics drawRallyPoint(Graphics g){
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
			this.hq = this.p.g.teams.get(potentialTeam).hq;
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

	public void setTeamExtra(){
		
	}
	public void setTeam(int i){
		
		if(!(this instanceof Bonus) && this.gameteam!=null){
			this.getGameTeam().pop-=2;
		}
		this.gameteam = this.p.g.teams.get(i);
		if(!(this instanceof Bonus)  && this.gameteam!=null){
			this.hq = this.getGameTeam().hq;
			this.getGameTeam().pop+=2;
		}
		this.setTeamExtra();
		this.updateImage();
		this.giveUpProcess = false;
	}
	
	public void setCharge(float charge){
		this.charge = charge;
		
	}

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub

	}

}
