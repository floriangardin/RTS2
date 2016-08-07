package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import model.Game;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellDash extends Spell{

	
	public SpellDash(){
		this.name = SpellsList.Dash;
	}

	public void launch(Objet target, Character launcher){
		launcher.attributsChanges.add(new AttributsChange(Attributs.damage,Change.SET,this.getAttribut(Attributs.bonusDamage),true));
		launcher.attributsChanges.add(new AttributsChange(Attributs.maxVelocity,Change.SET,this.getAttribut(Attributs.bonusSpeed),this.getAttribut(Attributs.totalTime)));
		Vector<Objet> v = new Vector<Objet>();
		v.add(launcher);
		Game.g.plateau.updateTarget(target.x,target.y,launcher.getTeam(),Character.AGGRESSIVE,v);		
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean spellOk) {
		// TODO Auto-generated method stub
		if(spellOk){
			g.setColor(Spell.colorOk);
		} else {
			g.setColor(Spell.colorPasOk);
		}
		g.setLineWidth(3f);
		float largeur = 15f;
		float longueur = 150f;
		float longueurPointe = 20f;
		
		float dist = (float) Math.sqrt((x-launcher.x)*(x-launcher.x)+(y-launcher.y)*(y-launcher.y));
		float xlauncherLeft = launcher.x+(launcher.y-y)*largeur/dist;
		float ylauncherLeft = launcher.y+(x-launcher.x)*10f/dist;
		float xlauncherRight = launcher.x-(launcher.y-y)*10f/dist;
		float ylauncherRight = launcher.y-(x-launcher.x)*10f/dist;
		float xtargetLeft = launcher.x+(x-launcher.x)*longueur/dist+(launcher.y-y)*10f/dist;
		float ytargetLeft = launcher.y+(y-launcher.y)*longueur/dist+(x-launcher.x)*10f/dist;
		float xtargetRight = launcher.x+(x-launcher.x)*longueur/dist-(launcher.y-y)*10f/dist;
		float ytargetRight = launcher.y+(y-launcher.y)*longueur/dist-(x-launcher.x)*10f/dist;
		float xpointe = launcher.x+(x-launcher.x)*(longueur+longueurPointe)/dist;
		float ypointe = launcher.y+(y-launcher.y)*(longueur+longueurPointe)/dist;
		g.drawLine(xlauncherLeft,ylauncherLeft,xtargetLeft,ytargetLeft);
		g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
		g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		g.drawLine(xlauncherRight,ylauncherRight,xtargetRight,ytargetRight);
		g.setLineWidth(1f);
	}

	
	
}
