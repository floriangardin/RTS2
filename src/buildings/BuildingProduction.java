package buildings;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import display.DisplayRessources;
import main.Main;
import model.Checkpoint;
import model.Colors;
import model.Game;
import model.IAPlayer;
import multiplaying.ChatMessage;
import units.Character;
import utils.UnitsList;

public abstract class BuildingProduction extends BuildingAction {


	public Vector<UnitsList> productionList;
	public Vector<Integer> queue ;
	public float random=0f;

	public boolean product(int unit){
		if(this.queue.size()<5 && unit<this.productionList.size()){
			if(this.productionList.get(unit).foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).goldPrice<=this.getGameTeam().gold && this.getGameTeam().pop<this.getGameTeam().maxPop){
				this.queue.add(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).foodPrice;
				if(this.team==Game.g.currentPlayer.getGameTeam().id){
					Game.g.addDisplayRessources(new DisplayRessources(-this.productionList.get(unit).goldPrice,"gold",this.x,this.y));
					Game.g.addDisplayRessources(new DisplayRessources(-this.productionList.get(unit).foodPrice,"food",this.x,this.y));
				}
				return true;
			}else {
				if(Game.g.players.get(this.getTeam()) instanceof IAPlayer){
					return false;
				}
				// Messages
				if(this.getTeam()==Game.g.currentPlayer.getTeam()){
					if(this.productionList.get(unit).foodPrice>this.getGameTeam().food){
						Game.g.sendMessage(ChatMessage.getById("food"));
					} else if(this.productionList.get(unit).goldPrice>this.getGameTeam().gold){
						Game.g.sendMessage(ChatMessage.getById("gold"));
					} else {
						Game.g.sendMessage(ChatMessage.getById("pop"));
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
		this.updateAttributsChange();
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
			this.setCharge(this.charge+Main.increment);
			if((this.getGameTeam().pop+1)<=this.getGameTeam().maxPop && this.charge>=this.productionList.get(this.queue.get(0)).time){
				this.setCharge(0f);
				float dirX = this.random+this.rallyPoint.x-this.x;
				float dirY = this.random+this.rallyPoint.y - this.y;
				float norm = (float) Math.sqrt(dirX*dirX+dirY*dirY);
				//Introduit du random
				float startX = this.x + this.getAttribut(Attributs.sizeX)*dirX/norm/2;
				float startY = this.y + this.getAttribut(Attributs.sizeY)*dirY/norm/2;
				Character c = new Character(startX,startY, this.productionList.get(this.queue.get(0)), this.getTeam());
				if(rallyPoint!=null && rallyPoint.lifePoints<=0){
					resetRallyPoint();
				}
				if(rallyPoint!=null){
					if(rallyPoint instanceof Checkpoint){
						c.setTarget(new Checkpoint(this.rallyPoint.x,this.rallyPoint.y));
					}
					else if(rallyPoint instanceof Character){
						c.setTarget(rallyPoint,null,Character.AGGRESSIVE);
					}
					else if(rallyPoint instanceof Building){
						c.setTarget(rallyPoint,null,Character.TAKE_BUILDING);
					}
				}
				this.queue.remove(0);
			}
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
		if( this.queue!=null && queue.size()>0){
			s+="qE:";
			for(int c : this.queue){
				s+=""+c+",";
			}
			if(this.queue.size()>0){
				s=s.substring(0, s.length()-1);
			}
			s+=";";
		}
		s+="rnD:"+random+";";
		return s;
	}

	public void parseBuildingProduction(HashMap<String, String> hs) {
		this.queue.clear();
		if(hs.containsKey("qE")){
			String[] r = hs.get("qE").split(",");
			if(!r[0].equals("")){
				for(int i = 0;i<r.length;i++){
					this.queue.addElement(Integer.parseInt(r[i]));
				}
			}
		}
		if(hs.containsKey("rnD")){
			this.random = Float.parseFloat(hs.get("rnD"));
		}
	}

	public void parse(HashMap<String,String> hs){
		this.parseBuilding(hs);
		this.parseBuildingProduction(hs);
	}

}
