package ressources;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import main.Main;
import model.Game;

public class IntroMovie {
	public float time = 0f;
	public float secondes = 0f;
	
	public boolean launchMusic = false;
	public boolean launchGdBSound = false;
	public boolean launchWSSound = false;
	Image im;

	public void render(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.g.resX, Game.g.resY);
		if(secondes>1f && secondes <3f){
			im = Game.g.images.get("titleGdBProduction").getScaledCopy(0.7f);
			g.drawImage(im, Game.g.resX/3-im.getWidth()/2, Game.g.resY*3/4-im.getHeight()/2);
		}
		if(secondes>3f && secondes <5f){
			im = Game.g.images.get("titleWelcomeStudio").getScaledCopy(0.7f);
			g.drawImage(im, Game.g.resX*2/3-im.getWidth()/2, Game.g.resY*3/4-im.getHeight()/2);
		}
	}
	
	public void update(GameContainer gc){
		this.end();
		if(launchMusic==false && secondes>6f){
			launchMusic = true;
			Game.g.musics.get("musicIntroMovie").play();
		}
		if(launchGdBSound==false && secondes>1f){
			launchGdBSound = true;
			Game.g.sounds.get("gdb_intro").play();
		}
		time+=1f;
		secondes = time / Main.framerate;
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			this.end();
		}
	}
	
	public void end(){
		Game.g.introMovieEnd = true;
	}
}
