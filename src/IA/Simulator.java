package IA;

import java.io.File;
import java.io.FileWriter;

import model.GameSimu;

public class Simulator{

	public int simulationNumber;
	public int numberParameters;
	public float[][] observations;
	public float[] results;
	public GameSimu game;
	
	public Simulator (){
		simulationNumber = 5;
		numberParameters = ArmyComparator.numberParameters;
		observations = new float[simulationNumber][numberParameters];
		results = new float[simulationNumber];
		game = new GameSimu(1600,1600);
	}
	
	public void run(){
		ArmyComparator ac;
		Simulation s;
		for(int i=0; i<simulationNumber; i++){
			s = new Simulation(game);
			ac = new ArmyComparator(s.armies.get(0), s.armies.get(1));
			s.simulate();
			Report r = s.report;
			results[i] = r.teamVictory;
			for(int j=0;j<numberParameters;j++){
				observations[i][j] = ac.obs[j];
			}
		}
		// CREATION DES FICHIERS
		String path = "././IA/FightSimulator";
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
            // creation d'un writer (un �crivain)
            final FileWriter writer = new FileWriter(fichier);
            try {
            	String message = "";
            	for(int i=0; i<simulationNumber; i++){
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
            // creation d'un writer (un �crivain)
            final FileWriter writer = new FileWriter(fichier1);
            try {
            	String message = "";
            	for(int i=0; i<simulationNumber; i++){
            		message= ""+results[i]+" ";
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
