package mapeditor;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Main;
import ressources.Icones;

public strictfp class SheetNamesPanel extends JTabbedPane{

	public static Dimension buttonDimension = new Dimension(24,24);

	public SheetNamesPanel(){
		super();
		update();
		this.setMinimumSize(new Dimension(500,5));
		this.setTabPlacement(BOTTOM);
		// Adding external actions bar
	}

	private strictfp class TabComponent extends JPanel{
		private static final long serialVersionUID = -5602079827923957403L;

		public TabComponent(String title, int index){
			super();
			add(new JLabel(title));
			TabButton bouton = new TabButton(index, Icones.getIcone("default"));
			add(bouton);
		}
	}

	private strictfp class TabButton extends JButton {
		private static final long serialVersionUID = 3028111188486384972L;
		public int index = 0;
		public TabButton(int index, ImageIcon icone) {
			super(new ImageIcon(icone.getImage().getScaledInstance(buttonDimension.width, buttonDimension.height, Image.SCALE_SMOOTH)));
			this.index = index;
			int size = 24;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			//Make it transparent
			setContentAreaFilled(false);
			//No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			//Making nice rollover effect
			setRolloverEnabled(true);
			//Close the proper tab by clicking the button
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Action.delete_sheet_id = index;
					Action.redoAction(ActionType.DeleteSheet);
				}
			});
		}

	}
	
	public void update(){
		removeAll();
		int i=0;
		for(SheetPanel sp : MainEditor.getSheets()){
			add(sp.getName(), sp);
			setTabComponentAt(i, new TabComponent(sp.getName(),i));
			i+=1;
		}
		if(getTabCount()>0){
			setSelectedIndex(getTabCount()-1);
		}
		removeChangeListener(changeListener);
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(getSelectedComponent()!=null){
					MainEditor.frame.setTitle("RTS Ultra Mythe Editor - "+getSelectedComponent().getName());
				} else {
					MainEditor.frame.setTitle("RTS Ultra Mythe Editor");
				}
			}
		});
		if(MainEditor.frame!=null){
			MainEditor.frame.repaint();
		}
	}


	

}
