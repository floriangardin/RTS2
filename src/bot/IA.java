package bot;


import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bot.IAUnit.Role;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import plateau.Building;
import plateau.Character;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;
public abstract class IA{

	public final static Vector<IA> ias = new Vector<IA>();
	public static boolean isInit = false;
	private Vector<IAUnit> units;
	private List<IAUnit> selection;
	Plateau plateau;
	private int teamId;
	private Team player;
	
	InputObject im;
	public static Vector<InputObject> play(Plateau plateau, int roundToPlay){
		Vector<InputObject> ims = new Vector<InputObject>();
		for(IA ia : ias){
			ims.add(ia.action(plateau, roundToPlay));
		}
		return ims;
	}
	public int getTeamId(){
		return player.id;
	}
	
	public static void init(Vector<IA> toAdd){
		for(IA ia : toAdd){
			ias.add(ia);
		}
		isInit = true;
	}
	public static void addIA(IA toAdd){
		ias.add(toAdd);
		isInit = true;
	}
	public static void removeIA(int id){
		ias.remove(id);
	}
	
	public IA(int teamid)  {
		this.teamId= teamid;
		units = new Vector<IAUnit>();
		
		// Create ennemy static IA
		// Contain all the objects visible by the IA
	}

	public Stream<IAUnit> getUnits(){
		return units.stream();
	}
	
	public Vector<IAUnit> getMyUnits(ObjetsList filter){
		/*
		 * Return objects of my team
		 */
		Vector<IAUnit> res = new Vector<IAUnit>();
		for(IAUnit unit: units ){
			if(unit.getName() == filter){
				res.add(unit);
			}
		}
		return res;
	}
	public Vector<IAUnit> getMyFreeUnits(ObjetsList filter){
		/*
		 * Return objects of my team
		 */
		Vector<IAUnit> res = new Vector<IAUnit>();
		for(IAUnit unit: units ){
			if(unit.getName() == filter && !unit.hasTarget()){
				res.add(unit);
			}
		}
		return res;
	}
	
	
	
	public void select(List<IAUnit> units){
		this.selection = units.stream()
		.filter(x-> x.getGameTeam()==this.getTeamId())
		.collect(Collectors.toList());
		
		this.selectUnits(this.selection);
		
	}
	public void select(IAUnit unit){
		Vector<IAUnit> units = new Vector<IAUnit>();
		if(unit.getGameTeam()==this.getTeamId()){
			units.add(unit);
		}
		this.selection = units;
		this.selectUnits(this.selection);
		
	}
	public void selectByIds(List<Integer> units){
		this.select(units.stream()
				.map(x-> getById(x))
				.filter(x-> x!=null)
				.collect(Collectors.toList()));
		
	}
	/*
	 * Method always called when IA is awaken
	 */
	public InputObject initForRound(Plateau plateau, int roundToPlay){
		if(player==null){
			for(Team team : plateau.teams){
				if(team.id==teamId){
					this.player = team;
				}
			}
		}
		InputObject toReturn = new InputObject(player.id, roundToPlay);
		this.plateau = plateau;
		Map<Integer, Role> roles = getUnits().collect(Collectors.toMap(IAUnit::getId, IAUnit::getRole));
		this.units.clear();
		
		if(plateau.round<5){
			return toReturn;
		}
		//Update IA Objects
		for(Character c : plateau.characters){
			if(plateau.isVisibleByTeam(this.player.id, c)){
				IAUnit newUnit = new IAUnit(c,this, plateau);
				if(roles.containsKey(newUnit.getId())){
					newUnit.setRole(roles.get(newUnit.getId()));
				}
				this.units.addElement(newUnit);
			}
		}
		for(Building b : plateau.buildings){			
			this.units.addElement(new IAUnit(b,this, plateau));
		}
		return toReturn;
	}

	public InputObject action(Plateau plateau, int roundToPlay){
		im = initForRound(plateau, roundToPlay); // Input object to return at the end of turn
		// Call abstract method to overrides
		try {
			this.update();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//alert("Error in IA ... "+e.toString());
			e.printStackTrace();
		}
		return im;
	}
	
