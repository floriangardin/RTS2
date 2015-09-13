package multiplaying;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;


import model.*;
import nature.Tree;
import nature.Water;

public class ConnectionModel {

	public long currentTime;
	public InetAddress ia;
	public float resX, resY;
	public int idMap;

	public ConnectionModel(InetAddress ia, int idMap){
		this.ia = ia;
		this.idMap = idMap;
		this.currentTime = System.currentTimeMillis();
	}
	public ConnectionModel(String s){
		String[] t = Utils.split(s, '|');
		String[] v;
		for(int i=0; i<t.length; i++){
			if(i==t.length-1)
				continue;
			if(i==0){
				currentTime = Long.parseLong(t[i]);
			} else if(i==1) {
				try { ia = InetAddress.getByName(t[i]); } catch (UnknownHostException e) {}
			} else {
				try { idMap = Integer.parseInt(t[i]); } catch (NumberFormatException e) {}
			}
		}
	}
	public String toString(){
		String s="2";
		s+=currentTime+"|"+ia.getHostName()+"|"+idMap+"|";
		return s;
	}


	


}
