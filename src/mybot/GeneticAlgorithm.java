package mybot;

import java.util.Random;

public class GeneticAlgorithm {

//	public Mergeable[] pop;
//	
//	public GeneticAlgorithm(int popSize){
//		pop = new Mergeable[popSize];
//	}
//	public int popSize(){
//		return pop.length;
//	}
//	
//	public Mergeable[] train(int generations){
//		for(int i = 0; i<generations; i++){
//			epoch();
//		}
//		return pop;
//	}
//	
//	private void epoch(){
//		shuffle(pop);
//		Mergeable[] newPop = select();
//		newPop = shuffle(newPop);
//		newPop = merge(newPop);
//		newPop = mutate(newPop);
//		this.pop = newPop;
//	}
//	
//	public Mergeable[] select(){
//		Mergeable[] res = new Mergeable[(int)(this.popSize()/2)];
//		for(int i=0; i<popSize(); i+=2){
//			float score1 = pop[i].estimate();
//			float score2 = pop[i+1].estimate();
//			if(score1>score2){
//				res[(int)(i/2)]= pop[i];
//			}else{
//				res[(int)(i/2)]= pop[i+1];
//			}
//		}
//		return res;
//	}
//	private Mergeable[] merge(Mergeable[] newPop){
//		Mergeable[] res = new Mergeable[(int)(this.popSize())];
//		for(int i=0; i<newPop.length; i+=2){
//			res[(int)(i/2)]= newPop[i].merge(newPop[i+1]);
//			res[(int)(i/2)+1]= newPop[i+1].merge(newPop[i]);
//		}
//		
//		for(int i=newPop.length; i<popSize(); i++){
//			int j = i - newPop.length;
//			res[i] = newPop[j];
//		}
//		return res;
//	}
//	private Mergeable[] mutate(Mergeable[] newPop){
//		for(int i =0; i<(int)(popSize()/2); i++){
//			newPop[i] = newPop[i].mutate();
//		}
//		return newPop;
//	}
//	
//	private Mergeable[] shuffle(Mergeable[] pop){
//		
//	    Random rnd = new Random();
//	    for (int i = pop.length - 1; i > 0; i--)
//	    {
//	      int index = rnd.nextInt(i + 1);
//	      // Simple swap
//	      Mergeable a = pop[index];
//	      pop[index] = pop[i];
//	      pop[i] = a;
//	    } 
//	    return pop;
//	}
}
