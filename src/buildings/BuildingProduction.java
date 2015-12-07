package buildings;
import java.util.HashMap;
import java.util.Vector;

import main.Main;
import model.Bonus;
import model.IAPlayer;
import multiplaying.ChatMessage;
import units.Character;
import units.UnitsList;

public abstract class BuildingProduction extends BuildingAction {


	public Vector<UnitsList> productionList;
	public Vector<Integer> queue ;
	public float random=0f;

	public boolean product(int unit){
		this.changes.queue=true;
		if(this.queue.size()<5 && unit<this.productionList.size()){
			if(this.productionList.get(unit).foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).goldPrice<=this.getGameTeam().gold && this.getGameTeam().pop<this.getGameTeam().maxPop){
				this.queue.add(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).foodPrice;
				return true;
			}else {
				if(this.p.g.players.get(this.getTeam()) instanceof IAPlayer){
					return false;
				}
				// Messages
				if(this.getTeam()==this.g.currentPlayer.getTeam()){
					if(this.productionList.get(unit).foodPrice>this.getGameTeam().food){
						this.g.sendMessage(ChatMessage.getById("food"));
					} else if(this.productionList.get(unit).goldPrice>this.getGameTeam().gold){
						this.g.sendMessage(ChatMessage.getById("gold"));
					} else {
						this.g.sendMessage(ChatMessage.getById("pop"));
					}
				}
			}
		}
		return false;
	}

	public void setTeamExtra(){

		if(this.queue!=null){
			this.queue.clear();
		}

		this.setCharge(0f);
	}

	public void action(){
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}

		//Do the action of Barrack
		//Product, increase state of the queue
		this.random+=0.01f;
		if(this.random>1f){
			this.random=0;
		}
		if(this.queue.size()>0){
			if(!this.isProducing){
				this.isProducing = true;
			}


			this.setCharge(this.charge+Main.increment);
			if((this.getGameTeam().pop+1)<=this.getGameTeam().maxPop && this.charge>=this.productionList.get(this.queue.get(0)).time){
				this.setCharge(0f);
				float dirX = this.random+this.rallyPoint.x-this.x;
				float dirY = this.random+this.rallyPoint.y - this.y;
				float norm = (float) Math.sqrt(dirX*dirX+dirY*dirY);
				//Introduit du random
				float startX = this.x + this.sizeX*dirX/norm/2;
				float startY = this.y + this.sizeY*dirY/norm/2;
				Character c = this.getGameTeam().data.create(this.productionList.get(this.queue.get(0)), startX,startY );
				c.setTarget(this.rallyPoint);
				this.changes.queue=true;
				this.queue.remove(0);
				if(this.queue.size()==0){
					this.isProducing =false;

				}
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
		}
		// if reach production reset and create first unit in the queue
		if(this.lifePoints<10f){
			this.setTeam(this.teamCapturing);
			this.updateImage();
			this.lifePoints=this.maxLifePoints;
		}
	}

	public void removeProd() {
		if(this.queue.size()>0){
			this.getGameTeam().food += this.productionList.get(queue.get(this.queue.size()-1)).foodPrice;
			this.getGameTeam().gold += this.productionList.get(queue.get(this.queue.size()-1)).goldPrice;
			this.queue.remove(this.queue.size()-1);
			if(this.queue.size()==0){
				this.setCharge(0f);
			}
		}

	}

	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringBuilding();
		if(changes.queue && this.queue!=null){
			s+="queue:";
			for(int c : this.queue){
				s+=""+c+",";
			}
			if(this.queue.size()>0){
				s=s.substring(0, s.length()-1);
			}
			s+=";";
			changes.queue=true;
		}
		if(changes.charge){
			s+="charge:"+this.charge+";";
			changes.charge=false;
		}
		return s;
	}

	public void parseBuildingProduction(HashMap<String, String> hs) {
		if(hs.containsKey("charge")){
			this.setCharge(Float.parseFloat(hs.get("charge")));
		}
		if(hs.containsKey("queue")){
			this.queue.clear();
			String[] r = hs.get("queue").split(",");
			if(!r[0].equals("")){
				for(int i = 0;i<r.length;i++){
					this.queue.addElement(Integer.parseInt(r[i]));
				}
			}
		}
	}

	public void parse(HashMap<String,String> hs){
		this.parseObjet(hs);
		this.parseActionObjet(hs);
		this.parseBuilding(hs);
		this.parseBuildingProduction(hs);
	}


}
