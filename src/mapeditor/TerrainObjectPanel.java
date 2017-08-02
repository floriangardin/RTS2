package mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import pathfinding.Case.IdTerrain;
import ressources.Icones;


public class TerrainObjectPanel extends JPanel {
	
	public static BrushStyle brushStyle = BrushStyle.SIZE1;
	public static IdTerrain groundStyle = IdTerrain.GRASS;
	
	public static Dimension buttonDimension = new Dimension(24,24);
	
	static Vector<JButton> boutons = new Vector<JButton>();
	
	public static void createGroundObjectPanel(JPanel panel){
		
		JLabel labelBrushStyle = new JLabel("Brush style");
		labelBrushStyle.setMinimumSize(new Dimension(1,30));
		JLabel labelGroundStyle = new JLabel("Terrain type");
		labelGroundStyle.setMinimumSize(new Dimension(1,30));
		JToolBar barBrush = new JToolBar();
		barBrush.setFloatable(false);
		barBrush.setRollover(true);
		Vector<ActionType> items;
		items = new Vector<ActionType>();
		items.add(ActionType.SelectBrushSize1);
		items.add(ActionType.SelectBrushSize2);
		items.add(ActionType.SelectBrushSize3);
		items.add(ActionType.SelectBrushFill);
		addItems(barBrush, items);
		JToolBar barGround = new JToolBar();
		barGround.setFloatable(false);
		barGround.setRollover(true);
		items = new Vector<ActionType>();
		items.add(ActionType.SelectGroundGrass);
		items.add(ActionType.SelectGroundSand);
		items.add(ActionType.SelectGroundWater);
		addItems(barGround, items);

		barBrush.setOpaque(false);
		barBrush.setBorder(null);
		
		barGround.setOpaque(false);
		barGround.setBorder(null);
		GroupLayout gl = new GroupLayout(panel);
		panel.setLayout(gl);
		gl.setHorizontalGroup(gl.createParallelGroup()
	            .addComponent(labelBrushStyle, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(barBrush, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(labelGroundStyle, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(barGround, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    );
	    gl.setVerticalGroup(gl.createSequentialGroup()
	            .addComponent(labelBrushStyle)
	            .addComponent(barBrush)
	            .addComponent(labelGroundStyle)
	            .addComponent(barGround)
	    );
	    update();
	}
	
	private static void addItems(JToolBar bar, Vector<ActionType> items){
		for(ActionType type: items){
			ImageIcon icon = new ImageIcon(type.getIcone().getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH));
			JButton bouton = new JButton(icon);
			bouton.setPreferredSize(buttonDimension);
			bouton.setToolTipText(type.getName());
			bouton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Action.redoAction(type);
				}
			});
			bar.add(bouton);
			boutons.add(bouton);
			bar.addSeparator();
		}
	}

	
	
	public enum BrushStyle{
		SIZE1, SIZE2, SIZE3, RECT, FILL;
	}


	public static void update() {
		for(JButton button : boutons){
			if(button.getToolTipText().startsWith("Brush")){
				button.setBackground(button.getToolTipText().substring(6).replace(" ", "").toUpperCase().equals(""+brushStyle.name()) ? Color.CYAN : UIManager.getColor("control"));
			} else if(button.getToolTipText().startsWith("Terrain")){
				button.setBackground(button.getToolTipText().substring(8).replace(" ", "").toUpperCase().equals(""+groundStyle.name()) ? Color.CYAN : UIManager.getColor("control"));
			}
			button.setPreferredSize(buttonDimension);
		}
	}

}
