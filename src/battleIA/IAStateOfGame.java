package battleIA;

import java.util.Vector;

import buildings.Building;
import buildings.BuildingsList;
import model.Plateau;
import units.Character;
import units.UnitsList;

public class IAStateOfGame {
	
	public Vector<UnitIA> units1;
	public Vector<UnitIA> units2;
	public Vector<BuildingIA> buildings;
	
	public float sizeX;
	public float sizeY;

	public IAStateOfGame(Plateau p){
		this.sizeX = p.maxX;
		this.sizeY = p.maxY;
		this.units1 = new Vector<UnitIA>();
		this.units2 = new Vector<UnitIA>();
		this.buildings = new Vector<BuildingIA>();
		for(Character c : p.characters){
			switch(c.getTeam()){
			case 1 : units1.add(new UnitIA(c));break;
			case 2 : units2.add(new UnitIA(c)); break;
			default:
			}
		}
		for(Building b : p.buildings){
			buildings.add(new BuildingIA(b));
		}
	}
	
	
	public class UnitIA{
		public int id;
		public float x;
		public float y;
		public UnitsList type;
		public float maxLifePoints;
		public float lifePoints;
		public float velocity;
		public float attackState;
		public int idTarget;
		
		public UnitIA(Character c){
			this.id = c.id;
			this.x = c.getX();
			this.y = c.getY();
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
	
	public class BuildingIA{
		public int id;
		public BuildingsList type;
		public float lifepoints;
		public float maxLifePoints;
		public int team;
		
		public BuildingIA(Building b){
			this.id = b.id;
			this.type = BuildingsList.switchName(b.name);
			this.lifepoints = b.lifePoints;
			this.maxLifePoints = b.maxLifePoints;
			this.team = b.getTeam();
		}
	}
	
}
