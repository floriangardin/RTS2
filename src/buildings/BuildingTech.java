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
		this.lastTechDiscovered = q;
	}

	public int getIndexOfQueue(){
		if(productionList.contains(queue))
			return productionList.indexOf(queue);
		return -1;
	}

	public String toString(){
		String s = toStringBuilding();
		
		s+="qE:"+this.queue.id+";";
		s+="chrg:"+this.charge+";";
		//TODO : il faut aussi parser les tech
		if(this.lastTechDiscovered!=null){
			s+="lTD:"+this.lastTechDiscovered.id+";";
		}
		return s;
	}
	
	public void parseBuildingTech(HashMap<String, String> hs) {
		if(hs.containsKey("qE")){
			this.queue = this.getTechnologieById(Integer.parseInt(hs.get("qE")));
		}
		else{
			this.queue = null;
		}
		if(hs.containsKey("chrg")){
			this.setCharge(Float.parseFloat(hs.get("chrg")));
		}
		if(hs.containsKey("lTD") && this.hq.allTechs.contains(this.getTechnologieById(Integer.parseInt(hs.get("lTD"))))){
			this.techTerminate(this.getTechnologieById(Integer.parseInt(hs.get("lTD"))));
		}
	}
	
	public void parse(HashMap<String,String> hs){
		this.parseBuilding(hs);
		this.parseBuildingTech(hs);
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


}
