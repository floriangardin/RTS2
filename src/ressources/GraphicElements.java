package ressources;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;
import org.newdawn.slick.font.effects.ShadowEffect;

import model.Game;


public strictfp class GraphicElements {

	// Fonts
	public static UnicodeFont font_red;
	public static UnicodeFont font_main;
	public static UnicodeFont font_mid;
	public static UnicodeFont font_big;
	public static UnicodeFont font_number;
	
	public static UnicodeFont font_menu_button;
	public static UnicodeFont font_menu_button_selected;
	public static UnicodeFont font_menu_small_button;
	public static UnicodeFont font_menu_small_button_selected;
	
	// Cursors
	public static Image attackCursor ; 
	public static Image cursor;
	public static Image currentCursor;
	
	
	// Draw fog of war
	public static Image imageFogOfWar;
	public static Graphics graphicFogOfWar;
	
	
	


	
	@SuppressWarnings("unchecked")
	public static void init(){
		// fonts
		font_red = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(24*Game.resX/1920)));
		font_red.addAsciiGlyphs();
		font_red.getEffects().add(new ColorEffect(java.awt.Color.red));
		font_red.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_red.loadGlyphs();} catch (SlickException e) {}
		font_big = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(48*Game.resX/1920)));
		font_big.addAsciiGlyphs();
		font_big.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_big.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_big.loadGlyphs();} catch (SlickException e) {}
		font_mid = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(36*Game.resX/1920)));
		font_mid.addAsciiGlyphs();
		font_mid.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_mid.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_mid.loadGlyphs();} catch (SlickException e) {}
		font_number = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(36*Game.resX/1920)));
		font_number.addAsciiGlyphs();
		font_number.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_number.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_number.loadGlyphs();} catch (SlickException e) {}
		font_main = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(25*Game.resX/1920)));
		font_main.addAsciiGlyphs();
		font_main.getEffects().add(new ColorEffect(java.awt.Color.white));
		try {font_main.loadGlyphs();} catch (SlickException e) {}
		// buttons
		font_menu_button = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(48*Game.resX/1920)));
		font_menu_button.addAsciiGlyphs();
		font_menu_button.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_menu_button.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_menu_button.loadGlyphs();} catch (SlickException e) {}
		font_menu_button_selected = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(55*Game.resX/1920)));
		font_menu_button_selected.addAsciiGlyphs();
		font_menu_button_selected.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_menu_button_selected.getEffects().add(new OutlineEffect(3, java.awt.Color.white));
		font_menu_button_selected.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_menu_button_selected.loadGlyphs();} catch (SlickException e) {}
		// small buttons
		font_menu_small_button = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(28*Game.resX/1920)));
		font_menu_small_button.addAsciiGlyphs();
		font_menu_small_button.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_menu_small_button.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_menu_small_button.loadGlyphs();} catch (SlickException e) {}
		font_menu_small_button_selected = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(34*Game.resX/1920)));
		font_menu_small_button_selected.addAsciiGlyphs();
		font_menu_small_button_selected.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_menu_small_button_selected.getEffects().add(new OutlineEffect(3, java.awt.Color.white));
		font_menu_small_button_selected.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_menu_small_button_selected.loadGlyphs();} catch (SlickException e) {}
		
		// cursors
		try {
			cursor = new Image("ressources/images/cursor.png").getSubImage(0, 0, 24, 64);	
			Game.app.setMouseCursor(cursor,5,16);
			imageFogOfWar = new Image(Game.resX,Game.resY);
			graphicFogOfWar = imageFogOfWar.getGraphics();
		} catch (SlickException e) {}
		
	}



	public static boolean isInit() {
		// TODO Auto-generated method stub
		return font_main!=null;
	}
	
	
}
