package plateau;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bonus.Bonus;
import bullets.Fireball;
import data.Attributs;
import display.DisplayHandler;
import display.DisplayRessources;
import events.EventNames;
import madness.ActRule;
import main.Main;
import model.Colors;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.Communications;
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

	public static int sizeXIcon = 30;
	public float chargeTime;
	public int age=1;

	public Vector<ObjetsList> techsDiscovered;
	public Vector<ObjetsList> currentTechsProduced;
	public MarkerBuilding marker;
	public Vector<Circle> corners=new Vector<Circle>();


	private Technologie queueTechnology;


	private Vector<ObjetsList> queue ;
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


	public Building(ObjetsList name, float x, float y, Team team, Plateau plateau){
		// SET UP TECH LIST ET PRODUCTION LIST
		super(plateau);
		this.name = name;

		this.x = x;
		this.y = y;
		teamCapturing= 0;
		this.team = team;
		this.initialize(x, y, plateau);
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
					 && this.getTeam().enoughPop(getProductionList(plateau).get(unit))){
				this.queue.add(getProductionList(plateau).get(unit));
				
				this.getTeam().food-=foodCost;
//				if(this.team.id==Game.gameSystem.getCurrentTeam()){
//					DisplayHandler.addDisplayRessources(new DisplayRessources(-foodCost,"food",this.x,this.y));
//				}
				return true;
			}else {

				// Messages
//				if(this.getTeam()==Game.gameSystem.getCurrentTeam()){
//					if(foodCost>this.getTeam().food){
//						Communications.sendMessage(ChatMessage.getById("food"));
//					}  else {
//						Communications.sendMessage(ChatMessage.getById("pop"));
//					}
//				}
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

			float goldCost = getAttribut(getTechnologyList(plateau).get(unit),Attributs.goldCost);
			float foodCost = getAttribut(getTechnologyList(plateau).get(unit),Attributs.foodCost);
			float faithCost = getAttribut(getTechnologyList(plateau).get(unit),Attributs.faithCost);
			float prodTime = getAttribut(getTechnologyList(plateau).get(unit),Attributs.prodTime);
			if(foodCost<=this.getTeam().food ){

				this.queueTechnology = Technologie.technologie(getTechnologyList(plateau).get(unit), this.getTeam(), plateau);
				this.getTeam().food-=foodCost;
//				if(this.team==Game.gameSystem.getCurrentTeam(plateau)){
//					DisplayHandler.addDisplayRessources(new DisplayRessources(-foodCost,"food",this.x,this.y));
//				}
				getHQ(plateau).currentTechsProduced.add(getTechnologyList(plateau).get(unit));

				return true;
			} else {
				// Messages
//				if(this.getTeam()==Game.gameSystem.getCurrentTeam()){
//					if(foodCost>this.getTeam().food){
//						Communications.sendMessage(ChatMessage.getById("food"));
//					} else {
//						Communications.sendMessage(ChatMessage.getById("pop"));
//					}
//				}
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
			this.setTarget(null);
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
					this.setTarget(target1.get(0));
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
			if(this.getTeam().enoughPop(getQueue().get(0)) && this.charge>=this.getAttribut(getQueue().get(0), Attributs.prodTime)){
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

						c.setTarget(new Checkpoint(rallyPoint.x,rallyPoint.y,true, plateau));
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

		this.stateRessourceGold+=Main.increment;
		this.stateRessourceFood+=Main.increment;
		this.stateRessourceFaith+=Main.increment;


		if(stateRessourceFood >= this.getAttribut(Attributs.frequencyProduceFood) && getTeam().id!=0){
			getTeam().food+=this.getAttribut(this.name,Attributs.produceFood)*getTeam().data.prodFood;
			stateRessourceFood = 0;
//			if(this.team==Game.gameSystem.getCurrentTeam() && this.getAttribut(this.name,Attributs.produceFood)==1 ){
//				DisplayHandler.addDisplayRessources(new DisplayRessources(getTeam().data.prodFood, "food", this.x, this.y));
//
//			}
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
			float goldCost = getAttribut(getProductionList(plateau).get(this.queue.size()-1),Attributs.goldCost);
			float foodCost = getAttribut(getProductionList(plateau).get(this.queue.size()-1),Attributs.foodCost);
			float faithCost = getAttribut(getProductionList(plateau).get(this.queue.size()-1),Attributs.faithCost);
			float prodTime = getAttribut(getProductionList(plateau).get(this.queue.size()-1),Attributs.prodTime);
			this.getTeam().food += foodCost;

			this.queue.remove(this.queue.size()-1);
			if(this.queue.size()==0){
				this.setCharge(0f);
			}
		}
	}

	public void initialize(float f, float h, Plateau plateau){

		this.x = f*Map.stepGrid+getAttribut(Attributs.sizeX)/2f;
		this.y = h*Map.stepGrid+getAttribut(Attributs.sizeY)/2f;

		plateau.addBuilding(this);
		this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		this.collisionBox= new Rectangle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,getAttribut(Attributs.sizeX),getAttribut(Attributs.sizeY));
		this.marker = new MarkerBuilding(x,y,this, plateau);
		this.selectionBox = (Rectangle)this.collisionBox;
		this.setXY(x, y, plateau);
		this.constructionPoints = 0f;
		this.potentialTeam = this.getTeam().id;
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y-getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x+getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));
		corners.add(new Circle(x-getAttribut(Attributs.sizeX)/2f,y+getAttribut(Attributs.sizeY)/2f,20f));

		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2,true, plateau).id;
		// Initialize production
		this.queue = new Vector<ObjetsList>();
		this.queueTechnology = null;


		teamCapturing= getTeam().id;

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

