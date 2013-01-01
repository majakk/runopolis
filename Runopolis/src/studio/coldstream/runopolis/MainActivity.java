package studio.coldstream.runopolis;


import studio.coldstream.runopolis.CoordinateConversion;
import studio.coldstream.runopolis.facebook.AsyncFacebookRunner;
import studio.coldstream.runopolis.facebook.AsyncFacebookRunner.RequestListener;
import studio.coldstream.runopolis.facebook.DialogError;
import studio.coldstream.runopolis.facebook.Facebook;
import studio.coldstream.runopolis.facebook.Facebook.DialogListener;
import studio.coldstream.runopolis.facebook.FacebookError;
import studio.coldstream.runopolis.facebook.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener{
    
	public static String TAG = "MAIN";
	public static boolean DEBUG = true;
	
	public static String FB_APP_ID = "275005152562970";
	public static int FB_METHOD = 1;
	
	public static int ivid[] = {0, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5};
	public static int hoid[] = {R.drawable.transparent, R.drawable.house1, R.drawable.house2, R.drawable.house3, R.drawable.house4, R.drawable.house5};
	public static int mpid[] = {R.raw.pick1, R.raw.pick2, R.raw.pick3, R.raw.chimes3};
		
	SharedPreferences prefsPrivate;
	SharedPreferences.Editor prefsEditor;
	
	LocationManager lm;
	
	PowerManager pm;
	PowerManager.WakeLock wl;
	
	TextView tw;
	Button buyStreet;
	Button buyHouse;
	
	List<MediaPlayer> mp;
	Vibrator vib;
	
	int nofixes;
	
	GameMechanics game;	
	DataBaseComm dbc;
	
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        Log.v(TAG, "RUNOPOLY STARTED");
        
        game = new GameMechanics();        
        dbc = new DataBaseComm();             
        
        //Fancy new login
        mFacebook = new Facebook(FB_APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);       
		
		loadPreferences();
		
		int storedcash = dbc.loadCash(game.getPlayer().getUserId());		
		if(storedcash > game.getCash())
			game.setCash(storedcash);
		Log.d(TAG, "Cash loaded: " + Integer.toString(storedcash));
        
        if(!mFacebook.isSessionValid()) {
	        mFacebook.authorize(this, new String[] { "email", "publish_stream" }, new DialogListener() {
	            @Override
	            public void onComplete(Bundle values) {	            	
                    prefsEditor.putString("access_token", mFacebook.getAccessToken());
                    prefsEditor.putLong("access_expires", mFacebook.getAccessExpires());
                    prefsEditor.commit();
                    
                    mAsyncRunner.request("me", new IDRequestListener());
	            }
	
	            @Override
	            public void onFacebookError(FacebookError error) {}
	
	            @Override
	            public void onError(DialogError e) {}
	
	            @Override
	            public void onCancel() {}
	        });       
        }
        
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "RUNOPOLIS");
        wl.acquire();
        
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                
        setContentView(R.layout.game);
        //game.setCash(1000);
        
        //game.setCurrentCard(new Card("1 V 3111 41111", "Norrmalmstorg", "Stockholm", 3, "Me", 1, 2));
        
        loadMainContent();
        updateMainContent(game.getCurrentCard());        
    }        
    
    @Override
	protected void onResume() {		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f, this);
		super.onResume();
	}
    
    @Override
	protected void onPause() {		
		lm.removeUpdates(this);		
		super.onResume();
	}
    
    @Override
	protected void onStop() {
		/* may as well just finish since saving the state is not important for this toy app */
		//wl.release();
		//Log.v(tag, "Released wlock??");
		/*for(int i = 0; i < 3; i++){        	
        	mp.get(i).release();
        } */		
		
		super.onStop();
	}
    
    @Override
   	protected void onDestroy() {
    	syncAllCash(true);
   		wl.release();
   		//Log.v(tag, "Released wlock??");
   		for(int i = 0; i < 3; i++){        	
           	mp.get(i).release();
        }
   		
   		super.onDestroy();
   	}
    
    @Override
    public void onLocationChanged(Location location) {
		Log.v(TAG, "Location Changed");		
		nofixes++;
		StringBuilder sb = new StringBuilder();
				
		//Get some cash on each fix and make sound
		game.foundCash();
		mp.get(0).start();
		//vib.vibrate(100);
		try {			
			sb.append(game.getPlayer().getUserId() + ", " + game.getPlayer().getUser() + ", " + game.getPlayer().getEmail() + "\n");
			sb.append(nofixes + ", ");			
			
			//Decode point address
			List<Address> listan = new LinkedList<Address>();
			Geocoder adam = new Geocoder(this.getBaseContext());
			listan.addAll(adam.getFromLocation(location.getLatitude(), location.getLongitude(), 9));
			Log.v(TAG, Integer.toString(nofixes));
			
			sb.append(location.getProvider());			
			
			//Conversion to UTM
			CoordinateConversion bertil = new CoordinateConversion();			
			String cesar = bertil.latLon2UTM(location.getLatitude(), location.getLongitude());						
			String[] david = cesar.split(" ");			
			
			//if new square - update!
			String newid =  david[0] + Character.getNumericValue(david[1].toCharArray()[0]) + david[2].substring(0, 3) + david[3].substring(0, 4);
					
			if(!newid.matches(game.getCurrentCard().getId()) && game.getPlayer().getUserId() != 0){
				Log.v(TAG, "NEW AREA!!!");	
				
	        	if(!dbc.existsCard(newid)){
	        		Log.v(TAG, "NEW CARD! (Insert card)");
	        		
	        		game.setCurrentCard(new Card(cesar, "" , "", 0, GameMechanics.DEFAULT_OWNER, 0, 0));
	        		game.getCurrentCard().setLocationData(this.getBaseContext(), cesar);
	        		
	        		dbc.insertCard(game.getCurrentCard());	        		
	        	}
	        	else{
	        		Log.v(TAG, "CARD EXISTS! (Load card)");
	        		Card lc = dbc.loadCard(newid); //Load the new card from db
	        		if(lc != null){
	        			Log.d(TAG,Integer.toString(lc.getOwnerId()));
	        			game.setCurrentCard(lc);
	        			game.getCurrentCard().setOwner(dbc.loadUserName(lc.getOwnerId()));
	        			game.getCurrentCard().setLocationData(this.getBaseContext(), cesar);
	        		}
	        		//potential owner / disable button issue? or will it be fixed in the update views?.... yes probably	  
	        		//pay taxes!
	        		if(game.getPlayer().getUserId() != lc.getOwnerId() && lc.getOwnerId() != 0)
	        			dbc.insertTransfer(game.getPlayer().getUserId(), lc.getOwnerId(), lc.getId(), lc.getTax(), 0); //0 = tax
	        	}	   
	        	syncAllCash(true);
	        	//make a new sound
	        	mp.get(2).start();
			}
			else{
				Log.v(TAG, "SAME OLD AREA!");
				game.getCurrentCard().setLocationData(this.getBaseContext(), cesar);
				
				syncAllCash(false);
			}
			
			//Update current card and funds							
			updateMainContent(game.getCurrentCard());
						
			sb.append(cesar + ", ");
			sb.append(game.getCurrentCard().getId() + "\n");
			sb.append("\n");
			sb.append(location.getProvider() + ", ");
			sb.append(location.getAccuracy() + "\n");
			
			/*for(int i = 0; i < listan.size(); i++){
				sb.append(listan.get(i).getThoroughfare() + ", " + listan.get(i).getLocality() + "\n");
			}*/
			
			//Debug text
			if(DEBUG) tw.setText(sb.toString());
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		if(game.getAddedCash() > 100)
			mp.get(3).start();
		
    }

	@Override
	public void onProviderDisabled(String provider) {		
		Log.v(TAG, "Disabled");

		Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {		
		Log.v(TAG, "Enabled");
		Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(TAG, "Status Changed: Out of Service");
			//Toast.makeText(this, "Status Changed: Out of Service",
			//		Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(TAG, "Status Changed: Temporarily Unavailable");
			//Toast.makeText(this, "Status Changed: Temporarily Unavailable",
			//		Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Log.v(TAG, "Status Changed: Available");
			//Toast.makeText(this, "Status Changed: Available",
			//		Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	public void loadPreferences(){				
		// Get the app's shared preferences
        //prefsPrivate = PreferenceManager.getDefaultSharedPreferences(this);
		prefsPrivate = getSharedPreferences("preferences", Context.MODE_PRIVATE); 
        int pcounter = prefsPrivate.getInt("pcounter", 0);
        
        //A program counter
        prefsEditor = prefsPrivate.edit();
        prefsEditor.putInt("pcounter", ++pcounter);
        prefsEditor.commit(); // Very important
        
        //Facebook access        
        String access_token = prefsPrivate.getString("access_token", null);
        Long access_expires = prefsPrivate.getLong("access_expires", 0);
        if(access_token != null) {
            mFacebook.setAccessToken(access_token);
        }
        if(access_expires != 0) {
            mFacebook.setAccessExpires(access_expires);
        }
		
        // App's private prefs       
        game.setPlayer(new Player(prefsPrivate.getInt("userid", 0), 
        		prefsPrivate.getString("user", "Anonomous"), 
        		prefsPrivate.getString("pass", ""), 
        		prefsPrivate.getString("email", "")));
                
        game.setCash(prefsPrivate.getInt("cash", GameMechanics.DEFAULT_CASH));
        //Start a RegisterActivity
		/*if(pcounter <= 1 || game.getPlayer().getUserId() == 0)
			showRegisterActivity();			
		return;*/
	}
	
	protected void syncAllCash(boolean online){
		prefsEditor = prefsPrivate.edit();
        prefsEditor.putInt("cash", game.getCash());
        prefsEditor.commit(); // Very important
        
        if(online){
        	dbc.syncCash(game.getPlayer().getUserId(), game.getCash());
        	
        	int income = dbc.retrieveTransfer(game.getPlayer().getUserId());
        	Log.v(TAG, "Tax Income: " + Integer.toString(income));
        	if(income > 0)
        		game.addCash(income);
        }
        
	}
	
	/*public void showRegisterActivity(){
		Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
		myIntent.putExtra("REGISTER_NAME","");
		myIntent.putExtra("REGISTER_EMAIL", "");
		myIntent.putExtra("REGISTER_PASS", "");
		MainActivity.this.startActivityForResult(myIntent, 1);      

		return;
	}*/
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

        mFacebook.authorizeCallback(requestCode, resultCode, data);
		
		/*if(resultCode == RESULT_OK){			
			String name = data.getStringExtra("REGISTER_NAME");
			String email = data.getStringExtra("REGISTER_EMAIL");
			String pass = data.getStringExtra("REGISTER_PASS");
						
			//hash pass. Should be done earlier actually.
			String hp = BCrypt.hashpw(pass, BCrypt.gensalt());
			
			//register user in database and get the userid
			int userid = dbc.insertUser(name, hp, email);
			
			//set game player
			game.setPlayer(new Player(userid, name, hp, email));
						
			//Save private data to preferences					
			SharedPreferences prefsPrivate = getSharedPreferences("preferences", Context.MODE_PRIVATE);
	        SharedPreferences.Editor prefsEditor = prefsPrivate.edit();	        
	        prefsEditor.putInt("userid", userid);
	        prefsEditor.putString("user", name);
	        prefsEditor.putString("email", email);
	        prefsEditor.putString("passw", hp);	        
	        prefsEditor.commit(); // Very important			
			
			Log.d(TAG, "Registrered Successfully! " + name + ", " + email + ", " + hp);
		}*/
	}
	
	public void loadMainContent(){
		tw = (TextView) findViewById(R.id.debugtext);
		
		mp = new LinkedList<MediaPlayer>();
        
        for(int i = 0; i < 4; i++){
        	mp.add(new MediaPlayer());
        	mp.set(i, MediaPlayer.create(MainActivity.this, mpid[i]));
        }       
        
        buyStreet = (Button) findViewById(R.id.button1);					
        buyStreet.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {  
				//add currentCard to myCards
				Card lc = dbc.loadCard(game.getCurrentCard().getId()); //Load the new card from db
				int oldid = lc.getOwnerId();
				Log.d(TAG,Integer.toString(oldid));
        		if(lc != null){
        			Log.d(TAG,Integer.toString(lc.getOwnerId()));
        			game.setCurrentCard(lc);
        			game.getCurrentCard().setOwner(dbc.loadUserName(oldid));
        			game.getCurrentCard().setLocationData(v.getContext(), game.getCurrentCard().getPosition());
        		}
        		//Not already owner?
        		if(game.getPlayer().getUserId() != oldid){
					game.getCurrentCard().setTransactions(game.getCurrentCard().getTransactions() + 1); //increase transactions!
					game.getCurrentCard().setOwner(game.getPlayer()); //claim the card!				
					game.getCurrentCard().setHouses(0); //remove those houses!
					game.addCash(-game.getCurrentCard().getValue());								
					
					//Send cash to the previous owner (replace taxes? yes!)
					Log.d(TAG,Integer.toString(oldid));
					dbc.insertTransfer(game.getPlayer().getUserId(), oldid, lc.getId(), lc.getValue(), 1); //1=buy property
					
					dbc.updateCard(game.getCurrentCard());	
        		}
				syncAllCash(true);
				
				updateMainContent(game.getCurrentCard());
				
				//TODO This will post on fb wall as soon as someone buys a street. Should be a toggle option in settings later on.
				try {
					Bundle parameters = new Bundle();
					// the message to post to the wall
			        parameters.putString("message", "I just bought " + game.getCurrentCard().getStreet().toUpperCase() + ", " + game.getCurrentCard().getArea() + "! (via Runopoly)");
					mFacebook.request("me/feed", parameters, "POST");
				} catch (FileNotFoundException e) {					
					e.printStackTrace();
				} catch (MalformedURLException e) {					
					e.printStackTrace();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
		});
        
        buyHouse = (Button) findViewById(R.id.button2);
        buyHouse.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {  
				//Do I still own the street??
				Card lc = dbc.loadCard(game.getCurrentCard().getId()); //Load the new card from db
        		if(lc != null){
        			Log.d(TAG,Integer.toString(lc.getOwnerId()));
        			game.setCurrentCard(lc);
        			game.getCurrentCard().setOwner(dbc.loadUserName(lc.getOwnerId()));
        			game.getCurrentCard().setLocationData(v.getContext(), game.getCurrentCard().getPosition());
        		}
				if(game.getPlayer().getUserId() == game.getCurrentCard().getOwnerId()){
					game.getCurrentCard().setHouses((game.getCurrentCard().getHouses() + 1) % 6);
					game.addCash(-game.getCurrentCard().getHousePrise());
					
					dbc.updateCard(game.getCurrentCard());					
				}
				else{
					Toast.makeText(v.getContext(), "Ooops, new Landlord!", Toast.LENGTH_SHORT).show();
				}
				
				syncAllCash(true);
				
				updateMainContent(game.getCurrentCard());				
			}
		});
        
        
	}	
	
	public void updateMainContent(Card cc){
		TextView cash = (TextView) findViewById(R.id.menu_text);
		if(game.getAddedCash() > 0)
			cash.setText("Cash: $"+ game.getCash() + "  (+$" + game.getAddedCash() + ")");
		else if(game.getAddedCash() < 0)
			cash.setText("Cash: $"+ game.getCash() + "  ($" + game.getAddedCash() + ")");
		else
			cash.setText("Cash: $"+ game.getCash());
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout3);
		new Color();
		ll.setBackgroundColor(Color.rgb(game.getCurrentCard().getColor()[0],
				game.getCurrentCard().getColor()[1],
				game.getCurrentCard().getColor()[2]));
		
		TextView h1 = (TextView) findViewById(R.id.textView1);
		h1.setText(cc.getStreet().toUpperCase());		
		
		TextView h2 = (TextView) findViewById(R.id.textView2);
		h2.setText(cc.getArea());		
		
		TextView own = (TextView) findViewById(R.id.textView3);
		own.setText("" + cc.getOwner());
		
		TextView val = (TextView) findViewById(R.id.textView4);
		val.setText("$" + Integer.toString(cc.getValue()));
		
		TextView tax = (TextView) findViewById(R.id.textView5);
		tax.setText("$" + Integer.toString(cc.getTax()));
		
		TextView hou = (TextView) findViewById(R.id.textView6);
		String pri = new String();
		if(cc.getHousePrise() <= 0)
			pri = " --"; 
		else
			pri = Integer.toString(cc.getHousePrise());
		hou.setText("$" + pri);		
		
		//The buttons
		buyStreet.setEnabled(game.getCash() >= game.getCurrentCard().getValue() && 
				game.getCurrentCard().getStreet() != GameMechanics.DEFAULT_STREET &&
				game.getPlayer().getUserId() != game.getCurrentCard().getOwnerId());
		
        buyHouse.setEnabled(game.getCash() >= game.getCurrentCard().getHousePrise() && 
        		game.getCurrentCard().getHouses() < 5 &&
        		game.getCurrentCard().getStreet() != GameMechanics.DEFAULT_STREET &&
        		game.getPlayer().getUserId() == game.getCurrentCard().getOwnerId());
		
        //Do the houses		
  		for(int i = 1; i <= 5; i++){
  			ImageView houses = new ImageView(this);
  			houses = (ImageView) findViewById(ivid[i]);
  			if(cc.getHouses() >= i)
  				houses.setImageResource(hoid[i]);
  			else
  				houses.setImageResource(hoid[0]);
  			houses.invalidate();
  		}  		
		
		return;
	}
	
	
	private class IDRequestListener implements RequestListener {
		private String fbid;
		private String username;
		private String email;
		
		@Override
		public void onComplete(String response, Object state) {
			try {
				// process the response here: executed in background thread
				Log.d(TAG, "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				
				fbid = json.getString("id");	
				try{
					username = json.getString("username");
				}catch(Exception e){
					e.printStackTrace();
				}
				if(username == "" || username == null)
					username = json.getString("name");
					
				email = json.getString("email");
 
				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."
				MainActivity.this.runOnUiThread(new Runnable() {
					public void run() {						
						//register user in database and get the userid
						int userid = dbc.insertUser(username, "", email, FB_METHOD, fbid);
						dbc.insertCash(userid , GameMechanics.DEFAULT_CASH);
						//set game player
						game.setPlayer(new Player(userid, username, "", email));						
						prefsEditor = prefsPrivate.edit();
						prefsEditor.putInt("userid", userid);
				        prefsEditor.putString("user", username);
				        prefsEditor.putString("email", email);
				        //prefsEditor.putString("passw", hp);	        
				        prefsEditor.commit(); // Very important
				        
				        tw.setText("Hello there, " + fbid + "!\n" + username + "\n" + email + "\n" + userid);
						Log.d(TAG,"Hello there, " + fbid + "! " + username + " : " + email + " : " + userid);
					}
				});
			} catch (JSONException e) {
				Log.w(TAG, "JSON Error in response");
			} catch (FacebookError e) {
				Log.w(TAG, "Facebook Error: " + e.getMessage());
			}
		}
 
		@Override
		public void onIOException(IOException e, Object state) {}
 
		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {}
 
		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {}
 
		@Override
		public void onFacebookError(FacebookError e, Object state) {}
 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);		    
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {	      
		    case R.id.about:
		    	Intent mainIntent = new Intent(MainActivity.this, AboutActivity.class);
		        MainActivity.this.startActivityForResult(mainIntent, -1);
		        return true;
		    case R.id.listcards:
		    	Intent mainIntent2 = new Intent(MainActivity.this, ListCardsActivity.class);
		    	mainIntent2.putExtra("USERID", game.getPlayer().getUserId());
		        MainActivity.this.startActivityForResult(mainIntent2, -1);
		        return true;
		    case R.id.statistics:
		    	Intent mainIntent3 = new Intent(MainActivity.this, StatisticsActivity.class);
		        MainActivity.this.startActivityForResult(mainIntent3, -1);
		        return true;
		    case R.id.refresh:
		    	//Refreshing stuff!	    	
		    	
		    	//New owner?
		    	Card lc = dbc.loadCard(game.getCurrentCard().getId()); //Load the new card from db
        		if(lc != null){
        			Log.d(TAG,Integer.toString(lc.getOwnerId()));
        			game.setCurrentCard(lc);
        			game.getCurrentCard().setOwner(dbc.loadUserName(lc.getOwnerId()));
        			game.getCurrentCard().setLocationData(this.getBaseContext(), game.getCurrentCard().getPosition());
        		}
		    	
        		//Cash
        		syncAllCash(true);
        		
		    	updateMainContent(game.getCurrentCard());
		    	
		    	Toast.makeText(this, "Refresh Complete!", Toast.LENGTH_SHORT).show();		
		    	
		        return true;
		    case R.id.logout:
		    	//Log out Facebook
		    	Log.d(TAG, "Logout Facebook");
		    	
		    	prefsEditor = prefsPrivate.edit();
		    	prefsEditor.putString("access_token", null);
                prefsEditor.putLong("access_expires", 0);
                prefsEditor.putString("user", null);
                prefsEditor.putString("pass", null);
                prefsEditor.putString("email", null);
                prefsEditor.putInt("userid", 0);
                prefsEditor.putInt("cash", 0);
                prefsEditor.commit();
		    	
		    	String method = "DELETE";
		        Bundle params = new Bundle();
		        /*
		         * this will revoke 'publish_stream' permission
		         * Note: If you don't specify a permission then this will de-authorize the application completely.
		         */
		        params.putString("permission", "");
		        mAsyncRunner.request("/me/permissions", params, method, new RequestListener(){

					@Override
					public void onComplete(String response, Object state) {}

					@Override
					public void onIOException(IOException e, Object state) {}

					@Override
					public void onFileNotFoundException(FileNotFoundException e, Object state) {}

					@Override
					public void onMalformedURLException(MalformedURLException e, Object state) {}

					@Override
					public void onFacebookError(FacebookError e, Object state) {}
					
		        }, null);
		        
		    	mAsyncRunner.logout(this.getBaseContext(), new RequestListener() {
					
					@Override
					public void onMalformedURLException(MalformedURLException e, Object state) {}
					
					@Override
					public void onIOException(IOException e, Object state) {}
					
					@Override
					public void onFileNotFoundException(FileNotFoundException e, Object state) {}
					
					@Override
					public void onFacebookError(FacebookError e, Object state) {}
					
					@Override
					public void onComplete(String response, Object state) {}
				});
		    	
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	    
	}
	
	
}