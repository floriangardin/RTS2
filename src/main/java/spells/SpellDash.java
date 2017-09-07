package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import events.EventHandler;
import events.EventNames;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public strictfp class SpellDash extends Spell{


	public SpellDash(){
		this.name = ObjetsList.Dash;
	}
	public boolean launch(Objet target, Character launcher, Plateau plateau){
		if(launcher==null || target==null){
			return false;
		}
		
		launcher.attributsChanges.add(new AttributsChange(Attributs.maxVelocity,Change.SET,this.getAttribut(Attributs.bonusSpeed),this.getAttribut(Attributs.totalTime)));
		Vector<Objet> v = new Vector<Objet>();
		v.add(launcher);
		launcher.inDash = this.getAttribut(Attributs.totalTime);
		if(target!=null && launcher!=null){
			plateau.updateTarget(launcher, target.getX(),target.getY(),launcher.getTeam().id, Character.MOVE, new Vector<Integer>());		
			if(launcher.getTarget(plateau)!=null && launcher.getTarget(plateau) instanceof Character && launcher.getTarget(plateau).getTeam()!=launcher.getTeam()){
				launcher.attributsChanges.add(new AttributsChange(Attributs.damage,Change.SET,this.getAttribut(Attributs.bonusDamage),true));
			}
		}
		EventHandler.addEvent(EventNames.Dash, launcher, plateau);
		return true;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean spellOk, Plateau plateau) {
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
		
		float dist = (float) StrictMath.sqrt((x-launcher.getX())*(x-launcher.getX())+(y-launcher.getY())*(y-launcher.getY()));
		float xlauncherLeft = launcher.getX()+(launcher.getY()-y)*largeur/dist;
		float ylauncherLeft = launcher.getY()+(x-launcher.getX())*10f/dist;
		float xlauncherRight = launcher.getX()-(launcher.getY()-y)*10f/dist;
		float ylauncherRight = launcher.getY()-(x-launcher.getX())*10f/dist;
		float xtargetLeft = launcher.getX()+(x-launcher.getX())*longueur/dist+(launcher.getY()-y)*10f/dist;
		float ytargetLeft = launcher.getY()+(y-launcher.getY())*longueur/dist+(x-launcher.getX())*10f/dist;
		float xtargetRight = launcher.getX()+(x-launcher.getX())*longueur/dist-(launcher.getY()-y)*10f/dist;
		float ytargetRight = launcher.getY()+(y-launcher.getY())*longueur/dist-(x-launcher.getX())*10f/dist;
		float xpointe = launcher.getX()+(x-launcher.getX())*(longueur+longueurPointe)/dist;
		float ypointe = launcher.getY()+(y-launcher.getY())*(longueur+longueurPointe)/dist;
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
			xtargetLeft = launcher.getX()+(x-launcher.getX())*ratio+(launcher.getY()-y)*10f/dist;
			ytargetLeft = launcher.getY()+(y-launcher.getY())*ratio+(x-launcher.getX())*10f/dist;
			xtargetRight = launcher.getX()+(x-launcher.getX())*ratio-(launcher.getY()-y)*10f/dist;
			ytargetRight = launcher.getY()+(y-launcher.getY())*ratio-(x-launcher.getX())*10f/dist;
			ratio = (longueur*i/nbTotalFleche+longueurPointe+longueur*offset/nbTotalFleche)/dist;
			xpointe = launcher.getX()+(x-launcher.getX())*ratio;
			ypointe = launcher.getY()+(y-launcher.getY())*ratio;

			g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
			g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		}
		g.setLineWidth(1f);
	}

	
	
}
