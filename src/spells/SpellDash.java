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
		launcher.inDash = this.getAttribut(Attributs.totalTime);
		Game.g.plateau.updateTarget(target.x,target.y,launcher.getTeam(),Character.MOVE,v);		
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
		g.setLineWidth(6f);
		g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
		g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		g.setLineWidth(3f);
		g.drawLine(xlauncherRight,ylauncherRight,xtargetRight,ytargetRight);
		int nbTotalFleche = 7;
		float ratio, offset = 0.002f*(System.currentTimeMillis()%500);
		g.setLineWidth(1f);
		for(int i=0; i<nbTotalFleche; i++){
			ratio = (longueur*i/nbTotalFleche+longueur*offset/nbTotalFleche)/dist;
			xtargetLeft = launcher.x+(x-launcher.x)*ratio+(launcher.y-y)*10f/dist;
			ytargetLeft = launcher.y+(y-launcher.y)*ratio+(x-launcher.x)*10f/dist;
			xtargetRight = launcher.x+(x-launcher.x)*ratio-(launcher.y-y)*10f/dist;
			ytargetRight = launcher.y+(y-launcher.y)*ratio-(x-launcher.x)*10f/dist;
			ratio = (longueur*i/nbTotalFleche+longueurPointe+longueur*offset/nbTotalFleche)/dist;
			xpointe = launcher.x+(x-launcher.x)*ratio;
			ypointe = launcher.y+(y-launcher.y)*ratio;

			g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
			g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		}
		g.setLineWidth(1f);
	}

	
	
}
