package IA;

import java.util.Vector;

import model.Objet;
import model.Utils;
import units.Character;

public class IAUtils {
	//Static methods for IA


	//Sort by methods ..
	public static Vector<Character> triDistance(Vector<Character> liste,Character ref){
		Vector<Character> result = new Vector<Character>();
		while(liste.size()>0){
			int idx = 0;
			float minDist= Utils.distance_2(liste.get(0),ref);
			for(int j = 1 ; j<liste.size();j++){
				float dist = Utils.distance_2(liste.get(j),ref);
				if(dist<minDist){
					minDist = dist;
					idx = j;
				}
			}
			result.addElement(liste.get(idx));
			liste.remove(idx);
		}
		
		liste = result;
		return liste;
	}

	public static Character nearestUnit(Vector<Character> close, Character caller){
		float ref_dist = 10000000f;
		Character closest = null;
		for(Character o : close){
			float dist = Utils.distance_2(o,caller);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;

	}

}
