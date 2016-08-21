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
		return this.getGameTeam().data.getAttributString(objet, Attributs.nameIcon);
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
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange, Attributs.armor, 2f);
			
			}
			
		};
			
		case DualistHealth2:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.maxLifepoints, 20f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange, Attributs.maxLifepoints, 20f);
			
			}
			
		};
			
		case DualistShield3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.armor, 2f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange, Attributs.armor, 2f);
			
			}
			
		};
			
		case DualistHealth3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Priest, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.maxLifepoints, 30f);
				this.getGameTeam().data.addAttribut(ObjetsList.Archange, Attributs.maxLifepoints, 30f);
			}
			
		};
			
		case DualistContactAttack2:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.damage, 1);
			}
			
		};
			
		case DualistRangeAttack2: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.damage, 1);
			}
			
		};
		case DualistContactAttack3:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.damage, 1);
			}
			
		};
		case DualistRangeAttack3: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor, Attributs.damage, 1);
				this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman, Attributs.damage, 1);
			}
			
		};
		case DualistExplosion:
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.setAttribut(ObjetsList.Spearman, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Crossbowman, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Knight, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Priest, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Inquisitor, Attributs.explosionWhenImmolate, 1f);
				this.getGameTeam().data.setAttribut(ObjetsList.Archange, Attributs.explosionWhenImmolate, 1f);
			}
			
		};
		case DualistEagleView: 
			return 	new Technologie(o, gameteam){
				
			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.getGameTeam().data.mulAttribut(ObjetsList.Spearman, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Crossbowman, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Knight, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Priest, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Inquisitor, Attributs.sight, 1.5f);
				this.getGameTeam().data.mulAttribut(ObjetsList.Archange, Attributs.sight, 1.5f);
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
