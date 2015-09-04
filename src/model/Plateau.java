package model;
//TODO

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.*;
import multiplaying.OutputModel.OutputBullet;
import multiplaying.OutputModel.OutputChar;

public class Plateau {

	float soundVolume;
	Sounds sounds;
	Images images;
	public Game g;
	protected Sound deathSound;
	protected int nTeams;
	protected float maxX ;
	protected float maxY ;
	// Camera 
	float Xcam;
	float Ycam;
	// fog of war
	Image fog;
	Graphics gf;

	// ADD ALL OBJETS 
	protected Vector<Character> characters;
	protected Vector<Character> toAddCharacters;
	protected Vector<Character> toRemoveCharacters;

	protected Vector<ActionObjet> equipments;
	protected Vector<ActionObjet> toAddEquipments;
	protected Vector<ActionObjet> toRemoveEquipments;

	protected Vector<Bullet> bullets;
	protected Vector<Bullet> toAddBullets;
	protected Vector<Bullet> toRemoveBullets;

	protected Vector<Building> buildings;
	protected Vector<Building> toAddBuildings;
	protected Vector<Building> toRemoveBuildings;

	protected Vector<NaturalObjet> naturalObjets ;
	protected Vector<NaturalObjet> toAddNaturalObjets;
	protected Vector<NaturalObjet> toRemoveNaturalObjets;

	protected Vector<Vector<Character>> selection;
	protected Vector<Vector<Character>> toAddSelection;
	protected Vector<Vector<Character>> toRemoveSelection ;
	protected Vector<Rectangle> rectangleSelection;
	Vector<Float> recX ;
	Vector<Float> recY ;

	protected Constants constants;
	//TODO : make actionsObjets and everything else private 

