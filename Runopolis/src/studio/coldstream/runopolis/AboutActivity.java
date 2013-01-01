package studio.coldstream.runopolis;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutActivity extends Activity{
	static String url = "market://search?q=pub:Coldstream Solutions";
	
	Button butt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.about);
	    
	    butt = (Button) findViewById(R.id.button1);      
        
        butt.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {  // onClick Method
				Intent i = new Intent(Intent.ACTION_VIEW);  
				i.setData(Uri.parse(url));  
				startActivity(i);  
				
			}
		});
	}
	 
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Intent mainIntent = new Intent(AboutActivity.this,AboutActivity.class);
		AboutActivity.this.startActivity(mainIntent);
		AboutActivity.this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
	            && keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {
	        // Take care of calling this method on earlier versions of
	        // the platform where it doesn't exist.
	        onBackPressed();
	    }
	    return super.onKeyDown(keyCode, event);
	}

	//@Override
	public void onBackPressed() {
	    // This will be called either automatically for you on 2.0
	    // or later, or by the code above on earlier versions of the
	    // platform.
		//Intent mainIntent = new Intent(AboutActivity.this,MainActivity.class);
		//AboutActivity.this.startActivity(mainIntent);
		AboutActivity.this.finish();

	    return;
	}
}


   

       