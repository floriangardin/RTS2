package mapeditor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import data.Attributs;
import mapeditor.Actions.ActionCreateObjet;
import mapeditor.Actions.ActionDeleteObjet;
import mapeditor.Actions.ActionPaintTerrain;
import mapeditor.MainEditor.Mode;
import mapeditor.TerrainObjectPanel.BrushStyle;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.Checkpoint;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.ImagesAwt;
import ressources.Map;
import utils.Utils;

public class SheetPanel extends JPanel {

	private static final long serialVersionUID = 4762952116167579096L;


	int offsetX, offsetY;
	int mouseClickX, mouseClickY;
	boolean mouseIn;
	boolean actionOK;

	public Objet mouseOver;

	private String name;
	private Plateau plateau;
	private File fileToSave = null;

	private HashSet<Case> highlightedCases = new HashSet<Case>();

	private Vector<Action> actionsDone = new Vector<Action>();
	private Vector<Action> actionsToDo = new Vector<Action>();


	Font titleFont = new Font("P22 Morris Golden", Font.PLAIN, 48);
	JTextField titleField;
	Font subtitleFont = new Font("P22 Morris Golden", Font.ITALIC, 32);
	JTextField subtitleField;

	Vector<Integer> currentSharps = new Vector<Integer>();
	Vector<Integer> currentFlats = new Vector<Integer>();


	public SheetPanel(){
		this.name = "New Sheet";
		this.highlightedCases = new HashSet<Case>();
		this.plateau = new Plateau(1500, 800);
		for(float mg = Map.stepGrid; mg<this.plateau.maxX; mg+=Map.stepGrid){
			this.plateau.mapGrid.insertNewX(mg);
		}
		for(float mg = Map.stepGrid; mg<this.plateau.maxY; mg+=Map.stepGrid){
			this.plateau.mapGrid.insertNewY(mg);
		}
		this.addMouseListener(createMouseListener(this));
		this.addMouseMotionListener(createMouseMotionListener(this));
		this.addMouseWheelListener(createMouseWheelListener(this));
	}

	public SheetPanel(String name, int sizeX, int sizeY){
		this.plateau = new Plateau((int)(Map.stepGrid*sizeX), (int)(Map.stepGrid*sizeY));
		for(float mg = Map.stepGrid; mg<this.plateau.maxX; mg+=Map.stepGrid){
			this.plateau.mapGrid.insertNewX(mg);
		}
		for(float mg = Map.stepGrid; mg<this.plateau.maxY; mg+=Map.stepGrid){
			this.plateau.mapGrid.insertNewY(mg);
		}
		this.name = name;
		this.addMouseListener(createMouseListener(this));
		this.addMouseMotionListener(createMouseMotionListener(this));
		this.addMouseWheelListener(createMouseWheelListener(this));
	}

	public SheetPanel(Plateau p, File f){
		this.setFileToSave(f);
		this.plateau = p;
		this.addMouseListener(createMouseListener(this));
		this.addMouseMotionListener(createMouseMotionListener(this));
		this.addMouseWheelListener(createMouseWheelListener(this));
	}


