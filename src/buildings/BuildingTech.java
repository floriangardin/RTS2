package buildings;

import java.util.HashMap;
import java.util.Vector;

import display.DisplayRessources;
import model.Game;
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
			this.hq.allTechs.addElement(this.queue);
			this.queue=null;
			this.setCharge(0f);
			this.hq.updateAllProductionList();
		}
	}

	public boolean product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).tech.goldPrice<=this.getGameTeam().gold){
				this.queue=this.productionList.get(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).tech.goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).tech.foodPrice;
				if(this.gameteam==Game.g.currentPlayer.getGameTeam()){
					this.g.addDisplayRessources(new DisplayRessources(-this.productionList.get(unit).tech.goldPrice,"gold",this.x,this.y));
					this.g.addDisplayRessources(new DisplayRessources(-this.productionList.get(unit).tech.foodPrice,"food",this.x,this.y));
				}
				this.hq.allTechs.remove(this.productionList.get(unit));
				this.hq.updateAllProductionList();
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
		if(this.queue!=null)
			s+="qE:"+this.queue.id+";";
		//TODO : il faut aussi parser les tech
		if(this.lastTechDiscovered!=null){
			s+="lTD:"+this.lastTechDiscovered.id+";";
		}
		return s;
	}
	
	public void parseBuildingTech(HashMap<String, String> hs) {
		if(hs.containsKey("qE") ){
			Technologie t = this.getTechnologieById(Integer.parseInt(hs.get("qE")));
			if(this.queue!=null && t!=null){
				if(!this.queue.name.equals(t.name)){
					this.removeProd();
					this.queue = t;
				}
			}
		}
		else{
			if(this.queue!=null){
				this.removeProd();
			}
			
		}
		if(hs.containsKey("lTD") && this.hq.allTechs.contains(this.getTechnologieById(Integer.parseInt(hs.get("lTD"))))){
			Technologie t = this.getTechnologieById(Integer.parseInt(hs.get("lTD")));
			if(this.queue!=null){
				if(!this.queue.name.equals(t.name)){
					this.removeProd();
				}
			}
			this.techTerminate(t);
		}
	}
	
	public void parse(HashMap<String,String> hs){
		this.parseBuilding(hs);
		this.parseBuildingTech(hs);
	}
	
	

	public void setTeamExtra(){
		if(this instanceof BuildingHeadquarters){
			this.getGameTeam().hq = hq;
			this.hq =(BuildingHeadquarters) this;
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
