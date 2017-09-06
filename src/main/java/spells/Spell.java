package spells;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import utils.ObjetsList;
import utils.Utils;;

public abstract class Spell implements java.io.Serializable{

	public ObjetsList name;
	public Team team;

	public static Color colorOk = new Color(0f,1f,0.3f,0.5f);
	public static Color colorPasOk = new Color(1f,.01f,0.1f,0.5f);

	public abstract boolean launch(Objet target, Character launcher, Plateau plateau);

	public void draw(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau){
		if(ok){
			g.setColor(Spell.colorOk);
		} else {
			g.setColor(Spell.colorPasOk);
		}
		if(this.getGameTeam().data.datas.get(this.name).attributs.containsKey(Attributs.range)){
			this.drawRange(g, launcher);
		}
		this.drawCast(g, target, x, y, launcher, ok, plateau);
	}
	public abstract void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau);

	public float getAttribut(Attributs attribut){
		return this.getGameTeam().data.getAttribut(this.name,attribut);
	}
	public String getAttributString(Attributs attribut){
		return this.getGameTeam().data.getAttributString(this.name,attribut);
	}
	public Vector<String> getAttributList(Attributs attribut){
		return this.getGameTeam().data.getAttributList(this.name,attribut);
	}

	public static Objet realTarget(Objet target, Character launcher, float range, boolean checkpoint, Plateau plateau){
		if(Utils.distance(target,launcher)>range){
			float ux = target.getX() - launcher.getX();
			float uy = target.getY() - launcher.getY();
			float norm = (float) Math.sqrt(ux*ux+uy*uy);
			ux = ux*range/norm;
			uy = uy*range/norm;
			return new Checkpoint(launcher.getX()+ux,launcher.getY()+uy, plateau);
		} else {
			if(checkpoint && !(target instanceof Checkpoint)){
				return new Checkpoint(target.x, target.y, plateau);
			} else {
				return target;
			}
		}
	}

	public Team getGameTeam(){
		return team;
	}


	public static Spell createSpell(ObjetsList s, Team team) {

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
		case Meditation: spell = new SpellMeditation(); break;
		case InstantDeath: spell = new SpellInstantDeath(); break;
		case InstantHealth: spell = new SpellInstantHealth(); break;
		case SpecialArrow: spell = new SpellSpecialArrow(); break;
		case Product: spell = new SpellProduct(); break;
		case SpellFireball: spell = new SpellFireball();break;
		case Health: spell = new SpellHealth(); break;
		default:
			System.out.println(s);
		}
		spell.team = team;
		return spell;
	}

	public void drawRange(Graphics g, Character launcher){
		g.setLineWidth(1f);
		float range = this.getAttribut(Attributs.range);
		g.drawOval(launcher.x-range, launcher.y-range, 2*range, 2*range);
	}

	public void drawTargetUnit(Graphics g, Character target){
		g.setLineWidth(3f);
		float size = target.getAttribut(Attributs.size);
		g.drawOval(target.x-size, target.y-size, 2*size, 2*size);
	}

	public void drawTargetBuilding(Graphics g, Building building){

	}

}
