package buildings;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import data.Attributs;
import display.DisplayRessources;
import main.Main;
import model.Checkpoint;
import model.Colors;
import model.Game;
import model.IAPlayer;
import model.MarkerBuilding;
import model.Objet;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import ressources.Map;
import technologies.Technologie;
import units.Character;
import utils.ObjetsList;
import utils.Utils;

public class Building extends Objet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6321347988991860645L;
	public int teamCapturing;
	public float constructionPoints;
	public int potentialTeam;

	public Objet rallyPoint;

	public float charge;


	public boolean giveUpProcess = false;
	public boolean underAttack;
	public float underAttackRemaining=0;
	public float state;

	public static int sizeXIcon = 30;
	public float chargeTime;
	public int age=1;

	public Vector<ObjetsList> techsDiscovered;
	public Vector<ObjetsList> currentTechsProduced;
	public MarkerBuilding marker;
	public Vector<Circle> corners=new Vector<Circle>();
	

	public Technologie queueTechnology;


	public Vector<Integer> queue ;
	public float random=0f;
	//TOWER
	public float chargeAttack;
	public String animationBleu;
	public String animationRouge;
	public boolean canAttack=false;
	private float animationTower;
	private float stateRessourceGold;
	private float stateRessourceFood;
	private float stateRessourceFaith;
	
	
	public Building(ObjetsList name,float x , float y,int team){
		// SET UP TECH LIST ET PRODUCTION LIST

		this.name = name;

		this.x = x;
		this.y = y;
		teamCapturing= 0;
		this.setTeam(team);

		this.initialize(x, y);
		if(name.equals(ObjetsList.Headquarters)){
			initHQ();
		}
		
		//TODO : Animations should be generic !
		if(name.equals(ObjetsList.Tower)){
			this.animationBleu ="buildingTowerBlueAnimation";
			this.animationRouge = "buildingTowerRedAnimation";
		}
		


	}
	

	public float getAttribut(ObjetsList o ,Attributs a){
		return this.getGameTeam().data.getAttribut(o, a);
	}
	public String getAttributString(ObjetsList o ,Attributs a){
		return this.getGameTeam().data.getAttributString(o, a);
	}
	public Vector<ObjetsList> getProductionList(){
		Vector<ObjetsList> toReturn = new Vector<ObjetsList>();
		Vector<ObjetsList> result =  getGameTeam().data.getAttributListAtt(this.name, Attributs.units);
		
		// Remove units which not match tech required
		
		Vector<ObjetsList> techDiscovered = getHQ().techsDiscovered;
		for(ObjetsList o : result){
			Vector<ObjetsList> techsRequired =  this.getGameTeam().data.getAttributListAtt(o, Attributs.techsRequired);
			if(techDiscovered.containsAll(techsRequired)){
				toReturn.add(o);
			}
		}
		
		
		return toReturn;
	}
	public Vector<ObjetsList> getRawTechnologyList(){
		return getGameTeam().data.getAttributListAtt(this.name, Attributs.technologies);
	}
	public Vector<ObjetsList> getAllTechs(){
		return getGameTeam().data.getAttributListAtt(this.name, Attributs.technologies);
	}
	
	public boolean product(int unit){
		// TODO : fix method in a generic way
		
		// PRODUCTION LIST
		//UNIT PRODUCTION
		if(this.queueTechnology!=null){
			return false;
		}
		if(this.queue.size()<5 && unit<getProductionList().size()){
			float goldCost = getAttribut(getProductionList().get(unit),Attributs.goldCost);
			float foodCost = getAttribut(getProductionList().get(unit),Attributs.foodCost);
			float faithCost = getAttribut(getProductionList().get(unit),Attributs.faithCost);
			float prodTime = getAttribut(getProductionList().get(unit),Attributs.prodTime);

			if(foodCost<=this.getGameTeam().food
					&& getAttribut(getProductionList().get(unit),Attributs.goldCost)<=this.getGameTeam().gold && this.getGameTeam().pop<this.getGameTeam().maxPop){
				this.queue.add(unit);
				this.getGameTeam().gold-=goldCost;
				this.getGameTeam().food-=foodCost;
				if(this.team==Game.g.currentPlayer.getGameTeam().id){
					Game.g.addDisplayRessources(new DisplayRessources(-goldCost,"gold",this.x,this.y));
					Game.g.addDisplayRessources(new DisplayRessources(-foodCost,"food",this.x,this.y));
				}
				return true;
			}else {
				if(Game.g.players.get(this.getTeam()) instanceof IAPlayer){
					return false;
				}
				// Messages
				if(this.getTeam()==Game.g.currentPlayer.getTeam()){
					if(foodCost>this.getGameTeam().food){
						Game.g.sendMessage(ChatMessage.getById("food"));
					} else if(goldCost>this.getGameTeam().gold){
						Game.g.sendMessage(ChatMessage.getById("gold"));
					} else {
						Game.g.sendMessage(ChatMessage.getById("pop"));
					}
				}
			}
		}
		
		return false;
		
		
		
	}
	
	

	public boolean productTech(int unit) {
		// TODO Auto-generated method stub
		// TECH PRODUCTION
		if(this.queue.size()>0){
			return false;
		}
		if(this.queueTechnology==null && unit<getTechnologyList().size()){
			
			float goldCost = getAttribut(getTechnologyList().get(unit),Attributs.goldCost);
			float foodCost = getAttribut(getTechnologyList().get(unit),Attributs.foodCost);
			float faithCost = getAttribut(getTechnologyList().get(unit),Attributs.faithCost);
			float prodTime = getAttribut(getTechnologyList().get(unit),Attributs.prodTime);
			if(foodCost<=this.getGameTeam().food && goldCost<=this.getGameTeam().gold){
				
				this.queueTechnology = Technologie.technologie(getTechnologyList().get(unit), this.getGameTeam().id);
				this.getGameTeam().gold-=goldCost;
				this.getGameTeam().food-=foodCost;
				if(this.team==Game.g.currentPlayer.getGameTeam().id){
					Game.g.addDisplayRessources(new DisplayRessources(-goldCost,"gold",this.x,this.y));
					Game.g.addDisplayRessources(new DisplayRessources(-foodCost,"food",this.x,this.y));
				}
				getHQ().currentTechsProduced.add(getTechnologyList().get(unit));
				
				return true;
			} else {
				// Messages
				if(this.getTeam()==Game.g.currentPlayer.getTeam()){
					if(foodCost>this.getGameTeam().food){
						Game.g.sendMessage(ChatMessage.getById("food"));
					} else if(goldCost>this.getGameTeam().gold){
						Game.g.sendMessage(ChatMessage.getById("gold"));
					} else {
						Game.g.sendMessage(ChatMessage.getById("pop"));
					}
				}
			}
		}
		return false;
	}
	
	public Building getHQ(){
		return this.getGameTeam().hq;
	}
	
	public void updateHeadQuarters(){
		
	}

	public void initHQ(){
		
		this.techsDiscovered = new Vector<ObjetsList>();
		
		this.currentTechsProduced = new Vector<ObjetsList>();
		this.setTeam(team);
		this.getGameTeam().hq = this;
		this.constructionPoints = this.getAttribut(Attributs.maxLifepoints);
		// List of potential production 
		
		
	}
	
	public void attack(){
		//Animation
		if(getTeam()!=0)
			this.animationTower+=2f;
		if(this.animationTower>120f)
			this.animationTower = 1;
		
		if(this.target!=null && this.target.getTeam()==this.getTeam()){
			this.target = null;
		}
		if(!this.canAttack)
			this.chargeAttack = (this.chargeAttack+Main.increment);
		if(this.chargeAttack>this.getAttribut(Attributs.chargeTime) && this.getGameTeam().id!=0){
			this.canAttack = true;
			this.chargeAttack = 0f;

		}
		if(canAttack){
			if(target==null || this.target.lifePoints<0f ||Utils.distance(this, this.target)>getAttribut(Attributs.sight)){
				Vector<Character> target= Game.g.plateau.getEnnemiesInSight(this);
				if(target.size()>0){
					this.target = target.get(0);
				}
			}

			//Launch a fireball on target
			if(target!=null && Utils.distance(this, this.target)<this.getAttribut(Attributs.sight)){
				new Fireball(this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage),-1);
				this.canAttack= false;
				this.chargeAttack = 0f;
			}
		}
	}
	public void action(){
		
		// PRODUCTION
		this.updateAttributsChange();
		giveUpProcess();
		
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}

		//Do the action of Barrack
		//Product, increase state of the queue
		this.random+=0.01f;
		if(this.random>1f){
			this.random=0;
		}
		if(this.queue.size()>0){
			this.setCharge(this.charge+Main.increment);
			if((this.getGameTeam().pop+1)<=this.getGameTeam().maxPop && this.charge>=this.getAttribut(getProductionList().get(this.queue.get(0)), Attributs.prodTime)){
				this.setCharge(0f);
				float dirX = this.random+this.rallyPoint.x-this.x;
				float dirY = this.random+this.rallyPoint.y - this.y;
				float norm = (float) Math.sqrt(dirX*dirX+dirY*dirY);
				//Introduit du random
				float startX = this.x + this.getAttribut(Attributs.sizeX)*dirX/norm/2;
				float startY = this.y + this.getAttribut(Attributs.sizeY)*dirY/norm/2;
				Character c = new Character(startX,startY, getProductionList().get(this.queue.get(0)), this.getTeam());
				
				if(rallyPoint!=null){
					if(rallyPoint instanceof Checkpoint){
					
						c.setTarget(rallyPoint);
					}
					else if(rallyPoint instanceof Character){
						c.setTarget(rallyPoint,null,Character.AGGRESSIVE);
					}
					else if(rallyPoint instanceof Building){
						c.setTarget(rallyPoint,null,Character.TAKE_BUILDING);
					}
				}
				this.queue.remove(0);
			}
		}
		
		// TECH
		if(this.queueTechnology!=null){
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.getAttribut(this.queueTechnology.objet, Attributs.prodTime)){
				this.techTerminate(this.queueTechnology);
			}
		}
		
		//MINE
		
		this.stateRessourceGold+=Main.increment;
		this.stateRessourceFood+=Main.increment;
		this.stateRessourceFaith+=Main.increment;
		
		if(stateRessourceGold >= this.getAttribut(Attributs.frequencyProduceGold) && getTeam()!=0){
			getGameTeam().gold+=this.getAttribut(this.name,Attributs.produceGold)*getGameTeam().data.prodGold;
			stateRessourceGold = 0;
			if(this.team==Game.g.currentPlayer.getGameTeam().id && this.getAttribut(this.name,Attributs.produceGold)==1 ){
				Game.g.addDisplayRessources(new DisplayRessources(getGameTeam().data.prodGold, "gold", this.x, this.y));
				
			}
		}
		if(stateRessourceFood >= this.getAttribut(Attributs.frequencyProduceFood) && getTeam()!=0){
			getGameTeam().food+=this.getAttribut(this.name,Attributs.produceFood)*getGameTeam().data.prodFood;
			stateRessourceFood = 0;
			if(this.team==Game.g.currentPlayer.getGameTeam().id && this.getAttribut(this.name,Attributs.produceFood)==1 ){
				Game.g.addDisplayRessources(new DisplayRessources(getGameTeam().data.prodFood, "food", this.x, this.y));
				
			}
		}
		if(stateRessourceFaith >= this.getAttribut(Attributs.frequencyProduceFaith) && getTeam()!=0){
			getGameTeam().special+=this.getAttribut(this.name,Attributs.produceFaith)*getGameTeam().data.prodFaith;
			stateRessourceFaith = 0;
			if(this.team==Game.g.currentPlayer.getGameTeam().id && this.getAttribut(this.name,Attributs.produceFaith)==1 ){
				// TODO : Produce faith display
				//				Game.g.addDisplayRessources(new DisplayRessources(getGameTeam().data.prodFaith, "special", this.x, this.y));
				
			}
		}
		//TOWER
		if(this.getGameTeam().data.getAttribut(this.name, Attributs.canAttack)>0){
			this.attack();
		}
		
	}
	
	public void removeProd(){
		if(this.queueTechnology!=null){
			
			this.getGameTeam().food += this.getAttribut(this.queueTechnology.objet, Attributs.foodCost);
			this.getGameTeam().gold += this.getAttribut(this.queueTechnology.objet, Attributs.goldCost);
			getHQ().currentTechsProduced.remove(this.queueTechnology.objet);
			this.queueTechnology=null;
			this.setCharge(0f);
			
		}
		if(this.queue.size()>0){
			float goldCost = getAttribut(getProductionList().get(this.queue.size()-1),Attributs.goldCost);
			float foodCost = getAttribut(getProductionList().get(this.queue.size()-1),Attributs.foodCost);
			float faithCost = getAttribut(getProductionList().get(this.queue.size()-1),Attributs.faithCost);
			float prodTime = getAttribut(getProductionList().get(this.queue.size()-1),Attributs.prodTime);
			this.getGameTeam().food += foodCost;
			this.getGameTeam().gold += goldCost;
			this.getGameTeam().special += faithCost;
			this.queue.remove(this.queue.size()-1);
			if(this.queue.size()==0){
				this.setCharge(0f);
			}
		}
	}
	
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
		
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
		
		// Initialize production
		this.queue = new Vector<Integer>();
		this.queueTechnology = null;
		
		
		teamCapturing= getTeam();
		
	}
	
	
	// TECH
	public Vector<ObjetsList> getTechnologyList(){

		Vector<ObjetsList> toReturn = new Vector<ObjetsList>();
		for(ObjetsList t:getRawTechnologyList()){
			boolean ok = true;
			if(getHQ().currentTechsProduced.contains(t) || getHQ().techsDiscovered.contains(t)){	
				continue;
			}

			// Get the techRequired
			Vector<String> techsRequired = getGameTeam().data.getAttributList(t, Attributs.techsRequired);
			boolean isValid = true;
			for(String techRequired : techsRequired){
				// Check we already have the techno required  discovered
				boolean hasTech=false;
				for(ObjetsList tech : getHQ().techsDiscovered){
					if(tech.getName().equals(techRequired.toLowerCase())){
						hasTech = true;
						break;
					}
				}
				if(!hasTech){
					isValid = false;
					break;
				}

			}
			if(isValid){
				toReturn.add(t);
			}

		}
		return toReturn;
	}
	public void techTerminate(Technologie q){
		if(q==null){
			return;
		}
		// Message research complete
		
		if(this.getTeam()==Game.g.currentPlayer.getTeam()){
			Game.g.sendMessage(ChatMessage.getById("research"));
		}
		this.setCharge(0f);
		getHQ().techsDiscovered.addElement(q.objet);
		getHQ().currentTechsProduced.remove(q.objet);
		q.applyEffect();
		this.queueTechnology=null;

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
		if(this.name.equals(ObjetsList.Stable) && c.getGameTeam().hq.age<2){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this.name.equals(ObjetsList.Academy) && c.getGameTeam().hq.age<3){
			//			this.p.addMessage(Message.getById(5), c.team);
			return;
		}
		if(this.potentialTeam!=c.getTeam() && c.mode==Character.TAKE_BUILDING && c.target==this){
			this.underAttack = true;
			this.underAttackRemaining =20f;

			if(this.constructionPoints<=0f){
				this.potentialTeam = c.getTeam();
				if(!(name.equals(ObjetsList.Headquarters))){
					this.setTeam(0);
				}
			}
			this.constructionPoints-=Main.increment;
		}
		if(this.potentialTeam==c.getTeam() && this.constructionPoints<this.getAttribut(Attributs.maxLifepoints) && c.mode==Character.TAKE_BUILDING && c.target==this){
			this.constructionPoints+=Main.increment;
		}
		if(this.constructionPoints>=this.getAttribut(Attributs.maxLifepoints) && this.potentialTeam==c.getTeam() && c.mode==Character.TAKE_BUILDING && c.target==this){
			if(this.potentialTeam!=this.getTeam()  ){
				if(((Game.g.teams.get(potentialTeam).pop+2)<=Game.g.teams.get(potentialTeam).maxPop)||this instanceof Bonus || (name.equals(ObjetsList.Headquarters))){

					this.setTeam(this.potentialTeam);
					if(name.equals(ObjetsList.Headquarters)){
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

	public void drawBasicImage(Graphics g){
		if(visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)){
			g.drawImage(Game.g.images.get("building"+name+this.getGameTeam().colorName),this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
		}else{
			g.drawImage(Game.g.images.get("building"+name+"neutral"), this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
		}
	}
	
	public Graphics draw(Graphics g){
		drawBasicImage(g);
		if((visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)) && mouseOver && Game.g.round>Game.nbRoundInit){
			Color color = new Color(this.getGameTeam().color.getRed(),this.getGameTeam().color.getGreen(),this.getGameTeam().color.getBlue(),0.1f);
			Game.g.images.get("building"+name+this.getGameTeam().colorName).drawFlash(this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY), 2*getAttribut(Attributs.sizeX)/1.8f, 3*getAttribut(Attributs.sizeY)/2,color);
		}
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
		if(this instanceof Building && this.getGameTeam().equals(Game.g.currentPlayer.getGameTeam())){
			Building bp = ((Building) this);
			if(bp.queue.size()>0){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bp.charge*(64*getAttribut(Attributs.sizeY))/this.getAttribut(bp.getProductionList().get(0),Attributs.prodTime));
				float opacity = 50*bp.charge/this.getAttribut(bp.getProductionList().get(bp.queue.get(0)),Attributs.prodTime);
				Image icone = Game.g.images.get("icon"+bp.getProductionList().get(bp.queue.get(0))+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				//g.setColor(new Color(0f,0f,0f,opacity));
				//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
				//						g.setColor(Color.white);
				//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
				g.setColor(new Color(bp.getGameTeam().color.r,bp.getGameTeam().color.g,bp.getGameTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bp.charge*(360f)/this.getAttribut(bp.getProductionList().get(bp.queue.get(0)),Attributs.prodTime));
				g.fillArc(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r, y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, x-sizeXIcon/2, y-offsetY-sizeXIcon/2);

			}
		}
		if(this.getGameTeam().equals(Game.g.currentPlayer.getGameTeam())){
			Building bt = ((Building) this);
			if(bt.queueTechnology!=null){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bt.charge*(64*getAttribut(Attributs.sizeY))/this.getAttribut(this.queueTechnology.objet, Attributs.prodTime));
				float opacity = 50*bt.charge/this.getAttribut(this.queueTechnology.objet, Attributs.prodTime);
				Image icone = Game.g.images.get(this.getAttributString(this.queueTechnology.objet, Attributs.nameIcon)+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				g.setColor(new Color(bt.getGameTeam().color.r,bt.getGameTeam().color.g,bt.getGameTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bt.charge*(360f)/this.getAttribut(this.queueTechnology.objet, Attributs.prodTime));
				g.fillArc(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r, y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, x-sizeXIcon/2, y-offsetY-sizeXIcon/2);

			}
		}
		
		
		//TOWER
		if(this.getGameTeam().data.getAttribut(this.name, Attributs.canAttack)>0){
			this.drawAnimationTower(g);
		}
		
		g.setAntiAlias(false);
		g.setLineWidth(2f);
		return g;
	}
	
	public void drawAnimationTower(Graphics g){
		float sizeX = getAttribut(Attributs.sizeX);
		float sizeY = getAttribut(Attributs.sizeY);
		if(getTeam()==1){
			g.drawImage(Game.g.images.get(this.animationBleu), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
		}
		if(getTeam()==2){
			g.drawImage(Game.g.images.get(this.animationRouge), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
		}
	}
	public Graphics drawRallyPoint(Graphics g){
		g.setColor(Colors.team0);
		g.setAntiAlias(true);
		g.setLineWidth(2f);
		g.fill(new Circle(this.rallyPoint.x,this.rallyPoint.y,3f));
		g.draw(new Circle(this.rallyPoint.x,this.rallyPoint.y,10f));
		g.setAntiAlias(false);
		g.setLineWidth(1f);
		return g;
	}
	
	
	public void drawAnimation(Graphics g){

	}
	

	
	public void setCharge(float charge){
		if(this.queue!=null && this.queue.size()>0 && charge>this.getAttribut(getProductionList().get(this.queue.get(0)),Attributs.prodTime)){
			this.charge = this.getAttribut(getProductionList().get(this.queue.get(0)),Attributs.prodTime);
			return;
		}
		
		this.charge= charge;
	}


	


	public void setTeamExtra(){

		if(this.queueTechnology!=null){
			this.queueTechnology=null;
		}
		this.setCharge(0f);
		

		if(this.queue!=null){
			this.queue.clear();
		}
		this.setCharge(0f);
	}
	
	
	public void setTeam(int i){

		if(!(this instanceof Bonus) && this.team!=0){
			this.getGameTeam().pop-=2;
		}
		if(Game.g.currentPlayer!=null && i==Game.g.currentPlayer.id && !(name.equals(ObjetsList.Headquarters))){
			Game.g.sendMessage(ChatMessage.getById("building taken"));

		}
		this.team = i;
		if(!(this instanceof Bonus)  && this.team!=0){
		
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
