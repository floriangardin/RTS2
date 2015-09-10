package model;

import java.util.Vector;

import multiplaying.OutputModel.OutputBuilding;

public abstract class BuildingProduction extends Building {


	Vector<UnitsList> productionList;
	public Vector<Integer> queue ;
	Vector<Float> productionTime;
	public float charge;
	boolean isProducing;

	public void product(int unit){

		if(this.queue.size()<5 && unit<this.productionList.size()){
			if(this.productionList.get(unit).foodPrice<=this.p.g.players.get(team).food
					&& this.productionList.get(unit).goldPrice<=this.p.g.players.get(team).gold){
				this.queue.add(unit);
				this.p.g.players.get(team).gold-=this.productionList.get(unit).goldPrice;
				this.p.g.players.get(team).food-=this.productionList.get(unit).foodPrice;
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
			this.animation+=2f;
			if(animation>120f)
				animation = 0f;
			this.charge+=0.1f;
			if(this.charge>=this.productionTime.get(this.queue.get(0))){
				this.charge=0f;
				this.p.g.players.get(team).create(this.productionList.get(this.queue.get(0)), x+(float)Math.random(), y+this.sizeY/2 );
				this.queue.remove(0);
				if(this.queue.size()==0){
					this.isProducing =false;
					this.animation = -1f;
				}
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			this.animation = -1f;
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
			this.p.g.players.get(this.team).food += this.productionList.get(queue.get(0)).foodPrice;
			this.p.g.players.get(this.team).gold += this.productionList.get(queue.get(0)).goldPrice;
			this.queue.remove(0);
			this.charge = 0f;
		}

	}



}
