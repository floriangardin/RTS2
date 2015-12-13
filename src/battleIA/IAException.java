package battleIA;

public class IAException extends Exception{

	public String message;
	
	public IAException(int idPlayer, String message){
		System.out.println("");
		System.out.println("Erreur d'exécution dans le code d'IA du joueur " + idPlayer);
		System.out.println("...");
		System.out.println(message);
		System.out.println("");
	}

}
