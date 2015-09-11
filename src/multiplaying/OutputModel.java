package multiplaying;

import java.util.Vector;

import buildings.Building;
import buildings.BuildingProduction;
import buildings.BuildingTech;
import model.Utils;
import spells.BlessedArea;
import spells.Firewall;
import spells.SpellEffect;
import units.Character;

public class OutputModel extends MultiObjetModel{

	public int gold;
	public int food;
	public int special;

	public Vector<OutputChar> toChangeCharacters;

	public Vector<OutputBullet> toChangeBullets;

	public Vector<OutputBuilding> toChangeBuildings;

	public Vector<OutputSpell> toChangeSpells;

	public Vector<Integer> selection;

	public OutputModel(String s){
		String[] t = Utils.split(s, '|');
		String[] v;
		toChangeCharacters = new Vector<OutputChar>();
		toChangeBullets = new Vector<OutputBullet>();
		toChangeBuildings = new Vector<OutputBuilding>();
		toChangeSpells = new Vector<OutputSpell>();
		selection = new Vector<Integer>();

		for(int i=0; i<t.length; i++){
			if(i==t.length-1)
				continue;
			if(i==0){
				timeValue = Integer.parseInt(t[i]);
			} else if(i==1) {
				food = Integer.parseInt(t[i]);
			} else if(i==2) {
				gold = Integer.parseInt(t[i]);
			} else if(i==3) {
				special = Integer.parseInt(t[i]);
			} else {
				v = Utils.split(t[i], '*');
				for(int j=0; j<v.length; j++){
					if(v[j].equals(""))
						continue;
					switch(i){
					case 4:
						toChangeCharacters.add(new OutputChar(v[j]));break;
					case 5:
						toChangeBullets.add(new OutputBullet(v[j]));break;
					case 6:
						toChangeBuildings.add(new OutputBuilding(v[j]));break;
					case 7:
						toChangeSpells.add(new OutputSpell(v[j]));break;
					case 8:
						selection.add(Integer.parseInt(v[j]));break;
					}
				}
			}
		}
	}
	public OutputModel(int timeValue){
		this.timeValue = timeValue;
		toChangeCharacters = new Vector<OutputChar>();
		toChangeBullets = new Vector<OutputBullet>();
		toChangeBuildings = new Vector<OutputBuilding>();
		toChangeSpells = new Vector<OutputSpell>();
		selection = new Vector<Integer>();
	}
	public String toString(){
		String s = "1" +(int)this.timeValue+"|"+this.food+"|"+this.gold+"|"+this.special+"|";
		/*STRUCTURE D'UN OUTPUT MODEL
		 * chaque grande partie est séparée par un '|'
		 * chaque élément au sein d'une partie est séparé par un '-'
		 * chaque attribut au sein d'un élément est séparé par un ' '
		 * 
		 * Selon l'ordre:
		 * toChangeChar | toChangeBullet | toChangeBuilding | Selection
		 */
		// For the Characters
		int size = toChangeCharacters.size();
		for(int i =0; i<size; i++){
			s+=toChangeCharacters.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		// For the bullets
		size = toChangeBullets.size();
		for(int i =0; i<size; i++){
			s+=toChangeBullets.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		// For the buildings
		size = toChangeBuildings.size();
		for(int i =0; i<size; i++){
			s+=toChangeBuildings.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		// for the spells
		size = toChangeSpells.size();
		for(int i =0; i<size; i++){
			s+=toChangeSpells.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		// for the selections
		size = selection.size();
		for(int i =0; i<size; i++){
			s+=selection.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		return s;
	}

	public static class OutputBuilding{
		public float x, y;
		public float sizeX, sizeY;
		public int id;
		public int typeBuilding;
		public float lifepoints, maxlifepoints, constrpoints;
		public int team;
		public float animation;
		public float sight;
		public float charge;
		public int[] queue = new int[5];

		public OutputBuilding(Building b){
			this.x = b.getX();
			this.y = b.getY();
			this.team = b.team;
			this.typeBuilding = b.type;
			this.id= b.id;
			this.sizeX = b.sizeX;
			this.sizeY = b.sizeY;
			this.lifepoints = b.lifePoints;
			this.maxlifepoints = b.maxLifePoints;
			this.constrpoints = b.constructionPoints;
			this.animation = b.animation;
			this.sight = b.sight;
			if(b instanceof BuildingProduction){
				this.charge = ((BuildingProduction)b).charge;
				for(int i=0;i<5;i++){
					if(i<((BuildingProduction)b).queue.size())
						this.queue[i] = ((BuildingProduction)b).queue.get(i);
					else
						this.queue[i] = -1;
				}
			} else if (b instanceof BuildingTech){
				this.charge = ((BuildingTech)b).charge;
				if(((BuildingTech)b).queue!=null)
					this.queue[0] = ((BuildingTech)b).getIndexOfQueue();
			}
		}
		public OutputBuilding(String s){
			try{
				String[] t = Utils.split(s, ' ');
				this.id = Integer.parseInt(t[0]);
				this.typeBuilding = Integer.parseInt(t[1]);
				this.x = Float.parseFloat(t[2]);
				this.y = Float.parseFloat(t[3]);
				this.sizeX = Float.parseFloat(t[4]);
				this.sizeY = Float.parseFloat(t[5]);
				this.team = Integer.parseInt(t[6]);
				this.lifepoints = Float.parseFloat(t[7]);
				this.maxlifepoints = Float.parseFloat(t[8]);
				this.constrpoints = Float.parseFloat(t[9]);
				this.animation = Float.parseFloat(t[10]);
				this.sight = Float.parseFloat(t[11]);
				this.charge = Float.parseFloat(t[12]);
				this.queue[0] = Integer.parseInt(t[13]);
				this.queue[1] = Integer.parseInt(t[14]);
				this.queue[2] = Integer.parseInt(t[15]);
				this.queue[3] = Integer.parseInt(t[16]);
				this.queue[4] = Integer.parseInt(t[17]);
			} catch (NumberFormatException e ){
				//System.out.println(s);
			}
		}
		public String toString(){
			String s = "";
			s+=id+" "+typeBuilding+" "+x+" "+y+" "+sizeX+" "+sizeY+" "+team+" "+lifepoints+" "+maxlifepoints+" "+constrpoints+" "+animation+" "+sight+" "+charge;
			for(int i=0;i<5;i++){
				s+=" "+this.queue[i];
			}
			return s;
		}
	}

	public static class OutputBullet{
		public float x,y,vx,vy;
		public int id;
		public int typeBullet;
		public OutputBullet(int id, int type, float x, float y, float vx, float vy){
			// Output to create a new bullet
			this.id = id;
			this.typeBullet = type;
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
		}
		public OutputBullet(String s){
			try{
				String[] t = Utils.split(s, ' ');
				this.id = Integer.parseInt(t[0]);
				this.typeBullet = Integer.parseInt(t[1]);
				this.x = Float.parseFloat(t[2]);
				this.y = Float.parseFloat(t[3]);
				this.vx = Float.parseFloat(t[4]);
				this.vy = Float.parseFloat(t[5]);
			} catch (NumberFormatException e ){
				//System.out.println(s);
			}
		}
		public String toString(){
			String s = "";
			s+=id+" "+typeBullet+" "+x+" "+y+" "+vx+" "+vy;
			return s;
		}
	}

	public static class OutputChar{
		public float x,y,lifePoints;
		public int id, team;
		public int weaponType, horseType;
		public int animation, direction;
		public float sight;
		public int isImmolating;
		public float[] spellState;
		public String name;
		/* Weapon Type
		 * 0 - none
		 * 1 - sword
		 * 2 - bow
		 * 3 - bible
		 * 4 - magicwand
		 * 
		 * HorseType
		 * 0 - none
		 * 1 - horse
		 */
		public OutputChar(Character c){
			// Output to create a new character
			this.id = c.id;
			this.team = c.team;
			this.x = c.getX();
			this.y = c.getY();
			this.lifePoints = c.lifePoints;
			this.weaponType = c.typeWeapon;
			this.horseType = c.typeHorse;
			this.animation = c.animation;
			this.direction = c.orientation;
			this.sight = c.sight;
			if(c.isImmolating)
				this.isImmolating = 1;
			else
				this.isImmolating = 0;
			this.spellState = new float[4];
			for(int i=0; i<c.spellsState.size();i++){
				this.spellState[i] =c.spellsState.get(i);
			}
			this.name = c.name;
		}
		public OutputChar(String s){
			String[] t = Utils.split(s, ' ');
			this.id = Integer.parseInt(t[0]);
			this.team = Integer.parseInt(t[1]);
			this.x = Float.parseFloat(t[2]);
			this.y = Float.parseFloat(t[3]);
			this.lifePoints = Float.parseFloat((t[4]));
			this.weaponType = Integer.parseInt(t[5]);
			this.horseType = Integer.parseInt(t[6]);
			this.animation = Integer.parseInt(t[7]);
			this.direction = Integer.parseInt(t[8]);
			this.sight = Float.parseFloat((t[9]));
			this.isImmolating = Integer.parseInt(t[10]);
			this.spellState[0] = Float.parseFloat((t[11]));
			this.spellState[1] = Float.parseFloat((t[12]));
			this.spellState[2] = Float.parseFloat((t[13]));
			this.spellState[3] = Float.parseFloat((t[14]));
			this.name = t[15];
		}
		public String toString(){
			String s= "";
			s+=id+" " +team +" "+x+" "+y+" "+lifePoints+" "+weaponType+ " "+horseType+" "+animation+" "+direction+" "+sight+" "+
					isImmolating+" "+spellState[0]+" "+spellState[1]+" "+spellState[2]+" "+spellState[3]+" "+name;
			return s;
		}
	}

	public static class OutputSpell{
		public int id;
		public float x1, y1, x2, y2;
		public int type;

		public OutputSpell(SpellEffect s){
			type = s.getType();
			id = s.id;
			switch(type){
			case 1 : Firewall f = (Firewall)s;
			x1 = f.x;
			x2 = f.x2;
			y1 = f.y;
			y2 = f.y2;
			break;
			case 2 : BlessedArea ba = (BlessedArea)s;
			x1 = ba.x;
			y1 = ba.y;
			break;
			default:
			}

		}

		public OutputSpell(String s){
			try{
				String[] t = Utils.split(s, ' ');
				this.id = Integer.parseInt(t[0]);
				this.type = Integer.parseInt(t[1]);
				this.x1 = Float.parseFloat(t[2]);
				this.y1 = Float.parseFloat(t[3]);
				this.x2 = Float.parseFloat(t[4]);
				this.y2 = Float.parseFloat(t[5]);
			} catch (NumberFormatException e ){
				//System.out.println(s);
			}
		}
	}
}
