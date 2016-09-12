package madness;


public enum ActRule implements java.io.Serializable{
	
	no_attack_building("Impossible d'attaquer les bâtiments ennemis"),
	no_building_production("Impossible de produire dans les bâtiments"),
	no_tower_attack("Les tours n'attaquent pas"),
	no_defense_building("Impossible de défendre ses bâtiments"),
	sudden_death("Gilles arrive !");
	
	public String description;
	
	ActRule(String string){
		this.description = string;
	}
	

}
