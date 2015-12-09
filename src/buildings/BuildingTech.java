package buildings;

import java.util.HashMap;
import java.util.Vector;

import multiplaying.ChatMessage;
import technologies.Technologie;

public abstract class BuildingTech extends BuildingAction {
	public Vector<Technologie> productionList;
	public Technologie queue;
	public Technologie lastTechDiscovered;


	public void updateProductionList(){
		if(this.hq==null){
			return;
		}
		this.productionList= new Vector<Technologie>();
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
	public void setCharge(float charge){
		if(charge>=this.queue.tech.prodTime){
			return;
		}
		this.charge = charge;
	}
	public void removeProd() {
		if(this.queue!=null){
			this.getGameTeam().food += queue.tech.foodPrice;
			this.getGameTeam().gold += queue.tech.goldPrice;
			this.queue=null;
			this.setCharge(0f);
		}
	}

	public boolean product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).tech.goldPrice<=this.getGameTeam().gold){
				this.queue=this.productionList.get(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).tech.goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).tech.foodPrice;
				return true;
			} else {
				// Messages
				if(this.getTeam()==this.g.currentPlayer.getTeam()){
					if(this.productionList.get(unit).tech.foodPrice>this.getGameTeam().food){
						this.g.sendMessage(ChatMessage.getById("food",g));
					} else if(this.productionList.get(unit).tech.goldPrice>this.getGameTeam().gold){
						this.g.sendMessage(ChatMessage.getById("gold",this.g));
					} else {
						this.g.sendMessage(ChatMessage.getById("pop",g));
					}
				}
			}
		}
		return false;
	}

	public void techTerminate(Technologie q){
		if(q==null){
			return;
		}
		// Message research complete
		
		if(this.getTeam()==this.g.currentPlayer.getTeam()){
			this.g.sendMessage(ChatMessage.getById("research",g));
		}
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
		
		s+="queue:"+this.queue.id+";";
		s+="charge:"+this.charge+";";

		if(this.changes.isFinished && this.lastTechDiscovered!=null){
			s+="isFinished:"+"1"+";";
			s+="lastTechDiscovered:"+this.lastTechDiscovered.id+";";
			this.changes.isFinished = true;
		}
		return s;
	}

	public void setTeamExtra(){
		if(this instanceof BuildingHeadQuarters){
			this.getGameTeam().hq = hq;
			this.hq =(BuildingHeadQuarters) this;
		}
		if(this.queue!=null){
			this.queue=null;
		}
		this.setCharge(0f);
		updateProductionList();
		if(this instanceof BuildingMine){
			((BuildingMine) this).bonusProd = 0;
		}
		if(this instanceof BuildingMill){
			((BuildingMill) this).bonusProd = 0;
		}
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
