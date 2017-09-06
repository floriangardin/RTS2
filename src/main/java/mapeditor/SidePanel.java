package mapeditor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pathfinding.Case;
import ressources.Map;

public class SidePanel extends JPanel{

	public static MinimapPanel minimapPanel;
	public static JPanel objectPanel;

	public SidePanel(){
		Dimension d = new Dimension(280,250);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		minimapPanel = new MinimapPanel();
		this.add(minimapPanel);
		objectPanel = new JPanel();
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.add(objectPanel);
	}

	public void update(){
//		groundObjectPanel.update();
		objectPanel.removeAll();
		switch(MainEditor.mode){
		case TERRAIN:
			TerrainObjectPanel.createGroundObjectPanel(objectPanel);
			break;
		case NATURE:
			PlateauObjectPanel.createNaturalObjectPanel(objectPanel);
			break;
		case CHARACTER:
			PlateauObjectPanel.createCharacterPanel(objectPanel);
			break;
		case BUILDING:
			PlateauObjectPanel.createBuildingPanel(objectPanel);
			break;
		case MOVE:
		case SELECT:
		default:
			break;
		}
		MainEditor.frame.repaint();
	}

	public class MinimapPanel extends JPanel{
		int mouseClickX;
		int mouseClickY;
		Stroke dashed;
		float ratio, offsetX, offsetY;
		public MinimapPanel(){
			Dimension d = new Dimension(250,250);
			this.setPreferredSize(d);
			this.setMinimumSize(d);
			this.setMaximumSize(d);
			dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2, new float[]{5}, 0);
			this.addMouseListener(createMouseListener());
			this.addMouseMotionListener(createMouseMotionListener());
		}
		public MouseListener createMouseListener(){
			return new MouseListener(){
				public void mouseClicked(MouseEvent arg0) {}
				public void mouseEntered(MouseEvent arg0) {}
				public void mouseExited(MouseEvent arg0) {}
				public void mousePressed(MouseEvent arg0) {
					if(MainEditor.getSheet()!=null){
						mouseClickX = arg0.getX();
						mouseClickY = arg0.getY();
						System.out.println(arg0.getX());
						MainEditor.getSheet().setOffset((offsetX-arg0.getX())/ratio+SwingMain.getSheetsPane().getWidth()/2, 
								(offsetY-arg0.getY())/ratio+SwingMain.getSheetsPane().getHeight()/2);
						MainEditor.frame.repaint();
					}
				}
				public void mouseReleased(MouseEvent arg0) {}
			};
		}

		public MouseMotionListener createMouseMotionListener(){
			return new MouseMotionListener(){
				public void mouseDragged(MouseEvent arg0) {
					if(MainEditor.getSheet()!=null){
						float x = StrictMath.max(0, StrictMath.min(getWidth(), arg0.getX()));
						float y = StrictMath.max(0, StrictMath.min(getHeight(), arg0.getY()));
						MainEditor.getSheet().setOffset(MainEditor.getSheet().offsetX-(x-mouseClickX)/ratio, 
								MainEditor.getSheet().offsetY-(y-mouseClickY)/ratio);
						mouseClickX = (int)x;
						mouseClickY = (int)y;
						MainEditor.frame.repaint();
					}
				}
				public void mouseMoved(MouseEvent arg0) {

				}
			};
		}


		public void paintComponent(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			RenderingHints rh = new RenderingHints(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHints(rh);
			g2.setColor(Color.black);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());

			// rendering plateau
			if(MainEditor.getSheet()!=null && MainEditor.getSheet().getPlateau()!=null){
				// computing ratios and offsets
				float ratioX = 1f*this.getWidth()/MainEditor.getSheet().getPlateau().maxX;
				float ratioY = 1f*this.getHeight()/MainEditor.getSheet().getPlateau().maxY;
				ratio = 0.01f*(int)(100*StrictMath.min(ratioX, ratioY));
				offsetY = (this.getHeight()-MainEditor.getSheet().getPlateau().maxY*ratio)/2f;
				offsetX = (this.getWidth()-MainEditor.getSheet().getPlateau().maxX*ratio)/2f;
				Color color;
				for(Case c : MainEditor.getSheet().getPlateau().mapGrid.idcases.values()){
					switch(c.getIdTerrain()){
					case GRASS:
						color = new Color(10,70,5);
						break;
					case SAND:
						color = Color.yellow;
						break;
					case WATER:
						color = Color.cyan;
						break;
					default:
						color = Color.black;
						break;
					}
					if(c.building != null){
						color = new Color(c.building.team.color.r,c.building.team.color.g,c.building.team.color.b);
					}
					if(c.naturesObjet.size()>0){
						color = new Color(30,120,10);
					}
					g2.setColor(color);
					g2.fillRect((int)(c.x*ratio+offsetX), (int)(c.y*ratio+offsetY),(int)(Map.stepGrid*ratio),(int)(Map.stepGrid*ratio));
					if(MainEditor.grid){
						g2.setColor(Color.DARK_GRAY);
						g2.drawRect((int)(c.x*ratio+offsetX), (int)(c.y*ratio+offsetY),(int)(Map.stepGrid*ratio),(int)(Map.stepGrid*ratio));
					}
				}
				// painting camera location
				g2.setColor(Color.WHITE);
				g2.setStroke(dashed);
				g2.drawRect((int)(offsetX-MainEditor.getSheet().offsetX*ratio), 
						(int)(offsetY-MainEditor.getSheet().offsetY*ratio),
						(int)(SwingMain.getSheetsPane().getWidth()*ratio),
						(int)(SwingMain.getSheetsPane().getHeight()*ratio));
			}
		}
	}


}
