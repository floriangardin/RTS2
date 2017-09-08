package mybot;

import bot.IA;
import bot.IAUnit;
import data.Attributs;
import utils.ObjetsList;

public strictfp class ProductionCommander extends Commander {

	public ProductionCommander(IA ia) {
		super(ia);
		// TODO Auto-generated constructor stub
	}

	public long getCountOf(ObjetsList o){
		return ia.getUnits()
				.filter(x-> x.getGameTeam()==ia.getTeamId())
				.filter(x-> x.getName()==o)
				.count();
	}
	public IAUnit getBuildingProducer(ObjetsList o){
		return ia.getUnits()
				.filter(x-> x.getGameTeam()==ia.getTeamId())
				.filter(x-> x.getProductionList().contains(o))
				.findAny()
				.orElse(null);
	}
	@Override
	public void play() {
		int food = ia.getFood();
		int pop = ia.getPop();
		int maxPop = ia.getMaxPop();

		if(maxPop<= pop){
			// Produce tech
			
		}else{// Produce units			
			long spearman = getCountOf(ObjetsList.Spearman);
			long crossbowman = getCountOf(ObjetsList.Crossbowman);
			long priest = getCountOf(ObjetsList.Priest);
			long knight = getCountOf(ObjetsList.Knight);
			long inquisitor = getCountOf(ObjetsList.Inquisitor);
			long stable = getCountOf(ObjetsList.Stable);
			if(stable==0){
				if(spearman > crossbowman){
					IAUnit producer = getBuildingProducer(ObjetsList.Crossbowman);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Crossbowman, Attributs.foodCost) ){
						ia.select(producer);
						ia.produce(ObjetsList.Crossbowman);						
					}
				}else if(crossbowman > inquisitor){
					IAUnit producer = getBuildingProducer(ObjetsList.Inquisitor);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Inquisitor, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Inquisitor));
						ia.produce(ObjetsList.Inquisitor);						
					}
				}else{
					IAUnit producer = getBuildingProducer(ObjetsList.Spearman);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Spearman, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Spearman));
						ia.produce(ObjetsList.Spearman);						
					}
				}
			}else{
				if(spearman > crossbowman){
					IAUnit producer = getBuildingProducer(ObjetsList.Crossbowman);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Crossbowman, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Crossbowman));
						ia.produce(ObjetsList.Crossbowman);						
					}
				}else if(crossbowman > inquisitor){
					IAUnit producer = getBuildingProducer(ObjetsList.Inquisitor);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Inquisitor, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Inquisitor));
						ia.produce(ObjetsList.Inquisitor);						
					}
				}
				else if(inquisitor > knight){
					IAUnit producer = getBuildingProducer(ObjetsList.Knight);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Knight, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Knight));
						ia.produce(ObjetsList.Knight);						
					}
				}
				else if(knight > priest){
					IAUnit producer = getBuildingProducer(ObjetsList.Priest);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Priest, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Priest));
						ia.produce(ObjetsList.Priest);						
					}

				}else{
					IAUnit producer = getBuildingProducer(ObjetsList.Spearman);
					if(producer!=null && food >= ia.getAttribut(ObjetsList.Spearman, Attributs.foodCost) ){
						ia.select(getBuildingProducer(ObjetsList.Spearman));
						ia.produce(ObjetsList.Spearman);						
					}
			}
		}
	}

}

}