	public Plateau(Constants constants,float maxX,float maxY,int nTeams, Game g){
		this.soundVolume = g.soundVolume;
		this.sounds = g.sounds;
		this.images = g.images;
		this.g = g;
		//GENERAL
		this.constants = constants;
		this.nTeams = nTeams;
		this.maxX= maxX;
		this.maxY = maxY;
		//CHARACTERS
		this.characters = new Vector<Character>();
		this.toAddCharacters = new Vector<Character>();
		this.toRemoveCharacters = new Vector<Character>();
		//WEAPONS
		this.equipments = new Vector<ActionObjet>();
		this.toAddEquipments = new Vector<ActionObjet>();
		this.toRemoveEquipments = new Vector<ActionObjet>();
		//WEAPONS
		this.bullets = new Vector<Bullet>();
		this.toAddBullets = new Vector<Bullet>();
		this.toRemoveBullets= new Vector<Bullet>();
		//NATURALOBJETS
		this.naturalObjets = new Vector<NaturalObjet>();
		this.toAddNaturalObjets = new Vector<NaturalObjet>();
		this.toRemoveNaturalObjets= new Vector<NaturalObjet>();
		//ENEMYGENERATOR
		this.buildings = new Vector<Building>();
		this.toAddBuildings = new Vector<Building>();
		this.toRemoveBuildings = new Vector<Building>();
		//SELECTION
		this.selection = new Vector<Vector<Character>>();
		this.toAddSelection = new Vector<Vector<Character>>();
		this.toRemoveSelection = new Vector<Vector<Character>>();
		this.rectangleSelection = new Vector<Rectangle>();
		this.recX = new Vector<Float>();
		this.recY = new Vector<Float>();
		for(int i =0; i<=nTeams;i++){
			this.recX.addElement(0f);
			this.recY.addElement(0f);
			this.selection.addElement(new Vector<Character>());
			this.toAddSelection.addElement(new Vector<Character>());
			this.toRemoveSelection.addElement(new Vector<Character>());
			this.rectangleSelection.addElement(null);
		}
		try {
			this.deathSound = new Sound("music/death.ogg");
			this.fog = new Image((int)this.g.resX,(int)this.g.resY);
			this.gf = fog.getGraphics();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// functions that handle buffers
	public void addCharacterObjets(Character o){
		toAddCharacters.addElement(o);
	}
	private void removeCharacter(Character o){
		toRemoveCharacters.addElement(o);
	}
	public void addEquipmentObjets(ActionObjet o){
		toAddEquipments.addElement(o);
	}
	private void removeEquipment(ActionObjet o){
		toRemoveEquipments.addElement(o);
	}
	public void addBulletObjets(Bullet o){
		toAddBullets.addElement(o);
	}
	private void removeBullet(Bullet o){
		toRemoveBullets.addElement(o);
	}
	public void addNaturalObjets(NaturalObjet o){
		toAddNaturalObjets.addElement(o);
	}
	private void removeNaturalObjets(NaturalObjet o){
		toRemoveNaturalObjets.addElement(o);
	}
	public void addBuilding(Building o){
		toAddBuildings.addElement(o);
	}
	public void removeBuilding(Building o){
		toRemoveBuildings.addElement(o);
	}
	public void addSelection(Character o,int team){
		toAddSelection.get(team).addElement(o);
	}
	public void removeSelection(Character o, int team){
		toRemoveSelection.get(team).addElement(o);
	}

	public void clearSelection(int team){
		this.selection.get(team).clear();
	}

	public void clean(){
		// Clean the buffers and handle die
		// Remove and add considering alive 
		for(Character o : characters){
			if(!o.isAlive()){
				this.removeCharacter(o);
				if(o.team==1)
					this.g.players.get(0).ennemiesKilled+=1;

				this.deathSound.play(0.8f+1f*((float)Math.random()),this.soundVolume);
			}
		}
		for(ActionObjet o : equipments){
			if(!o.isAlive()){
				this.removeEquipment(o);
			}
		}
		for(Bullet o : bullets){
			if(!o.isAlive()){
				this.removeBullet(o);
			}
		}
		for(Character o : characters){
			if(!o.isAlive()){
				this.removeCharacter(o);
			}
		}
		for(NaturalObjet o : naturalObjets){
			if(!o.isAlive()){
				this.removeNaturalObjets(o);
			}
		}
		for(Building o : buildings){
			if(!o.isAlive()){
				this.removeBuilding(o);
			}
		}

		// Update selection
		for(int i=0;i<nTeams;i++){
			for(Character c: selection.get(i)){
				if(!c.isAlive()){
					this.removeSelection(c, i);
				}
			}
			for(Character o: toRemoveSelection.get(i)){
				selection.get(i).remove(o);
			}
			for(Character o: toAddSelection.get(i)){
				selection.get(i).addElement(o);
			}
		}
		// Remove objets from lists
		for(Character o: toRemoveCharacters){
			characters.remove(o);
			o.destroy();
		}
		for(Character o: toAddCharacters){
			characters.addElement(o);
		}
		for(ActionObjet o: toRemoveEquipments){
			equipments.remove(o);
		}
		for(ActionObjet o: toAddEquipments){
			equipments.addElement(o);
		}
		for(Bullet o: toRemoveBullets){
			bullets.remove(o);
		}
		for(Bullet o: toAddBullets){
			bullets.addElement(o);
		}
		for(NaturalObjet o: toRemoveNaturalObjets){
			naturalObjets.remove(o);
		}
		for(NaturalObjet o: toAddNaturalObjets){
			naturalObjets.addElement(o);
		}
		for(Building o: toRemoveBuildings){
			buildings.remove(o);
		}
		for(Building o: toAddBuildings){
			buildings.addElement(o);
		}

		// Clear the vector :
		for(int i = 0;i<nTeams;i++){
			toAddSelection.get(i).clear();
			toRemoveSelection.get(i).clear();
		}

		toRemoveCharacters.clear();
		toRemoveEquipments.clear();
		toRemoveBullets.clear();
		toRemoveNaturalObjets.clear();
		toRemoveBuildings.clear();
		toAddCharacters.clear();
		toAddEquipments.clear();
		toAddBullets.clear();
		toAddNaturalObjets.clear();
		toAddBuildings.clear();
	}

	//getters and setters
	public float getMaxX(){return maxX;}
	public float getMaxY(){return maxY;}

	//general method calling collision method of the regarded objects
	public void collision(){
		for(Character o : characters){
			// Handle collision between actionObjets and action objects
			for(Character i:characters){
				if(i.collisionBox.intersects(o.collisionBox) && i!=o){
					i.collision(o);
					o.collision(i);
				}

			}
			// between Characters and Natural objects
			for(NaturalObjet i: naturalObjets){
				if(i.collisionBox.intersects(o.collisionBox)){
					o.collision(i);
				}
			}
			// Between Characters and bullets
			for(Bullet i: bullets){
				if(i.collisionBox.intersects(o.collisionBox)){
					i.collision(o);
				}
			}
			// Between characters and weapons
			for(ActionObjet i:equipments){

				if(i.collisionBox.intersects(o.collisionBox)){
					i.collision(o);
				}
			}
			// Between characters and generator
			for(Building e:buildings){

				if(e.collisionBox.intersects(o.collisionBox)){
					o.collision(e);
				}
			}
		}
		// Between bullets and natural objets
		for(Bullet b : bullets){
			for(NaturalObjet n: naturalObjets){
				b.collision(n);
			}
		}
	}

	public void updateSelection(Rectangle select,int team){
		if(select!=null){
			this.clearSelection(team);
			this.selection(select,team);
		}
	}



	//calling method to the environment
	public Vector<Objet> getEnnemiesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o.team!=caller.team && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.team==caller.team && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getWoundedAlliesInSight(Character caller){
		Vector<Objet> ennemies_in_sight = new Vector<Objet>();
		for(Character o : characters){
			if(o!=caller && o.team==caller.team && o.lifePoints<o.maxLifePoints && o.collisionBox.intersects(caller.sightBox)){
				ennemies_in_sight.add(o);
			}
		}
		return ennemies_in_sight;
	}
	public Vector<Objet> getRessourcesInSight(Character caller){
		Vector<Objet> ressources_in_sight = new Vector<Objet>();
		for(NaturalObjet o : naturalObjets){
			if(o.collisionBox.intersects(caller.sightBox)){
				ressources_in_sight.add(o);
			}
		}
		return ressources_in_sight;
	}


	//handling the input
	public void updateTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		//TODO : look amongst horses and weapons too
		if(target==null){
			target = new Checkpoint(this,x,y);
		}
		Character leader = null;
		for(Character o:this.selection.get(team)){
			if(leader==null){
				leader=o;
			}
			//first we deal with o's elder group
			//if o was the leader and there were other members in the group
			if(o.isLeader() && o.group.size()>1){
				//we set the group of the new leader
				o.group.get(1).group = o.group;
				//we set the new leader amongst the member of the group
				for(Character o1: o.group){
					o1.leader = o.group.get(1);
				}
				//we remove o from the group

			}
			if(o.leader!=null){
				o.leader.group.remove(o);
			}
			if(leader==o){
				o.group = new Vector<Character>();
			}
			//we set to o its new leader and to its leader's group the new member
			o.leader = leader;
			if(!o.isLeader())
				o.group=null;
			o.leader.group.add(o);
			//eventually we assign the target
			o.setTarget(target);
			o.secondaryTargets.clear();
		}

	}

	public void updateSecondaryTarget(float x, float y, int team){
		//called when right click on the mouse
		Objet target = this.findTarget(x, y);
		//TODO : look amongst horses and weapons too
		if(target==null){
			target = new Checkpoint(this,x,y);
		}
		Character leader = null;
		for(Character o:this.selection.get(team)){
			if(leader==null){
				leader=o;
			}
			//first we deal with o's elder group
			//if o was the leader and there were other members in the group
			if(o.isLeader() && o.group.size()>1){
				//we set the group of the new leader
				o.group.get(1).group = o.group;
				//we set the new leader amongst the member of the group
				for(Character o1: o.group){
					o1.leader = o.group.get(1);
				}
				//we remove o from the group

			}
			if(o.leader!=null){
				o.leader.group.remove(o);
			}
			if(leader==o){
				o.group = new Vector<Character>();
			}
			//we set to o its new leader and to its leader's group the new member
			o.leader = leader;
			if(!o.isLeader())
				o.group=null;
			o.leader.group.add(o);
			//eventually we assign the target
			o.secondaryTargets.add(target);
		}

	}

	public Objet findTarget(float x, float y){
		Point point= new Point(x,y);
		Objet target = null;
		//looking for the object on the target
		for(Character i:this.characters){
			// looking amongst other characters
			if(i.collisionBox.contains(point)){
				target = i;
				break;
			}
		}
		if(target==null){
			for(NaturalObjet i: naturalObjets){
				// looking amongst natural object
				if(i.collisionBox.contains(point)){
					target = i;
					break;
				}
			}
		}	
		return target;
	}

	private void selection(Rectangle select, int team) {
		//handling the selection
		for(Character o: characters){
			if(o.collisionBox.intersects(select) && o.team==team ){
				//add character to team selection
				this.addSelection(o, team);
			}
		}
	}


	//general methods 
	public void action(){
		for(Character o: this.characters){
			o.action();
		}
		for(ActionObjet o: this.equipments){
			o.action();
		}
		for(Bullet o: bullets){
			o.action();
		}
		for(Building e: this.buildings){
			e.action();
		}
	}
	public OutputModel update(Vector<InputModel> ims){
		/* Pipeline of the update:
		 * 1 - If ESC start menu
		 * 2 - Handling inputs (1 loop per player)
		 * 3 - Collision, Action, Cleaning
		 * 4 - Other updates
		 */

		OutputModel om = new OutputModel(0);

		// 1 - If ESC start menu
		if(!this.g.inMultiplayer && !g.isInMenu && ims.get(0)!=null && ims.get(0).isPressedESC){
			this.g.setMenu(g.menuPause);
			return om;
		}
		// 2 - Handling inputs (1 loop per player)
		InputModel im;
		for(int player=1; player<this.g.players.size(); player++){
			im = null;
			System.out.println("vaneau: "+player + " + " + ims);
			for(InputModel inp : ims)
				if(inp.team==player)
					im = inp;
			//im = ims.get(player-1);
			if(im!=null){
				System.out.println("Ok pour player "+ im.team+" Plateau line 522");
				if(player==this.g.currentPlayer){
					// Move camera according to inputs :
					if(im.isPressedUP || im.yMouse<Ycam+10){
						Ycam -= 10;
					}
					if(im.isPressedDOWN || im.yMouse>Ycam+this.g.resY-10){
						Ycam +=10;
					}
					if(im.isPressedLEFT|| im.xMouse<Xcam+10){
						Xcam -=10;
					}
					if(im.isPressedRIGHT || im.xMouse>Xcam+this.g.resX-10){
						Xcam += 10;
					}
					if(im.isPressedLeftClick){
						this.clearSelection(player);
					}
				}
				for(int to=0; to<10; to++){
					if(im.isPressedNumPad[to]){
						this.g.players.get(player).groupSelection = to;
						if(im.isPressedCTRL){
							// Creating a new group made of the selection
							this.g.players.get(player).groups.get(to).clear();
							for(Character c: this.selection.get(player))
								this.g.players.get(player).groups.get(to).add(c);
						} else if(im.isPressedMAJ){
							// Adding the current selection to the group
							for(Character c: this.selection.get(player))
								this.g.players.get(player).groups.get(to).add(c);
						} else {
							this.selection.get(player).clear();
							for(Character c: this.g.players.get(player).groups.get(to))
								this.selection.get(player).add(c);
						}
						System.out.println("group "+ player + " " + to + " "+ this.g.players.get(player).groups.get(to));
					}
				}

				if(im.isPressedLeftClick){
					this.clearSelection(player);
				}
				// Update the rectangle
				if(im.leftClick){
					// As long as the button is pressed, the selection is updated
					if(rectangleSelection.get(player)==null){
						recX.set(player, (float)im.xMouse);
						recY.set(player, (float)im.yMouse);
						rectangleSelection.set(player, new Rectangle(recX.get(player),recY.get(player),0.1f,0.1f));
					}
					rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)Math.min(recY.get(player), im.yMouse),
							(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(im.yMouse-recY.get(player))+0.1f);
				}
				else{
					// The button is not pressed and wasn't, the selection is non null
					this.rectangleSelection.set(player, null);
				}
				if(this.selection!=null){
					// The button is not pressed and wasn't, the selection is non null
					this.updateSelection(rectangleSelection.get(player), player);
					
				}
				

				// Action for player k
				if(im.isPressedRightClick){
					if(im.isPressedMAJ){
						updateSecondaryTarget(im.xMouse,im.yMouse,player);
					} else {				
						updateTarget(im.xMouse,im.yMouse,player);
					}
				}
				// Update the selections of the players
				this.g.players.get(player).selection.clear();
				for(Character c: this.selection.get(player))
					this.g.players.get(player).selection.addElement(c);

			}
		}
		// Handling the changes
		// TODO : Mulitplayer selection
		for(Character c : this.selection.get(2)){
			om.selection.add(c.id);
		}

		// 3 - Collision, Action, Cleaning
		this.collision();
		this.clean();
		this.action();


		// 4 - Update of the music
		if(!g.isInMenu && !this.g.musicStartGame.playing() && !this.g.mainMusic.playing()){
			this.g.mainMusic.loop();
		}
		for(Character c: this.characters){
			om.toChangeCharacters.add(new OutputChar(c.id,c.team,c.x,c.y,c.lifePoints,c.typeWeapon, c.typeHorse, c.animation, c.orientation));
		}
		for(Bullet b: this.bullets){
			if(b instanceof Arrow)
				om.toChangeBullets.add(new OutputBullet(b.id,0,b.x,b.y,b.vx,b.vy));
			else
				om.toChangeBullets.add(new OutputBullet(b.id,1,b.x,b.y,b.vx,b.vy));
		}
		return om;
	}

