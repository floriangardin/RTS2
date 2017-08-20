package system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class ClassSystem {
	
	public abstract void render(GameContainer gc, Graphics g) throws SlickException;
	
	public abstract void update(GameContainer arg0, int arg1) throws SlickException;

	

}
