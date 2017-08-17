package plateau;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bonus.Bonus;
import control.Player;
import data.Attributs;
import display.DisplayRessources;
import events.EventHandler;
import events.EventNames;

import main.Main;
import model.Colors;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.ChatMessage.MessageType;
import ressources.Images;
import ressources.Map;
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
	private int rallyPoint;
	public float charge;
	public boolean isDestroyed = false;
	public boolean giveUpProcess = false;
	public boolean underAttack;
	public float underAttackRemaining=0;
	public float state;
	public int i, j;

	public static int sizeXIcon = 30;
	public float chargeTime;
	public int age=1;

	public Vector<ObjetsList> techsDiscovered;
	public Vector<ObjetsList> currentTechsProduced;
	public MarkerBuilding marker;
	public Vector<Circle> corners=new Vector<Circle>();


	public Technologie queueTechnology;


	public Vector<ObjetsList> queue ;
	public float random=0f;
	//TOWER
	public float chargeAttack;
	public String animationBleu;
	public String animationRouge;
	public boolean canAttack=false;
	private float animationTower;


	private float stateRessourceFood;


	public Building(ObjetsList name, int i, int j, Team team, Plateau plateau){
		// SET UP TECH LIST ET PRODUCTION LIST
		super(plateau);
		this.name = name;

		this.x = (i*Map.stepGrid+this.getAttribut(Attributs.sizeX)/2);
		this.y = (j*Map.stepGrid+this.getAttribut(Attributs.sizeY)/2);
		this.i = i;
		this.j = j;
		teamCapturing= 0;
		this.team = team;
		plateau.addBuilding(this);
		this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		
		this.collisionBox= new Rectangle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,getAttribut(Attributs.sizeX),getAttribut(Attributs.sizeY));
		this.marker = new MarkerBuilding(x,y,this, plateau);
		this.selectionBox = (Rectangle)this.collisionBox;
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2,true, plateau).id;
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));

		// Initialize production
		this.queue = new Vector<ObjetsList>();
		this.queueTechnology = null;
		this.constructionPoints = 0f;
		this.potentialTeam = this.getTeam().id;
		teamCapturing= getTeam().id;
		if(this.team.id>0){
			this.constructionPoints = this.getAttribut(Attributs.maxLifepoints);
		}
		if(name.equals(ObjetsList.Headquarters)){
			initHQ();
		}
		//TODO : Animations should be generic !
		if(name.equals(ObjetsList.Tower)){
			this.animationBleu ="buildingTowerBlueAnimation";
			this.animationRouge = "buildingTowerRedAnimation";
		}
		// Add an event
		EventHandler.addEvent(EventNames.BuildingTakingGlobal, this, plateau);

	}


	public float getAttribut(ObjetsList o ,Attributs a){
		return this.getTeam().data.getAttribut(o, a);
	}
	public String getAttributString(ObjetsList o ,Attributs a){
		return this.getTeam().data.getAttributString(o, a);
	}
	public Vector<ObjetsList> getProductionList(Plateau plateau){
		Vector<ObjetsList> toReturn = new Vector<ObjetsList>();
		Vector<ObjetsList> result =  getTeam().data.getAttributListAtt(this.name, Attributs.units);

		// Remove units which not match tech required

		Vector<ObjetsList> techDiscovered = getHQ(plateau).techsDiscovered;
		for(ObjetsList o : result){
			Vector<ObjetsList> techsRequired =  this.getTeam().data.getAttributListAtt(o, Attributs.techsRequired);
			if(techDiscovered.containsAll(techsRequired)){
				toReturn.add(o);
			}
		}


		return toReturn;
	}
	public Vector<ObjetsList> getRawTechnologyList(){
		return getTeam().data.getAttributListAtt(this.name, Attributs.technologies);
	}
	public Vector<ObjetsList> getAllTechs(){
		return getTeam().data.getAttributListAtt(this.name, Attributs.technologies);
	}

	public boolean product(int unit, Plateau plateau){
		// TODO : fix method in a generic way

		// PRODUCTION LIST
		//UNIT PRODUCTION
		if(this.queueTechnology!=null){
			return false;
		}
		if(this.queue.size()<5 && unit<getProductionList(plateau).size()){
			float foodCost = getAttribut(getProductionList(plateau).get(unit),Attributs.foodCost);

			if(foodCost<=this.getTeam().food
					 && this.getTeam().enoughPop(getProductionList(plateau).get(unit), plateau)){
				this.queue.add(getProductionList(plateau).get(unit));
				System.out.println(this.getTeam().food+" "+foodCost);
				this.getTeam().food-=foodCost;
				if(this.team.id==Player.team){
					EventHandler.addEvent(new DisplayRessources(this, plateau,-foodCost,"food"), plateau);
				}
				return true;
			}else {

				// Messages
				if(this.team.id==Player.team){
					if(foodCost>this.getTeam().food){
						ChatHandler.addMessage(ChatMessage.getById(MessageType.NOTENOUGHFOOD));
					}  else {
						ChatHandler.addMessage(ChatMessage.getById(MessageType.NOTENOUGHPOP));
					}
				}
			}
		}

		return false;

	}

	public Technologie getQueueTechnologie(){
		return this.queueTechnology;
	}

	public boolean productTech(int unit, Plateau plateau) {
		// TODO Auto-generated method stub
		// TECH PRODUCTION
		if(this.queue.size()>0){
			return false;
		}
		if(this.queueTechnology==null && unit<getTechnologyList(plateau).size()){

			float foodCost = getAttribut(getTechnologyList(plateau).get(unit),Attributs.foodCost);
			if(foodCost<=this.getTeam().food ){
				this.queueTechnology = Technologie.technologie(getTechnologyList(plateau).get(unit), this.getTeam(), plateau);
				this.getTeam().food-=foodCost;
				if(this.team.id==Player.team){
					EventHandler.addEvent(new DisplayRessources(this, plateau,-foodCost,"food"), plateau);
				}
				getHQ(plateau).currentTechsProduced.add(getTechnologyList(plateau).get(unit));
				return true;
			} else {
				// Messages
				if(this.team.id==Player.team){
					if(foodCost>this.getTeam().food){
						ChatHandler.addMessage(ChatMessage.getById(MessageType.NOTENOUGHFOOD));
					}  else {
						ChatHandler.addMessage(ChatMessage.getById(MessageType.NOTENOUGHPOP));
					}
				}
			}
		}
		return false;
	}

	public Building getHQ(Plateau plateau){
		return plateau.getHQ(getTeam());
	}

	public void updateHeadQuarters(){

	}

	public void initHQ(){

		this.techsDiscovered = new Vector<ObjetsList>();

		this.currentTechsProduced = new Vector<ObjetsList>();
		
		this.getTeam().hq = this.id;
		// List of potential production 


	}

	public void attack(Plateau plateau){
//		if(Game.gameSystem.plateau.isRuleActive(ActRule.no_tower_attack)){
//			return;
//		}
		//Animation
		Objet target = getTarget(plateau);
		if(getTeam().id!=0)
			this.animationTower+=2f;
		if(this.animationTower>120f)
			this.animationTower = 1;

		if(target!=null && target.getTeam()==this.getTeam()){
			this.setTarget(null, plateau);
		}
		if(!this.canAttack)
			this.chargeAttack = (this.chargeAttack+Main.increment);
		if(this.chargeAttack>this.getAttribut(Attributs.chargeTime) && this.getTeam().id!=0){
			this.canAttack = true;
			this.chargeAttack = 0f;

		}
		if(canAttack){
			if(target==null || this.getTarget(plateau).lifePoints<0f ||Utils.distance(this, target)>getAttribut(Attributs.sight)){
				Vector<Character> target1= plateau.getEnnemiesInSight(this);
				if(target1.size()>0){
					this.setTarget(target1.get(0), plateau);
				}
			}

			//Launch a fireball on target
			if(target!=null && Utils.distance(this, target)<this.getAttribut(Attributs.sight)){
				new Fireball(this,target.getX(),target.getY(),this.getTarget(plateau).getX()-this.getX(),target.getY()-this.getY(),this.getAttribut(Attributs.damage),plateau);
				this.canAttack= false;
				this.chargeAttack = 0f;
			}
		}
	}

	public Objet getRallyPoint(Plateau plateau){
		return plateau.getById(this.rallyPoint);
	}
	public void action(Plateau plateau){
		Objet rallyPoint = getRallyPoint(plateau);
		// PRODUCTION	
		this.updateAttributsChange();
		giveUpProcess(plateau);

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
			if(this.getTeam().enoughPop(getQueue().get(0), plateau) && this.charge>=this.getAttribut(getQueue().get(0), Attributs.prodTime)){
				this.setCharge(0f);
				float dirX = this.random+getRallyPoint(plateau).x-this.x;
				float dirY = this.random+getRallyPoint(plateau).y - this.y;
				float norm = (float) Math.sqrt(dirX*dirX+dirY*dirY);
				//Introduit du random
				float startX = this.x + this.getAttribut(Attributs.sizeX)*dirX/norm/2;
				float startY = this.y + this.getAttribut(Attributs.sizeY)*dirY/norm/2;
				Character c = new Character(startX,startY, getQueue().get(0), this.getTeam(), plateau);

				if(rallyPoint!=null){
					if(rallyPoint instanceof Checkpoint){

						c.setTarget(new Checkpoint(rallyPoint.x,rallyPoint.y,true, plateau), plateau);
					}
					else if(rallyPoint instanceof Character){
						c.setTarget(rallyPoint,null,Character.AGGRESSIVE, plateau);
					}
					else if(rallyPoint instanceof Building){
						c.setTarget(rallyPoint,null,Character.TAKE_BUILDING, plateau);
					}
				}
				this.queue.remove(0);
			}
		}

		// TECH
		if(this.queueTechnology!=null){
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.getAttribut(this.queueTechnology.objet, Attributs.prodTime)){
				this.techTerminate(this.queueTechnology, plateau);
			}
		}

		//MINE

		this.stateRessourceFood+=Main.increment;


		if(stateRessourceFood >= this.getAttribut(Attributs.frequencyProduceFood) && getTeam().id!=0){
			getTeam().food+=this.getAttribut(this.name,Attributs.produceFood)*getTeam().data.prodFood;
			stateRessourceFood = 0;
			if(this.team.id==Player.team && this.getAttribut(this.name,Attributs.produceFood)!=0){
				EventHandler.addEvent(new DisplayRessources(this, plateau,this.getAttribut(this.name,Attributs.produceFood)*getTeam().data.prodFood,"food"), plateau);
			}
		}

		//TOWER
		if(this.getTeam().data.getAttribut(this.name, Attributs.canAttack)>0){
			this.attack(plateau);
		}

	}

	public void removeProd(Plateau plateau){
		if(this.queueTechnology!=null){

			this.getTeam().food += this.getAttribut(this.queueTechnology.objet, Attributs.foodCost);
			getHQ(plateau).currentTechsProduced.remove(this.queueTechnology.objet);
			this.queueTechnology=null;
			this.setCharge(0f);

		}
		if(this.queue.size()>0){
			float foodCost = getAttribut(getProductionList(plateau).get(this.queue.size()-1),Attributs.foodCost);
			this.getTeam().food += foodCost;

			this.queue.remove(this.queue.size()-1);
			if(this.queue.size()==0){
				this.setCharge(0f);
			}
		}
	}



	public void setRallyPoint(float x , float y, Plateau plateau){
		Objet rp = this.getRallyPoint(plateau);
		rp.x = x;
		rp.y = y;

	}


	// TECH
	public Vector<ObjetsList> getTechnologyList(Plateau plateau){

		Vector<ObjetsList> toReturn = new Vector<ObjetsList>();
		for(ObjetsList t:getRawTechnologyList()){
			boolean ok = true;
			if(getHQ(plateau).currentTechsProduced.contains(t) || getHQ(plateau).techsDiscovered.contains(t)){	
				continue;
			}

			// Get the techRequired
			Vector<String> techsRequired = getTeam().data.getAttributList(t, Attributs.techsRequired);
			boolean isValid = true;
			for(String techRequired : techsRequired){
				// Check we already have the techno required  discovered
				boolean hasTech=false;
				for(ObjetsList tech : getHQ(plateau).techsDiscovered){
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
	public void techTerminate(Technologie q, Plateau plateau){
		if(q==null){
			return;
		}
		// Message research complete

		if(this.team.id==Player.team){
			ChatHandler.addMessage(ChatMessage.getById(MessageType.RESEARCHCOMPLETE));
		}
		this.setCharge(0f);
		getHQ(plateau).techsDiscovered.addElement(q.objet);
		getHQ(plateau).currentTechsProduced.remove(q.objet);
		q.applyEffect();
		this.queueTechnology=null;

	}

	public Vector<ObjetsList> getQueue(){
		return this.queue;
	}
	public void resetRallyPoint(Plateau plateau){
		Objet rallyPoint = this.getRallyPoint(plateau);
		rallyPoint.x = this.x;
		rallyPoint.y = this.y+this.getAttribut(Attributs.sizeY)/2+10;

	}

	public void giveUpProcess(Plateau plateau){
		if(giveUpProcess){
			constructionPoints-=Main.increment;
			if(constructionPoints<=0f){
				this.setTeam(0, plateau);
			}
		}
	}
	public void collisionWeapon(Character c, Plateau plateau){
		if(this instanceof Bonus && !((Bonus)this).bonusPresent){
			return;
		}
		if( c.getAttributString(Attributs.weapon).equals("bow") || c.getAttributString(Attributs.weapon).equals("wand") || c.getAttributString(Attributs.weapon).equals("bible"))
			return;

		if(this.potentialTeam!=c.getTeam().id && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			this.underAttack = true;
			this.underAttackRemaining =20f;

			if(this.constructionPoints<=0f){
				if(this.getAttribut(Attributs.defendable)==0){
					if(this.getTeam().id!=0 && this.name==ObjetsList.Tower){
						EventHandler.addEvent(EventNames.DestructionTower, this, plateau);
					}
					this.isDestroyed = true;
					if(this.name!=ObjetsList.Headquarters){
						this.setTeam(0, plateau);
					}
				} else {
					this.setTeam(0, plateau);
					this.potentialTeam = c.getTeam().id;
				}
			}
			this.chargeAttack = Math.max(this.chargeAttack-2*Main.increment, 0);
			if(!this.isDestroyed){
				EventHandler.addEventBuildingTaking(c, this, plateau);
				this.constructionPoints-=Main.increment;
			}
		}
		if(this.potentialTeam==c.getTeam().id && this.constructionPoints<this.getAttribut(Attributs.maxLifepoints) && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			this.constructionPoints+=Main.increment;
			EventHandler.addEventBuildingTaking(c, this, plateau);
		}
		if(this.constructionPoints>=this.getAttribut(Attributs.maxLifepoints) && this.potentialTeam==c.getTeam().id && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			if(this.potentialTeam!=this.getTeam().id  ){
				if(this.getTeam().enoughPop(this.name, plateau)||this instanceof Bonus || (name.equals(ObjetsList.Headquarters))){

					this.setTeam(this.potentialTeam, plateau);

				}else if(ChatHandler.remainingTimeNotEnoughRoom<=0f){
					ChatHandler.remainingTimeNotEnoughRoom=10f;
					ChatHandler.addMessage(ChatMessage.getById(MessageType.NOTENOUGHPOP));
				}
			}
		}
	}


	public void setCharge(float charge){
		if(this.queue!=null && this.queue.size()>0 && charge>this.getAttribut(this.getQueue().get(0),Attributs.prodTime)){
			this.charge = this.getAttribut(this.getQueue().get(0),Attributs.prodTime);
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


	public void setTeam(int i, Plateau plateau){


		if(i==Player.team && !(name.equals(ObjetsList.Headquarters))){
			ChatHandler.addMessage(ChatMessage.getById(MessageType.BUILDINGTAKEN));
		}
		this.team = plateau.teams.get(i);
		this.setTeamExtra();
		this.giveUpProcess = false;
	}

	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub

	}




}
