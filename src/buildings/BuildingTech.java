package buildings;

import java.util.Vector;

import technologies.Technologie;

public abstract class BuildingTech extends Building {
	public Vector<Technologie> productionList;
	public Technologie queue;
	
	
	
	public void updateProductionList(){
		if(this.hq==null){
			System.out.println("c'est nul");
			return;
		}
		this.productionList.clear();
		for(Technologie t:this.hq.allTechs){
			if((t.techRequired==null || this.hq.techsDiscovered.contains(t.techRequired)) && t.tech.building == this.name ){
				this.productionList.addElement(t);
			}
		}
	}
	
	
	public void removeProd() {
		if(this.queue!=null){
			this.p.g.players.get(this.team).food += queue.tech.foodPrice;
			this.p.g.players.get(this.team).gold += queue.tech.goldPrice;
			this.queue=null;
			this.charge = 0f;
		}
	}
	
	public void product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.p.g.players.get(team).food
					&& this.productionList.get(unit).tech.goldPrice<=this.p.g.players.get(team).gold){
				this.queue=this.productionList.get(unit);
				this.p.g.players.get(team).gold-=this.productionList.get(unit).tech.goldPrice;
				this.p.g.players.get(team).food-=this.productionList.get(unit).tech.foodPrice;
			}
		}
	}
}
