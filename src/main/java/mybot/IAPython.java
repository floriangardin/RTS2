package mybot;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bot.IA;
@Path("/hello")
public class IAPython extends IA {
	final GsonBuilder builder = new GsonBuilder();
	public final Gson gson = builder.create();
	
	public List<Action> actions = new Vector<Action>();

	public IAPython(int teamid) {
		super(teamid);
		// IA qui fournit une api rest pour un script python
        HttpServer server=null;
		try {
			int port = 0;
			if(teamid==1){
				port = 8001;
			}else{
				port = 8002;
			}
			server = HttpServer.create(new InetSocketAddress(port), 0);
			IAPython self = this;
			
			// API : GET STATE OF PLATEAU
			// TODO : Filter in plateau.toJson to get only 
	        server.createContext("/get", new HttpHandler(){
				@Override
				public void handle(HttpExchange t) throws IOException {
					String response= gson.toJson(self.plateau.toJson());
		            t.sendResponseHeaders(200, response.length());
		            OutputStream os = t.getResponseBody();
		            os.write(response.getBytes());
		            os.close();
				}
	        	
	        });
	        
	        // API : ASK TO PERFORM ACTION
	        server.createContext("/post", new HttpHandler(){
				@Override
				public void handle(HttpExchange t) throws IOException {
					int b;
					
					InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
					BufferedReader br = new BufferedReader(isr);
					StringBuilder buf = new StringBuilder(512);
					while ((b = br.read()) != -1) {
					    buf.append((char) b);
					}
					
					String res = buf.toString();
					synchronized(actions){	
						actions.addAll(Action.parse(res));
					}
					
					br.close();
					isr.close();
					t.sendResponseHeaders(200, 10);
					OutputStream os = t.getResponseBody();
		            os.write("ok".getBytes());
		            os.close();
					
				}
	        	
	        });
	        
	        server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void update() throws Exception {
		// Deal with actions
		synchronized(actions){
			if(actions.size()>0){				
				Action toDo = actions.remove(0);
				toDo.play(this);
			}
		}
		
	}
	

}
