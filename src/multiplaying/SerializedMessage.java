package multiplaying;

public class SerializedMessage implements java.io.Serializable{
	
	public byte[] msg;
	
	public SerializedMessage(){
		
	}
	
	public SerializedMessage(byte[] msg){
		this.msg = msg;
	}
}
