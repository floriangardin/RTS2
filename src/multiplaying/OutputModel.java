package multiplaying;

import java.util.Vector;

import model.Utils;

public class OutputModel extends MultiObjetModel{


	public Vector<OutputChar> toChangeCharacters;

	// Not useful yet, because there are no isolated equipment to display
	//	protected Vector<ActionObjet> equipments;
	//	protected Vector<ActionObjet> toAddEquipments;
	//	protected Vector<ActionObjet> toRemoveEquipments;

	public Vector<OutputBullet> toChangeBullets;

	public Vector<Integer> selection;

	public OutputModel(String s){
		String[] t = Utils.split(s, '|');
		String[] v;
		toChangeCharacters = new Vector<OutputChar>();
		toChangeBullets = new Vector<OutputBullet>();
		selection = new Vector<Integer>();

		for(int i=0; i<t.length; i++){
			if(i==t.length-1)
				continue;
			if(i==0){
				timeValue = Integer.parseInt(t[i]);
			} else {
				v = Utils.split(t[i], '*');
				for(int j=0; j<v.length; j++){
					if(v[j].equals(""))
						continue;
					switch(i){
					case 1:
						toChangeCharacters.add(new OutputChar(v[j]));break;
					case 2:
						toChangeBullets.add(new OutputBullet(v[j]));break;
					case 3:
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
		selection = new Vector<Integer>();
	}
	public String toString(){
		String s = "1" +(int)this.timeValue+"|";
		/*STRUCTURE D'UN OUTPUT MODEL
		 * chaque grande partie est séparée par un '|'
		 * chaque élément au sein d'une partie est séparé par un '-'
		 * chaque attribut au sein d'un élément est séparé par un ' '
		 * 
		 * Selon l'ordre:
		 * toChangeChar | toAddChar | toRemoveChar | toChangeBullet | toAddBullet | toRemoveBullet | Selection
		 */
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
		size = selection.size();
		for(int i =0; i<size; i++){
			s+=selection.get(i).toString();
			if(i<size-1)
				s+="*";
		}
		s+="|";
		return s;
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
		public int armorType, weaponType, horseType;
		public int animation, direction;
		/* Armor Type:
		 * 0 - none
		 * 1 - light
		 * 2 - medium
		 * 3 - heavy
		 * 
		 * Weapon Type
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
		public OutputChar(int id, int team, float x, float y, float lifePoints, int armor, int weapon, int horse, int animation, int direction){
			// Output to create a new character
			this.id = id;
			this.team = team;
			this.x = x;
			this.y = y;
			this.lifePoints = lifePoints;
			this.armorType = armor;
			this.weaponType = weapon;
			this.horseType = horse;
			this.animation = animation;
			this.direction = direction;
		}
		public OutputChar(String s){
			String[] t = Utils.split(s, ' ');
			this.id = Integer.parseInt(t[0]);
			this.team = Integer.parseInt(t[1]);
			this.x = Float.parseFloat(t[2]);
			this.y = Float.parseFloat(t[3]);
			this.lifePoints = Float.parseFloat((t[4]));
			this.armorType = Integer.parseInt(t[5]);
			this.weaponType = Integer.parseInt(t[6]);
			this.horseType = Integer.parseInt(t[7]);
			this.animation = Integer.parseInt(t[8]);
			this.direction = Integer.parseInt(t[9]);
		}
		public String toString(){
			String s= "";
			s+=id+" " +team +" "+x+" "+y+" "+lifePoints+" "+armorType+" "+weaponType+ " "+horseType+" "+animation+" "+direction;
			return s;
		}
	}
	

}
