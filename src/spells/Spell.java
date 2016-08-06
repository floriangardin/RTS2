package spells;

import org.newdawn.slick.Image;

import data.Attributs;
import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;;

public abstract class Spell {

	public SpellsList name;
	public int team;



	public abstract void launch(Objet target, Character launcher);


	public float getAttribut(Attributs attribut){
		return this.getGameTeam().data.getAttribut(this.name.name().toLowerCase(),attribut);
	}

	public static Objet realTarget(Objet target, Character launcher, float range){
		if(Utils.distance(target,launcher)>range){
			float ux = target.getX() - launcher.getX();
			float uy = target.getY() - launcher.getY();
			float norm = (float) Math.sqrt(ux*ux+uy*uy);
			ux = ux*range/norm;
			uy = uy*range/norm;
			return new Checkpoint(launcher.getX()+ux,launcher.getY()+uy);
		} else {
			return target;
		}
	}

	public GameTeam getGameTeam(){
		return Game.g.teams.get(team);
	}


	public static Spell createSpell(SpellsList s, int team) {
		Spell spell = null;
		switch(s){
		case BlessedArea: spell = new SpellBlessedArea();break;
		case Conversion: spell = new SpellConversion();break;
		case Dash: spell = new SpellDash();break;
		case Eclair: spell = new SpellEclair(); break;
		case Firewall: spell = new SpellFirewall(); break;
		case Frozen: spell = new SpellFrozen(); break;
		case Heal : spell = new SpellHeal();break;
		case Immolation: spell = new SpellImmolation(); break;
		case InstantDeath: spell = new SpellInstantDeath(); break;
		case InstantHealth: spell = new SpellInstantHealth(); break;
		case SpecialArrow: spell = new SpellSpecialArrow(); break;
		case Product: spell = new SpellProduct(); break;
		default:
		}
		spell.team = team;
		return spell;
	}

}
