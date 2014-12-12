package com.vu.managephonecall;

import com.vu.managephonecall.database.ManageAutoAnswerDao;
import com.vu.managephonecall.util.GlobalManagePhoneCall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddAutoAnswer extends Activity {

	private EditText title;
	private EditText descrption;
	private EditText audioFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_auto_answer);
		
		title=(EditText)findViewById(R.id.title);
		descrption=(EditText)findViewById(R.id.description);
		audioFile=(EditText)findViewById(R.id.audio_file);
		
	}
	
	public void selectFile(View view){
		Intent intent=new Intent(getApplicationContext(), AndroidExplorer.class);
		startActivity(intent);
	}
	public void saveAutoAnswer(View view){
		
		
		
		ManageAutoAnswerDao answerDao=new ManageAutoAnswerDao(getApplicationContext());
		
		if(answerDao.savesAutoAnswer(title.getText().toString(), descrption.getText().toString(), audioFile.getText().toString())){
		Toast.makeText(getApplicationContext(), "Succussfully inserted", Toast.LENGTH_LONG).show();	
		}else{
			Toast.makeText(getApplicationContext(), "insertion failed", Toast.LENGTH_LONG).show();
		}
		finish();
	
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(GlobalManagePhoneCall.path!=null){
		audioFile.setText(GlobalManagePhoneCall.path);
		
		Log.d("TAG FILE", GlobalManagePhoneCall.path);
		}
	}
	

}
