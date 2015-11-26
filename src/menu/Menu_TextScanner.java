package menu;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import multiplaying.InputObject;

public class Menu_TextScanner extends Menu_Item{

	public String s;
	public HashMap<Integer, Character> intToChar = new HashMap<Integer, Character>();
	public boolean isSelected = false;
	public int animation = 0;
	public int cooldown = 0;
	public int back = 0;
	public boolean keyDown;
	public int idKeyDown;

	public Menu_TextScanner(String s, float x, float y, float sizeX, float sizeY){
		if(s!=null)
			this.s = s;
		else 
			s = "";
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		intToChar.put(Input.KEY_A,'A');
		intToChar.put(Input.KEY_B,'B');
		intToChar.put(Input.KEY_C,'C');
		intToChar.put(Input.KEY_D,'D');
		intToChar.put(Input.KEY_E,'E');
		intToChar.put(Input.KEY_F,'F');
		intToChar.put(Input.KEY_G,'G');
		intToChar.put(Input.KEY_H,'H');
		intToChar.put(Input.KEY_I,'I');
		intToChar.put(Input.KEY_J,'J');
		intToChar.put(Input.KEY_K,'K');
		intToChar.put(Input.KEY_L,'L');
		intToChar.put(Input.KEY_M,'M');
		intToChar.put(Input.KEY_N,'N');
		intToChar.put(Input.KEY_O,'O');
		intToChar.put(Input.KEY_P,'P');
		intToChar.put(Input.KEY_Q,'Q');
		intToChar.put(Input.KEY_R,'R');
		intToChar.put(Input.KEY_S,'S');
		intToChar.put(Input.KEY_T,'T');
		intToChar.put(Input.KEY_U,'U');
		intToChar.put(Input.KEY_V,'V');
		intToChar.put(Input.KEY_W,'W');
		intToChar.put(Input.KEY_X,'X');
		intToChar.put(Input.KEY_Y,'Y');
		intToChar.put(Input.KEY_Z,'Z');
		intToChar.put(Input.KEY_SPACE,' ');
	}

	public void update(Input i, InputObject im){
		if(isSelected){
			if(s.length()<14){
				int l = intToChar.size();
				for(Integer k: intToChar.keySet()){
					if(i.isKeyDown(k)){
						if(!keyDown || idKeyDown!=k){
							this.keyDown = true;
							this.idKeyDown = k;
							if(i.isKeyDown(Input.KEY_LSHIFT)||i.isKeyDown(Input.KEY_RSHIFT))
								s+=intToChar.get(k);
							else
								s+=Character.toLowerCase((intToChar.get(k)));
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
		if(isSelected)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.white);
		g.drawRect(x-sizeX/2f, y-sizeY/2f, sizeX, sizeY);
		g.setColor(Color.white);
		float height = g.getFont().getHeight("Hg");
		if(s!=null){
			if(animation>30){
				g.drawString(s+"|", x-sizeX/2.3f, y-height/2);
			} else {
				g.drawString(s, x-sizeX/2.3f, y-height/2);				
			}
		}
	}


}
