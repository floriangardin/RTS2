package mapeditor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public strictfp class MenuBar {
	
	public static JMenuBar getMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		
		ActionType[] actionFile = {ActionType.CreateNewSheet,
									ActionType.OpenNewSheet,
									ActionType.SaveCurrentSheet,
									ActionType.SaveCurrentSheetAs,
									ActionType.ExitSoftware};
		menuBar.add(createJMenu("File", actionFile));
		ActionType[] actionEdition = {ActionType.CopyElement,
										ActionType.PasteElement,
										ActionType.Undo,
										ActionType.Redo};
		menuBar.add(createJMenu("Editing", actionEdition));
		
		return menuBar;
	}
	
	private static JMenu createJMenu(String name, ActionType[] actions){
		JMenu jMenu = new JMenu(name);
		JMenuItem item;
		for(ActionType type : actions){
			item = new JMenuItem(type.getName());
			item.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                Action.redoAction(type);
	            }
	        });
			
			jMenu.add(item);	
		}
		return jMenu;
	}

}
