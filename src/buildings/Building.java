package buildings;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import data.Attributs;
import main.Main;
import model.Checkpoint;
import model.Colors;
import model.Game;
import model.MarkerBuilding;
import model.Objet;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import ressources.Map;
import technologies.Technologie;
import units.Character;

public class Building extends Objet{
	public int teamCapturing;
	public float constructionPoints;
	public int potentialTeam;
	public int type;
	public Objet rallyPoint;
	public BuildingHeadquarters hq;
	public float charge;
	public boolean isProducing;
	public boolean giveUpProcess = false;
	public boolean underAttack;
	public float underAttackRemaining=0;
	public float state;
	
	public static int sizeXIcon = 30;

	public MarkerBuilding marker;
	public Vector<Circle> corners=new Vector<Circle>();

	public Building(){}



	public void initialize(float f, float h){
		
		this.x = f*Map.stepGrid+getAttribut(Attributs.sizeX)/2f;
		this.y = h*Map.stepGrid+getAttribut(Attributs.sizeY)/2f;
		
		
		Game.g.plateau.addBuilding(this);
		this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		this.id = Game.g.idChar;
		Game.g.idChar+=1;
		this.collisionBox= new Rectangle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,getAttribut(Attributs.sizeX),getAttribut(Attributs.sizeY));
		this.marker = new MarkerBuilding(x,y,this);
		this.selectionBox = (Rectangle)this.collisionBox;
		this.setXY(x, y);
		resetRallyPoint();
		this.constructionPoints = 0f;
		this.potentialTeam = this.getTeam();
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));
	}
	
	public void resetRallyPoint(){
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2+10);
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
		if( c.getAttributString(Attributs.weapon)== "bow" || c.getAttributString(Attributs.weapon)== "wand" || c.getAttributString(Attributs.weapon)=="bible")
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
				if(!(this instanceof BuildingHeadquarters)){
					this.setTeam(0);
				}
				this.hq = this.getGameTeam().hq;
			}
			this.constructionPoints-=Main.increment;
		}
		if(this.potentialTeam==c.getTeam() && this.constructionPoints<this.getAttribut(Attributs.maxLifepoints) && c.mode==Character.TAKE_BUILDING && c.target==this){
			this.constructionPoints+=Main.increment;
		}

		if(this.constructionPoints>=this.getAttribut(Attributs.maxLifepoints) && this.potentialTeam==c.getTeam() && c.mode==Character.TAKE_BUILDING && c.target==this){
			if(this.potentialTeam!=this.getTeam()  ){
				if(((Game.g.teams.get(potentialTeam).pop+2)<=Game.g.teams.get(potentialTeam).maxPop)||this instanceof Bonus || (this instanceof BuildingHeadquarters)){

					this.setTeam(this.potentialTeam);
					if(this instanceof BuildingHeadquarters){
						Game.g.endGame = true;
						if(this.getTeam()==Game.g.currentPlayer.getTeam()){
							Game.g.victory = true;
						}
						else{
							Game.g.victory = false;
						}
					}
				}else if(ChatHandler.remainingTimeNotEnoughRoom<=0f){
					ChatHandler.remainingTimeNotEnoughRoom=10f;
					Game.g.sendMessage(ChatMessage.getById("pop"));
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

	public Graphics draw(Graphics g){

		if(visibleByCurrentTeam || this instanceof BuildingHeadquarters){
			g.drawImage(Game.g.images.get("building"+name+this.getGameTeam().colorName),this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
			if(mouseOver && Game.g.round>Game.nbRoundInit){
				Color color = new Color(this.getGameTeam().color.getRed(),this.getGameTeam().color.getGreen(),this.getGameTeam().color.getBlue(),0.1f);
				Game.g.images.get("building"+name+this.getGameTeam().colorName).drawFlash(this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY), 2*getAttribut(Attributs.sizeX)/1.8f, 3*getAttribut(Attributs.sizeY)/2,color);
			}
		}
		else
			g.drawImage(Game.g.images.get("building"+name+"neutral"), this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
		if(visibleByCurrentTeam)
			this.drawAnimation(g);

		g.setAntiAlias(false);
		g.setLineWidth(25f);
		// Construction points
		if(this.constructionPoints<this.getAttribut(Attributs.maxLifepoints) && this.visibleByCurrentTeam && this.constructionPoints>0){
			g.setColor(Color.black);
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fillRect(-1f+this.getX()-getAttribut(Attributs.sizeX)/4,-1f+this.getY()-3*this.getAttribut(Attributs.sizeY)/4,getAttribut(Attributs.sizeX)/2+2f,12f);
			float x = this.constructionPoints/this.getAttribut(Attributs.maxLifepoints);
			if(this.potentialTeam==1)
				g.setColor(Colors.team1);
			else if(this.potentialTeam==2)
				g.setColor(Colors.team2);
			else if(this.potentialTeam==0){
				g.setColor(Colors.team0);
			}
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
			g.fillRect(this.getX()-getAttribut(Attributs.sizeX)/4,this.getY()-3*this.getAttribut(Attributs.sizeY)/4,x*getAttribut(Attributs.sizeX)/2,10f);
		}
		g.setAntiAlias(true);
		// draw production
		if(this instanceof BuildingProduction && this.getGameTeam().equals(Game.g.currentPlayer.getGameTeam())){
			BuildingProduction bp = ((BuildingProduction) this);
			if(bp.queue.size()>0){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bp.charge*(64*getAttribut(Attributs.sizeY))/bp.productionList.get(0).time);
				float opacity = 50*bp.charge/bp.productionList.get(0).time;
				Image icone = Game.g.images.get("icon"+bp.productionList.get(bp.queue.get(0)).name+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				//g.setColor(new Color(0f,0f,0f,opacity));
				//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
				//						g.setColor(Color.white);
				//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
				g.setColor(new Color(bp.getGameTeam().color.r,bp.getGameTeam().color.g,bp.getGameTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bp.charge*(360f)/bp.productionList.get(0).time);
				g.fillArc(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r, y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, x-sizeXIcon/2, y-offsetY-sizeXIcon/2);

			}
		}
		if(this instanceof BuildingTech && this.getGameTeam().equals(Game.g.currentPlayer.getGameTeam())){
			BuildingTech bt = ((BuildingTech) this);
			if(bt.queue!=null){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bt.charge*(64*getAttribut(Attributs.sizeY))/bt.queue.tech.prodTime);
				float opacity = 50*bt.charge/bt.queue.tech.prodTime;
				Image icone = Game.g.images.get(bt.queue.tech.nameIcon+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				g.setColor(new Color(bt.getGameTeam().color.r,bt.getGameTeam().color.g,bt.getGameTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bt.charge*(360f)/bt.queue.tech.prodTime);
				g.fillArc(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r, y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, x-sizeXIcon/2, y-offsetY-sizeXIcon/2);

			}
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
			this.rallyPoint = new Checkpoint(Integer.parseInt(hs.get("rX")),Integer.parseInt(hs.get("rY")));
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
				this.hq = Game.g.teams.get(potentialTeam).hq;
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

		if(!(this instanceof Bonus) && this.team!=0){
			this.getGameTeam().pop-=2;
		}
		if(Game.g.currentPlayer!=null && i==Game.g.currentPlayer.id && !(this instanceof BuildingHeadquarters)){
			Game.g.sendMessage(ChatMessage.getById("building taken"));

		}
		this.team = i;
		if(!(this instanceof Bonus)  && this.team!=0){
			this.hq = this.getGameTeam().hq;
			this.getGameTeam().pop+=2;
		}
		this.setTeamExtra();
		this.giveUpProcess = false;
	}

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub

	}

}
