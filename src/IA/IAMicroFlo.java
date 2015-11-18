package IA;

import java.util.Vector;

import model.Checkpoint;
import model.GameTeam;
import model.IAPlayer;
import model.Plateau;
import model.Utils;
import spells.SpellConversion;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;


public class IAMicroFlo extends IAPlayer {

	//Groups of units
	static int SPEARMAN=0;
	static int CROSSBOWMAN=1;
	static int KNIGHT=2;
	static int INQUISITOR=3;
	static int PRIEST=4;
	static int ARCHANGE=5;



	public IAMicroFlo(Plateau p, int id, String name, GameTeam gameteam,int resX, int resY) {
		super(p, id, name, gameteam, resX, resY);

	}


	public void update(){

		//MAKE UNITS IN CORRESPONDING GROUPS
		makeUnitGroups();

		//Get ennemy units
		Vector<Character> ennemies =  getEnnemyUnitsInSight();

		//handle type of unit separately
		handleSpearman(ennemies);
		handleCrossbowman(ennemies);
		handleKnight(ennemies);
		handlePriest(ennemies);
		handleInquisitor(ennemies);

	}


	public void handleSpearman(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(SPEARMAN);
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
		}
	}
	public void handleCrossbowman(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(CROSSBOWMAN);
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
			//TODO : handle hit and run
			//Given ennemies considering charge run away from ennemies ( stay at range)
			//Get first and second nearest ennemy, move in orthogonal direction
			if(charac.state<charac.chargeTime){
				Character c1 = IAUtils.nearestUnit(ennemies, charac);
				if(Utils.distance(c1, charac)>charac.range){
					continue;
				}
				ennemies.remove(c1);
				Character c2 = IAUtils.nearestUnit(ennemies, charac);
				float norm = Utils.distance(c1, c2);
				float dirX = c1.getY()-c2.getY();
				float dirY = c2.getX()-c1.getX();
				dirX /=norm;
				dirY /= norm;
				
				charac.setTarget(new Checkpoint(charac.getX()+10f*dirX,charac.getY()+10f*dirY));
			}


		}
	}


	public void handleKnight(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(KNIGHT);
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
		}
	}
	public void handlePriest(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(PRIEST);
		for(Character charac : units){
			Character targetConversion = IAUtils.nearestUnit(ennemies, charac);
			SpellConversion sp = (SpellConversion) charac.spells.get(1);
			if(this.getGameTeam().special>=sp.faithCost && targetConversion!=null){
				sp.launch(targetConversion, charac);
			}
		}
	}
	public void handleInquisitor(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(INQUISITOR);
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
			if(charac.lifePoints<0.10*charac.maxLifePoints){
				if(charac.spells.size()>0){
					charac.spells.get(0).launch(new Checkpoint(0f,0f), charac);
				}
			}
			if(charac.state<charac.chargeTime){
				Character c1 = IAUtils.nearestUnit(ennemies, charac);
				if(Utils.distance(c1, charac)>charac.range){
					continue;
				}
				ennemies.remove(c1);
				Character c2 = IAUtils.nearestUnit(ennemies, charac);
				float norm = Utils.distance(c1, c2);
				float dirX = c1.getY()-c2.getY();
				float dirY = c2.getX()-c1.getX();
				dirX /=norm;
				dirY /= norm;
				
				charac.setTarget(new Checkpoint(charac.getX()+10f*dirX,charac.getY()+10f*dirY));
			}
		}
	}
	public void handleArchange(Vector<Character> ennemies){
		Vector<Character> units = this.getUnitsGroup(ARCHANGE);
		for(Character charac : units){
			charac.setTarget(IAUtils.nearestUnit(ennemies, charac));
		}
	}



	public void makeUnitGroups(){
		Vector<Character> alive = this.getMyAliveUnits();

		//Split in 5 groups
		for(Character c : alive){
			Vector<Character> toAdd = new Vector<Character>();
			toAdd.add(c);
			if(c instanceof UnitSpearman){

				this.addInUnitGroup(toAdd,SPEARMAN);
			}
			else if(c instanceof UnitCrossbowman){
				this.addInUnitGroup(toAdd,CROSSBOWMAN);
			}
			else if(c instanceof UnitKnight){
				this.addInUnitGroup(toAdd,KNIGHT);
			}
			else if(c instanceof UnitInquisitor){
				this.addInUnitGroup(toAdd,INQUISITOR);
			}
			else if(c instanceof UnitPriest){
				this.addInUnitGroup(toAdd,PRIEST);
			}
			else if(c instanceof UnitArchange){
				this.addInUnitGroup(toAdd,ARCHANGE);
			}
		}
	}

}
