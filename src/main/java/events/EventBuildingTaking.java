package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import model.Colors;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;
import render.RenderBuilding;
import utils.Utils;

public class EventBuildingTaking extends Event{
	
	public int idTarget = -1;
	private Vector<Rond> ronds;
	private float xEnd, yEnd;
	public boolean isActive = false;
	private EventBuildingTakingGlobal e;

	public EventBuildingTaking(Objet parent, Plateau plateau, EventBuildingTakingGlobal event) {
		super(parent, plateau);
		this.topLayer = true;
		this.idTarget = parent.getTarget(plateau).id;
		this.ronds = new Vector<Rond>();
		this.xEnd = parent.getTarget(plateau).x;
		this.yEnd = parent.getTarget(plateau).y;
		this.isActive = true;
		this.e = event;
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		if(this.isActive){
			if(parent.getTarget(plateau)!=null && this.idTarget == parent.getTarget(plateau).id && parent.getTarget(plateau).getTeam().id!=parent.team.id){
				if(plateau.round%10==0 || ronds.size()==0){
					ronds.add(new Rond(parent.x, parent.y, xEnd, yEnd));
				}
			} else {
				this.isActive = false;
				if(e!=null){
					e.removeAttacker(parent.team.id);
				}
			}
		}
		Vector<Rond> toRemove = new Vector<Rond>();
		for(Rond r : ronds){
			if(!r.play(g, toDraw)){
				toRemove.add(r);
			}
		}
		ronds.removeAll(toRemove);
		return ronds.size()>0;
	}
	
	private class Rond{
		float x, y;
		float xEnd, yEnd;
		float vi, vj, aj;
		float v;
		float ix, iy, jx, jy;
		public Rond(float xStart, float yStart, float xEnd, float yEnd){
			this.x = xStart;
			this.y = yStart;
			this.xEnd = xEnd;
			this.yEnd = yEnd;
			this.ix = xEnd-xStart;
			this.iy = yEnd-yStart;
			this.v = (float) Math.sqrt(ix*ix+iy*iy);
			this.ix /= v;
			this.iy /= v;
			this.jx = -iy;
			this.jy = ix;
			this.vi = (float) (Math.random()/2+0.5f)*6f;
			this.vj = (float) (Math.random()-0.5f)*10f;
		}
		
		public boolean play(Graphics g, boolean toDraw){
			float a = (x-xEnd)*ix+(y-yEnd)*iy;
			if(a>-15f){
				return false;
			}
			aj = -0.01f*((x-xEnd)*jx+(y-yEnd)*jy)+0.0005f*vj;
			vi = Math.max(1f,-a/30f);
			vj += aj;
			x += vi*ix + vj*jx;
			y += vi*iy + vj*jy;
			if(toDraw){
				Color c = Colors.getTeamColor(parent.team.id);
				g.setColor(new Color(c.r,c.g,c.b,Math.min(0.6f, 1.0f+a/v)));
				float size = Math.max(25f, 40f*(1f+a/v));
				g.fillOval(x-size/2f, y-size/2f, size, size);
			}				
			return true;
		}
		
	}

}