	public void updateFromOutput(OutputModel om, InputModel im){
		// Handling im

		if(im!=null){
			int player = this.g.currentPlayer;

			// Update the rectangle
			if(im.leftClick){
				// As long as the button is pressed, the selection is updated
				if(rectangleSelection.get(player)==null){
					recX.set(player,(float)im.xMouse);
					recY.set(player,(float)im.yMouse);
					rectangleSelection.set(player, new Rectangle(recX.get(player),recY.get(player),0.1f,0.1f));
				}
				rectangleSelection.get(player).setBounds( (float)Math.min(recX.get(player),im.xMouse), (float)Math.min(recY.get(player), im.yMouse),
						(float)Math.abs(im.xMouse-recX.get(player))+0.1f, (float)Math.abs(im.yMouse-recY.get(player))+0.1f);
			}
			else {
				this.rectangleSelection.set(player, null);
			}

			// Move camera according to inputs :
			if(im.isPressedUP || im.yMouse<Ycam+10){
				Ycam -= 10;
			}
			if(im.isPressedDOWN || im.yMouse>Ycam+this.g.resY-10){
				Ycam +=10;
			}
			if(im.isPressedLEFT|| im.xMouse<Xcam+10){
				Xcam -=10;
			}
			if(im.isPressedRIGHT || im.xMouse>Xcam+this.g.resX-10){
				Xcam += 10;
			}
		}
		if(om!=null){
			// Characters
			// Changing characters
			Character c=null;
			for(OutputChar occ : om.toChangeCharacters){
				c = null;
				for(Character c2: this.characters)
					if(c2.id==occ.id)
						c = c2;
				if(c!=null){
					c.change(occ);
				} else {
					new Character(occ,this);
				}
			}
			boolean toErase = true;
			for(Character c2: this.characters){
				toErase = true;
				for(OutputChar occ : om.toChangeCharacters){
					if(occ.id==c2.id)
						toErase = false;
				}
				if(toErase)
					this.toRemoveCharacters.addElement(c2);
			}
			// Bullets
			// Changing bullets
			Bullet b=null;
			for(OutputBullet ocb : om.toChangeBullets){
				b = null;
				for(Bullet b2: this.bullets)
					if(b2.id==ocb.id)
						b = b2;
				if(b!=null){
					b.change(ocb);
				}else{
					if(ocb.typeBullet==0){
						new Arrow(ocb,this);
					} else if (ocb.typeBullet == 1){
						new Fireball(ocb,this);				
					}
				}

			}
			toErase = true;
			for(Bullet c2: this.bullets){
				toErase = true;
				for(OutputBullet occ : om.toChangeBullets){
					if(occ.id==c2.id)
						toErase = false;
				}
				if(toErase)
					this.toRemoveBullets.addElement(c2);
			}
			this.selection.set(this.g.currentPlayer, new Vector<Character>());
			for(int i : om.selection){
				for(Character c2: this.characters)
					if(c2.id==i)
						this.selection.get(this.g.currentPlayer).addElement(c2);
			}
		}
		// Remove objets from lists
		this.clean();
	}

