package mapeditor;

import java.io.File;
import java.util.Vector;

import javax.swing.JFrame;

import plateau.Plateau;
import ressources.Icones;
import ressources.Images;
import ressources.ImagesAwt;

public strictfp class MainEditor {
	
	public static Images images = new Images();
	public static SwingMain frame;
	public static Mode mode = Mode.MOVE;
	
	public static boolean collision = true;
	public static boolean grid = true;
	public static TeamSelected teamSelected = TeamSelected.Neutral;
	private static Vector<SheetPanel> sheets;
	
	public static void main(String[] args) {
		try {
			sheets = new Vector<SheetPanel>();
			Icones.init();
			ImagesAwt.init();
			frame = new SwingMain();
			SwingMain.getSheetsPane().update();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static SheetPanel getSheet(){
		return ((SheetPanel) (SwingMain.getSheetsPane().getSelectedComponent()));
	}

	public static Vector<SheetPanel> getSheets() {
		return sheets;
	}

	public static void createNewSheet(String title, int sizeX, int sizeY) {
		sheets.add(new SheetPanel(title, sizeX, sizeY));
		SwingMain.getSheetsPane().update();
	}
	
	public static void openSheet(Plateau p, File fileToSave) {
		sheets.add(new SheetPanel(p, fileToSave));
		SwingMain.getSheetsPane().update();
	}
	
	public static void deleteSheet(int delete_sheet_id) {
		if(delete_sheet_id<sheets.size()){
			sheets.remove(delete_sheet_id);
		}
		SwingMain.getSheetsPane().update();
	}
	
	public enum Mode{
		SELECT, MOVE, ERASE, TERRAIN, NATURE, CHARACTER, BUILDING, VICTORY;
	}
	
	public enum TeamSelected{
		Neutral(0, "Neutral"),
		Team1(1, "Blue"), 
		Team2(2, "Red");
		
		public int team;
		public String text;
		private TeamSelected(int team, String text){
			this.team = team;
			this.text = text;
		}
		
		public static String getText(int i){
			for(TeamSelected ts : values()){
				if(ts.team==i){
					return ts.text;
				}
			}
			return "";
		}
	}
	
	
}
