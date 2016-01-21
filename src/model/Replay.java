package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import multiplaying.InputObject;

public class Replay {

	String map;
	Vector<InputObject> inputs;
	int nTeams;
	int delay;
	Game game;

	public Replay(int nPlayers,String map,int delay,Game game){
		//Each inputs contains the round number ( where it has been generated)
		inputs = new Vector<InputObject>();
		this.map = map;
		this.nTeams = nPlayers;
		this.delay = delay;
		this.game = game;
	}

	public Vector<InputObject> getInputForRound(int round){
		Vector<InputObject> result = new Vector<InputObject>();
		//On suppose que les vecteurs sont triés
		int currentRound = 0;
		int id =0;
		while(currentRound<=round){
			if(inputs.get(id).round==round){
				result.add(inputs.get(id));
			}
			id++;
		}
		
		return result;
	}
	public void addInReplay(Vector<InputObject> ims){
		for(InputObject o : ims){
			addInReplay(o);
		}
	}
	public void addInReplay(InputObject im){
		inputs.add(im);
	}
	public void write(String filename){
		try {
			FileWriter fw = new FileWriter ("ressources/"+filename+".rtsreplay");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
		for(InputObject v : inputs){
				fichierSortie.println(v.toString());
		}
		fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
		}	
	}

	public void Read(String filename){
		String fichier = "ressources/"+filename+".rtsreplay";
		try{
			//lecture du fichier texte	
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;

			while ((ligne=br.readLine())!=null){
				InputObject o = new InputObject(ligne,game);
				this.inputs.add(o);
			}
			br.close();
		} catch (Exception e){
			System.out.print("erreur");
			e.printStackTrace();
		}
	}
}
