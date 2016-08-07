package spells;

import org.newdawn.slick.Graphics;

import bullets.Arrow;
import control.InputObject;
import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellSpecialArrow extends Spell{

	public float remainingTime;
	
	public SpellSpecialArrow(){
		this.name = SpellsList.SpecialArrow;
	}

	public void launch(Objet target, Character launcher){
		new Arrow(launcher,target.getX()-launcher.getX(),target.getY()-launcher.getY(),this.getAttribut(Attributs.damage),-1);
		
		launcher.stop();
	}



	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean spellOk) {
		// TODO Auto-generated method stub
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true);
		if(spellOk){
			g.setColor(Spell.colorOk);
		} else {
			g.setColor(Spell.colorPasOk);
		}
		g.setLineWidth(3f);
		float largeur = 2f;
		
		float dist = (float) Math.sqrt((t.x-launcher.x)*(t.x-launcher.x)+(t.y-launcher.y)*(t.y-launcher.y));
		float longueur = dist-20f;
		float longueurPointe = 20f;
		float xlauncherLeft = launcher.x+(launcher.y-t.y)*largeur/dist;
		float ylauncherLeft = launcher.y+(t.x-launcher.x)*largeur/dist;
		float xlauncherRight = launcher.x-(launcher.y-t.y)*largeur/dist;
		float ylauncherRight = launcher.y-(t.x-launcher.x)*largeur/dist;
		float xtargetLeft = launcher.x+(t.x-launcher.x)*longueur/dist+(launcher.y-t.y)*largeur/dist;
		float ytargetLeft = launcher.y+(t.y-launcher.y)*longueur/dist+(t.x-launcher.x)*largeur/dist;
		float xtargetRight = launcher.x+(t.x-launcher.x)*longueur/dist-(launcher.y-t.y)*largeur/dist;
		float ytargetRight = launcher.y+(t.y-launcher.y)*longueur/dist-(t.x-launcher.x)*largeur/dist;
		float xpointe = launcher.x+(t.x-launcher.x)*(longueur+longueurPointe)/dist;
		float ypointe = launcher.y+(t.y-launcher.y)*(longueur+longueurPointe)/dist;
		g.drawLine(xlauncherLeft,ylauncherLeft,xtargetLeft,ytargetLeft);
		g.setLineWidth(3f);
		g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
		g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		g.setLineWidth(3f);
		g.drawLine(xlauncherRight,ylauncherRight,xtargetRight,ytargetRight);
		
	}
	
}
