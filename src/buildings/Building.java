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
import multiplaying.ChatHandler;
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
	public float state;

	public Vector<Circle> corners=new Vector<Circle>();

	public Building(){}



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
		resetRallyPoint();
		this.constructionPoints = 0f;
		this.potentialTeam = this.getTeam();
		this.updateImage();
		corners.add(new Circle(x-sizeX/2f,y-sizeY/2f,20f));
		corners.add(new Circle(x+sizeX/2f,y-sizeY/2f,20f));
		corners.add(new Circle(x+sizeX/2f,y+sizeY/2f,20f));
		corners.add(new Circle(x-sizeX/2f,y+sizeY/2f,20f));
	}
	
	public void resetRallyPoint(){
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
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
				if(((this.g.teams.get(potentialTeam).pop+2)<=this.g.teams.get(potentialTeam).maxPop)||this instanceof Bonus || (this instanceof BuildingHeadQuarters)){

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
				}else if(ChatHandler.remainingTimeNotEnoughRoom<=0f){
					ChatHandler.remainingTimeNotEnoughRoom=10f;
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
			this.imageNeutre = this.p.g.images.get("buildingBarrackNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingBarrackBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingBarrackRed");
			} else {
				this.image = this.p.g.images.get("buildingBarrackNeutral");
			}
		}

		else if(this instanceof BuildingStable){
			this.imageNeutre = this.p.g.images.get("buildingStableNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingStableBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingStableRed");
			} else {
				this.image = this.p.g.images.get("buildingStableNeutral");
			}
		}
		else if(this instanceof BuildingAcademy){
			this.imageNeutre = this.p.g.images.get("buildingAcademyNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingAcademyBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingAcademyRed");
			} else {
				this.image = this.p.g.images.get("buildingAcademyNeutral");
			}
		}
		else if(this instanceof BuildingMill){
			this.imageNeutre = this.p.g.images.get("buildingMillNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingMillBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingMillRed");
			} else {
				this.image = this.p.g.images.get("buildingMillNeutral");
			}
		}
		else if(this instanceof BuildingMine){
			this.imageNeutre = this.p.g.images.get("buildingMineNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingMineBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingMineRed");
			} else {
				this.image = this.p.g.images.get("buildingMineNeutral");
			}
		}
		else if(this instanceof BuildingUniversity){
			this.imageNeutre = this.p.g.images.get("buildingUniversityNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingUniversityBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingUniversityRed");
			} else {
				this.image = this.p.g.images.get("buildingUniversityNeutral");
			}
		}

		else if(this instanceof BuildingTower){
			this.imageNeutre = this.p.g.images.get("buildingTowerNeutral");
			if(getTeam()==1){
				this.image = this.p.g.images.get("buildingTowerBlue");
			} else if(getTeam()==2){
				this.image = this.p.g.images.get("buildingTowerRed");
			} else {
				this.image = this.p.g.images.get("buildingTowerNeutral");
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
			g.setColor(new Color(0,0,0));
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fill(new Rectangle(-1f+this.getX()-sizeX/4,-1f+this.getY()-3*this.sizeY/4,sizeX/2+2f,12f));
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
	
	public String toString(){
		return toStringBuilding();
	}
	
	public void setCharge(float charge){
		this.charge= charge;
	}
	public String toStringBuilding(){
		String s = "";
		s+="id:"+id+";";
		s+="tm:"+getTeam()+";";
		s+="pT:"+potentialTeam+";";
		s+="cP:"+constructionPoints+";";
		s+="chrg:"+charge+";";
		s+="sta:"+state+";";
		if(this.rallyPoint!=null){
			s+="rX:"+(int)this.rallyPoint.x+";";
			s+="rY:"+(int)this.rallyPoint.y+";";
		}
		return s;
	}

	public void parseBuilding(HashMap<String, String> hs) {
		if(hs.containsKey("rX") && hs.containsKey("rY") ){
			this.rallyPoint = new Checkpoint(p, Integer.parseInt(hs.get("rX")),Integer.parseInt(hs.get("rY")));
		}
		if(hs.containsKey("cP")){
			this.constructionPoints = Float.parseFloat(hs.get("cP"));
		}
		if(hs.containsKey("chrg")){
			this.setCharge(Float.parseFloat(hs.get("chrg")));
		}
		if(hs.containsKey("sta")){
			this.state = Float.parseFloat(hs.get("sta"));
		}
		if(hs.containsKey("tm")){
			if(this.getTeam()!=Integer.parseInt(hs.get("tm"))){
				this.setTeam(Integer.parseInt(hs.get("tm")));
			}
		}
		if(hs.containsKey("pT")){
			if(this.potentialTeam!=Integer.parseInt(hs.get("pT"))){
				this.potentialTeam = Integer.parseInt(hs.get("pT"));
				if(potentialTeam!=getTeam()){
					this.setTeam(0);
				}
				this.hq = this.p.g.teams.get(potentialTeam).hq;
			}
		}
	}

	public void parse(HashMap<String, String> hs){
		parseBuilding(hs);
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
		if(this.g.currentPlayer!=null && i==this.g.currentPlayer.id && !(this instanceof BuildingHeadQuarters)){
			this.g.sendMessage(ChatMessage.getById("building taken",this.g));

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

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub

	}

}
