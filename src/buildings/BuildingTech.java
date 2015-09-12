package buildings;

import java.util.Vector;

import technologies.Technologie;

public abstract class BuildingTech extends Building {
	public Vector<Technologie> productionList;
	public Technologie queue;
	
	
	
	public void updateProductionList(){
		if(this.hq==null){
			return;
		}
		this.productionList.clear();
		for(Technologie t:this.hq.allTechs){
			boolean ok = true;
			if(t.techRequired!=null){
				int idr = t.techRequired.id;
				ok = false;
				for(Technologie te : this.hq.techsDiscovered)
					if(idr==te.id)
						ok = true;
			}
			if(this.hq.age>=t.tech.age && ok && t.tech.building == this.name ){
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
	
	
	public void techTerminate(Technologie q){
		this.charge=0f;
		this.hq.techsDiscovered.addElement(q);
		this.productionList.removeElement(q);
		Technologie toDelete = null;
		for(Technologie t:this.hq.allTechs)
			if(t.id==q.id)
				toDelete = t;
		if(toDelete != null)
			this.hq.allTechs.removeElement(toDelete);
		q.applyEffect();
		this.queue=null;
		this.isProducing =false;
		this.animation = -1f;
		this.updateProductionList();
	}
	
	public int getIndexOfQueue(){
		if(productionList.contains(queue))
			return productionList.indexOf(queue);
		return -1;
	}
}
