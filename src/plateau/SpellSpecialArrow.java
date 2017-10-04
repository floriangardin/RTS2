package plateau;

import org.newdawn.slick.Graphics;

import data.Attributs;
import utils.ObjetsList;

public strictfp class SpellSpecialArrow extends Spell{

	public float remainingTime;
	
	public SpellSpecialArrow(){
		this.name = ObjetsList.SpecialArrow;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		if(target!=null && launcher!=null && target.getId()!=launcher.getId()){			
			new Arrow(launcher,target.getX()-launcher.getX(),target.getY()-launcher.getY(),this.getAttribut(Attributs.damage), plateau);
			launcher.stop(plateau);
			return true;
		}	
		return false;
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean spellOk, Plateau plateau) {
		// TODO Auto-generated method stub
		Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true, plateau);
		if(spellOk){
			g.setColor(Spell.colorOk);
		} else {
			g.setColor(Spell.colorPasOk);
		}
		g.setLineWidth(3f);
		float largeur = 2f;
		
		float dist = (float) StrictMath.sqrt((t.getX()-launcher.getX())*(t.getX()-launcher.getX())+(t.getY()-launcher.getY())*(t.getY()-launcher.getY()));
		float longueur = dist-20f;
		float longueurPointe = 20f;
		float xlauncherLeft = launcher.getX()+(launcher.getY()-t.getY())*largeur/dist;
		float ylauncherLeft = launcher.getY()+(t.getX()-launcher.getX())*largeur/dist;
		float xlauncherRight = launcher.getX()-(launcher.getY()-t.getY())*largeur/dist;
		float ylauncherRight = launcher.getY()-(t.getX()-launcher.getX())*largeur/dist;
		float xtargetLeft = launcher.getX()+(t.getX()-launcher.getX())*longueur/dist+(launcher.getY()-t.getY())*largeur/dist;
		float ytargetLeft = launcher.getY()+(t.getY()-launcher.getY())*longueur/dist+(t.getX()-launcher.getX())*largeur/dist;
		float xtargetRight = launcher.getX()+(t.getX()-launcher.getX())*longueur/dist-(launcher.getY()-t.getY())*largeur/dist;
		float ytargetRight = launcher.getY()+(t.getY()-launcher.getY())*longueur/dist-(t.getX()-launcher.getX())*largeur/dist;
		float xpointe = launcher.getX()+(t.getX()-launcher.getX())*(longueur+longueurPointe)/dist;
		float ypointe = launcher.getY()+(t.getY()-launcher.getY())*(longueur+longueurPointe)/dist;
		g.drawLine(xlauncherLeft,ylauncherLeft,xtargetLeft,ytargetLeft);
		g.setLineWidth(3f);
		g.drawLine(xtargetLeft,ytargetLeft,xpointe,ypointe);
		g.drawLine(xpointe,ypointe,xtargetRight,ytargetRight);
		g.setLineWidth(3f);
		g.drawLine(xlauncherRight,ylauncherRight,xtargetRight,ytargetRight);
		
	}
	
}