	public float getAttributs(ObjetsList o, Attributs a){
		return this.player.data.getAttribut(o, a);
	}
	public void selectUnits(List<IAUnit> units){
		im.selection = new Vector<Integer>();
		units.forEach(x ->  im.selection.add(x.getId()));
		
	}
	public void rightClick(float x, float y){
		im.rightClick(x, y);
	}
	public void rightClick(IAUnit unit){
		
		im.rightClick(unit.getX(), unit.getY());
	}
	
	public int getRound(){
		return plateau.round;
	}
	public void stopMove(){
		im.pressed.add(KeyEnum.StopperMouvement);
	}
	public  void attack(float x , float y){
		im.attack(x, y);
	}; // attack move
	
	public  void produceUnit(ObjetsList production){
		// Check price of unit first
		if(this.selection.get(0).objet instanceof Building){
			if(this.selection.get(0).getProductionList().contains(production)){
				int index = this.selection.get(0).getProductionList().indexOf(production);
				switch(index){
				case 0:
					im.pressed.add(KeyEnum.Prod0);
					break;
				case 1:
					im.pressed.add(KeyEnum.Prod1);
					break;
				case 2:
					im.pressed.add(KeyEnum.Prod2);
					break;
				case 3:
					im.pressed.add(KeyEnum.Prod3);
					break;
				default:
					break;
				}
			}
		}else{
			//Communications.sendMessage(new ChatMessage("1|Warning : Tried to produce not in a building ..."));
		}
	}; // Produce unit
	public  void produceTechnology(ObjetsList tech){
		if(this.selection.get(0).objet instanceof Building){
			int index = this.selection.get(0).getResearchList().indexOf(tech);
			switch(index){
			case 0:
				im.pressed.add(KeyEnum.Tech0);
				break;
			case 1:
				im.pressed.add(KeyEnum.Tech1);
				break;
			case 2:
				im.pressed.add(KeyEnum.Tech2);
				break;
			case 3:
				im.pressed.add(KeyEnum.Tech3);
				break;
			default:
				break;
			}
		}else{
			//Communications.sendMessage(new ChatMessage("1|Warning : Tried to research not in a building ..."));
		}
	}; // Produce research
	
	public void produce(ObjetsList o){
		produceUnit(o);
		produceTechnology(o);
	}
	
	public void launchSpell(float x , float y,ObjetsList spell){
		
		if(this.selection.get(0).objet instanceof Character ){
			Character c = (Character) this.selection.get(0).objet;
			if(c.getSpellsName().contains(spell)){
				int index = this.selection.get(0).getSpells().indexOf(spell);
				im.x = x;
				im.y = y;
				switch(index){
				case 0:
					im.pressed.add(KeyEnum.Prod0);
					break;
				case 1:
					im.pressed.add(KeyEnum.Prod1);
					break;
				case 2:
					im.pressed.add(KeyEnum.Prod2);
					break;
				case 3:
					im.pressed.add(KeyEnum.Prod3);
					break;
				default:
					break;
				}	
			}	
		}
	}
	
	public void launchSpell(IAUnit target,ObjetsList spell){
		launchSpell(target.getX(), target.getY(), spell);
	}
	
	public abstract void update() throws Exception;
	
	/*Find an unit on a mouse click
	 * 
	 */
	public IAUnit findUnit(float x,  float y){
		Vector<IAUnit> units = new Vector<IAUnit>();
		units.addAll(this.units);
		for(IAUnit u : units){
			if(u.clickIn(x, y)){
				return u;
			}
		}
		return null;
	}


	public int getFood(){
		return player.food;
	}
	
	public int getPop(){
		return player.getPop(plateau);
	}
	public int getMaxPop(){
		return player.getMaxPop(plateau);
	}
	
	public boolean canProduce(ObjetsList o){
		
		return this.getFood()>this.getAttribut(o, Attributs.foodCost)
				&& (this.getMaxPop()-this.getPop())>=1; //FIXME : Not generic 
	}
	
	public float getAttribut(ObjetsList o , Attributs a){
		return player.data.getAttribut(o, a);
	}
	
