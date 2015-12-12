package menu;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import model.Game;
import multiplaying.InputObject;

public class Menu_TextScanner extends Menu_Item{

	public String s = "";
	public HashMap<Integer, String> intToChar = new HashMap<Integer, String>();
	public HashMap<Integer, String> intToCharMin = new HashMap<Integer, String>();
	public boolean isSelected = false;
	public int animation = 0;
	public int cooldown = 0;
	public int back = 0;
	public boolean keyDown;
	public int idKeyDown;

	public Vector<String> autocompletion = new Vector<String>();
	public Vector<String> autocompletionTEMP = new Vector<String>();

	public Menu_TextScanner(String s, float x, float y, float sizeX, float sizeY, Game g){
		this.game = g;
		if(s!=null)
			this.s = s;
		else 
			s = "";
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		intToChar.put(Input.KEY_A,"A");
		intToChar.put(Input.KEY_B,"B");
		intToChar.put(Input.KEY_C,"C");
		intToChar.put(Input.KEY_D,"D");
		intToChar.put(Input.KEY_E,"E");
		intToChar.put(Input.KEY_F,"F");
		intToChar.put(Input.KEY_G,"G");
		intToChar.put(Input.KEY_H,"H");
		intToChar.put(Input.KEY_I,"I");
		intToChar.put(Input.KEY_J,"J");
		intToChar.put(Input.KEY_K,"K");
		intToChar.put(Input.KEY_L,"L");
		intToChar.put(Input.KEY_M,"M");
		intToChar.put(Input.KEY_N,"N");
		intToChar.put(Input.KEY_O,"O");
		intToChar.put(Input.KEY_P,"P");
		intToChar.put(Input.KEY_Q,"Q");
		intToChar.put(Input.KEY_R,"R");
		intToChar.put(Input.KEY_S,"S");
		intToChar.put(Input.KEY_T,"T");
		intToChar.put(Input.KEY_U,"U");
		intToChar.put(Input.KEY_V,"V");
		intToChar.put(Input.KEY_W,"W");
		intToChar.put(Input.KEY_X,"X");
		intToChar.put(Input.KEY_Y,"Y");
		intToChar.put(Input.KEY_Z,"Z");
		intToChar.put(51, "?");
		intToChar.put(52, ".");
		intToChar.put(53, "/");
		intToChar.put(41, "§");
		intToChar.put(26, "°");
		intToChar.put(Input.KEY_0, "0");
		intToChar.put(Input.KEY_1, "1");
		intToChar.put(Input.KEY_2, "2");
		intToChar.put(Input.KEY_3, "3");
		intToChar.put(Input.KEY_4, "4");
		intToChar.put(Input.KEY_5, "5");
		intToChar.put(Input.KEY_6, "6");
		intToChar.put(Input.KEY_7, "7");
		intToChar.put(Input.KEY_8, "8");
		intToChar.put(Input.KEY_9, "9");
		intToChar.put(Input.KEY_NUMPAD0, "0");
		intToChar.put(Input.KEY_NUMPAD1, "1");
		intToChar.put(Input.KEY_NUMPAD2, "2");
		intToChar.put(Input.KEY_NUMPAD3, "3");
		intToChar.put(Input.KEY_NUMPAD4, "4");
		intToChar.put(Input.KEY_NUMPAD5, "5");
		intToChar.put(Input.KEY_NUMPAD6, "6");
		intToChar.put(Input.KEY_NUMPAD7, "7");
		intToChar.put(Input.KEY_NUMPAD8, "8");
		intToChar.put(Input.KEY_NUMPAD9, "9");
		intToChar.put(Input.KEY_SPACE," ");
		intToCharMin.put(Input.KEY_SPACE," ");
		intToCharMin.put(Input.KEY_A,"a");
		intToCharMin.put(Input.KEY_B,"b");
		intToCharMin.put(Input.KEY_C,"c");
		intToCharMin.put(Input.KEY_D,"d");
		intToCharMin.put(Input.KEY_E,"e");
		intToCharMin.put(Input.KEY_F,"f");
		intToCharMin.put(Input.KEY_G,"g");
		intToCharMin.put(Input.KEY_H,"h");
		intToCharMin.put(Input.KEY_I,"i");
		intToCharMin.put(Input.KEY_J,"j");
		intToCharMin.put(Input.KEY_K,"k");
		intToCharMin.put(Input.KEY_L,"l");
		intToCharMin.put(Input.KEY_M,"m");
		intToCharMin.put(Input.KEY_N,"n");
		intToCharMin.put(Input.KEY_O,"o");
		intToCharMin.put(Input.KEY_P,"p");
		intToCharMin.put(Input.KEY_Q,"q");
		intToCharMin.put(Input.KEY_R,"r");
		intToCharMin.put(Input.KEY_S,"s");
		intToCharMin.put(Input.KEY_T,"t");
		intToCharMin.put(Input.KEY_U,"u");
		intToCharMin.put(Input.KEY_V,"v");
		intToCharMin.put(Input.KEY_W,"w");
		intToCharMin.put(Input.KEY_X,"x");
		intToCharMin.put(Input.KEY_Y,"y");
		intToCharMin.put(Input.KEY_Z,"z");
		intToCharMin.put(Input.KEY_0, "à");
		intToCharMin.put(Input.KEY_1, "&");
		intToCharMin.put(Input.KEY_2, "é");
		intToCharMin.put(Input.KEY_3, "\"");
		intToCharMin.put(Input.KEY_4, "\'");
		intToCharMin.put(Input.KEY_5, "(");
		intToCharMin.put(Input.KEY_6, "-");
		intToCharMin.put(Input.KEY_7, "è");
		intToCharMin.put(Input.KEY_8, "_");
		intToCharMin.put(Input.KEY_9, "ç");
		intToCharMin.put(Input.KEY_NUMPAD0, "0");
		intToCharMin.put(Input.KEY_NUMPAD1, "1");
		intToCharMin.put(Input.KEY_NUMPAD2, "2");
		intToCharMin.put(Input.KEY_NUMPAD3, "3");
		intToCharMin.put(Input.KEY_NUMPAD4, "4");
		intToCharMin.put(Input.KEY_NUMPAD5, "5");
		intToCharMin.put(Input.KEY_NUMPAD6, "6");
		intToCharMin.put(Input.KEY_NUMPAD7, "7");
		intToCharMin.put(Input.KEY_NUMPAD8, "8");
		intToCharMin.put(Input.KEY_NUMPAD9, "9");
		intToCharMin.put(51, ",");
		intToCharMin.put(52, ";");
		intToCharMin.put(53, ":");
		intToCharMin.put(41, "!");
		intToCharMin.put(26, ")");
	}

