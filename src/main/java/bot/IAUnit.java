package bot;

import java.util.List;
import java.util.Vector;

import data.Attributs;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public strictfp class IAUnit {
	protected Objet objet;
	private IA ia;
	boolean free = true;
	protected Plateau plateau;
	protected Role role= Role.noRole;
	public enum Role{
		noRole,
		build,
		product,
		recon,
		war;
		public static List<Role> getRoles(){
			List<Role> res= new Vector<Role>();
			for(Role role : Role.values()){
				res.add(role);
			}
			return res;
		}
	}
	public IAUnit(Objet o, IA ia, Plateau plateau){
		this.objet = o;
		this.ia = ia;
		this.plateau = plateau;
		if(objet instanceof Building){
			setRole(Role.product);
		}
	}
	
	public boolean isinstance(Class<?> classe){
		return classe.isInstance(this.objet);
	}
	public Role getRole(){
		return role;
	}
	public void setRole(Role role){
		this.role = role;
	}
	public ObjetsList getName(){
		return objet.getName();
	}
	public void setFree(){
		this.free = true;
	}
	public void setBusy(){
		this.free = false;
	}
	public boolean isFree(){
		return this.free;
	}
	public Class<? extends Objet> getType(){
		return objet.getClass();
	}
	
	public float getX(){
		return objet.getX();
	}
	public float getY(){
		return objet.getY();
	}
	public int getId(){
		return objet.getId();
	}
	public float getLifepoints(){
		return objet.getLifePoints();
	}
	public int getGameTeam(){
		if(this.objet.getTeam() != null){
			return this.objet.getTeam().id;
		}
		return 0;
	}
	public boolean canLaunch(ObjetsList spell){
		if(getObjet() instanceof Character){
			Character c = (Character) getObjet();
			int index = c.getSpellsName().indexOf(spell);
			if(index>=0){
				return c.canLaunch(index);
			}
			
		}

		return false;
	}

	public Vector<ObjetsList> getQueue(){
		Vector<ObjetsList> res = new Vector<ObjetsList>();
		if(getObjet() instanceof Building){
			Building b = (Building) getObjet();
			res.addAll(b.getQueue());
		}
		
		return res;
	}
	public int roundsSinceLastAttack(){
		return this.getObjet().roundSinceLastAttack(plateau.getRound());
	}
	Objet getObjet(){
		return this.objet;
	}
	public float getAttribut(Attributs a){
		return objet.getAttribut(a);
	}
	public Vector<String> getAttributList(Attributs a){
		return objet.getAttributList(a);
	}
	public String getAttributString(Attributs a){
		return objet.getAttributString(a);
	}
	
	public boolean clickIn(float x , float y){
		return objet.getCollisionBox().contains(x, y);
	}
	
	public IA getIA(){
		return ia;
	}
	public Vector<ObjetsList> getProductionList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getProductionList(plateau);
		}
		return new Vector<ObjetsList>();
	}
	public Vector<ObjetsList> getResearchList(){
		if(objet instanceof Building){
			return ((Building) objet).getTechnologyList(plateau);
		}
		return new Vector<ObjetsList>();
	}
	
	
	public Vector<ObjetsList> getSpells(){
		if(getObjet() instanceof Character){
			return getObjet().getSpellsName();
		}
		return new Vector<ObjetsList>();
	}
	

	public IAUnit getNearest(ObjetsList o){ 
		return ia.getUnits()
				.filter(x -> x.getName()==o)
				.sorted((x,y) -> Float.compare(Utils.distance2(x,this), Utils.distance2(y,this)) )
				.findFirst()
				.orElse(null);	
	}
	
	public IAUnit getNearestEnemy(ObjetsList o){ 
		return ia.getUnits()
				.filter(x -> x.getName()==o)
				.filter(x ->x.getGameTeam()!=ia.getTeamId() && x.getGameTeam()!=0)
				.sorted((x,y) -> Float.compare(Utils.distance2(x,this), Utils.distance2(y,this)) )
				.findFirst()
				.orElse(null);	
	}

	public IAUnit getTarget(){
		return new IAUnit(this.objet.getTarget(plateau),this.ia, plateau);
	}
	public boolean hasTarget(){
		return objet.getTarget(plateau)!=null;
	}
	
	public boolean equals(Object o){
		if(o instanceof IAUnit){
			return this.getId()==((IAUnit)o).getId();
		}
		return false;
	}
	
	// All things that you can produce (tech,units,spells ...)
	public Vector<ObjetsList> getAllProductions(){
		Vector<ObjetsList> result = new Vector<ObjetsList>();
		result.addAll(getProductionList());
		result.addAll(getResearchList());
		result.addAll(getSpells());
		return result;
	}
	
	
}
