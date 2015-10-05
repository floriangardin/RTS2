package IA;

import java.io.File;
import java.io.FileWriter;

public class Simulator {

	public int simulationNumber;
	public int numberParameters;
	public float[][] observations;
	public float[] results;
	
	public Simulator(int n){
		simulationNumber = n;
		numberParameters = ArmyComparator.numberParameters;
		observations = new float[numberParameters][n];
		results = new float[n];
		ArmyComparator ac;
		Simulation s;
		for(int i=0; i<n; i++){
			s = new Simulation();
			ac = new ArmyComparator(s.armies.get(0), s.armies.get(1));
			s.simulate();
			Report r = s.report;
			results[i] = r.teamVictory %2;
			for(int j=0;j<numberParameters;j++){
				observations[i][j] = ac.obs[j];
			}
		}
		// CREATION DES FICHIERS
		String path = "C:/Users/Kévin/Documents/GitHub/RTS2/IA/FightSimulator";
		File di   = new File(path);
		File fl[] = di.listFiles();
		// ECRITURE DE LA MATRICE
        final File fichier =new File(path+"/matrix_0"+fl.length+".txt"); 
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
                    writer.write(message);            		
            	}
            } finally {
                // quoiqu'il arrive, on ferme le fichier
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Impossible de creer le fichier matrice");
        }
        File fichier1 =new File(path+"/results_0"+fl.length+".txt"); 
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
