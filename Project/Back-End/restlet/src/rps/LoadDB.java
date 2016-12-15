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
	
	private String url = "";
	private String username = "";
	private String password = "";
	private CloudantClient client;
	
	public LoadDB()
	{
		loadConfigProperties("./Config.properties");
	}
	
	public static void loadData()
	{
		//load data from cloudantDB to statsMove
		
		
	}
	
	 public void loadConfigProperties(String path) {
	        
        InputStream confobj = null;
        
        try {
        	System.out.println(this.getClass().getClassLoader());
            confobj = new FileInputStream(path);
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
	
	
	public class RPSdata{
		private String _id = "";
		private String _rev = null;
		private String match ="";
		
		public RPSdata(String id,String match)
		{
			this._id = id;
			this.match = match;
			
			
		}
		public String getMatch()
		{
			return match;
		}
		public void setMatch(String match)
		{
			this.match = match;
		}
		public String toString()
		{
			return "id:" + _id + "| match:"+ match ;
		}
	}
	
	public RPSdata readRPSDB(String _id) throws IOException
	{
		Database db = client.database("rockpaperscissordb", false);
		//int testing = db.view
		try{
			RPSdata example = db.find(RPSdata.class, _id);
			return example;
		}catch(Exception ex)
		{
			return null;
		}
	    
	}
	
	
	public void saveRPSGame(String _id,String newMatch) throws IOException
	{
		//read to see if it exist
		RPSdata data = readRPSDB(_id);
		String match ="";
		if(data == null){
			//save to cloudant
			match = newMatch;
			data = new RPSdata(_id, match);
			Database db = client.database("rockpaperscissordb", false);
			db.save(data);
			
		}else{
			//concat with new match
			match = data.getMatch() + newMatch;
			//save to database
			data.setMatch(match);
			Database db = client.database("rockpaperscissordb", false);
			db.update(data);
		}
		
	}
	
}
