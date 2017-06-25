package system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import control.InputObject;
import menu.Credits;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import multiplaying.ChatHandler;
import ressources.Musics;

public class MenuSystem extends ClassSystem {
	
	public Menu currentMenu;
	
	public MenuIntro menuIntro;
	public MenuMapChoice menuMapChoice;
	public MenuMulti menuMulti;
	public MenuOptions menuOptions;
	public Credits credits;
	
	public MenuSystem(){
		menuIntro = new MenuIntro();
		menuMapChoice = new MenuMapChoice();
		menuMulti = new MenuMulti();
		menuOptions = new MenuOptions();
		credits = new Credits();
		setMenu(MenuNames.MenuIntro);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		currentMenu.draw(g);
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		InputObject im = new InputObject(in);
		if((currentMenu instanceof MenuMapChoice && ((MenuMapChoice) currentMenu).lobby.multiplayer) || currentMenu instanceof MenuMulti){
			ChatHandler.action(in,im);
		}
		currentMenu.update(im);
		//this.send();
	}
	
	
	public void setMenu(MenuNames m){
		// handle change of menu
		// including the change of music
		switch(m){
		case MenuIntro:
			Musics.playMusicFading("themeMenu");
			this.currentMenu = menuIntro;
			break;
		case MenuMapChoice:
			Musics.playMusicFading("themeMulti");
			this.currentMenu = menuMapChoice;
			break;
		case MenuMulti:
			Musics.playMusicFading("themeMulti");
			this.currentMenu = menuMulti;
			break;
		case MenuOptions:
			Musics.playMusicFading("themeMenu");
			this.currentMenu = menuOptions;
			break;
		case Credits:
			Musics.playMusicFading("themeMapEditor");
			this.currentMenu = credits;
		default:
			break;
		}
		this.currentMenu.init();
	}
	
	public enum MenuNames{
		MenuIntro,
		MenuMapChoice,
		MenuOptions,
		Credits,
		MenuMulti;
	}

}
