package com.vu.managephonecall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vu.managephonecall.database.ManageAutoAnswerDao;

public class ManageAutoAnswerActivity extends Activity {
	String[] countryArray = { "No records" };
	 ManageAutoAnswerDao manageAutoAnswerDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_auto_answer);
		manageAutoAnswerDao=new ManageAutoAnswerDao(getApplicationContext());
		autoAnswersList();
	}

	private void autoAnswersList() {
		@SuppressWarnings("rawtypes")
		ArrayAdapter adapter;
	   
		String[] descriptions=manageAutoAnswerDao.getListOfAutoAnswer();
		
		    if(descriptions==null){
		    	 adapter = new ArrayAdapter<String>(this, 
					      R.layout.activity_listview, countryArray);
					
		    }else{
		    	 adapter = new ArrayAdapter<String>(this, 
					      R.layout.activity_listview, descriptions);
				
		    }
		
		ListView listView = (ListView) findViewById(R.id.auto_answer_list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				TextView textView=(TextView)view;
				
				alert(textView.getText().toString());

			}
		});

	}
	public void alert(String PhoneNumbervalue) {
		final String previousValue=PhoneNumbervalue;
		String[] values=manageAutoAnswerDao.getEditPhoneNumberDetails(PhoneNumbervalue);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// alert.setMessage("Phone Number");

		// Set an EditText view to get user input
		final EditText phoneNumber = new EditText(this);
      phoneNumber.setText(values[0]);
      
		final EditText description = new EditText(this);
        description.setText(values[1]);
        
		final EditText dateandtime = new EditText(this);
		dateandtime.setText(values[2]);
		 LinearLayout layout = new LinearLayout(getApplicationContext());
	        layout.setOrientation(LinearLayout.VERTICAL);
	        layout.addView(phoneNumber);
	        layout.addView(description);
	        layout.addView(dateandtime);

		
		alert.setView(layout);

		alert.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if(manageAutoAnswerDao.update(previousValue, phoneNumber.getText().toString(),
								description.getText().toString(), dateandtime.getText().toString())){
							autoAnswersList();
							Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
							
							
						}else{
							Toast.makeText(getApplicationContext(), "Update Fail", Toast.LENGTH_LONG).show();
										
						}
						
					}

				});

		alert.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						
						if(manageAutoAnswerDao.delete(previousValue)){
							autoAnswersList();
							Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_LONG).show();
							
							
						}else{
							Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_LONG).show();
											
						}
					}
				});

		alert.show();
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		autoAnswersList();
	}

	public void addAutoAnswer(View view) {
		Intent intent = new Intent(getApplicationContext(), AddAutoAnswer.class);
		startActivity(intent);
	}

	
}