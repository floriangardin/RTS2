package buildings;

import java.util.HashMap;
import java.util.Vector;

import display.Message;
import technologies.Technologie;

public abstract class BuildingTech extends BuildingAction {
	public Vector<Technologie> productionList;
	public Technologie queue;
	public Technologie lastTechDiscovered;
	
	
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
			this.getGameTeam().food += queue.tech.foodPrice;
			this.getGameTeam().gold += queue.tech.goldPrice;
			this.queue=null;
			this.setCharge(0f);
		}
	}
	
	public void product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).tech.goldPrice<=this.getGameTeam().gold){
				this.queue=this.productionList.get(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).tech.goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).tech.foodPrice;
			} else {
				if(this.productionList.get(unit).tech.foodPrice>this.getGameTeam().food)
					this.p.addMessage(Message.getById(0), getTeam());
				else
					this.p.addMessage(Message.getById(1), getTeam());
			}
		}
	}
	
	public void techTerminate(Technologie q){
		if(q==null){
			return;
		}
		this.p.addMessage(Message.getById(4), getTeam());
		this.setCharge(0f);
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
		
		this.updateProductionList();
		this.changes.isFinished = true;
		this.lastTechDiscovered = q;
	}
	
	public int getIndexOfQueue(){
		if(productionList.contains(queue))
			return productionList.indexOf(queue);
		return -1;
	}
	
	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringBuilding();
		if(changes.queue && this.queue!=null){
			s+="queue:"+this.queue.id+";";
			changes.queue=true;
		}
		if(changes.charge){
			s+="charge:"+this.charge+";";
			changes.charge=true;
		}
		if(this.changes.isFinished && this.lastTechDiscovered!=null){
			s+="isFinished:"+"1"+";";
			s+="lastTechDiscovered:"+this.lastTechDiscovered.id+";";
			this.changes.isFinished = true;
		}
		return s;
	}
	
	public void parseBuildingTech(HashMap<String, String> hs) {
		if(hs.containsKey("queue")){
			this.queue = this.getTechnologieById(Integer.parseInt(hs.get("queue")));
		}
		else{
			this.queue = null;
		}
		if(hs.containsKey("charge")){
			this.setCharge(Float.parseFloat(hs.get("charge")));
		}
		if(hs.containsKey("isFinished") && hs.get("isFinished").equals("1") ){
			this.techTerminate(this.getTechnologieById(Integer.parseInt(hs.get("lastTechDiscovered"))));
		}
	}
	public void parse(HashMap<String,String> hs){
		this.parseObjet(hs);
		this.parseActionObjet(hs);
		this.parseBuilding(hs);
		this.parseBuildingTech(hs);
	}
}
