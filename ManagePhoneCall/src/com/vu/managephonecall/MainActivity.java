package com.vu.managephonecall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	
	public void managePhoneCall(View view){
		Intent intent=new Intent(getApplicationContext(),ManageCallBlockActivity.class);
		
		startActivity(intent);
		
	}
	
	public void scheduledCalls(View view){
		Intent intent=new Intent(getApplicationContext(),ScheduledCallsActivity.class);
		
		startActivity(intent);
		
	}
	
	public void manageAutoAnswer(View view){
		Intent intent=new Intent(getApplicationContext(),ManageAutoAnswerActivity.class);
		
		startActivity(intent);
		
	}
	
	public void manageMeetings(View view){
		Intent intent=new Intent(getApplicationContext(),ManageMeetingsActivity.class);
		
		startActivity(intent);
		
	}
	
	public void spyCallHistory(View view){
		Intent intent=new Intent(getApplicationContext(),SpyCallHistoryActivity.class);
		
		startActivity(intent);
		
	}
	
	
	
}
