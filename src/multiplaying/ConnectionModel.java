package multiplaying;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import model.*;

public class ConnectionModel {

	public long currentTime;
	public InetAddress ia;
	public float resX, resY;
	public Vector<ConnectionObjet> naturalObjets;

	public ConnectionModel(InetAddress ia, Vector<NaturalObjet> n){
		this.ia = ia;
		this.naturalObjets = new Vector<ConnectionObjet>();
		for(NaturalObjet no :n){
			if(no instanceof Tree){
				naturalObjets.add(new ConnectionTree(((Tree)no).type,no.getX(),no.getY()));
			} else if(no instanceof Water){
				naturalObjets.add(new ConnectionWater(no.getX(),no.getY(),((Water)no).sizeX,((Water)no).sizeY));
			} else {
			}
		}
		this.currentTime = System.currentTimeMillis();
	}
	public ConnectionModel(String s){
		String[] t = Utils.split(s, '|');
		String[] v;
		naturalObjets = new Vector<ConnectionObjet>();
		for(int i=0; i<t.length; i++){
			if(i==t.length-1)
				continue;
			if(i==0){
				currentTime = Long.parseLong(t[i]);
			} else if(i==1) {
				try { ia = InetAddress.getByName(t[i]); } catch (UnknownHostException e) {}
			} else {
				v = Utils.split(t[i], '*');
				for(int j=0; j<v.length; j++){
					if(v[j].equals(""))
						continue;
					if(v[j].charAt(0)=='0'){
						naturalObjets.add(new ConnectionTree(v[j]));
					} else {
						naturalObjets.add(new ConnectionWater(v[j]));
					}
				}
			}
		}
	}
	public String toString(){
		String s="2";
		s+=currentTime+"|"+ia.getHostName()+"|";
		for(int i=0; i<naturalObjets.size(); i++){
			s+=naturalObjets.get(i).toString();
			if(i<naturalObjets.size()-1)
				s+='*';
		}
		s+='|';
		return s;
	}


	public static class ConnectionObjet{
		public float x,y;
	}
	public static class ConnectionTree extends ConnectionObjet{
		public int type;
		public ConnectionTree(int type, float x, float y){
			// Output to create a new bullet
			this.type = type;
			this.x = x;
			this.y = y;
		}
		public ConnectionTree(String s){
			try{
				String[] t = Utils.split(s.substring(1), ' ');
				this.type = Integer.parseInt(t[0]);
				this.x = Float.parseFloat(t[1]);
				this.y = Float.parseFloat(t[2]);
			} catch (NumberFormatException e ){
				//System.out.println(s);
			}
		}
		public String toString(){
			String s = "";
			s+="0"+type+" "+x+" "+y;
			return s;
		}
	}
	public static class ConnectionWater extends ConnectionObjet{
		public float sizeX,sizeY;
		public ConnectionWater(float x, float y, float sizeX, float sizeY){
			// Output to create a new bullet
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}
		public ConnectionWater(String s){
			try{
				String[] t = Utils.split(s.substring(1), ' ');
				this.x = Float.parseFloat(t[0]);
				this.y = Float.parseFloat(t[1]);
				this.sizeX = Float.parseFloat(t[2]);
				this.sizeY = Float.parseFloat(t[3]);
			} catch (NumberFormatException e ){
				//System.out.println(s);
			}
		}
		public String toString(){
			String s = "";
			s+="1"+x+" "+y+" "+sizeX+" "+sizeY;
			return s;
		}
	}


}
