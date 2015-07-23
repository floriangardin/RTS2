import java.util.Vector;

// Class for static methods
public class Utils {

	
	
	
	public static float distance_2(Objet a ,Objet b){
		return (a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) ;
		
	}
	
	public static float distance(Objet a ,Objet b){
		return (float) Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) );
		
	}
	public static Objet nearestObject(Vector<Objet> close, Objet caller){
		float ref_dist = 100000f;
		Objet closest = null;
		for(Objet o : close){
			float dist = Utils.distance_2(o,caller);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;
		
	}
}
