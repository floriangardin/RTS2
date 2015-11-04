package menu;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Menu_TextScanner extends Menu_Item{

	public String s;
	public HashMap<Integer, Character> intToChar = new HashMap<Integer, Character>();
	public boolean isSelected = false;
	public int animation = 0;
	public int cooldown = 0;
	public int back = 0;

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

	public void update(Input i){
		if(isSelected){
			if(s.length()<13){
				for(Integer k: intToChar.keySet()){
					if(i.isKeyPressed(k)){
						if(i.isKeyDown(Input.KEY_LSHIFT)||i.isKeyDown(Input.KEY_RSHIFT))
							s+=intToChar.get(k);
						else
							s+=(intToChar.get(k)).toLowerCase((intToChar.get(k)));
					}
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
			if(i.isKeyPressed(Input.KEY_RETURN))
				isSelected = false;
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
		g.drawRect(x, y, sizeX, sizeY);
		g.setColor(Color.white);
		float height = g.getFont().getHeight(s);
		if(s!=null){
			if(animation>30){
				g.drawString(s+"|", x+15f, y+sizeY/2-height/2);
			} else {
				g.drawString(s, x+15f, y+sizeY/2-height/2);				
			}
		}
	}


}
