package ressources;

import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;

import model.Game;


public class Fonts {

	public static UnicodeFont font_main_red;
	public static UnicodeFont font_mid;
	public static UnicodeFont font_big;
	public static UnicodeFont font_number;
	
	public static void init(){
		// init fonts
		font_main_red = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(24*Game.g.resX/1920)));
		font_main_red.addAsciiGlyphs();
		font_main_red.getEffects().add(new ColorEffect(java.awt.Color.red));
		font_main_red.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_main_red.loadGlyphs();} catch (SlickException e) {}
		font_big = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(48*Game.g.resX/1920)));
		font_big.addAsciiGlyphs();
		font_big.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_big.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_big.loadGlyphs();} catch (SlickException e) {}
		font_mid = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(36*Game.g.resX/1920)));
		font_mid.addAsciiGlyphs();
		font_mid.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_mid.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_mid.loadGlyphs();} catch (SlickException e) {}
		font_number = new UnicodeFont(new Font("Candara", Font.BOLD, (int)(36*Game.g.resX/1920)));
		font_number.addAsciiGlyphs();
		font_number.getEffects().add(new ColorEffect(java.awt.Color.white));
		font_number.getEffects().add(new OutlineEffect(1, java.awt.Color.black));
		try {font_number.loadGlyphs();} catch (SlickException e) {}
	}
	
}