	public String getAttributString(ObjetsList o , Attributs a){
		return player.data.getAttributString(o, a);
	}
	public Vector<String> getAttributList(ObjetsList o , Attributs a){
		return player.data.getAttributList(o, a);
	}
	public IAUnit getById(int id) {
		// TODO Auto-generated method stub
		return getUnits()
				.filter(x-> x.getId()==id)
				.findFirst()
				.orElse(null);
	}
	
	
//	
//	
//	
//	// High levels functions For IA Strategy
//	
//	
//	public Vector<ObjetsList> getTechsRequired(ObjetsList o){
//		return player.data.getAttributListAtt(o, Attributs.techsRequired);
//	}
//	
//	/*
//	 * Liste et/ou
//	 */
//	public Vector<Vector<ObjetsList>> getRequirements(ObjetsList o ){
//		Vector<Vector<ObjetsList>> res = new Vector<Vector<ObjetsList>>();
//		res.add(getProducers(o));
//		for(ObjetsList ol : getTechsRequired(o)){
//			Vector<ObjetsList> tech = new Vector<ObjetsList>();
//			tech.add(ol);
//			res.add(tech);
//		}
//		return res;
//	}
//
//	
//	public Vector<Vector<ObjetsList>> getUnsatisfiedRequirements(ObjetsList o){
//		
//		Vector<Vector<ObjetsList>> req = getRequirements(o);
//		Vector<Vector<ObjetsList>> res = new Vector<Vector<ObjetsList>>();
//		for(Vector<ObjetsList> v : req){
//			boolean toAdd = true;
//			for(ObjetsList h : v){
//				if(has(h)){
//					toAdd = false;
//					break;
//				}
//			}
//			if(toAdd){				
//				res.add(v);
//			}
//		}
//		return res;
//	}
//	
//	public Vector<ObjetsList> getTechsDiscovered(){
//		return ((Building) plateau.getById(player.hq)).techsDiscovered;
//	}
//	private Team getPlayer(){
//		return player;
//	}
//
//	
//	public Vector<ObjetsList> getProducers(ObjetsList o){
//		Vector<ObjetsList> res = new Vector<ObjetsList>();
//		for(ObjetsList ol : ObjetsList.values()){
//			if(getPlayer().data.getAttributListAtt(ol, Attributs.units).contains(o)){
//				res.add(ol);
//			}
//			if(getPlayer().data.getAttributListAtt(ol, Attributs.technologies).contains(o)){
//				res.add(ol);
//			}
//		}
//		return res;
//	}
//	
//	public Vector<IAUnit> getMyProducers(ObjetsList o){
//		Vector<ObjetsList> res = getProducers(o);
//		Vector<IAUnit> toReturn = new Vector<IAUnit>();
//		for(IAUnit u : getMyUnits()){
//			for(ObjetsList ol : res){
//				if(u.getName()==ol ){
//					toReturn.add(u);
//				}
//			}
//		}
//		
//		return toReturn;
//	}
//	
//	
//	
//	/*
//	 * Look if we have the object or the tech
//	 */
//	public boolean has(ObjetsList o){
//		for(IAUnit u : getMyUnits()){
//			if(u.getName()==o){
//				return true;
//			}
//		}
//		
//		//Now check if we have a technology of this name
//		if(getTechsDiscovered().contains(o)){
//			return true;
//		}
//		return false;
//	}
//	
//	
//	/*
//	 * Check if we have at least one of the Object at our command
//	 */
//	public boolean hasOneOf(Vector<ObjetsList> o){
//		for(IAUnit u : getMyUnits()){
//			for(ObjetsList ol : o){
//				if(u.getName()==ol){
//					return true;
//				}
//			}
//
//		}
//		
//		//Now check if we have a technology of this name
//		if(getTechsDiscovered().contains(o)){
//			return true;
//		}
//		return false;
//	}
//	
//	/*
//	 * Look if we have the requirements for producing the object or the tech
//	 */
//	public boolean hasRequirements(ObjetsList o){
//		for(Vector<ObjetsList> ol : getRequirements(o)){
//			if(!hasOneOf(ol)){
//				return false;
//			}
//		}
//		return true;
//	}
	

	
}
