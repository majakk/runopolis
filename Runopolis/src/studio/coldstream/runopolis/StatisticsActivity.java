package studio.coldstream.runopolis;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StatisticsActivity extends Activity{
	
	private static final String TAG = "StatisticsActivity";

		
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                
        Log.v(TAG, "STATS ACTIVITY STARTED");
        
        setContentView(R.layout.stats);
	}

}
