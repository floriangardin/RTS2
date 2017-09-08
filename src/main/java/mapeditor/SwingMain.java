package mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public strictfp class SwingMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8503157207806339141L;
	private JPanel contentPane;
	private static EditorActionBar editorActionBar;
	private static SheetNamesPanel sheetsPane;
	private static SidePanel sidePanel;

	
	/**
	 * Create the frame.
	 */
	
	public SwingMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("RTS Ultra Mythe Editor");
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 1280, 720);
		contentPane = new JPanel();
		setContentPane(contentPane);

		// Création de la bar de menu
		setJMenuBar(MenuBar.getMenuBar());
		contentPane.setLayout(new BorderLayout(0, 0));
		editorActionBar = new EditorActionBar(contentPane);
		contentPane.add(editorActionBar, BorderLayout.NORTH);
		sheetsPane = new SheetNamesPanel();
		sidePanel = new SidePanel();
		JSplitPane p = new JSplitPane( JSplitPane. HORIZONTAL_SPLIT,sheetsPane, sidePanel);
		p.setOneTouchExpandable ( false );
		p.setEnabled(false);
		p.setDividerLocation(this.getWidth()*4/5);
		contentPane.add(p, BorderLayout.CENTER);
		setMinimumSize(new Dimension(1280, 720));
		addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		    	p.setDividerLocation(getWidth()*4/5);          
		    }
		    public void componentHidden(ComponentEvent arg0) {}
		    public void componentMoved(ComponentEvent arg0) {}
		    public void componentShown(ComponentEvent arg0) {}
		});
		
	}
	
	public static SheetNamesPanel getSheetsPane(){
		return sheetsPane;
	}
	public static EditorActionBar getItemBarPanel() {
		return editorActionBar;
	}
	public static SidePanel getSidePanel() {
		return sidePanel;
	}

}
