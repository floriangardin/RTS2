package plateau;

import utils.ObjetsList;
import data.Attributs;

public abstract strictfp class Technologie implements java.io.Serializable{
	

	public Team team;
	public ObjetsList objet;

	public abstract void applyEffect();

	
	public Technologie(ObjetsList o, Team team, Plateau plateau){
		this.objet = o;
		this.team = team;		
	}
	public String getName(){
		return this.objet.name();
	}
	
	public String getIcon(){
		return team.data.getAttributString(objet, Attributs.nameIcon);
	}
	
	public static Technologie technologie(ObjetsList o, Team gameteam, Plateau plateau){
		switch(o){
		case DualistAge2:
			
			return 	new Technologie(o, gameteam, plateau){
			
			/**
				 * 
				 */
				private static final long serialVersionUID = 7849208106681217142L;

			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				plateau.getHQ(team).age = 2;
			
			}
			
		};
		case DualistAge3:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 1471827763762372496L;

			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				plateau.getHQ(team).age = 2;
				
			}
			
		};
			
		case DualistBonusFood: 
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = -4941002180544285759L;

			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.team.data.prodFood+=4;
			}
			
		};
		case DualistBonusGold:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 265349469812493092L;

			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				this.team.data.prodGold+=4;
			}
			
		};
		case DualistShield2:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 7072326746162551883L;

			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.armor, 1f);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.armor, 1f);
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.armor, 1f);
				this.team.data.addAttribut(ObjetsList.Priest, Attributs.armor, 1f);
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.armor, 1f);
			
			}
			
		};
			
		case DualistHealth2:
			return 	new Technologie(o, gameteam, plateau){	
			private static final long serialVersionUID = 1065615291990746589L;
			@Override
			public void applyEffect() {
				float bonus = 20f;
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.maxLifepoints, bonus);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.maxLifepoints, bonus);
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.maxLifepoints, bonus);
				this.team.data.addAttribut(ObjetsList.Priest, Attributs.maxLifepoints, bonus);
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.maxLifepoints, bonus);
				for(Character c : plateau.getCharacters()){
					c.setLifePoints(c.getLifePoints()+bonus, plateau);
				}
				
			}
			
		};
			
		case DualistShield3:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 3981043616830016713L;

			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.armor, 2f);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.armor, 2f);
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.armor, 2f);
				this.team.data.addAttribut(ObjetsList.Priest, Attributs.armor, 2f);
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.armor, 2f);
			}
			
		};
			
		case DualistHealth3:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = -5645611184043974443L;

			@Override
			public void applyEffect() {
				// TODO Auto-generated method stub
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.maxLifepoints, 20f);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.maxLifepoints, 20f);
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.maxLifepoints, 20f);
				this.team.data.addAttribut(ObjetsList.Priest, Attributs.maxLifepoints, 20f);
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.maxLifepoints, 20f);
			}
			
		};
			
		case DualistContactAttack2:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = -1911743227820137189L;

			@Override
			public void applyEffect() {
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.damage, 1);
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.damage, 1);
			}
			
		};
			
		case DualistRangeAttack2: 
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 4093688968126666594L;

			@Override
			public void applyEffect() {
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.damage, 1);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.damage, 1);
			}
			
		};
		case DualistContactAttack3:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = -3797423010268300801L;

			@Override
			public void applyEffect() {
				this.team.data.addAttribut(ObjetsList.Knight, Attributs.damage, 1);
				this.team.data.addAttribut(ObjetsList.Spearman, Attributs.damage, 1);
			}
			
		};
		case DualistRangeAttack3: 
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 3968281430598253836L;

			@Override
			public void applyEffect() {
				this.team.data.addAttribut(ObjetsList.Inquisitor, Attributs.damage, 1);
				this.team.data.addAttribut(ObjetsList.Crossbowman, Attributs.damage, 1);
			}
			
		};
		case DualistExplosion:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 6929598547186654867L;

			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.setAttribut(ObjetsList.Spearman, Attributs.explosionWhenImmolate, 1f);
				this.team.data.setAttribut(ObjetsList.Crossbowman, Attributs.explosionWhenImmolate, 1f);
				this.team.data.setAttribut(ObjetsList.Knight, Attributs.explosionWhenImmolate, 1f);
				this.team.data.setAttribut(ObjetsList.Priest, Attributs.explosionWhenImmolate, 1f);
				this.team.data.setAttribut(ObjetsList.Inquisitor, Attributs.explosionWhenImmolate, 1f);
			}
			
		};
		case DualistImmolationAuto:
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 6929598547186654867L;

			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.setAttribut(ObjetsList.Spearman, Attributs.autoImmolation, 1f);
				this.team.data.setAttribut(ObjetsList.Crossbowman, Attributs.autoImmolation, 1f);
				this.team.data.setAttribut(ObjetsList.Knight,Attributs.autoImmolation, 1f);
				this.team.data.setAttribut(ObjetsList.Priest, Attributs.autoImmolation, 1f);
				this.team.data.setAttribut(ObjetsList.Inquisitor,Attributs.autoImmolation, 1f);
			}
			
		};
		case DualistEagleView: 
			return 	new Technologie(o, gameteam, plateau){
				
			private static final long serialVersionUID = 7256891693162888534L;

			@Override
			public void applyEffect() {
				// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
				this.team.data.mulAttribut(ObjetsList.Spearman, Attributs.sight, 1.5f);
				this.team.data.mulAttribut(ObjetsList.Crossbowman, Attributs.sight, 1.5f);
				this.team.data.mulAttribut(ObjetsList.Knight, Attributs.sight, 1.5f);
				this.team.data.mulAttribut(ObjetsList.Priest, Attributs.sight, 1.5f);
				this.team.data.mulAttribut(ObjetsList.Inquisitor, Attributs.sight, 1.5f);
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
