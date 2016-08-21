package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import utils.ObjetsList;
import utils.ObjetsList;



public class KeyMapper {

	private static String location = "././ressources/data/keymapping.rtsfile";
	public HashMap<KeyEnum, Vector<Integer>> mapping;
	public HashMap<Integer, KeyEnum> mouseMapping;

	public KeyMapper(){
		String fichier = location;
		mapping = new HashMap<KeyEnum,  Vector<Integer>>();
		for(KeyEnum ke : KeyEnum.values()){
			mapping.put(ke, new Vector<Integer>());

		}

		// Constructeur par défaut, associe le mapping standard
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			String[] tab;
			while ((ligne=br.readLine())!=null){
				if(!ligne.contains("_")){
					throw new Exception();
				}
				tab = ligne.split("_");
				
				if(mapping.containsKey(KeyEnum.valueOf(tab[1]))){
					
					mapping.get(KeyEnum.valueOf(tab[1])).add(Integer.parseInt(tab[0]));
				}
			}
			br.close(); 

		}		
		catch (Exception e){}
		mouseMapping = new HashMap<Integer, KeyEnum>();
		mouseMapping.put(0, KeyEnum.LeftClick);
		mouseMapping.put(1, KeyEnum.RightClick);
		mouseMapping.put(2, KeyEnum.MiddleClick);

	}

	public void saveMapping(){
		try {
			FileWriter fw = new FileWriter(location);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			for(KeyEnum ke : this.mapping.keySet()){
				for(Integer i : this.mapping.get(ke)){
					fichierSortie.println (i+"_"+ke);
				}
			}
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public enum KeyEnum implements java.io.Serializable{
		LeftClick,
		RightClick,
		MiddleClick,
		Up,
		Down,
		Right,
		Left,
		Prod0,
		Prod1,
		Prod2,
		Prod3,
		Tech0,
		Tech1,
		Tech2,
		Tech3,
		TenirPosition,
		DeplacementOffensif,
		PouvoirSpecial,
		AjouterSelection,
		ToutSelection,
		AbandonnerPartie,
		Spearman,
		Crossbowman,
		Knight,
		Inquisitor,
		Priest,
		Enter, 
		Tab, 
		Escape, 
		StopperMouvement, 
		Immolation,
		Barracks,
		Stable,
		Headquarters, 
		GlobalRallyPoint,
		AllUnits;

		public Vector<ObjetsList> getUnitsList() {
			Vector<ObjetsList> v = new Vector<ObjetsList>();
			switch(this){
			case Spearman : 
			case Crossbowman:
			case Knight:
			case Priest:
			case Inquisitor:v.add(ObjetsList.valueOf(this.name()));
			break;
			case AllUnits:
				v.add(ObjetsList.Spearman);
				v.add(ObjetsList.Crossbowman);
				v.add(ObjetsList.Knight);
				v.add(ObjetsList.Priest);
				v.add(ObjetsList.Inquisitor);
			default:
			}
			return v;
		}
		
		public Vector<ObjetsList> getBuildingsList() {
			Vector<ObjetsList> v = new Vector<ObjetsList>();
			switch(this){
			case Barracks:
			case Stable:
			case Headquarters:v.add(ObjetsList.valueOf(this.name()));
			default:
			}
			return v;
		}



	}
}
