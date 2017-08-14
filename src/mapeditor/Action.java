package mapeditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import mapeditor.TerrainObjectPanel.BrushStyle;
import mapeditor.MainEditor.Mode;
import mapeditor.MainEditor.TeamSelected;
import pathfinding.Case.IdTerrain;
import plateau.Plateau;

public abstract class Action {

	ActionType type;
	Plateau plateau;

	// Action parameters
	public static int delete_sheet_id;

	public abstract void undo();
	public abstract void redo();
	
	public Action(ActionType type, Plateau plateau){
		this.type = type;
		this.plateau = plateau;
	}

	public static void redoAction(ActionType type){
		JFileChooser fileChooser;
		int userSelection;
		String s;
		switch(type){
		case CreateNewSheet:
			int i = ActionHelper.getNumberOfNewSheet();
			if(i==0){
				s = "New map";
			} else {
				s = "New map ("+i+")";				
			}
			JTextField nameField = new JTextField(20);
			JTextField xField = new JTextField(5);
			JTextField yField = new JTextField(5);

			JPanel myPanel = new JPanel();
			myPanel.add(new JLabel("name:"));
			myPanel.add(nameField);
			nameField.setText(s);
			myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			myPanel.add(new JLabel("size X:"));
			xField.setText("15");
			myPanel.add(xField);
			myPanel.add(Box.createHorizontalStrut(15)); // a spacer
			myPanel.add(new JLabel("size Y:"));
			yField.setText("15");
			myPanel.add(yField);

			final JOptionPane optionPane = new JOptionPane(myPanel);

			JDialog dialog = new JDialog(MainEditor.frame, "Create a new map",true);
			dialog.setContentPane(optionPane);
			dialog.setDefaultCloseOperation(
				    JDialog.DO_NOTHING_ON_CLOSE);
				dialog.addWindowListener(new WindowAdapter() {
				    public void windowClosing(WindowEvent we) {
				        optionPane.setValue(null);
				        dialog.setVisible(false);
				    }
				});
			optionPane.addPropertyChangeListener(
					new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent e) {
							String prop = e.getPropertyName();
							if (dialog.isVisible() 
									&& (e.getSource() == optionPane)
									&& (optionPane.getValue() != null)
									&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
								//If you were going to check something
								//before closing the window, you'd do
								//it here.
								if (optionPane.getValue() == JOptionPane.UNINITIALIZED_VALUE) {
					                //ignore reset
					                return;
					            }
								try{
									Integer.parseInt(xField.getText());
									Integer.parseInt(yField.getText());
									try{
										char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':', '.' };
										for(char c : ILLEGAL_CHARACTERS){
											if(nameField.getText().contains(""+c)){
												System.out.println(""+c);
												throw new Exception();
											}
										}
										dialog.setVisible(false);
									} catch(Exception exception){
										JOptionPane.showMessageDialog(MainEditor.frame,
											    "Please enter a valid name.",
											    "Name error",
											    JOptionPane.ERROR_MESSAGE);
										optionPane.setValue(
							                    JOptionPane.UNINITIALIZED_VALUE);
									}
								} catch(Exception exception){
									JOptionPane.showMessageDialog(MainEditor.frame,
										    "Please enter valid sizes.",
										    "Size error",
										    JOptionPane.ERROR_MESSAGE);	
									optionPane.setValue(
						                    JOptionPane.UNINITIALIZED_VALUE);
								}
								
							}
						}
					});
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(MainEditor.frame);
			dialog.setVisible(true);
			if(optionPane.getValue() != null){
				MainEditor.createNewSheet(nameField.getText(), Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
			}
			break;
		case DeleteSheet:
			MainEditor.deleteSheet(delete_sheet_id);
			break;
		case SaveCurrentSheet:
			if(MainEditor.getSheet() == null){
				break;
			}
			if(MainEditor.getSheet().getFileToSave()==null){
				redoAction(ActionType.SaveCurrentSheetAs);
			} else {
				FileOutputStream fout;
				try {
					fout = new FileOutputStream(MainEditor.getSheet().getFileToSave());
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(MainEditor.getSheet().getPlateau());
					oos.close();
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case SaveCurrentSheetAs:
			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("RTS maps files", "rtsmap"));
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setCurrentDirectory(new File("ressources/maps/"));
			fileChooser.setDialogTitle("Specify a file to save");
			fileChooser.setApproveButtonText("Save");
			userSelection = fileChooser.showSaveDialog(MainEditor.frame);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				String fileToSave = fileChooser.getSelectedFile().getAbsolutePath();
				if(!fileToSave.endsWith(".rtsmap")){
					if(fileToSave.contains(".")){
						break;
					}
					fileToSave = fileToSave+".rtsmap";
				}
				MainEditor.getSheet().setFileToSave(new File(fileToSave));
				redoAction(ActionType.SaveCurrentSheet);
			}
			break;
		case OpenNewSheet:
			fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("RTS maps files", "rtsmap"));
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setCurrentDirectory(new File("ressources/maps/"));
			fileChooser.setDialogTitle("Specify a file to open");
			userSelection = fileChooser.showOpenDialog(MainEditor.frame);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				try{
					File fileToSave = fileChooser.getSelectedFile();
					ObjectInputStream objectinputstream = null;
					FileInputStream streamIn = new FileInputStream(fileToSave);
					objectinputstream = new ObjectInputStream(streamIn);
					Plateau p = (Plateau) objectinputstream.readObject();
					if(objectinputstream != null){
						objectinputstream .close();
					} 
					MainEditor.openSheet(p, fileToSave);
				} catch(Exception e){
					JOptionPane.showMessageDialog(MainEditor.frame,
						    "Corrupted file - unable to parse.",
						    "Open file error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
			break;
		case SwitchGrid:
			MainEditor.grid = !MainEditor.grid;
			SwingMain.getItemBarPanel().update();
			MainEditor.frame.repaint();
			break;
		case SwitchCollision:
			MainEditor.collision = !MainEditor.collision;
			SwingMain.getItemBarPanel().update();
			MainEditor.frame.repaint();
			break;
		case SelectTeam0:
			MainEditor.teamSelected = TeamSelected.Neutral;
			SwingMain.getItemBarPanel().update();
			SwingMain.getSidePanel().update();
			break;
		case SelectTeam1:
			MainEditor.teamSelected = TeamSelected.Team1;
			SwingMain.getItemBarPanel().update();
			SwingMain.getSidePanel().update();
			break;
		case SelectTeam2:
			MainEditor.teamSelected = TeamSelected.Team2;
			SwingMain.getItemBarPanel().update();
			SwingMain.getSidePanel().update();
			break;
		case SelectToolMove:
		case SelectToolErase:
		case SelectToolTerrain:
		case SelectToolNature:
		case SelectToolCharacter:
		case SelectToolBuilding:
			MainEditor.mode = Mode.valueOf(type.name().substring(10).toUpperCase());
			SwingMain.getItemBarPanel().update();
			SwingMain.getSidePanel().update();
			break;
		case SelectBrushSize1:
		case SelectBrushSize2:
		case SelectBrushSize3:
		case SelectBrushFill:
			TerrainObjectPanel.brushStyle = BrushStyle.valueOf(type.name().substring(11).toUpperCase());
			TerrainObjectPanel.update();
			break;
		case SelectGroundSand:
		case SelectGroundGrass:
		case SelectGroundWater:
			TerrainObjectPanel.groundStyle = IdTerrain.valueOf(type.name().substring(12).toUpperCase());
			TerrainObjectPanel.update();
			break;
		case Undo:
			if(MainEditor.getSheet() != null)
				MainEditor.getSheet().undoLastAction();
			break;
		case Redo:
			if(MainEditor.getSheet() != null)
				MainEditor.getSheet().redoNextAction();
			break;
		case ExitSoftware:
			MainEditor.frame.setVisible(false); //you can't see me!
			MainEditor.frame.dispose();
			break;
		default : System.out.println("redo action : "+type+"\n   Pour ne plus voir ce message s'afficher,\n    overridez la fonction redo de cette action");
		}
	}

	public static void undoAction(ActionType type){
		switch(type){
		default : System.out.println("undo action : "+type+"\n   Pour ne plus voir ce message s'afficher,\n    overridez la fonction undo de cette action");
		}
	}


}
