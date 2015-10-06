package IA;

import java.io.File;
import java.io.FileWriter;

import main.Main;
import model.Constants;
import model.Game;

public class Simulator {

	public int simulationNumber;
	public int numberParameters;
	public float[][] observations;
	public float[] results;
	
	public Simulator(int n){
		
		simulationNumber = n;
		numberParameters = ArmyComparator.numberParameters;
		observations = new float[n][numberParameters];
		results = new float[n];
		ArmyComparator ac;
		Simulation s;
		Game game = new Game();
		for(int i=0; i<n; i++){
			game = new Game();
			game.setParams(new Constants(Main.framerate),10,10);
			s = new Simulation(game);
			ac = new ArmyComparator(s.armies.get(0), s.armies.get(1));
			System.out.println(ac.ally);
			System.out.println(ac.ennemy);
			//s.simulate();
			//Report r = s.report;
			//results[i] = r.teamVictory %2;
			for(int j=0;j<numberParameters;j++){
				observations[i][j] = ac.obs[j];
			}
		}
		// CREATION DES FICHIERS
		String path = "C:/Users/Kévin/Documents/GitHub/RTS2/IA/FightSimulator";
		File di   = new File(path);
		File fl[] = di.listFiles();
		String zeros = "";
		if(fl.length/2<1000)
			zeros+="0";
		if(fl.length/2<100)
			zeros+="0";
		if(fl.length/2<10)
			zeros+="0";
		// ECRITURE DE LA MATRICE
        final File fichier =new File(path+"/matrix_"+zeros+fl.length/2+".txt"); 
        try {
            // Creation du fichier
            fichier .createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier);
            try {
            	String message = "";
            	for(int i=0; i<n; i++){
            		message= "";
            		for(int j=0;j<numberParameters;j++){
            			message+=observations[i][j]+" ";
            		}
                    writer.write(message+"\r\n");            		
            	}
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier matrice");
        }
        File fichier1 =new File(path+"/results_"+zeros+fl.length/2+".txt"); 
        try {
            // Creation du fichier
            fichier1 .createNewFile();
            // creation d'un writer (un écrivain)
            final FileWriter writer = new FileWriter(fichier1);
            try {
            	String message = "";
            	for(int i=0; i<n; i++){
            		message= ""+results[i];
                    writer.write(message);            		
            	}
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier results");
        }
	}
}
