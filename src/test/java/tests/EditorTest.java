package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import data.Attributs;
import mapeditor.Actions.ActionCreateObjet;
import mapeditor.Actions.ActionDeleteObjet;
import mapeditor.Actions.ActionMoveObjet;
import mapeditor.Actions.ActionPaintTerrain;
import mapeditor.MainEditor;
import mapeditor.MainEditor.Mode;
import mapeditor.MainEditor.TeamSelected;
import mapeditor.PlateauObjectPanel;
import mapeditor.SheetPanel;
import mapeditor.TerrainObjectPanel;
import mapeditor.TerrainObjectPanel.BrushStyle;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.NaturalObjet;
import plateau.Character;
import ressources.Map;
import utils.ObjetsList;

public strictfp class EditorTest {

	/**
	 * 	Terrain
	 */
	@Test
	public void testTerrainVide() {
		SheetPanel sp = new SheetPanel();
		byte[] data = sp.getPlateauBytes();
		HashSet<Case> cases = new HashSet<Case>();
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.GRASS));
		assertTrue(Arrays.equals(data, sp.getPlateauBytes()));
	}
	@Test
	public void testTerrainMemeCouleur() {
		SheetPanel sp = new SheetPanel();
		byte[] data = sp.getPlateauBytes();
		HashSet<Case> cases = new HashSet<Case>();
		cases.add((Case)sp.getPlateau().getMapGrid().idcases.values().toArray()[0]);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.GRASS));
		assertTrue(Arrays.equals(data, sp.getPlateauBytes()));
	}
	@Test
	public void testTerrainDifferenteCouleur() {
		SheetPanel sp = new SheetPanel();
		byte[] data = sp.getPlateauBytes();
		HashSet<Case> cases = new HashSet<Case>();
		Case c = (Case)sp.getPlateau().getMapGrid().idcases.values().toArray()[0];
		cases.add(c);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.SAND));
		assertFalse(Arrays.equals(data, sp.getPlateauBytes()));
		assertEquals(c.getIdTerrain(), IdTerrain.SAND);
	}
	@Test
	public void testTerrainTotal() {
		SheetPanel sp = new SheetPanel();
		byte[] data = sp.getPlateauBytes();
		HashSet<Case> cases = new HashSet<Case>();
		Case c = (Case)sp.getPlateau().getMapGrid().idcases.values().toArray()[0];
		cases.addAll(sp.getPlateau().getMapGrid().idcases.values());
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.SAND));
		assertFalse(Arrays.equals(data, sp.getPlateauBytes()));
		assertEquals(c.getIdTerrain(), IdTerrain.SAND);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.GRASS));
		assertTrue(Arrays.equals(data, sp.getPlateauBytes()));
	}
	@Test
	public void testTerrainUndoRedo() {
		SheetPanel sp = new SheetPanel();
		byte[] data = sp.getPlateauBytes();
		HashSet<Case> cases = new HashSet<Case>();
		cases.add((Case)sp.getPlateau().getMapGrid().idcases.values().toArray()[0]);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), cases, IdTerrain.SAND));
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		assertTrue(Arrays.equals(data, sp.getPlateauBytes()));
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testTerrainClickSourisBrushSize1() {
		SheetPanel sp = new SheetPanel();
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE1;
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==1);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		sp.mouseClickY = (int) (10+Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==1);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
	}
	@Test
	public void testTerrainClickSourisBrushSize2() {
		SheetPanel sp = new SheetPanel();
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE2;
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==1);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		sp.mouseClickY = (int) (10+Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==2);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.WATER);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
		sp.mouseClickX = (int) (10+Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==4);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.SAND);
	}
	@Test
	public void testTerrainClickSourisBrushSize3() {
		SheetPanel sp = new SheetPanel();
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE3;
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==4);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.SAND);
		sp.mouseClickY = (int) (10+2*Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==6);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
		sp.mouseClickX = (int) (10+Map.stepGrid);
		sp.mouseClickY = (int) (10+Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.GRASS;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==9);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.GRASS);
	}
	@Test
	public void testTerrainClickSourisBrushFill() {
		SheetPanel sp = new SheetPanel();
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE3;
		sp.mouseClickX = 10;
		sp.mouseClickY = (int) (10+2*Map.stepGrid);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateHighlightedCases();
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		sp.mouseClickY = 10;
		sp.mouseClickX = (int) (10+2*Map.stepGrid);
		sp.updateHighlightedCases();
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		TerrainObjectPanel.brushStyle = BrushStyle.FILL;
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==1);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10).getIdTerrain(), IdTerrain.WATER);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10+Map.stepGrid).getIdTerrain(), IdTerrain.WATER);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==1);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.WATER);
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.updateHighlightedCases();
		assertTrue(sp.highlightedCases.size()==12);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		assertEquals(sp.getPlateau().getMapGrid().getCase(10, 10).getIdTerrain(), IdTerrain.SAND);
	}
	/**
	 *  Nature objects
	 */
	@Test
	public void testNatureObjectUndoRedo() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertEquals(c.naturesObjet.size(), 1);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testNatureObjectCollision() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = 15;
		sp.mouseClickY = 15;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
	}
	@Test
	public void testNatureObjectCollisionWater() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.TERRAIN;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE1;
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
	}
	@Test
	public void testNatureObjectErase() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertEquals(c.naturesObjet.size(), 1);
		MainEditor.mode = Mode.ERASE;
		sp.updateMouseMove();
		assertTrue(sp.mouseOver!=null);
		sp.doAction(new ActionDeleteObjet(sp.getPlateau(), sp.mouseOver));
		assertEquals(c.naturesObjet.size(), 0);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		assertEquals(c.naturesObjet.size(), 1);
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testNatureObjetMove(){
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		Case c1 = sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid);
		NaturalObjet no = (NaturalObjet) c.naturesObjet.toArray()[0];
		sp.doAction(new ActionMoveObjet(sp.getPlateau(), no, 10+Map.stepGrid, 10+Map.stepGrid));
		assertEquals(c.naturesObjet.size(), 0);
		assertEquals(c1.naturesObjet.size(), 1);
		sp.undoLastAction();
		assertEquals(c.naturesObjet.size(), 1);
		assertEquals(c1.naturesObjet.size(), 0);
		sp.redoNextAction();
		assertEquals(c.naturesObjet.size(), 0);
		assertEquals(c1.naturesObjet.size(), 1);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	/*
	 * Units
	 */
	@Test
	public void testUnitUndoRedo() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertEquals(c.characters.size(), 1);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testUnitCollision() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = 15;
		sp.mouseClickY = 15;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
	}
	@Test
	public void testUnitCollisionWater() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.TERRAIN;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE1;
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
	}
	@Test
	public void testUnitCollisionNatureObject() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = 15;
		sp.mouseClickY = 15;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid-5);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		sp.mouseClickX = (int) (Map.stepGrid-5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (Map.stepGrid+5);
		sp.mouseClickY = (int) (Map.stepGrid+5);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
	}
	@Test
	public void testUnitErase() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertEquals(c.characters.size(), 1);
		MainEditor.mode = Mode.ERASE;
		sp.updateMouseMove();
		assertTrue(sp.mouseOver!=null);
		sp.doAction(new ActionDeleteObjet(sp.getPlateau(), sp.mouseOver));
		assertEquals(c.characters.size(), 0);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		assertEquals(c.characters.size(), 1);
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testUnitMove(){
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Crossbowman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		Case c1 = sp.getPlateau().getMapGrid().getCase(10+Map.stepGrid, 10+Map.stepGrid);
		Character no = (Character) c.characters.toArray()[0];
		sp.doAction(new ActionMoveObjet(sp.getPlateau(), no, 10+Map.stepGrid, 10+Map.stepGrid));
		assertEquals(c.characters.size(), 0);
		assertEquals(c1.characters.size(), 1);
		sp.undoLastAction();
		assertEquals(c.characters.size(), 1);
		assertEquals(c1.characters.size(), 0);
		sp.redoNextAction();
		assertEquals(c.characters.size(), 0);
		assertEquals(c1.characters.size(), 1);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	/*
	 * Buildings
	 */
	@Test
	public void testBuildingUndoRedo() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertTrue(c.building!=null);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		assertTrue(c.building==null);
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
		Building b = c.building;
		assertTrue(sp.getPlateau().getMapGrid().getCase(Map.stepGrid+1, Map.stepGrid+1).building==b);
	}
	@Test
	public void testBuildingCollision() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (3*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
	}
	@Test
	public void testBuildingCollisionUnit() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (3*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
	}
	@Test
	public void testBuildingCollisionNatureObject() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (3*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
	}
	@Test
	public void testBuildingCollisionWater() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.TERRAIN;
		TerrainObjectPanel.brushStyle = BrushStyle.SIZE1;
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertFalse(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.mouseClickX = (int) (3*Map.stepGrid+1);
		sp.mouseClickY = (int) (3*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
	}
	@Test
	public void testBuildingErase() {
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		assertTrue(c.building!=null);
		MainEditor.mode = Mode.ERASE;
		sp.updateMouseMove();
		assertTrue(sp.mouseOver!=null);
		sp.doAction(new ActionDeleteObjet(sp.getPlateau(), sp.mouseOver));
		assertTrue(c.building==null);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		assertTrue(c.building!=null);
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testBuildingMove(){
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		Case c1 = sp.getPlateau().getMapGrid().getCase(10, 2*Map.stepGrid+10);
		Building b = c.building;
		sp.doAction(new ActionMoveObjet(sp.getPlateau(), b, 3*Map.stepGrid/2+1, 3*Map.stepGrid+1));
		assertEquals(c.building, null);
		assertEquals(c1.building, b);
		sp.undoLastAction();
		assertEquals(c.building, b);
		assertEquals(c1.building, null);
		sp.redoNextAction();
		assertEquals(c.building, null);
		assertEquals(c1.building, b);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	@Test
	public void testBuildingMoveOnOldPosition(){
		SheetPanel sp = new SheetPanel();
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (3*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		Case c = sp.getPlateau().getMapGrid().getCase(10, 10);
		Case c1 = sp.getPlateau().getMapGrid().getCase(10, 1*Map.stepGrid+10);
		Case c2 = sp.getPlateau().getMapGrid().getCase(10, 2*Map.stepGrid+10);
		Building b = c.building;
		sp.doAction(new ActionMoveObjet(sp.getPlateau(), b, 3*Map.stepGrid/2+1, 2*Map.stepGrid+1));
		assertEquals(c.building, null);
		assertEquals(c1.building, b);
		assertEquals(c2.building, b);
		sp.undoLastAction();
		assertEquals(c.building, b);
		assertEquals(c1.building, b);
		assertEquals(c2.building, null);
		sp.redoNextAction();
		assertEquals(c.building, null);
		assertEquals(c1.building, b);
		assertEquals(c2.building, b);
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
	}
	/**
	 * Interactions between terrain, nature, unit and building
	 */
	@Test
	public void testFillWater(){
		SheetPanel sp = new SheetPanel("vaneau", 10, 10);
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = (int) (4*Map.stepGrid+10);
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (5*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (2*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		MainEditor.mode = Mode.TERRAIN;
		TerrainObjectPanel.brushStyle = BrushStyle.FILL;
		TerrainObjectPanel.groundStyle = IdTerrain.SAND;
		sp.mouseClickX = (int) (5*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (1);
		sp.updateMouseMove();
		assertEquals(sp.highlightedCases.size(),100);
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.updateMouseMove();
		assertEquals(sp.highlightedCases.size(),3);
		sp.mouseClickX = (int) (5*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (5*Map.stepGrid);
		sp.updateMouseMove();
		assertEquals(sp.highlightedCases.size(),89);
		sp.undoLastAction();
		sp.undoLastAction();
		sp.undoLastAction();
		sp.updateMouseMove();
		assertEquals(sp.highlightedCases.size(),100);
	}
	@Test
	public void testCompleteUndoRedo(){
		SheetPanel sp = new SheetPanel("vaneau", 10, 10);
		MainEditor.mode = Mode.CHARACTER;
		PlateauObjectPanel.selectedObject = ObjetsList.Spearman;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = 10;
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.NATURE;
		PlateauObjectPanel.selectedObject = ObjetsList.Tree00;
		MainEditor.teamSelected = TeamSelected.Neutral;
		sp.mouseClickX = (int) (4*Map.stepGrid+10);
		sp.mouseClickY = 10;
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(sp.mouseClickX-sp.offsetX), (int)(sp.mouseClickY-sp.offsetY)));
		MainEditor.mode = Mode.BUILDING;
		PlateauObjectPanel.selectedObject = ObjetsList.Mill;
		MainEditor.teamSelected = TeamSelected.Team1;
		sp.mouseClickX = (int) (5*Map.stepGrid/2+1);
		sp.mouseClickY = (int) (2*Map.stepGrid+1);
		sp.updateMouseMove();
		assertTrue(sp.actionOK);
		float sizeX = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
		float sizeY = sp.getPlateau().getTeams().get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
		sp.doAction(new ActionCreateObjet(sp.getPlateau(), PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((sp.mouseClickX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((sp.mouseClickY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
		MainEditor.mode = Mode.TERRAIN;
		TerrainObjectPanel.brushStyle = BrushStyle.FILL;
		TerrainObjectPanel.groundStyle = IdTerrain.WATER;
		sp.mouseClickY = (int) (5*Map.stepGrid+1);
		sp.updateMouseMove();
		sp.doAction(new ActionPaintTerrain(sp.getPlateau(), sp.highlightedCases, TerrainObjectPanel.groundStyle));
		byte[] data2 = sp.getPlateauBytes();
		sp.undoLastAction();
		sp.undoLastAction();
		sp.undoLastAction();
		sp.undoLastAction();
		sp.redoNextAction();
		sp.redoNextAction();
		sp.redoNextAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
		sp.undoLastAction();
		sp.undoLastAction();
		sp.undoLastAction();
		sp.redoNextAction();
		sp.redoNextAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
		sp.undoLastAction();
		sp.undoLastAction();
		sp.redoNextAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
		sp.undoLastAction();
		sp.redoNextAction();
		assertTrue(Arrays.equals(data2, sp.getPlateauBytes()));
		
	}

	
}
