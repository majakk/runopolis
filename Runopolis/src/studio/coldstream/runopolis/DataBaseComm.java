package studio.coldstream.runopolis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DataBaseComm {
	public static String TAG = "DATABASECOMM";
	
	private static String POST_CARD_ADDR = "http://scalaris.servebeer.com/runopolis/post_card.php";
	private static String POST_USER_ADDR = "http://scalaris.servebeer.com/runopolis/post_user.php";
	private static String POST_CASH_ADDR = "http://scalaris.servebeer.com/runopolis/post_cash.php";
	
	private InputStream is;
	
	public DataBaseComm(){
		;
	}
	
	//---------------------------------------------------
	//		CARD
	//---------------------------------------------------
	public boolean insertCard(Card insc){
	    String result = "";
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "insert_card"));
	    //Log.d("LOGG",insc.getId().toString());
	    nameValuePairs.add(new BasicNameValuePair("cardid",insc.getId()));
	    nameValuePairs.add(new BasicNameValuePair("transactions", Integer.toString(insc.getTransactions())));
	    nameValuePairs.add(new BasicNameValuePair("userid",Integer.toString(insc.getOwnerId())));
	    nameValuePairs.add(new BasicNameValuePair("position",insc.getPosition()));
	    nameValuePairs.add(new BasicNameValuePair("street",insc.getStreet()));
	    nameValuePairs.add(new BasicNameValuePair("area",insc.getArea()));
	    nameValuePairs.add(new BasicNameValuePair("houses",Integer.toString(insc.getHouses())));	    
	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CARD_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString());
	    }
	    Log.d("INSERTCARD", String.valueOf(result.contains("true")));
	   
	    return result.contains("true");
	}
	
	public boolean updateCard(Card insc){
	    String result = "";
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "update_card"));
	    //Log.d("LOGG",insc.getId().toString());
	    nameValuePairs.add(new BasicNameValuePair("cardid",insc.getId()));
	    nameValuePairs.add(new BasicNameValuePair("transactions", Integer.toString(insc.getTransactions())));
	    nameValuePairs.add(new BasicNameValuePair("userid",Integer.toString(insc.getOwnerId())));
	    nameValuePairs.add(new BasicNameValuePair("position",insc.getPosition()));
	    nameValuePairs.add(new BasicNameValuePair("street",insc.getStreet()));
	    nameValuePairs.add(new BasicNameValuePair("area",insc.getArea()));
	    nameValuePairs.add(new BasicNameValuePair("houses",Integer.toString(insc.getHouses())));	    
	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CARD_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString());
	    }
	    Log.d("UPDATECARD", String.valueOf(result.contains("true")));
	   
	    return result.contains("true");
	}
	
	public boolean existsCard(String caid){
	    String result = "";
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "exists_card"));
	    
	    nameValuePairs.add(new BasicNameValuePair("cardid", caid));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CARD_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("EXSISTSCARD", result);
	    Log.d("EXSISTSCARD", String.valueOf(result.contains("true")));
	    
	    return result.contains("true");
	}
	
	public Card loadCard(String caid){
	    String result = "";
	    Card rt = null;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "load_card"));
	    
	    nameValuePairs.add(new BasicNameValuePair("cardid", caid));
	    Log.d(TAG, caid);
	   	    
	    //http post
	    try{
	    	
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CARD_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("LOADCARD", result);
	    Log.d("LOADCARD", String.valueOf(result.contains("true")));
	    //parse json data
	    if(result != null){
		    try{
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i(TAG,"cardid: "+json_data.getString("cardid")+
		                            ", transactions: "+json_data.getInt("transactions")+
		                            ", userid: "+json_data.getInt("userid")+
		                            ", position: "+json_data.getString("position")+
		                            ", street: "+json_data.getString("street")+
		                            ", area: "+json_data.getString("area")+
		                            ", houses: "+json_data.getInt("houses")
		                    );
		                    rt = new Card(json_data.getString("position"),json_data.getString("street"),json_data.getString("area"),
		                    		json_data.getInt("transactions"), "", json_data.getInt("userid"),json_data.getInt("houses"));
		            }
		    
		    }catch(JSONException e){
		            Log.e(TAG, "Error parsing data "+e.toString());
		            return null;
		    }	    
	    }
	    
	    return rt;
	}
	
	public List<Card> listCards(String userid){
	    String result = "";
	    List<Card> rt = new LinkedList<Card>();
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "list_cards"));
	    
	    nameValuePairs.add(new BasicNameValuePair("userid", userid));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CARD_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("LISTCARDS", result);
	    Log.d("LISTCARDS", String.valueOf(result.contains("true")));
	    //parse json data
	    if(result != null){
		    try{
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i(TAG,"cardid: "+json_data.getString("cardid")+
		                            ", transactions: "+json_data.getInt("transactions")+
		                            ", userid: "+json_data.getInt("userid")+
		                            ", position: "+json_data.getString("position")+
		                            ", street: "+json_data.getString("street")+
		                            ", area: "+json_data.getString("area")+
		                            ", houses: "+json_data.getInt("houses")
		                    );
		                    rt.add(new Card(json_data.getString("position"),json_data.getString("street"),json_data.getString("area"),
		                    		json_data.getInt("transactions"), "", json_data.getInt("userid"),json_data.getInt("houses")));
		            }
		    
		    }catch(JSONException e){
		            Log.e(TAG, "Error parsing data "+e.toString());
		            return null;
		    }	    
	    }
	    
	    return rt;
	}


	
	//---------------------------------------------------
	//		USER
	//---------------------------------------------------
	public int insertUser(String name, String pass, String email, int method, String fbid){
	    String result = "";
	    int userid = 0;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "insert_user"));
	    //Log.d("LOGG",insc.getId().toString());
	    nameValuePairs.add(new BasicNameValuePair("name", name));
	    nameValuePairs.add(new BasicNameValuePair("pass", pass));
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	    nameValuePairs.add(new BasicNameValuePair("loginmethod", Integer.toString(method)));
	    nameValuePairs.add(new BasicNameValuePair("fbid", fbid));
	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_USER_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString());
	    }
	    Log.d("INSERT_USER", result);
	    Log.d("INSERT_USER", String.valueOf(result.contains("true")));
	    
	    //Parse response to get userid
	    try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Log.i(TAG,"userid: "+json_data.getInt("userid")+
                            ", name: "+json_data.getString("name")+
                            ", pass: "+json_data.getString("pass")+
                            ", email: "+json_data.getString("email")
                           
                    );
                    userid = json_data.getInt("userid");
            }    
	    }catch(JSONException e){
	            Log.e(TAG, "Error parsing data " + e.toString());	            
	    }	    	    
	   
	    return userid;
	}
			
	public boolean existsUser(String email){
	    String result = "";
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "exists_user"));
	    
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_USER_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("EXSISTS_USER", result);
	    Log.d("EXSISTS_USER", String.valueOf(result.contains("true")));
	    
	    return result.contains("true");
	}
	
	public Player loadUser(String email){
	    String result = "";
	    Player pl = null;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "load_user"));
	    
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_USER_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("LOAD_USER", result);
	    Log.d("LOAD_USER", String.valueOf(result.contains("true")));
	    //parse json data
	    try{
	            JSONArray jArray = new JSONArray(result);
	            for(int i=0;i<jArray.length();i++){
	                    JSONObject json_data = jArray.getJSONObject(i);
	                    Log.i(TAG,"userid: "+json_data.getInt("userid")+
	                            ", name: "+json_data.getString("name")+
	                            ", pass: "+json_data.getString("pass")+
	                            ", email: "+json_data.getString("email")
	                           
	                    );
	                    pl = new Player(json_data.getInt("userid"), json_data.getString("name"), json_data.getString("pass"), json_data.getString("email"));
	            }
	    
	    }catch(JSONException e){
	            Log.e(TAG, "Error parsing data "+e.toString());
	            return null;
	    }	    
	    
	    return pl;
	}	
	
	public String loadUserName(int uid){	
	    String result = "";
	    String name = null;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "load_user_name"));
	    
	    nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(uid)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_USER_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("LOAD_USER_NAME", result);
	    Log.d("LOAD_USER_NAME", String.valueOf(result.contains("true")));
	    //parse json data
	    try{
	            JSONArray jArray = new JSONArray(result);
	            for(int i=0;i<jArray.length();i++){
	                    JSONObject json_data = jArray.getJSONObject(i);
	                    Log.i(TAG,"userid: "+json_data.getInt("userid")+
	                            ", name: "+json_data.getString("name")+
	                            ", pass: "+json_data.getString("pass")+
	                            ", email: "+json_data.getString("email")	                           
	                    );
	                    name = json_data.getString("name");
	            }
	    
	    }catch(JSONException e){
	            Log.e(TAG, "Error parsing data "+e.toString());
	            return null;
	    }	    
	    
	    return name;
	}	
	

	//---------------------------------------------------
	//		CASH
	//---------------------------------------------------
	
	public int loadCash(int uid){	
	    String result = "";
	    int cash = 0;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "load_cash"));
	    
	    nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(uid)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CASH_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("LOAD_CASH", result);
	    Log.d("LOAD_CASH", String.valueOf(result.contains("true")));
	    //parse json data
	    if(result != null){
		    try{
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i(TAG,"userid: "+json_data.getInt("userid")+
		                            ", ammount: "+json_data.getInt("ammount")	                           
		                    );
		                    cash = json_data.getInt("ammount");
		            }
		    
		    }catch(JSONException e){
		            Log.e(TAG, "Error parsing data "+e.toString());
		            return 0;
		    }	    
	    }
	    
	    return cash;
	}
	
	public void insertCash(int uid, int ammount){	
	    String result = null;
	    //int cash = 0;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "insert_cash"));
	    
	    nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(uid)));
	    nameValuePairs.add(new BasicNameValuePair("ammount", Integer.toString(ammount)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CASH_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("INSERT_CASH", result);
	    Log.d("INSERT_CASH", String.valueOf(result.contains("true")));	    
	}
	
	public void syncCash(int uid, int ammount){	
	    String result = null;
	  
	    //post payload
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	    	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "sync_cash"));	    
	    nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(uid)));
	    nameValuePairs.add(new BasicNameValuePair("ammount", Integer.toString(ammount)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CASH_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("SYNC_CASH", result);
	    Log.d("SYNC_CASH", String.valueOf(result.contains("true")));
	    
	}
	
	public void insertTransfer(int fromuserid, int destuserid, String cardid, int ammount, int type){	
	    String result = null;
	    //int cash = 0;
	    //the year data to send
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    //nameValuePairs.add(new BasicNameValuePair("year","1980"));
	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "insert_transfer"));
	    
	    nameValuePairs.add(new BasicNameValuePair("fromuserid", Integer.toString(fromuserid)));
	    nameValuePairs.add(new BasicNameValuePair("destuserid", Integer.toString(destuserid)));
	    nameValuePairs.add(new BasicNameValuePair("cardid", cardid));
	    nameValuePairs.add(new BasicNameValuePair("ammount", Integer.toString(ammount)));
	    nameValuePairs.add(new BasicNameValuePair("transtype", Integer.toString(type)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CASH_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("INSERT_TAX", result);
	    Log.d("INSERT_TAX", String.valueOf(result.contains("true")));	    
	}

	public int retrieveTransfer(int uid){	
	    String result = "";
	    int cash = 0;
	    
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	    
	    nameValuePairs.add(new BasicNameValuePair("operation", "retrieve_transfer"));	    
	    nameValuePairs.add(new BasicNameValuePair("userid", Integer.toString(uid)));
	   	    
	    //http post
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new HttpPost(POST_CASH_ADDR);
	            httppost.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();
	    }catch(Exception e){
	            Log.e(TAG, "Error in http connection "+e.toString());
	    }
	    //convert response to string	    
	    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            is.close();
	     
	            result=sb.toString();
	    }catch(Exception e){
	            Log.e(TAG, "Error converting result "+e.toString()); 
	    }
	    Log.d("RETRIEVE_TAX", result);
	    Log.d("RETRIEVE_TAX", String.valueOf(result.contains("true")));
	    //parse json data
	    if(result != null){
		    try{
		            JSONArray jArray = new JSONArray(result);
		            for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i(TAG,"cardid: "+json_data.getInt("cardid")+
		                            ", ammount: "+json_data.getInt("ammount")	                           
		                    );
		                    cash += json_data.getInt("ammount");
		                    Log.d("RETRIEVE_TAX", String.valueOf(cash));
		            }
		    
		    }catch(JSONException e){
		            Log.e(TAG, "Error parsing data "+e.toString());
		            return 0;
		    }	    
	    }
	    
	    return cash;
	}	
}