	// Listeners
	public MouseListener createMouseListener(SheetPanel sp){
		return new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {
				mouseIn = true;
				MainEditor.frame.repaint();
			}
			public void mouseExited(MouseEvent arg0) {
				mouseIn = false;
				MainEditor.frame.repaint();
			}
			public void mousePressed(MouseEvent arg0) {
				// if actionOk performAction
				mouseClickX = arg0.getX();
				mouseClickY = arg0.getY();
				if(actionOK){
					switch(MainEditor.mode){
					case MOVE:
						break;
					case TERRAIN:
						for(Case c1 : highlightedCases){
							if(c1!=null && c1.getIdTerrain()!=TerrainObjectPanel.groundStyle){
								System.out.println("vaneau");
								doAction(new ActionPaintTerrain(plateau, highlightedCases, TerrainObjectPanel.groundStyle));
								break;
							}
						}
						break;
					case NATURE:
					case CHARACTER:
						doAction(new ActionCreateObjet(plateau, PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)(mouseClickX-offsetX), (int)(mouseClickY-offsetY)));
						break;
					case BUILDING:
						float sizeX = getPlateau().teams.get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
						float sizeY = getPlateau().teams.get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
						doAction(new ActionCreateObjet(plateau, PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, (int)((mouseClickX-offsetX-sizeX/2+Map.stepGrid/2)/Map.stepGrid), (int)((mouseClickY-offsetY-sizeY/2+Map.stepGrid/2)/Map.stepGrid)));
						break;
					case ERASE:
						if(mouseOver!=null){
							doAction(new ActionDeleteObjet(plateau, mouseOver));
							mouseOver = null;
						}
						break;
					default:
						break;
					}
					MainEditor.frame.repaint();
				}
			}
			public void mouseReleased(MouseEvent arg0) {

			}
		};
	}

	public MouseMotionListener createMouseMotionListener(SheetPanel sp){
		return new MouseMotionListener(){
			public void mouseDragged(MouseEvent arg0) {
				switch(MainEditor.mode){
				case MOVE:
					setOffset(offsetX + arg0.getX() - mouseClickX, offsetY + arg0.getY() - mouseClickY);
					mouseClickX = arg0.getX();
					mouseClickY = arg0.getY();
					break;
				case TERRAIN:
					mouseClickX = arg0.getX();
					mouseClickY = arg0.getY();
					updateHighlightedCases();
					for(Case c1 : highlightedCases){
						if(c1!=null && c1.getIdTerrain()!=TerrainObjectPanel.groundStyle){
							doAction(new ActionPaintTerrain(plateau, highlightedCases, TerrainObjectPanel.groundStyle), true);
							break;
						}
					}
					break;
				default:
					break;
				}
				MainEditor.frame.repaint();
			}
			public void mouseMoved(MouseEvent arg0) {
				mouseClickX = arg0.getX();
				mouseClickY = arg0.getY();
				// update actionOK
				Case c;
				actionOK = false; 
				float min = 60f, d;
				switch(MainEditor.mode){
				case TERRAIN:
					updateHighlightedCases();
					actionOK = highlightedCases.size()>0;
					break;
				case CHARACTER:
					c = getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY);
					actionOK = c!=null && c.ok;
					if(actionOK){
						for(Objet ch : getPlateau().mapGrid.getSurroundingChars(c)){
							d = Utils.distance(ch.x, ch.y, mouseClickX-offsetX, mouseClickY-offsetY);
							if(d<min){
								actionOK = false;
								break;
							}
						}
					}
					break;
				case BUILDING:
					float sizeX = getPlateau().teams.get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeX);
					float sizeY = getPlateau().teams.get(0).data.getAttribut(PlateauObjectPanel.selectedObject, Attributs.sizeY);
					highlightedCases.clear();
					c = getPlateau().mapGrid.getCase(mouseClickX-offsetX-sizeX/2+Map.stepGrid/2, mouseClickY-offsetY-sizeY/2+Map.stepGrid/2);
					actionOK = c!=null;
					if(actionOK){
						for(int i = c.i;i<c.i + (int)(sizeX/Map.stepGrid);i++){
							for(int j = c.j; j<c.j + (int)(sizeY/Map.stepGrid); j++){
								if(i>=0 && i<getPlateau().mapGrid.grid.size() && j>=0 && j<getPlateau().mapGrid.grid.get(0).size()){
									highlightedCases.add(getPlateau().mapGrid.grid.get(i).get(j));
									actionOK = actionOK && getPlateau().mapGrid.grid.get(i).get(j).ok;
								} else {
									actionOK = false;
								}
							}
						}
					}
					break;
				case NATURE:
					c = getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY);
					actionOK = c!=null && (c.ok || c.naturesObjet.size()>0) && c.characters.size()==0;
					if(actionOK){
						for(NaturalObjet ch : getPlateau().mapGrid.getSurroundingNaturalObjet(c)){
							d = Utils.distance(ch.x, ch.y, mouseClickX-offsetX, mouseClickY-offsetY);
							if(d<min){
								actionOK = false;
								break;
							}
						}
					}
					break;
				case ERASE:
					mouseOver = null;
					getPlateau().mapGrid.updateSurroundingChars();
					c = getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY);
					if(c==null){
						break;
					}
					for(Objet ch : getPlateau().mapGrid.getSurroundingChars(c)){
						d = Utils.distance(ch.x, ch.y, mouseClickX-offsetX, mouseClickY-offsetY);
						if(d<min){
							mouseOver = ch;
							actionOK = true;
							min = d;
						}
					}
					for(NaturalObjet ch : c.naturesObjet){
						d = Utils.distance(ch.x, ch.y, mouseClickX-offsetX, mouseClickY-offsetY);
						if(d<min){
							mouseOver = ch;
							actionOK = true;
							min = d;
						}
					}
					break;
				default:
					break;
				}
				MainEditor.frame.repaint();
			}
		};
	}

	public MouseWheelListener createMouseWheelListener(SheetPanel sp){
		return new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int coeff = 30;
				if(e.isControlDown()){
					coeff *= 4;
				}
				if(e.isAltDown()){
					setOffset(offsetX - coeff*e.getWheelRotation(), offsetY);
				} else {
					setOffset(offsetX, offsetY - coeff*e.getWheelRotation());
				}
				MainEditor.frame.repaint();
			}
		};
	}

	public void updateHighlightedCases(){
		highlightedCases.clear();
		if(getPlateau() == null || getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY)==null){
			return;
		}
		Case c = getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY);
		if(!TerrainObjectPanel.groundStyle.ok && (c.characters.size()>0 || c.building!=null || c.naturesObjet.size()>0)){
			return;
		}
		switch(TerrainObjectPanel.brushStyle){
		case FILL:
			highlightedCases.add(getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY));
			IdTerrain idTerrain = getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY).getIdTerrain();
			boolean newCases;
			int i,j;
			do{
				newCases = false;
				Case c1;
				for(Case c2 : getPlateau().mapGrid.idcases.values()){
					if(highlightedCases.contains(c2)){
						for(Integer u : new int[]{-1, +1}){
							for(Integer v : new int[]{-1, +1}){
								i = c2.i+(u-v)/2;
								j = c2.j+(u+v)/2;
								if(i>=0 && j>=0 && i<getPlateau().mapGrid.grid.size() && j<getPlateau().mapGrid.grid.get(0).size()){
									c1 = getPlateau().mapGrid.grid.get(i).get(j);
									if(!highlightedCases.contains(c1) && c1.getIdTerrain()==idTerrain 
											&& (TerrainObjectPanel.groundStyle.ok || (c1.characters.size()==0 && c1.building==null && c1.naturesObjet.size()==0))){
										newCases = true;
										highlightedCases.add(c1);
									}
								}
							}
						}
					}
				}
			} while(newCases);
			break;
		case SIZE1:
			if(c!=null && (TerrainObjectPanel.groundStyle.ok || (c.characters.size()==0 && c.building==null && c.naturesObjet.size()==0))){
				highlightedCases.add(c);
			}
			break;
		case SIZE2:
			for(Case c1 : new Case[]{getPlateau().mapGrid.getCase(mouseClickX-offsetX-Map.stepGrid/2, mouseClickY-offsetY-Map.stepGrid/2),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX-Map.stepGrid/2, mouseClickY-offsetY+Map.stepGrid/2),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX+Map.stepGrid/2, mouseClickY-offsetY-Map.stepGrid/2),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX+Map.stepGrid/2, mouseClickY-offsetY+Map.stepGrid/2)}){
				if(c1!=null && (TerrainObjectPanel.groundStyle.ok || (c1.characters.size()==0 && c1.building==null && c1.naturesObjet.size()==0))){
					highlightedCases.add(c1);
				}
			}
			break;
		case SIZE3:
			for(Case c1 : new Case[]{getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY+Map.stepGrid),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY-Map.stepGrid),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX-Map.stepGrid, mouseClickY-offsetY),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX+Map.stepGrid, mouseClickY-offsetY),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX-Map.stepGrid, mouseClickY-offsetY+Map.stepGrid),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX+Map.stepGrid, mouseClickY-offsetY+Map.stepGrid),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX-Map.stepGrid, mouseClickY-offsetY-Map.stepGrid),
					getPlateau().mapGrid.getCase(mouseClickX-offsetX+Map.stepGrid, mouseClickY-offsetY-Map.stepGrid)
			}){
				if(c1!=null && (TerrainObjectPanel.groundStyle.ok || (c1.characters.size()==0 && c1.building==null && c1.naturesObjet.size()==0))){
					highlightedCases.add(c1);
				}
			}
			break;
		case RECT:
		default:
			break;
		}
		highlightedCases.add(getPlateau().mapGrid.getCase(mouseClickX-offsetX, mouseClickY-offsetY));
		if(TerrainObjectPanel.brushStyle == BrushStyle.SIZE2 || TerrainObjectPanel.brushStyle == BrushStyle.SIZE3){
		}
		if(TerrainObjectPanel.brushStyle == BrushStyle.SIZE3){
		}
	}

	public void setOffset(float offsetX, float offsetY){
		this.offsetX = (int) Math.min(getParent().getSize().getWidth()/2, Math.max(-getPlateau().maxX+getParent().getSize().getWidth()/2,offsetX));
		this.offsetY = (int) Math.min(getParent().getSize().getHeight()/2, Math.max(-getPlateau().maxY+getParent().getSize().getHeight()/2,offsetY));
	}


	// Render method
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHints(rh);
		g2.setColor(Color.black);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		// rendering plateau
		Image im;
		g2.translate(offsetX, offsetY);
		for(Case c : getPlateau().mapGrid.idcases.values()){
			im = null;
			switch(c.getIdTerrain()){
			case GRASS:
				im = ImagesAwt.get("grasstile0");
				break;
			case SAND:
				im = ImagesAwt.get("sandtile0");
				break;
			case WATER:
				im = ImagesAwt.get("watertile0");
				break;
			default:
				break;
			}
			if(im!=null){
				g2.drawImage(im, (int)c.x, (int)c.y, null);
			}
		}
		// rendering mouseOver
		if(mouseOver!=null && (MainEditor.mode==Mode.ERASE)){
			g2.setColor(Color.green);
			g2.drawOval((int)mouseOver.x-30, (int)mouseOver.y-20,60,40);
		}

		// rendering objects
		Vector<Objet> objets = new Vector<Objet>();
		for(Objet o : plateau.objets.values()){
			if(o instanceof Checkpoint){
				continue;
			}
			objets.add(o);
		}
		objets = Utils.triY(objets);
		// rendering plateau
		for(Objet o : objets){
			im = ImagesAwt.getImage(o.name, o.team.id, false);
			if(o.name.type.equals("Building") && getPlateau().teams.get(0).data.getAttribut(o.name, Attributs.newdesign)==0){
				g2.drawImage(im, (int)(o.x-im.getWidth(null)/2), (int)(o.y-im.getHeight(null)+o.getAttribut(Attributs.sizeY)/2), null);
			} else {
				g2.drawImage(im, (int)(o.x-im.getWidth(null)/2), (int)(o.y-im.getHeight(null)*2/3), null);
			}
		}

		// rendering overlay
		for(Case c : getPlateau().mapGrid.idcases.values()){
			// highlighted cases
			if(MainEditor.mode == Mode.TERRAIN && highlightedCases.contains(c) && mouseIn){
				g2.setColor(new Color(255,52,0,105));
				g2.fillRect((int)c.x, (int)c.y, (int)c.sizeX, (int)c.sizeY);
			} else if(MainEditor.mode == Mode.BUILDING && highlightedCases.contains(c) && mouseIn){
				if(actionOK)
					g2.setColor(new Color(52,255,0,105));
				else
					g2.setColor(new Color(255,52,0,105));
				g2.fillRect((int)c.x, (int)c.y, (int)c.sizeX, (int)c.sizeY);
			}
			// collision ok ?
			if(MainEditor.collision && (!c.ok && (MainEditor.mode != Mode.NATURE || c.naturesObjet.size()==0))){
				g2.setColor(Color.red);
				for(int k=0; k<Map.stepGrid*2; k+=10){
					g2.drawLine((int)(c.x+Math.max(0,k-Map.stepGrid)), 
							(int)(c.y+Map.stepGrid+Math.min(0,k-Map.stepGrid)), 
							(int)(c.x+Map.stepGrid+Math.min(0,k-Map.stepGrid)), 
							(int)(c.y+Math.max(0,k-Map.stepGrid)));
				}
			}
			// grid
			if(MainEditor.grid){
				g2.setColor(Color.WHITE);
				g2.drawRect((int)c.x, (int)c.y, (int)c.sizeX, (int)c.sizeY);
			}
		}
		// creation item
		if(MainEditor.mode==Mode.CHARACTER || MainEditor.mode==Mode.NATURE){
			if(actionOK & mouseIn & PlateauObjectPanel.selectedObject!=null){
				im = ImagesAwt.getImage(PlateauObjectPanel.selectedObject, MainEditor.teamSelected.team, false);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2.drawImage(im, (int)(mouseClickX-offsetX-im.getWidth(null)/2), (int)(mouseClickY-offsetY-im.getHeight(null)*2/3), null);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			}
		}
		g2.translate(-offsetX, -offsetY);


		// rules
		g2.setColor(UIManager.getColor( "Panel.background" ));
		g2.fillRect(0, 0, this.getWidth(), 15);
		g2.fillRect(0, 0, 15, this.getHeight());
		g2.setColor(UIManager.getColor( "Panel.foreground" ));
		g2.drawRect(0, 0, this.getWidth(), 15);
		g2.drawRect(0, 0, 15, this.getHeight());
		int i = 0;
		for(Float x : plateau.mapGrid.Xcoord){
			if(x+offsetX>-Map.stepGrid/2+15 && x+offsetX<this.getWidth()){
				if(x+offsetX>15){
					g2.drawLine((int)(x+offsetX), 0, (int)(x+offsetX), 15);
				}
				if(i<plateau.mapGrid.Xcoord.size()-1){
					g2.drawString(""+i, (int)(x+offsetX+Map.stepGrid/2-g2.getFontMetrics().stringWidth(""+i)/2), 13);
				}
			}
			i+=1;
		}
		i = 0;

		g2.rotate(-Math.PI/2);
		for(Float y : plateau.mapGrid.Ycoord){
			if(y+offsetY>-Map.stepGrid/2+15 && y+offsetY<this.getHeight()){
				if(y+offsetY>15){
					g2.drawLine((int)(-y-offsetY), 0, (int)(-y-offsetY), 15);
				}
				if(i<plateau.mapGrid.Ycoord.size()-1){
					g2.drawString(""+i, (int)(-y-offsetY-Map.stepGrid/2-g2.getFontMetrics().stringWidth(""+i)/2), 13);
				}
			}
			i+=1;
		}
		g2.rotate(Math.PI/2);
		g2.setColor(UIManager.getColor( "Panel.background" ));
		g2.fillRect(1, 1, 14, 14);

	}

	// Setters and getters
	public String getName(){
		return this.name;
	}
	public Plateau getPlateau(){
		return plateau;
	}
	public File getFileToSave(){
		return this.fileToSave;
	}
	public void setFileToSave(File newFile){
		this.fileToSave = newFile;
		this.name = newFile.getName().substring(0, newFile.getName().length()-7);
		SwingMain.getSheetsPane().update();
	}

	// Handling the action stack
	public void doAction(Action action){
		doAction(action, false);
	}
	public void doAction(Action action, boolean mergeTry){
		this.actionsToDo.clear();
		if(!mergeTry || this.actionsDone.size()==0 || this.actionsDone.lastElement().type!=action.type){
			this.actionsToDo.add(action);
		} else {
			boolean merged = false;
			switch(action.type){
			case PaintTerrain:
				ActionPaintTerrain lastAction = ((ActionPaintTerrain)actionsDone.lastElement());
				ActionPaintTerrain newAction = ((ActionPaintTerrain)action);
				if(newAction.idTerrain==lastAction.idTerrain){
					newAction.idCases.putAll(lastAction.idCases);
					lastAction.idCases = newAction.idCases;
					merged = true;
				}
				break;
			default:
				break;
			}
			if(!merged){
				this.actionsToDo.add(action);
			} else {
				this.undoLastAction();
			}
		}
		this.redoNextAction();

	}
	public void undoLastAction(){
		if(this.actionsDone.size()>0){
			Action action = this.actionsDone.remove(this.actionsDone.size()-1);
			action.undo();
			this.actionsToDo.insertElementAt(action, 0);
			MainEditor.frame.repaint();
		}
	}
	public void redoNextAction(){
		if(this.actionsToDo.size()>0){
			Action action = this.actionsToDo.remove(0);
			action.redo();
			this.actionsDone.add(action);
			MainEditor.frame.repaint();
		}
	}

}


