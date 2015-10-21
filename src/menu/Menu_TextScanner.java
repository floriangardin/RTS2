package menu;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Menu_TextScanner extends Menu_Item{

	public String s;
	public float x,y,sizeX,sizeY;
	public HashMap<Character, Integer> intToChar = new HashMap<Character, Integer>();
	
	public Menu_TextScanner(String s, float x, float y, float sizeX, float sizeY){
		this.s = s;
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		intToChar.put('A',Input.KEY_A);
		intToChar.put('B',Input.KEY_B);
		intToChar.put('C',Input.KEY_C);
		intToChar.put('D',Input.KEY_D);
		intToChar.put('E',Input.KEY_E);
		intToChar.put('F',Input.KEY_F);
		intToChar.put('G',Input.KEY_G);
		intToChar.put('H',Input.KEY_H);
		intToChar.put('I',Input.KEY_I);
		intToChar.put('J',Input.KEY_J);
		intToChar.put('K',Input.KEY_K);
		intToChar.put('L',Input.KEY_L);
		intToChar.put('M',Input.KEY_M);
		intToChar.put('N',Input.KEY_N);
		intToChar.put('O',Input.KEY_O);
		intToChar.put('P',Input.KEY_P);
		intToChar.put('Q',Input.KEY_Q);
		intToChar.put('R',Input.KEY_R);
		intToChar.put('S',Input.KEY_S);
		intToChar.put('T',Input.KEY_T);
		intToChar.put('U',Input.KEY_U);
		intToChar.put('V',Input.KEY_V);
		intToChar.put('W',Input.KEY_W);
		intToChar.put('X',Input.KEY_X);
		intToChar.put('Y',Input.KEY_Y);
		intToChar.put('Z',Input.KEY_Z);
	}
	
	public void update(Input i){
		for(int k=0; k<255; k++){
			if(i.isKeyPressed(k) && Input.getKeyName(k).length()==5){
				s+=Input.getKeyName(k).substring(4);
			}
		}
	}
	
	public void reset(){
		s = "";
	}
	
	public void draw(Graphics g){
		g.setColor(Color.white);
		g.drawRect(x, y, sizeX, sizeY);
		g.drawString(s, x, y);
	}
	
	
}
