package buildings;
import java.util.Vector;

import display.Message;
import multiplaying.OutputModel.OutputBuilding;
import units.Character;
import units.UnitsList;

public abstract class BuildingProduction extends BuildingAction {


	public Vector<UnitsList> productionList;
	public Vector<Integer> queue ;
	public Vector<Float> productionTime;

	public void product(int unit){

		if(this.queue.size()<5 && unit<this.productionList.size()){
			if(this.productionList.get(unit).foodPrice<=this.p.g.players.get(team).food
					&& this.productionList.get(unit).goldPrice<=this.p.g.players.get(team).gold){
				this.queue.add(unit);
				this.p.g.players.get(team).gold-=this.productionList.get(unit).goldPrice;
				this.p.g.players.get(team).food-=this.productionList.get(unit).foodPrice;
			}else {
				if(this.productionList.get(unit).foodPrice>this.p.g.players.get(team).food)
					this.p.addMessage(Message.getById(0), team);
				else
					this.p.addMessage(Message.getById(1), team);
			}
		}
	}

	public void action(){
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queue.size()>0){
			if(!this.isProducing){
				this.isProducing = true;
			}

			
			this.charge+=0.1f;
			if(this.charge>=this.productionTime.get(this.queue.get(0))){
				this.charge=0f;
				float dirX = this.rallyPoint.x-this.x;
				float dirY = this.rallyPoint.y - this.y;
				float norm = (float) Math.sqrt(dirX*dirX+dirY*dirY);
				float startX = (float)Math.random()+this.x + this.sizeX*dirX/norm/2;
				float startY =(float) Math.random()+ this.y + this.sizeY*dirY/norm/2;
				Character c = this.p.g.players.get(team).create(this.productionList.get(this.queue.get(0)), startX,startY );
				c.setTarget(this.rallyPoint);

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

			this.team = this.teamCapturing;
			this.updateImage();

			this.lifePoints=this.maxLifePoints;

		}
	}

	public void changeQueue(OutputBuilding ocb) {
		this.charge = ocb.charge;
		this.queue.clear();
		for(int i=0; i<5; i++)
			if(ocb.queue[i]!=-1)
				this.queue.add(ocb.queue[i]);
	}

	public void removeProd() {
		if(this.queue.size()>0){
			this.p.g.players.get(this.team).food += this.productionList.get(queue.get(this.queue.size()-1)).foodPrice;
			this.p.g.players.get(this.team).gold += this.productionList.get(queue.get(this.queue.size()-1)).goldPrice;
			this.queue.remove(this.queue.size()-1);
			if(this.queue.size()==0){
				this.charge=0f;
			}
		}

	}
	
	public String toString(){
		String s = toString1()+toString2()+toString3();
		if(changes.queue){
			s+="queue:";
			for(int c : this.queue){
				s+=c+",";
			}
			s=s.substring(0, s.length()-1);
			s+=";";
			changes.queue=false;
		}
		if(changes.productionTime){
			s+="productionTime:";
			for(float c : this.productionTime){
				s+=c+",";
			}
			s=s.substring(0, s.length()-1);
			s+=";";
			changes.productionTime=false;
		}
		return s;
	}
}
