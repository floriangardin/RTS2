package IA;

import java.io.File;
import java.io.FileWriter;

import plateau.Plateau;

public class MicroSimulator extends Simulator{

	public GeneticAlgorithm gen;
	public int N_POP = 24;
	public int N_PARAMS = 4;
	public int N_FEATURES = 6;
	public int N_GENERATIONS = 4;
	public float proba_muta = 0.001f;
	
	
	public MicroSimulator (Plateau plateau){
		game = new GameSimu();
		gen = new GeneticAlgorithm(plateau, N_POP,N_PARAMS,N_FEATURES,proba_muta,game);
		gen.init();
	}
	
	public void run(){
		float[][] result = gen.optimize(N_GENERATIONS);
		// CREATION DES FICHIERS
		String path = "././IA/MicroSimulator";
		File di   = new File(path);
		File fl[] = di.listFiles();
		String zeros = "";
		if(fl.length<1000)
			zeros+="0";
		if(fl.length<100)
			zeros+="0";
		if(fl.length<10)
			zeros+="0";
		// ECRITURE DE LA MATRICE
        final File fichier =new File(path+"/matrix_"+zeros+fl.length+".txt"); 
        try {
            // Creation du fichier
            fichier .createNewFile();
            // creation d'un writer (un �crivain)
            final FileWriter writer = new FileWriter(fichier);
            try {
            	String message = "";
            	for(int i=0; i<result.length; i++){
            		message= "";
            		for(int j=0;j<result[0].length;j++){
            			message+=result[i][j]+" ";
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
        
	}

	
}