//		if(this.getTeam(plateau).id==Game.gameSystem.getCurrentTeam()){
//			Communications.sendMessage(ChatMessage.getById("research"));
//		}
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
		if(this.name.equals(ObjetsList.Stable) && this.getHQ(plateau).age<2){
			return;
		}
		if(this.name.equals(ObjetsList.Academy) && this.getHQ(plateau).age<3){
			return;
		}

		if(this.potentialTeam!=c.getTeam().id && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			this.underAttack = true;
			this.underAttackRemaining =20f;

			if(this.constructionPoints<=0f){
				if(this.getAttribut(Attributs.defendable)==0){
					if(this.getTeam().id!=0 && this.name==ObjetsList.Tower){
						DisplayHandler.addEvent(EventNames.DestructionTower, this);
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
				this.constructionPoints-=Main.increment;
			}
		}
		if(this.potentialTeam==c.getTeam().id && this.constructionPoints<this.getAttribut(Attributs.maxLifepoints) && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			this.constructionPoints+=Main.increment;
		}
		if(this.constructionPoints>=this.getAttribut(Attributs.maxLifepoints) && this.potentialTeam==c.getTeam().id && c.mode==Character.TAKE_BUILDING && c.getTarget(plateau)==this){
			if(this.potentialTeam!=this.getTeam().id  ){
				if(this.getTeam().enoughPop(this.name)||this instanceof Bonus || (name.equals(ObjetsList.Headquarters))){

					this.setTeam(this.potentialTeam, plateau);

				}else if(ChatHandler.remainingTimeNotEnoughRoom<=0f){
					ChatHandler.remainingTimeNotEnoughRoom=10f;
					Communications.sendMessage(ChatMessage.getById("pop"));
				}
			}
		}
	}

	public void drawIsSelected(Graphics g, Plateau plateau){
		g.setColor(Colors.selection);
		g.setLineWidth(2f);
		g.draw(this.collisionBox);
		this.drawRallyPoint(g, plateau);
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
	}	

	public void drawBasicImage(Graphics g){
		if(visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)){
			g.drawImage(Images.get("building"+name+this.getTeam().colorName),this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
		}else{
			g.drawImage(Images.get("building"+name+"neutral"), this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY));
		}
	}
	public void drawBasicImageNewDesign(Graphics g){
		Image im = Images.get("building"+name+this.getTeam().colorName);
		if(visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)){
			if(this.getTeam().id==0){
				if(this.constructionPoints*4<this.getAttribut(Attributs.maxLifepoints))
					im = Images.get("building"+name+"neutral");
				else if(this.constructionPoints*2<this.getAttribut(Attributs.maxLifepoints))
					im = Images.get("building"+name+"neutral_1");
				else if(this.constructionPoints*4<this.getAttribut(Attributs.maxLifepoints)*3)
					im = Images.get("building"+name+"neutral_2");
				else
					im = Images.get("building"+name+"neutral_3");
			} else {
				im = Images.get("building"+name+this.getTeam().colorName);
			}
		}else{
			im = Images.get("building"+name+"neutral");
		}
		g.drawImage(im,this.x-im.getWidth()/2f, this.y+this.getAttribut(Attributs.sizeY)/2-im.getHeight());
	}

	public void drawFlash(Graphics g, Color color){
		Image im = Images.get("building"+name+"neutral");
		im.drawFlash(this.x-im.getWidth()/2f, this.y+this.getAttribut(Attributs.sizeY)/2-im.getHeight(), im.getWidth(), im.getHeight(), color);
	}
	
	public Graphics draw(Graphics g, Plateau plateau){
		return draw(g, false, plateau);
	}
	public Graphics draw(Graphics g, boolean isCurrentTeam, Plateau plateau){
		if(this.getAttribut(Attributs.newdesign)==0){
			drawBasicImageNewDesign(g);
			if((visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)) && mouseOver){
				Color color = new Color(this.getTeam().color.getRed(),this.getTeam().color.getGreen(),this.getTeam().color.getBlue(),0.1f);
				drawFlash(g, color);
			}
		} else {
			drawBasicImage(g);
			if((visibleByCurrentTeam || name.equals(ObjetsList.Headquarters)) && mouseOver){
				Color color = new Color(this.getTeam().color.getRed(),this.getTeam().color.getGreen(),this.getTeam().color.getBlue(),0.1f);
				Images.get("building"+name+this.getTeam().colorName).drawFlash(this.x-this.getAttribut(Attributs.sizeX)/1.8f, this.y-this.getAttribut(Attributs.sizeY), 2*getAttribut(Attributs.sizeX)/1.8f, 3*getAttribut(Attributs.sizeY)/2,color);
			}
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
		if(this instanceof Building && isCurrentTeam){
			Building bp = ((Building) this);
			if(bp.queue.size()>0){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bp.charge*(64*getAttribut(Attributs.sizeY))/this.getAttribut(bp.getProductionList(plateau).get(0),Attributs.prodTime));
				float opacity = 50*bp.charge/this.getAttribut(bp.queue.get(0),Attributs.prodTime);
				Image icone = Images.get("icon"+bp.getQueue().get(0)+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				//g.setColor(new Color(0f,0f,0f,opacity));
				//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
				//						g.setColor(Color.white);
				//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
				g.setColor(new Color(bp.getTeam().color.r,bp.getTeam().color.g,bp.getTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bp.charge*(360f)/this.getAttribut(bp.getQueue().get(0),Attributs.prodTime));
				g.fillArc(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r, y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, x-sizeXIcon/2, y-offsetY-sizeXIcon/2);

			}
		}
		if(isCurrentTeam){
			Building bt = ((Building) this);
			if(bt.queueTechnology!=null){
				float offsetY = Math.min(2*getAttribut(Attributs.sizeY)/3, bt.charge*(64*getAttribut(Attributs.sizeY))/this.getAttribut(this.queueTechnology.objet, Attributs.prodTime));
				float opacity = 50*bt.charge/this.getAttribut(this.queueTechnology.objet, Attributs.prodTime);
				Image icone = Images.get(this.getAttributString(this.queueTechnology.objet, Attributs.nameIcon)+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(x-r-10f, y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				g.setColor(new Color(bt.getTeam().color.r,bt.getTeam().color.g,bt.getTeam().color.b,opacity));
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
		if(this.getTeam().data.getAttribut(this.name, Attributs.canAttack)>0){
			this.drawAnimationTower(g);
		}

		g.setAntiAlias(false);
		g.setLineWidth(2f);
		return g;
	}

	public void drawAnimationTower(Graphics g){
		float sizeX = getAttribut(Attributs.sizeX);
		float sizeY = getAttribut(Attributs.sizeY);
		if(getTeam().id==1){
			g.drawImage(Images.get(this.animationBleu), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
		}
		if(getTeam().id==2){
			g.drawImage(Images.get(this.animationRouge), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
		}
	}
	public Graphics drawRallyPoint(Graphics g, Plateau plateau){
		Objet rallyPoint = getRallyPoint(plateau);
		g.setColor(Colors.team0);
		g.setAntiAlias(true);
		g.setLineWidth(2f);
		g.fill(new Circle(rallyPoint.x,rallyPoint.y,3f));
		g.draw(new Circle(rallyPoint.x,rallyPoint.y,10f));
		g.setAntiAlias(false);
		g.setLineWidth(1f);
		return g;
	}


	public void drawAnimation(Graphics g){

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


//		if(i==Game.gameSystem.getCurrentTeam() && !(name.equals(ObjetsList.Headquarters))){
//			Communications.sendMessage(ChatMessage.getById("building taken"));
//		}
		this.team = plateau.teams.get(i);
		this.setTeamExtra();
		this.giveUpProcess = false;
	}

	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub

	}




}
