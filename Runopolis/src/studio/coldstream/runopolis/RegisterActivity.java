package studio.coldstream.runopolis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity{	
	
	EditText nameView;
    EditText emailView;
    EditText pass1View;
    EditText pass2View;	    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.register);
	    
	    nameView = (EditText) findViewById(R.id.editText1);
	    emailView = (EditText) findViewById(R.id.editText2);
	    pass1View = (EditText) findViewById(R.id.editText3);
	    pass2View = (EditText) findViewById(R.id.editText4);
	    
	    //The  button pressed means start than backPressed function -> intent
	    Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {  // onClick Method
				if(pass1View.getText().toString().equals(pass2View.getText().toString()) && nameView.getText().length() > 3 && emailView.getText().length() > 3){
					Log.d("REGISTER", "OK");
					onBackPressed();
				}	
				else{
					Log.d("REGISTER", "NOT OK!");
					Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	    
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
		
		Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
		mainIntent.putExtra("REGISTER_NAME",nameView.getText().toString());
		mainIntent.putExtra("REGISTER_EMAIL", emailView.getText().toString());
		mainIntent.putExtra("REGISTER_PASS", pass2View.getText().toString());
		RegisterActivity.this.setResult(RESULT_OK, mainIntent);
        RegisterActivity.this.finish();
		
	    return;
	}
	
}
