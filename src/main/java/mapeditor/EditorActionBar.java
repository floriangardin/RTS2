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
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import ressources.Icones;



public class EditorActionBar extends JPanel{

	public static Dimension buttonDimension = new Dimension(24,24);
	public static JToolBar bar1, bar2;

	Vector<JButton> boutons = new Vector<JButton>();

	public EditorActionBar(JPanel pan){
		super();
		bar1 = new JToolBar();
		bar1.setFloatable(false);
		bar1.setRollover(true);
		bar1.setBorder(null);
		bar1.setOpaque(false);

		// Bar 1 
		Vector<ActionType> items;
		items = new Vector<ActionType>();
		items.add(ActionType.CreateNewSheet);
		items.add(ActionType.OpenNewSheet);
		items.add(ActionType.SaveCurrentSheet);
		addItems(bar1, items);
		items = new Vector<ActionType>();
		items.add(ActionType.Redo);
		items.add(ActionType.Undo);
		addItems(bar1, items);
		items = new Vector<ActionType>();
		items.add(ActionType.SwitchGrid);
		items.add(ActionType.SwitchCollision);
		addItems(bar1, items);
		items = new Vector<ActionType>();
		items.add(ActionType.SelectTeam0);
		items.add(ActionType.SelectTeam1);
		items.add(ActionType.SelectTeam2);
		addItems(bar1, items);
		items = new Vector<ActionType>();
		items.add(ActionType.Test);
		addItems(bar1, items);
		
		bar2 = new JToolBar();
		bar2.setFloatable(false);
		bar2.setRollover(true);
		bar2.setBorder(null);
		bar2.setOpaque(false);
		items = new Vector<ActionType>();
		items.add(ActionType.SelectToolMove);
		items.add(ActionType.SelectToolSelect);
		items.add(ActionType.SelectToolErase);
		addItems(bar2, items);
		items = new Vector<ActionType>();
		items.add(ActionType.SelectToolTerrain);
		addItems(bar2, items);
		items = new Vector<ActionType>();
		items.add(ActionType.SelectToolNature);
		items.add(ActionType.SelectToolCharacter);
		items.add(ActionType.SelectToolBuilding);
		addItems(bar2, items);
		
		GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);
		gl.setHorizontalGroup(gl.createParallelGroup()
	            .addComponent(bar1, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	            .addComponent(bar2, GroupLayout.DEFAULT_SIZE, 
	                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	    );
	    gl.setVerticalGroup(gl.createSequentialGroup()
	            .addComponent(bar1)
	            .addComponent(bar2)
	    );
	    bar1.setBackground(UIManager.getColor( "Panel.background" ));
	    bar2.setBackground(UIManager.getColor( "Panel.background" ));
		update();
		
	}

	private void addItems(JToolBar bar, Vector<ActionType> items){
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
		}
		bar.addSeparator();
	}

	public void update(){
		for(JButton button : boutons){
			if(button.getToolTipText().equals("Grid")){
				if(MainEditor.grid){
					button.setIcon(new ImageIcon(Icones.getIcone("iconGridOn").getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH)));
				} else {
					button.setIcon(new ImageIcon(Icones.getIcone("iconGridOff").getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH)));
				}
			} else if(button.getToolTipText().equals("Collision")){
				if(MainEditor.collision){
					button.setIcon(new ImageIcon(Icones.getIcone("iconCollisionOn").getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH)));
				} else {
					button.setIcon(new ImageIcon(Icones.getIcone("iconCollisionOff").getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH)));
				}
			} else if(button.getToolTipText().startsWith("Select Team")){
				button.setBackground(button.getToolTipText().substring(12).equals(""+MainEditor.teamSelected.team) ? Color.CYAN : UIManager.getColor("control"));
			} else if(button.getToolTipText().startsWith("Tool")){
				button.setBackground(button.getToolTipText().toUpperCase().endsWith(MainEditor.mode.name()) ? Color.CYAN : UIManager.getColor("control"));
			}
			button.setPreferredSize(buttonDimension);
		}
	}

}
