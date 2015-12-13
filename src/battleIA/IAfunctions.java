package battleIA;

import buildings.Building;
import buildings.BuildingHeadQuarters;
import model.Checkpoint;
import model.Plateau;
import units.Character;

public final class IAfunctions {
	
	public int currentTeam;
	public Plateau p;
	
	public IAfunctions(Plateau p, int currentTeam){
		this.p = p;
		this.currentTeam = currentTeam;
	}

	public void setAttackOrder(int idAlliedPlayer, int idTarget) throws IAException{
		/**
		 * function that set the order to the character with id 'idAlliedPlayer'
		 * to attack the character with id 'idTarget'
		 * 
		 * The character will pursue and attack its target as long as 
		 * it remains in sight and alive
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player 
		 * or idTarget not an ennemy
		 */
		Character ally = null, enemy = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
			if(c.id == idTarget){
				if(c.getTeam() == currentTeam){
					throw new IAException(currentTeam, "Vous ne pouvez pas cibler une de vos unités pour l'attaque");
				} else {
					enemy = c;
				}
			}
		}
		if(ally==null || enemy==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(enemy);
			// TODO : set mode ? 
//			ally.mode = 0;
		}
	}
	
	public void setHoldPositionOrder(int idAlliedPlayer) throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to hold Position and attack any enemy unit in range
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(null);
			ally.stop();
			ally.mode = Character.HOLD_POSITION;
		}
	}

	public void setMoveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to move to the selected location. The character won't attack 
		 * until it arrives to the location
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied 
		 * player or location out of the map
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else {
			ally.setTarget(null);
			ally.stop();
			ally.mode = Character.HOLD_POSITION;
		}
	}

	public void setAggressiveOrder(int idAlliedPlayer, float xToGo, float yToGo) throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to move in an aggressive fashion to the selected location. 
		 * The character will attack every enemy unit in sight until 
		 * it arrives to the location
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied 
		 * player or location out of the map
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(xToGo<0 || xToGo>=p.maxX || yToGo<0 || yToGo>p.maxY){
			throw new IAException(currentTeam, "Vous ne pouvez pas donner l'ordre d'aller en dehors du plateau");
		} else {
			ally.setTarget(new Checkpoint(p, xToGo,yToGo));
			ally.mode = Character.AGGRESSIVE;
		}
	}

	public void setTakeBuildingOrder(int idAlliedPlayer, int idBuildingToTake) throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to take the building with id 'idBuildingToTake'
		 * 
		 * the unit won't attack any other unit till the building is taken
		 * 
		 * if the building is already yours the unit will stand and defend it, 
		 * but won't attack either
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 * if idBuildingToTake is not an existing building
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b.id == idAlliedPlayer){
				if(b.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					buildingToTake = b;
				}
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			throw new IAException(currentTeam, "Le batiment ciblé n'existe pas");
		} else {
			ally.setTarget(buildingToTake);
		}
	}

	public void setTakeEnemyHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to take the enemy headquarters
		 * 
		 * the unit won't attack any other unit till the building is taken
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()!=currentTeam){
				buildingToTake = b;
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			IAException e = new IAException(currentTeam, "Tiens Kévin a encore fait de la merde en codant... => IA function ligne 205");
			e.printStackTrace();
			throw e;
		} else {
			ally.setTarget(buildingToTake);
		}
	}
	
	public void setDefendHeadQuarterOrder(int idAlliedPlayer)  throws IAException{
		/**
		 * function that orders the character with id 'idAlliedPlayer'
		 * to defend your own headquarters
		 * 
		 * the unit won't attack any other unit until its order changes
		 * 
		 * Throw IAException if idAlliedPlayer is not an allied player
		 */
		Character ally = null;
		for(Character c : this.p.characters){
			if(c.id == idAlliedPlayer){
				if(c.getTeam() != currentTeam){
					throw new IAException(currentTeam, "Vous avez donné un ordre à une unité ennemie");
				} else {
					ally = c;
				}
			}
		}
		Building buildingToTake = null;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingHeadQuarters && b.getTeam()==currentTeam){
				buildingToTake = b;
			}
		}
		if(ally==null){
			throw new IAException(currentTeam, "L'ordre que vous avez donné ne concerne personne");
		} else if(buildingToTake==null){
			IAException e = new IAException(currentTeam, "Tiens Kévin a encore fait de la merde en codant... => IA function ligne 205");
			e.printStackTrace();
			throw e;
		} else {
			ally.setTarget(buildingToTake);
		}
	}
	
	
}
