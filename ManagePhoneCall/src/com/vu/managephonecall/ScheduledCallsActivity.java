package com.vu.managephonecall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
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
import com.vu.managephonecall.database.SchcheduledCallListDao;
import com.vu.managephonecall.receivers.AlarmReceiver;

public class ScheduledCallsActivity extends Activity {
	String[] countryArray = { "No records" };
	private SchcheduledCallListDao schcheduledCallListDao;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduled_calls);
		schcheduledCallListDao = new SchcheduledCallListDao(
				getApplicationContext());
		scheduledList();
	}

	private void scheduledList() {
		

		String[] numbers = schcheduledCallListDao
				.getListOfScheduledPhoneNumbers();
		if (numbers == null) {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, countryArray);

		} else {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, numbers);

		}

		ListView listView = (ListView) findViewById(R.id.scheduled_list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				TextView textView = (TextView) view;

				alert(textView.getText().toString());

			}
		});

	}

	public void addScheduleNumber(View view) {

		Intent intent = new Intent(getApplicationContext(),
				AddScheduledCallList.class);

		startActivity(intent);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scheduledList();
	}

	@SuppressLint("SimpleDateFormat")
	public void alert(String PhoneNumbervalue) {
		final String previousValue=PhoneNumbervalue;
		String[] values=schcheduledCallListDao.getEditPhoneNumberDetails(PhoneNumbervalue);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// alert.setMessage("Phone Number");

		// Set an EditText view to get user input
		final EditText phoneNumber = new EditText(this);
      phoneNumber.setText(values[2]);
      
		final EditText description = new EditText(this);
        description.setText(values[0]);
        
		final EditText dateandtime = new EditText(this);
		dateandtime.setText(values[1]);
		 LinearLayout layout = new LinearLayout(getApplicationContext());
	        layout.setOrientation(LinearLayout.VERTICAL);
	        layout.addView(phoneNumber);
	        layout.addView(description);
	        layout.addView(dateandtime);

		
		alert.setView(layout);

		alert.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String schedleTimeStr = dateandtime.getText().toString();
						Date scheduleTime = null;
						boolean validRequest = false;
						try {
							scheduleTime = simpleDateFormat.parse(schedleTimeStr);
							validRequest = true;
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if (!validRequest) {
							Toast.makeText(getApplicationContext(), "Invalid date entered.\nValid date format is (YYYY-MM-DD HH:MM:SS)", Toast.LENGTH_LONG).show();
						} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(scheduleTime);
						calendar.set(Calendar.SECOND, 0);
						scheduleTime = calendar.getTime(); 
						schedleTimeStr = scheduleTime.toString();
						boolean result = schcheduledCallListDao.update(previousValue, phoneNumber.getText().toString(),
								description.getText().toString(), simpleDateFormat.format(calendar.getTime()));
						
						if(result){
							Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();
							Intent intentAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
							intentAlarm.putExtra("com.vu.managephonecall.notification.call", description.getText().toString());
							AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
							alarmManager.set(AlarmManager.RTC_WAKEUP, scheduleTime.getTime(), PendingIntent
									.getBroadcast(getApplicationContext(),1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
							scheduledList();
							
						}else{
							Toast.makeText(getApplicationContext(), "Update Fail", Toast.LENGTH_LONG).show();
										
						}
						
					}}

				});

		alert.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						
						if(schcheduledCallListDao.delete(previousValue)){
							Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_LONG).show();
							scheduledList();
							
						}else{
							Toast.makeText(getApplicationContext(), "Delete Failed", Toast.LENGTH_LONG).show();
											
						}
					}
				});

		alert.show();
	}
	
	

}
