package rps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.views.AllDocsResponse;

public class LoadDB {
	
	private HashMap<String, Integer> statsMove = new HashMap<String, Integer>();
	private ArrayList<String> nthCurrentMove = new ArrayList<String>();
	private String url = "";
	private String username = "";
	private String password = "";
	private CloudantClient client;
	
	public static void loadData()
	{
		//load data from cloudantDB to statsMove
		
	}
	
	public void updateMove()
	{
		
	}
	 public void loadConfigProperties() {
	        
        InputStream confobj = null;
        
        try {
        	System.out.println(this.getClass().getClassLoader());
            confobj = new FileInputStream("./src/resources/Config.properties");
            Properties propObj = new Properties();
            propObj.load(confobj);
            this.url =(String) propObj.get("url");
            this.username = (String) propObj.getProperty("username");
            this.password = (String) propObj.getProperty("password");
        } 
        catch (NumberFormatException e) {
           // logger.error("NumberFormatException: ", e);
        } 
        catch (IOException e) {
           // logger.error("IOException: ", e);
        }
        finally {
            try {
                if(confobj != null) {
                    confobj.close();
                }
            } catch (IOException e) {
               // logger.error("IOException: ", e);
            }
        }
	}
	 
	public void connect()
	{
		try{
		client = ClientBuilder.url(new URL(url))
                .username(username)
                .password(password)
                .build();
		}catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
	
	public void listDBs()
	{
		List<String> databases = client.getAllDbs();
		System.out.println("All my databases : ");
		for ( String db : databases ) {
		    System.out.println(db);
		}
	}
	
	public class RPSexample{
		private String _id = "";
		private String _rev = null;
		private String match ="";
		private String score = "";
		public RPSexample(String id,String match, String score)
		{
			this._id = id;
			this.match = match;
			this.score = score; 
		}
		public String getMatch()
		{
			return match;
		}
		
		public String getScore()
		{
			return score;
		}
		
		public String toString()
		{
			return "id:" + _id + "| match:"+match +"| score:"+score;
		}
	}
	
	public void readRPSDB() throws IOException
	{
		Database db = client.database("rockpaperscissordb", false);
		//int testing = db.view
		for(int i=1;i<3;i++){
			RPSexample example = db.find(RPSexample.class , i+"");
			System.out.println(example);
		}
		
		
	}
	
	
	public static void main(String[]args) throws IOException
	{
		
		LoadDB move = new LoadDB();
		move.loadConfigProperties();
		move.connect();
		//move.listDBs();	
		move.readRPSDB();
	}
	
}
