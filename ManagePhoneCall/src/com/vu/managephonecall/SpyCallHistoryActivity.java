package com.vu.managephonecall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SpyCallHistoryActivity extends Activity {

	private EditText userName;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spy_call_history);
		
	  userName=(EditText)findViewById(R.id.user_name);	
	  password=(EditText)findViewById(R.id.password);	
	  
	}
	
	public void spyLogin(View view ){
		
	if(userName.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
		Toast.makeText(getApplicationContext(), "Please enter all values", Toast.LENGTH_SHORT).show();
	}else{
		
		if(userName.getText().toString().equalsIgnoreCase("ADMIN")||password.getText().toString().equalsIgnoreCase("ADMIN")){
			Intent intent=new Intent(getApplicationContext(), SpyCallHistoryLog.class);
			startActivity(intent);	
		}else{
			Toast.makeText(getApplicationContext(), "username/password wrong", Toast.LENGTH_SHORT).show();
	
		}
		
	}
	
	}

}
