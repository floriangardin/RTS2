package mapeditor;

import javax.swing.ImageIcon;

import main.Main;
import ressources.Icones;

public enum ActionType {
	
	// Actions - File Menu
	CreateNewSheet("iconNewFile2", "New File"),
	SaveCurrentSheet("iconSaveFile2", "Save File"),
	SaveCurrentSheetAs("iconSaveFile2", "Save File As"),
	OpenNewSheet("iconOpenFile2", "Open File"),
	ExitSoftware("iconExit2", "Exit"), 
	DeleteSheet("", "Delete Sheet"), 
	// Actions - Edition Menu
	CopyElement("iconCopy", "Copy"), 
	PasteElement("iconPaste", "Paste"), 
	Undo("iconUndo2", "Undo"), 
	Redo("iconRedo2", "Redo"),
	// Actions - Tools
	SwitchGrid("iconGridOn", "Grid"), 
	SwitchCollision("iconCollisionOn","Collision"), 
	SelectTeam0("iconTeam0","Select Team 0"), 
	SelectTeam1("iconTeam1","Select Team 1"),  
	SelectTeam2("iconTeam2","Select Team 2"),
	SelectToolMove("iconMoveTool","Tool Move"),
	SelectToolErase("iconEraseTool","Tool Erase"),
	SelectToolTerrain("iconGroundTool","Tool Terrain"),
	SelectToolNature("iconNatureTool","Tool Nature"),
	SelectToolCharacter("iconCharacterTool","Tool Character"),
	SelectToolBuilding("iconBuildingTool","Tool Building"),
	// Ground Tool Actions
	SelectBrushSize1("iconBrushSize1","Brush Size 1"),
	SelectBrushSize2("iconBrushSize2","Brush Size 2"),
	SelectBrushSize3("iconBrushSize3","Brush Size 3"),
	SelectBrushFill("iconBrushFill","Brush Fill"),
	SelectGroundSand("iconGroundSand","Terrain Sand"),
	SelectGroundGrass("iconGroundGrass","Terrain Grass"),
	SelectGroundWater("iconGroundWater","Terrain Water"), 
	// Actions with non generic effects
	CreateObjet, 
	PaintTerrain, 
	;

	private ActionType(String iconname, String name){
		this.icone = Icones.getIcone(iconname);
		this.name = name;
	}
	private ActionType(String iconname, String name, int mode){
		this.icone = Icones.getIcone(iconname);
		this.name = name;
		this.selectionMode = mode;
	}
	private ActionType(){
		this.name = this.name();
		this.icone = Icones.getIcone("default");
	}
	
	ImageIcon icone = null;	
	String name = null;
	int selectionMode = 0;
	
	public ImageIcon getIcone() {
		return icone;
	}
	public String getName() {
		return name;
	}
	
}