	public void update(Input i, InputObject im){
		if(im.isPressedTAB){
			// autocompletion
			// mise à jour de la liste de nom
			//TODO améliorer le système d'autocompéltion
			autocompletionTEMP.clear();
			for(String s : this.autocompletion){
				if(s.startsWith(this.s)){
					autocompletionTEMP.add(s);
				}
			}
			if(autocompletionTEMP.size()>0){
				this.s = autocompletionTEMP.firstElement();
			}
			return;
		}
		if((!game.isInMenu && !game.inEditor) || isSelected){
			if(this.game.font.getWidth(s)<this.sizeX){
				int l = intToChar.size();
				for(Integer k: intToChar.keySet()){
					if(i.isKeyDown(k)){
						if(!keyDown || idKeyDown!=k){
							this.keyDown = true;
							this.idKeyDown = k;
							if(i.isKeyDown(Input.KEY_LSHIFT)||i.isKeyDown(Input.KEY_RSHIFT)){
								s+=intToChar.get(k);
							}else{
								s+=intToCharMin.get(k);
							}
						}
						break;
					}
					l--;
				}
				if(l==0){
					this.keyDown = false;
				}
			}
			if(i.isKeyDown(Input.KEY_BACK) && s.length()>0){
				if(cooldown<=0){
					s=s.substring(0,s.length()-1);
					back++;
					cooldown = Math.max(25-back*5,5);
				} else {
					cooldown -= 1;
				}
			} else {
				back = 0;
				cooldown = 0;
			}
			if(im.isPressedENTER){
				isSelected = false;
			}
			animation +=1;
			if(animation>60)
				animation = 0;
		} else {
			animation = 0;
		}
	}

	public void reset(){
		s = "";
	}

	public void draw(Graphics g){
		if(!game.isInMenu){
			g.setColor(Color.black);
			g.fillRect(x-sizeX/2f-10f, y-sizeY/2f, sizeX+20f, sizeY);
		}
		if(isSelected)
			g.setColor(Color.white);
		else
			g.setColor(Color.gray);
		g.drawRect(x-sizeX/2f-10f, y-sizeY/2f, sizeX+20f, sizeY);
		g.setColor(Color.white);
		float height = g.getFont().getHeight("Hg");
		if(s!=null){
			if(animation>30){
				g.drawString(s+"|", x-sizeX/2f, y-height/2);
			} else {
				g.drawString(s, x-sizeX/2f, y-height/2);				
			}
		}
	}
	public void draw(Graphics g, int i){
		//dessiner depuis le coin haut gauche
		if(!game.isInMenu){
			g.setColor(Color.black);
			g.fillRect(x, y, sizeX+20f, sizeY);
		}
		if(isSelected)
			g.setColor(Color.white);
		else
			g.setColor(Color.gray);
		g.drawRect(x, y, sizeX+20f, sizeY);
		g.setColor(Color.white);
		float height = g.getFont().getHeight("Hg");
		if(s!=null){
			if(animation>30){
				g.drawString(s+"|", x+10f, y-height/2+sizeY/2);
			} else {
				g.drawString(s, x+10f, y-height/2+sizeY/2);				
			}
		}
	}

}
