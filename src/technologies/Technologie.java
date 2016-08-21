package technologies;

import utils.ObjetsList;
import data.Attributs;

import model.Game;
import model.GameTeam;

public abstract class Technologie implements java.io.Serializable{
	/**
	 * 
	 */

	public int team;
	public ObjetsList objet;

	public abstract void applyEffect();
	public GameTeam getGameTeam(){
		return Game.g.teams.get(this.team);
	}
	
	
	public Technologie(ObjetsList o, int gameteam){
		this.objet = o;
		this.team = gameteam;		
	}
	public String getName(){
		return this.objet.name();
	}
	
	public String getIcon(){
		return this.getGameTeam().data.getAttributString(objet.name, Attributs.nameIcon);
	}

	public static Technologie technologie(ObjetsList o, int gameteam){
		switch(o){
		case DualistAge2:
			
			return 	new Technologie(o, gameteam){
			
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.getGameTeam().hq.age = 2;
				this.getGameTeam().maxPop= 20;
			}
			
		};
		case DualistAge3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.getGameTeam().hq.age = 3;
				this.getGameTeam().maxPop= 40;
			}
			
		};
			
		case DualistBonusFood: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.getGameTeam().data.prodFood=4;
			}
			
		};
		case DualistBonusGold:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.getGameTeam().data.prodGold=4;
			}
			
		};
		case DualistShield2:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name().toLowerCase(), Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name().toLowerCase(), Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name().toLowerCase(), Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest.name().toLowerCase(), Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name().toLowerCase(), Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange.name().toLowerCase(), Attributs.armor, 2f);
			
			}
			
		};
			
		case DualistHealth2:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest.name, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange.name, Attributs.maxLifepoints, 20f);
			
			}
			
		};
			
		case DualistShield3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest.name, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange.name, Attributs.armor, 2f);
			
			}
			
		};
			
		case DualistHealth3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest.name, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange.name, Attributs.maxLifepoints, 30f);
			}
			
		};
			
		case DualistContact2:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.damage, 1);
			}
			
		};
			
		case DualistRangeAttack2: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.damage, 1);
			}
			
		};
		case DualistContact3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.damage, 1);
			}
			
		};
		case DualistRangeAttack3: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.damage, 1);
			}
			
		};
		case DualistExplosion:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.setAttribut(ObjetsList.Spearman.name, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Crossbowman.name, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Knight.name, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Priest.name, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Inquisitor.name, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Archange.name, Attributs.explosionWhenImmolate, 1f);
			}
			
		};
		case DualistEagleView: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.mulAttribut(ObjetsList.Spearman.name, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Crossbowman.name, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Knight.name, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Priest.name, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Inquisitor.name, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Archange.name, Attributs.sight, 1.5f);
			}
			
		};
		default : return null;
		}
	}
	
	public boolean equals(Object o){
		return o instanceof Technologie && this.objet.equals(((Technologie) o).objet) ;
	}
	

	
	
	// CLASS


	
	
	
	
	
	
	
	
}
