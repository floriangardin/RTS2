package mapeditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import ressources.ImagesAwt;
import utils.ObjetsList;


public class PlateauObjectPanel extends JPanel {
	
	public static Dimension buttonDimension = new Dimension(72,72);
	
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
			ImageIcon icon = new ImageIcon(ImagesAwt.getImage(o, MainEditor.teamSelected.team));
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
			if(ol.getType().equals("NatureObject")){
				objets.add(ol);
			}
		}
		createPlateauObjectPanel(panel, objets);		
	}
	public static void createCharacterPanel(JPanel panel){
		Vector<ObjetsList> objets = new Vector<ObjetsList>();
		for(ObjetsList ol : ObjetsList.values()){
			if(ol.getType().equals("Character") && ol!=ObjetsList.Unit){
				objets.add(ol);
			}
		}
		createPlateauObjectPanel(panel, objets);		
	}
	
	
	public static void update() {
		for(JButton button : boutons){
			button.setBackground((selectedObject!=null && button.getToolTipText().equals(selectedObject.name())) ? Color.CYAN : UIManager.getColor("control"));
			button.setPreferredSize(buttonDimension);
		}
	}

}
