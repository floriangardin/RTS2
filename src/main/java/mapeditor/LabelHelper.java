package mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import plateau.Objet;

public class LabelHelper {

	static private JList<String>  sourceList;

	static private Vector<String> sourceListModel;

	static private JList<String> destList;

	static private Vector<String> destListModel;

	static private JButton addButton;

	static private JButton removeButton;
	
	static private JButton newButton;
	
	static private JDialog dialog;

	static private class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int i = sourceList.getSelectedIndex();
			if(i>=0 && i<sourceListModel.size() && !destListModel.contains(sourceListModel.get(i))){
				destListModel.add(sourceListModel.get(i));
			}
			destList.setListData(destListModel.toArray(new String[destListModel.size()]));
		}
	}

	static private class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int i = destList.getSelectedIndex();
			if(i>=0 && i<destListModel.size()){
				destListModel.removeElementAt(i);
			}
			destList.getSelectionModel().clearSelection();
			destList.setListData(destListModel.toArray(new String[destListModel.size()]));
		}
	}
	
	static private class NewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog(dialog, "New Label ?");
			if(name != null && name.length()>0){
				MainEditor.getSheet().labels.add(name);
				sourceList.setListData(sourceListModel.toArray(new String[sourceListModel.size()]));
			}
		}
	}

	public static void HandleProperties(Vector<String> labelSrc, Objet o) {


		JPanel jp = new JPanel();
		BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
		jp.setLayout(bl);
		sourceListModel = labelSrc;
		sourceList = new JList<String> (sourceListModel);

		addButton = new JButton(">>");
		addButton.addActionListener(new AddListener());
		removeButton = new JButton("<<");
		removeButton.addActionListener(new RemoveListener());
		newButton = new JButton(" + ");
		newButton.addActionListener(new NewListener());

		destListModel = o.getLabels();
		destList = new JList<String> (destListModel);

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JLabel("Existing Labels:"), BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(sourceList), BorderLayout.CENTER);
		

		JPanel rightPanel = new JPanel(new BorderLayout());

		rightPanel.add(new JLabel("Label for this "+o.getName()+" :"), BorderLayout.NORTH);
		rightPanel.add(new JScrollPane(destList), BorderLayout.CENTER);
		
		
		jp.add(Box.createRigidArea(new Dimension(0,25)));
		JPanel jp2 = new JPanel();
		BoxLayout bl2 = new BoxLayout(jp2, BoxLayout.X_AXIS);
		jp2.setLayout(bl2);
		jp2.add(Box.createRigidArea(new Dimension(20,0)));
		jp2.add(leftPanel);
		jp2.add(Box.createRigidArea(new Dimension(10,0)));
		
		JPanel jp3 = new JPanel();
		BoxLayout bl3 = new BoxLayout(jp3, BoxLayout.Y_AXIS);
		jp3.setLayout(bl3);
		jp3.add(Box.createRigidArea(new Dimension(0,30)));
		jp3.add(addButton);
		jp3.add(Box.createRigidArea(new Dimension(0,10)));
		jp3.add(removeButton);
		jp3.add(Box.createRigidArea(new Dimension(0,10)));
		jp3.add(newButton);
		jp3.add(Box.createRigidArea(new Dimension(0,30)));
		jp2.add(jp3);
		
		jp2.add(Box.createRigidArea(new Dimension(10,0)));
		jp2.add(rightPanel);
		jp2.add(Box.createRigidArea(new Dimension(20,0)));
		
		jp.add(jp2);
		jp.add(Box.createRigidArea(new Dimension(0,25)));
		
		dialog = new JDialog(MainEditor.frame, "Properties of "+o.getName()+" _"+o.getId(),true);
		dialog.setContentPane(jp);
		dialog.setDefaultCloseOperation(
				JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dialog.setVisible(false);
				MainEditor.getSheet().updateLabelsFromPlateau();
			}
		});
		
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(MainEditor.frame);
		dialog.setVisible(true);
		
	}

}
