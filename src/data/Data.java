package data;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.geom.Point;

import main.Main;
import ressources.Map;
import spells.Spell;
import utils.ObjetsList;
import utils.ObjetsList;
import utils.Utils;

public class Data implements java.io.Serializable {


	public int team;

	public final float ACC;
	public final float FROT;
	public final int FRAMERATE;


	public HashMap<ObjetsList, DataObjet> datas;

	public HashMap<ObjetsList, Spell> spells;

	//// Special

	public float gainedFaithByImmolation = 1f;

	//// Attack Bonuses

	public float bonusSpearHorse = 2f;
	public float bonusSwordBow = 1.5f;
	public float bonusBowFoot = 2f;
	public float bonusWandBow = 2f;

	// Ressources
	public int bonusFood = 0;
	public int bonusGold = 0;

	// Gestion des coefficients multiplicateurs pour les attributs
	public Vector<Attributs> ratioSpaceObjet;


	public Data(int team){
		this.team = team;
		this.ratioSpaceObjet = new Vector<Attributs>();
		this.ratioSpaceObjet.addElement(Attributs.sight);
		this.ratioSpaceObjet.addElement(Attributs.range);
		this.ratioSpaceObjet.addElement(Attributs.size);
		this.ratioSpaceObjet.addElement(Attributs.width);
		this.ACC = 40f;
		this.FROT = 1f;
		this.FRAMERATE = Main.framerate;
		datas = new HashMap<ObjetsList, DataObjet>();
		// add the objets
		this.initHashMap();
		// init the spells
		this.spells = new HashMap<ObjetsList, Spell>();
		for(ObjetsList s : ObjetsList.getSpells()){
			this.spells.put(s, (Spell)Spell.createSpell(s,this.team));
		}
	}

	public void initHashMap(){
		// création de la hashmap d'attributs
		HashMap<String, String> files = Utils.loadRepertoire("ressources/data/objets/", "json");
		for(String name : files.keySet()){
			this.datas.put(ObjetsList.get(name), new DataObjet(files.get(name)));
		}
		// Do the prototyping (overrides non existing attributes
		for(ObjetsList s : datas.keySet()){
			overrides(s);
		}
		// Convert to float ...
		convert();
		//		for(String s : datas.keySet()){
		//			System.out.println(datas.get(s).toString());
		//		}
	}

	// Fonctions méga propres de Flo
	public ObjetsList overrides(ObjetsList s){
		// On cherche le parent le plus vieux
		if(datas.get(s).attributsString.containsKey(Attributs.prototype)){
			// Merge parent...
			merge(s,overrides(ObjetsList.get(datas.get(s).attributsString.get(Attributs.prototype))));
		}
		return s;

	}

	public void convert(){
		Vector<Attributs> toRemove;
		for(ObjetsList s : datas.keySet()){
			DataObjet d = datas.get(s);
			d.attributs = new HashMap<Attributs,Float>();
			toRemove = new Vector<Attributs>();
			float f;
			for(Attributs a : d.attributsString.keySet()){
				if(a!=Attributs.prototype){
					try{
						f = Float.parseFloat(d.attributsString.get(a));
						//sizeX and sizeY
						if(a==Attributs.sizeX || a==Attributs.sizeY){
							f *= Map.stepGrid;
						}
						if(this.ratioSpaceObjet.contains(a)){
							f *= Main.ratioSpace;
						}
						d.attributs.put(a, f);
						toRemove.add(a);
					} catch (NumberFormatException e) {
						if(a==Attributs.spells || a==Attributs.units || a==Attributs.technologies  || a==Attributs.productions || a==Attributs.list){
							d.attributsList.put(a, new Vector<String>());
							if(d.attributsString.get(a).length()>2){
								for(String spell : d.attributsString.get(a).split("-")){
									d.attributsList.get(a).add(spell);
								}
							}
							toRemove.add(a);
						} 
					}
				}
			}
			for(Attributs a : toRemove){
				d.attributsString.remove(a);
			}
		}
	}
	public void merge(ObjetsList child,ObjetsList parent){
		HashMap<Attributs,String> c = datas.get(child).attributsString;
		HashMap<Attributs,String> p = datas.get(parent).attributsString;
		for(Attributs s : p.keySet()){
			if(!c.containsKey(s)){
				c.put(s, p.get(s));
			}
		}
	}

	// getting and setting attributs
	public float getAttribut(ObjetsList name, Attributs attribut){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas "+name);
			return 1f;
		}
		if(!this.datas.get(name).attributs.containsKey(attribut)){
			System.out.println(name+" n'a pas d'attribut "+attribut);
			Throwable t = new Throwable();
			t.printStackTrace();
			return 1f;
		}
		return this.datas.get(name).attributs.get(attribut);
	}
	public String getAttributString(ObjetsList name, Attributs attribut){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas de string pour "+name);
			return "vide";
		}
		if(!this.datas.get(name).attributsString.containsKey(attribut)){
			System.out.println(name+" n'a pas d'attribut "+attribut);
			return "vide";
		}
		return this.datas.get(name).attributsString.get(attribut);
	}
	public Vector<String> getAttributList(ObjetsList name, Attributs attribut){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas de list pour "+name);
			return new Vector<String>();
		}
		if(!this.datas.get(name).attributsList.containsKey(attribut)){
			System.out.println(name+" n'a pas d'attribut "+attribut);
			return new Vector<String>();
		}
		return this.datas.get(name).attributsList.get(attribut);
	}
	
	public Vector<ObjetsList> getAttributListAtt(ObjetsList name, Attributs attribut){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas de list pour "+name);
			return new Vector<ObjetsList>();
		}
		if(!this.datas.get(name).attributsList.containsKey(attribut)){
			System.out.println(name+" n'a pas d'attribut "+attribut);
			return new Vector<ObjetsList>();
		}
		Vector<String> s = this.datas.get(name).attributsList.get(attribut);
		Vector<ObjetsList> result = new Vector<ObjetsList>();
		for(String s1 : s){
			result.add(ObjetsList.valueOf(s1));
		}
		return result;
		
	}
	public void setAttributString(ObjetsList name, Attributs attribut, String value){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas "+name.name());
			return;
		}
		this.datas.get(name).attributsString.put(attribut, value);
	}
	public void setAttribut(ObjetsList name, Attributs attribut, float value){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas "+name.name());
			return;
		}
		this.datas.get(name).attributs.put(attribut, value);
	}
	public void addAttribut(ObjetsList name, Attributs attribut, float value){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas "+name.name());
			return;
		}
		this.datas.get(name).attributs.put(attribut, this.datas.get(name).attributs.get(attribut)+value);
	}
	public void mulAttribut(ObjetsList name, Attributs attribut, float value){
		if(!this.datas.containsKey(name)){
			System.out.println("erreur : data ne contient pas "+name.name());
			return;
		}
		this.datas.get(name).attributs.put(attribut, this.datas.get(name).attributs.get(attribut)*value);
	}

	// getting spells
	public Spell getSpell(ObjetsList s){
		if(this.spells.containsKey(s)){
			return this.spells.get(s);
		}
		System.out.println("spell non existant : "+s);
		return null;
	}

	public Point getSize(ObjetsList name){
		return new Point(getAttribut(name,Attributs.sizeX),getAttribut(name, Attributs.sizeY));

	}

}
