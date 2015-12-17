package battleIA;

import java.util.Vector;

import buildings.Building;
import buildings.BuildingsList;
import model.Objet;
import model.Plateau;
import units.Character;
import units.UnitsList;

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
		public UnitsList type;
		public float velocity;
		public float attackState;
		public int idTarget;
		
		public UnitIA(Character c){
			this.id = c.id;
			this.x = c.getX();
			this.y = c.getY();
			this.team = c.getTeam();
			this.type = UnitsList.switchName(c.name);
			this.maxLifePoints = c.maxLifePoints;
			this.velocity = c.maxVelocity;
			this.attackState = c.state/c.chargeTime;
			if(c.getTarget()!=null){
				this.idTarget = c.getTarget().id;
			} else {
				this.idTarget = -1;
			}
		}
	}
	
	public class BuildingIA extends ObjetIA{
		public BuildingsList type;
		
		public BuildingIA(Building b){
			this.id = b.id;
			this.type = BuildingsList.switchName(b.name);
			this.lifepoints = b.lifePoints;
			this.maxLifePoints = b.maxLifePoints;
			this.team = b.getTeam();
		}
	}
	
}
