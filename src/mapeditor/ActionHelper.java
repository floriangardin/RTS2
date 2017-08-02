package mapeditor;

import java.util.Vector;

public class ActionHelper {

	public static int getNumberOfNewSheet(){
		Vector<Integer> v = new Vector<Integer>();
		for(SheetPanel s : MainEditor.getSheets()){
			if(s.getName().equals("New Sheet")){
				v.add(0);
			} else if(s.getName().startsWith("New Sheet (") &&
					s.getName().endsWith(")")){
				try{
					v.addElement(Integer.parseInt(s.getName().substring(11, s.getName().length()-1)));
				}catch(NumberFormatException e) {}
				}
		}
		int i=0;
		while(v.contains(i))
			i++;
		return i;
	}
}
