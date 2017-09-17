package mapeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import plateau.EndCondition;
import plateau.EndConditionBuilding;
import ressources.Icones;
import ressources.ImagesAwt;
import utils.ObjetType;
import utils.ObjetsList;


public strictfp class PlateauObjectPanel extends JPanel {

	public static Dimension buttonDimension = new Dimension(72,72);
	public static Dimension smallButtonDimension = new Dimension(24,24);
	public static Dimension listDimension = new Dimension(200,120);

	static Vector<JButton> boutons = new Vector<JButton>();

	public static ObjetsList selectedObject;

	private static void createPlateauObjectPanel(JPanel panel, Vector<ObjetsList> objets){

		Vector<JToolBar> vectors = new Vector<JToolBar>();
		int nbPerLine = 4;
		int index = 0;
		for(ObjetsList o : objets){
			if(index%nbPerLine==0){
				vectors.add(new JToolBar());
				vectors.lastElement().setOpaque(false);
				vectors.lastElement().setBorder(null);
			}
			ImageIcon icon = new ImageIcon(ImagesAwt.getImage(o, MainEditor.teamSelected.team, true));
			JButton bouton = new JButton(icon);
			bouton.setPreferredSize(buttonDimension);
			bouton.setMinimumSize(buttonDimension);
			bouton.setMaximumSize(buttonDimension);
			bouton.setToolTipText(o.name());
			bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
			bouton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedObject = o;
					update();
				}
			});
			vectors.lastElement().add(bouton);
			boutons.add(bouton);
			index += 1;
		}
		for(int i=index ; i%nbPerLine!=0; i++){
			JButton bouton = new JButton();
			bouton.setPreferredSize(buttonDimension);
			bouton.setMinimumSize(buttonDimension);
			bouton.setMaximumSize(buttonDimension);
			bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
			vectors.lastElement().add(bouton);
		}
		BoxLayout bl = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(bl);
		panel.add(Box.createRigidArea(new Dimension(0,25)));
		for(JToolBar bar : vectors){
			panel.add(bar);
		}
		update();
		panel.revalidate();
		panel.repaint();
	}

	public static void createNaturalObjectPanel(JPanel panel){
		Vector<ObjetsList> objets = new Vector<ObjetsList>();
		for(ObjetsList ol : ObjetsList.values()){
			if(ol.getType().equals(ObjetType.NatureObject)){
				objets.add(ol);
			}
		}
		createPlateauObjectPanel(panel, objets);		
	}
	public static void createCharacterPanel(JPanel panel){
		Vector<ObjetsList> objets = new Vector<ObjetsList>();
		for(ObjetsList ol : ObjetsList.values()){
			if(ol.getType().equals(ObjetType.Character) && ol!=ObjetsList.Unit){
				objets.add(ol);
			}
		}
		createPlateauObjectPanel(panel, objets);		
	}
	public static void createBuildingPanel(JPanel panel){
		Vector<ObjetsList> objets = new Vector<ObjetsList>();
		for(ObjetsList ol : ObjetsList.values()){
			if(ol.getType().equals(ObjetType.Building) && ol!=ObjetsList.Building){
				objets.add(ol);
			}
		}
		createPlateauObjectPanel(panel, objets);		
	}
	public static void createVictoryPanel(JPanel panel){
		BoxLayout bl = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(bl);
		for(int team=1; team<3; team++){
			panel.add(Box.createRigidArea(new Dimension(0,25)));
			JLabel jl = new JLabel("Defeat conditions for team "+team);
			jl.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(jl);
			JPanel panel2 = new JPanel();
			BoxLayout bl2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
			panel2.setLayout(bl2);
			String[] list = new String[MainEditor.getSheet().getPlateau().getEndConditions(team).size()];
			for(int j = 0; j<list.length; j++){
				list[j] = MainEditor.getSheet().getPlateau().getEndConditions(team).get(j).nameForList();
			}
			final JList<String> jlist = new JList<String>(list);
			final int i = team;
			jlist.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					EndCondition ec = MainEditor.getSheet().getPlateau().getEndConditions(i).get(arg0.getFirstIndex());
					switch(ec.getType()){
					case buildings:
						MainEditor.getSheet().label = ((EndConditionBuilding) ec).getLabel();
						break;
					default:
						MainEditor.getSheet().label = null;
						break;
					}
					MainEditor.frame.repaint();
				}
			});
			jlist.setSize(buttonDimension);
			final JScrollPane jsp = new JScrollPane(jlist);
			jsp.setMaximumSize(listDimension);
			jsp.setMinimumSize(listDimension);
			panel2.add(Box.createRigidArea(new Dimension(25,0)));
			panel2.add(jsp);
			JPanel panel3 = new JPanel();
			BoxLayout bl3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
			panel3.setLayout(bl3);
			ImageIcon icon = new ImageIcon(Icones.getIcone("iconPlus").getImage().getScaledInstance(smallButtonDimension.width, smallButtonDimension.height, Image.SCALE_SMOOTH));
			JButton bouton = new JButton(icon);
			bouton.setPreferredSize(smallButtonDimension);
			bouton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// ADD NEW CONDITION

					EndCondition.EndConditions input = (EndCondition.EndConditions) JOptionPane.showInputDialog(null, "Select one",
							"New Defeat Condition", JOptionPane.QUESTION_MESSAGE, null, 
							EndCondition.EndConditions.values(), 
							EndCondition.EndConditions.buildings);
					switch(input){
					case buildings:
						String[] t = (MainEditor.getSheet().labels.size()>0) ? MainEditor.getSheet().labels.toArray(new String[MainEditor.getSheet().labels.size()]) : new String[]{"new label"};
						String inputLabel = (String) JOptionPane.showInputDialog(null, "Select a label",
								"Building defeat type", JOptionPane.QUESTION_MESSAGE, null, 
								t, 
								t[0]);
						MainEditor.getSheet().getPlateau().getTeams().get(i).addEndCondition(new EndConditionBuilding(i, inputLabel));
						break;
					default:
						MainEditor.getSheet().getPlateau().getTeams().get(i).addEndCondition(EndCondition.getEndCondition(input, i));
						break;
					}
					SwingMain.getSidePanel().update();
				}
			});
			panel3.add(bouton);
			panel.add(Box.createRigidArea(new Dimension(0,5)));
			icon = new ImageIcon(Icones.getIcone("iconEdit").getImage().getScaledInstance(smallButtonDimension.width, smallButtonDimension.height, Image.SCALE_SMOOTH));
			bouton = new JButton(icon);
			bouton.setPreferredSize(smallButtonDimension);
			bouton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = jlist.getSelectedIndex();
					EndCondition ec = MainEditor.getSheet().getPlateau().getEndConditions(i).get(index);
					switch(ec.getType()){
					case buildings:
						String[] t = (MainEditor.getSheet().labels.size()>0) ? MainEditor.getSheet().labels.toArray(new String[MainEditor.getSheet().labels.size()]) : new String[]{"new label"};
						String inputLabel = (String) JOptionPane.showInputDialog(null, "Select a label",
								"Building defeat type", JOptionPane.QUESTION_MESSAGE, null, 
								t, 
								((EndConditionBuilding) ec).getLabel());
						((EndConditionBuilding)ec).setLabel(inputLabel);
						break;
					case units:
						break;
					default:
						break;
					}
				}
			});
			panel3.add(bouton);
			panel.add(Box.createRigidArea(new Dimension(0,5)));
			icon = new ImageIcon(Icones.getIcone("iconMinus").getImage().getScaledInstance(smallButtonDimension.width, smallButtonDimension.height, Image.SCALE_SMOOTH));
			bouton = new JButton(icon);
			bouton.setPreferredSize(smallButtonDimension);
			bouton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int indexToDelete = jlist.getSelectedIndex();
					if(indexToDelete>=0 && indexToDelete<MainEditor.getSheet().getPlateau().getEndConditions(i).size()){
						MainEditor.getSheet().getPlateau().getTeams().get(i).removeEndCondition(indexToDelete);
					}
					jlist.setSelectedIndex(-1);
					SwingMain.getSidePanel().update();
				}
			});
			panel3.setBackground(UIManager.getColor( "Panel.background" ));
			panel3.add(bouton);
			panel2.add(Box.createRigidArea(new Dimension(25,0)));
			panel2.add(panel3);
			panel2.add(Box.createRigidArea(new Dimension(25,0)));
			panel.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(panel2);
		}
		panel.revalidate();
		panel.repaint();	
	}


	public static void update() {
		for(JButton button : boutons){
			button.setBackground((selectedObject!=null && button.getToolTipText().equals(selectedObject.name())) ? Color.CYAN : UIManager.getColor("control"));
			button.setPreferredSize(buttonDimension);
		}
	}

}
