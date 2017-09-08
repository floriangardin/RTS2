package system;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import control.InputObject;
import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import display.Camera;
import menu.Credits;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuKeyMapping;
import menu.MenuMapChoice;
import menu.MenuMulti;
import menu.MenuOptions;
import model.Game;
import model.WholeGame;
import multiplaying.ChatHandler;
import nature.Tree;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import ressources.Map;
import ressources.Musics;
import ressources.Taunts;
import utils.ObjetsList;
import plateau.Plateau;
import plateau.UnitsEndCondition;
import render.RenderEngine;
import plateau.Character;

public strictfp class MenuSystem extends ClassSystem {

	public Menu currentMenu;

	public MenuIntro menuIntro;
	public MenuMapChoice menuMapChoice;
	public MenuMulti menuMulti;
	public MenuOptions menuOptions;
	public MenuKeyMapping menuKeyMapping;
	public Credits credits;

	public Plateau plateau;

	public MenuSystem(){
		init();
	}


	public void init(){
		menuIntro = new MenuIntro();
		menuMapChoice = new MenuMapChoice();
		menuMulti = new MenuMulti();
		menuOptions = new MenuOptions();
		menuKeyMapping = new MenuKeyMapping();
		credits = new Credits();
		setMenu(MenuNames.MenuIntro);
		plateau = generatePlateau();
		RenderEngine.init(plateau);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		if(RenderEngine.isReady()){
			RenderEngine.renderPlateau(g, plateau, false);
		}
		g.setColor(new Color(0.3f,0.3f,0.6f,0.4f));
		g.fillRect(Game.resX/6, 2*Game.resY/5, 2*Game.resX/3, 5.5f*Game.resY/10);
		currentMenu.draw(g);
		if(currentMenu == menuMapChoice){
			ChatHandler.draw(g);
		}
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		if(currentMenu==menuKeyMapping){
			menuKeyMapping.update(in);
		} else {
			InputObject im = new InputObject(in);
			currentMenu.update(im);
			ChatHandler.action(in, im);
		}
		if(Taunts.isInit()){
			Taunts.update();
		}
		plateau.update();
		if(plateau.getTeamLooser()>0){
			plateau = generatePlateau();
		}
		//this.send();
	}


	public void setMenu(MenuNames m){
		// handle change of menu
		// including the change of music
		Musics.playMusic("themeMenu");
		switch(m){
		case MenuIntro:
			this.currentMenu = menuIntro;
			break;
		case MenuMapChoice:
			this.currentMenu = menuMapChoice;
			break;
		case MenuMulti:
			menuMulti.init();
			this.currentMenu = menuMulti;
			break;
		case MenuKeyMapping:
			this.currentMenu = menuKeyMapping;
			break;
		case MenuOptions:
			this.currentMenu = menuOptions;
			break;
		case Credits:
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
		MenuMulti, 
		MenuKeyMapping;
	}

	public Plateau generatePlateau(){
		Plateau plateau = new Plateau((int)(20*Map.stepGrid), (int)(11*Map.stepGrid));
		Vector<Vector<Case>> g = plateau.getMapGrid().grid;
		float x, y;
		Vector<ObjetsList> v = ObjetsList.getUnits();
		ObjetsList ol;
		int i = 0;
		for(Case c : plateau.getMapGrid().idcases.values()){
			if(c.i==0 || c.j==0 || c.i==plateau.getMapGrid().grid.size()-1 || c.j==plateau.getMapGrid().grid.get(0).size()-1){
				c.setIdTerrain(IdTerrain.WATER);
			}
		}
		while(StrictMath.random()>0.04){
			Case c = g.get((int)(StrictMath.random()*g.size())).get((int)(StrictMath.random()*g.get(0).size()));
			if(c.getIdTerrain()!=IdTerrain.WATER){
				c.setIdTerrain(IdTerrain.SAND);
			}
		}
		i = 0;
		while(StrictMath.random()>0.02 && i<20){
			do{
				x = (float)(StrictMath.random()*plateau.getMaxX());
				y = (float)(StrictMath.random()*plateau.getMaxY());
			} while(plateau.getMapGrid().getCase(x,y).getIdTerrain()==IdTerrain.WATER);
			plateau.addNaturalObjets(new Tree(x, y, (int)(StrictMath.random()*2)+1, plateau));
			i++;
		}
		Character c;
		i = 0;
		while(StrictMath.random()>0.02 && i<50){
			do{
				x = (float)(StrictMath.random()*plateau.getMaxX());
				y = (float)(StrictMath.random()*plateau.getMaxY());
			} while(plateau.getMapGrid().getCase(x,y).getIdTerrain()==IdTerrain.WATER);
			do{
				ol = v.get((int)(StrictMath.random()*v.size()));
			} while(ol==ObjetsList.Priest || ol==ObjetsList.Inquisitor);
			c = new Character(x,y,ol,plateau.getTeams().get((int)(StrictMath.random()*2)+1),plateau);
			c.attributsChanges.add(new AttributsChange(Attributs.sight, Change.MUL, 4f, false));
			plateau.addCharacterObjets(c);
			i++;
		}
		plateau.setEndCondition(new UnitsEndCondition());
		plateau.setRound((int) (WholeGame.nbRoundStart+10));
		plateau.update();
		Camera.init(Game.resX, Game.resY, plateau.getMaxX()/2-1920/2, plateau.getMaxY()/2-1080/2, (int)plateau.getMaxX(), (int)plateau.getMaxY());
		return plateau;
	}
}