	// drawing method
	public void drawFogOfWar(Graphics g){
		Vector<Objet> visibleObjet = new Vector<Objet>();
		visibleObjet = this.getInCamObjets(this.g.currentPlayer);
		float resX = this.g.resX;
		float resY = this.g.resY;
		this.gf.setColor(new Color(50,50,50));
		gf.fillRect(0, 0, resX, resY);
		gf.setColor(Color.white);
		for(Objet o:visibleObjet)
			gf.fillOval(o.x-Xcam-o.sight,o.y-Ycam-o.sight,o.sight*2f,o.sight*2f);
		gf.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.drawImage(fog,Xcam,Ycam);		
		g.setDrawMode(Graphics.MODE_NORMAL);
		//Utils.printCurrentState(this);
	}

	private Vector<Objet> getInCamObjets(int team) {
		Vector<Objet> obj = new Vector<Objet>();
		for(Character c: this.characters)
			if(c.team==team&&(c.x+c.sight>Xcam&&c.x-c.sight<Xcam+this.g.resX&&c.y+c.sight>Ycam&&c.y-c.sight<Ycam+this.g.resY))
				obj.add(c);
		for(Building c: this.buildings)
			if(c.team==team&&(c.x+c.sight>Xcam||c.x-c.sight<Xcam+this.g.resX||c.y+c.sight>Ycam||c.y-c.sight<Ycam+this.g.resY))
				obj.add(c);
		return obj;
	}
	
	public boolean isVisibleByPlayer(int player, Objet objet){
		if(objet.x+objet.sight<Xcam||objet.x-objet.sight>Xcam+this.g.resX||objet.y+objet.sight<Ycam||objet.y-objet.sight>Ycam+this.g.resY)
			return false;
		for(Character c: this.characters)
			if(c.team==player && Utils.distance(c, objet)<c.sight)
				return true;
		for(Building b: this.buildings)
			if(b.team==player && Utils.distance(b,  objet)<b.sight)
				return true;
		return false;
	}

}



