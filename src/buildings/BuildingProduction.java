package buildings;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Checkpoint;
import model.Colors;
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
						this.g.sendMessage(ChatMessage.getById("food",g));
					} else if(this.productionList.get(unit).goldPrice>this.getGameTeam().gold){
						this.g.sendMessage(ChatMessage.getById("gold",this.g));
					} else {
						this.g.sendMessage(ChatMessage.getById("pop",g));
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
	
	public void setCharge(float charge){
		if(this.queue!=null && this.queue.size()>0 && charge>(this.productionList.get(this.queue.get(0)).time)){
			this.charge = (this.productionList.get(this.queue.get(0)).time);
			return;
		}
		this.charge = charge;
	}
	
	public void action(){
		giveUpProcess();
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
				if(rallyPoint!=null){
					if(rallyPoint instanceof Checkpoint){
						c.setTarget(new Checkpoint(p,this.rallyPoint.x,this.rallyPoint.y));
					}
					else if(rallyPoint instanceof Character){
						c.setTarget(rallyPoint,null,Character.AGGRESSIVE);
					}
					else if(rallyPoint instanceof Building){
						c.setTarget(rallyPoint,null,Character.TAKE_BUILDING);
					}
					
				}
				
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
	
	public Graphics drawRallyPoint(Graphics g){
		
		g.setColor(Colors.team0);
		g.setAntiAlias(true);
		g.setLineWidth(2f);
		g.fill(new Circle(this.rallyPoint.x,this.rallyPoint.y,3f));
		g.draw(new Circle(this.rallyPoint.x,this.rallyPoint.y,10f));
		g.setAntiAlias(false);
		g.setLineWidth(1f);
		
		return g;
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
		String s = toStringBuilding();
		if( this.queue!=null){
			s+="qE:";
			for(int c : this.queue){
				s+=""+c+",";
			}
			if(this.queue.size()>0){
				s=s.substring(0, s.length()-1);
			}
			s+=";";
		}
		if(changes.charge){
			s+="chrg:"+this.charge+";";
		}
		return s;
	}

	public void parseBuildingProduction(HashMap<String, String> hs) {
		if(hs.containsKey("chrg")){
			this.setCharge(Float.parseFloat(hs.get("chrg")));
		}
		if(hs.containsKey("qE")){
			this.queue.clear();
			String[] r = hs.get("qE").split(",");
			if(!r[0].equals("")){
				for(int i = 0;i<r.length;i++){
					this.queue.addElement(Integer.parseInt(r[i]));
				}
			}
		}
	}

	public void parse(HashMap<String,String> hs){
		this.parseBuilding(hs);
		this.parseBuildingProduction(hs);
	}

}
