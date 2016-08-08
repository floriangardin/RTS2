package battleIA;

import java.util.Vector;

import buildings.Building;
import data.Attributs;
import model.Objet;
import model.Plateau;
import units.Character;
import utils.ObjetsList;
import utils.ObjetsList;

public class IAStateOfGame {
	
	public Vector<UnitIA> units;
	public Vector<BuildingIA> buildings;
	
	public float sizeX;
	public float sizeY;

	public IAStateOfGame(Plateau p){
		this.sizeX = p.maxX;
		this.sizeY = p.maxY;
		this.units = new Vector<UnitIA>();
		this.buildings = new Vector<BuildingIA>();
		for(Character c : p.characters){
			units.add(new UnitIA(c));
		}
		for(Building b : p.buildings){
			buildings.add(new BuildingIA(b));
		}
	}
	
	
	public class ObjetIA{
		public int id;
		public float x;
		public float y;
		public int team;
		public float lifepoints;
		public float maxLifePoints;		
	}
	
	public class UnitIA extends ObjetIA{
		public ObjetsList type;
		public float velocity;
		public float attackState;
		public int idTarget;
		
		public UnitIA(Character c){
			this.id = c.id;
			this.x = c.getX();
			this.y = c.getY();
			this.team = c.getTeam();
			this.type = c.name;
			this.maxLifePoints = c.getAttribut(Attributs.maxLifepoints);
			this.velocity = c.getAttribut(Attributs.maxVelocity);
			this.attackState = c.state/c.getAttribut(Attributs.chargeTime);
			if(c.getTarget()!=null){
				this.idTarget = c.getTarget().id;
			} else {
				this.idTarget = -1;
			}
		}
	}
	
	public class BuildingIA extends ObjetIA{
		public ObjetsList type;
		public Vector<Integer> queue;
		
		public BuildingIA(Building b){
			this.id = b.id;
			this.type = b.name;
			this.lifepoints = b.lifePoints;
			this.maxLifePoints = b.getAttribut(Attributs.maxLifepoints);
			this.team = b.getTeam();
		}
	}
	
}
