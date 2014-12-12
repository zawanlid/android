package com.vu.managephonecall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vu.managephonecall.database.SchcheduledCallListDao;
import com.vu.managephonecall.receivers.AlarmReciever;

@SuppressLint("SimpleDateFormat")
public class AddScheduledCallList extends Activity {

	private EditText phonenumberEditText;
	private EditText descriptionEditText;
	private EditText dateTimeeditText;
	//private Spinner alertTypeSpinner;
	SchcheduledCallListDao schcheduledCallListDao;
	private com.vu.managephonecall.CustomDateTimePicker datetimePicket;
	private Button shceduleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_scheduled_call_list);
		
		phonenumberEditText=(EditText)findViewById(R.id.phone_number);
		descriptionEditText=(EditText)findViewById(R.id.description);
		dateTimeeditText=(EditText)findViewById(R.id.date_time);
		dateTimeeditText.setInputType(InputType.TYPE_NULL);
		shceduleButton=(Button)findViewById(R.id.schedule_time);
		customDate();
		 schcheduledCallListDao=new SchcheduledCallListDao(getApplicationContext());
		 
		 shceduleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				datetimePicket.showDialog();
				
			}
		});
		 
		 
		
	}
	@SuppressLint("SimpleDateFormat")
	public void saveScheduledCall(View view){
		
		try {
			
			String currentTime=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			
		
			
			String schedleTime=dateTimeeditText.getText().toString();
			
			Date date1=simpleDateFormat.parse(currentTime);
			Date date2=simpleDateFormat.parse(schedleTime);
			
			
			
			
			
			
			Long time = date2.getTime()-date1.getTime();
			Log.d("DATE", ""+time);
			Intent intentAlarm = new Intent(this, AlarmReciever.class);
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
			Toast.makeText(this, "Alarm Scheduled for Tommrrow", Toast.LENGTH_LONG).show();
				
			if(schcheduledCallListDao.savescheduledPhonenumber(phonenumberEditText.getText().toString(), descriptionEditText.getText().toString(),
					dateTimeeditText.getText().toString())){
				
			Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();	
			}else{

				Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
			}
			finish();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void customDate(){
		datetimePicket = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

					private String hourString;
					private String minString;
					private String secString;
					private String monthNumberString;
					private String day;

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						
						 if(String.valueOf(hour24).length()==1){
						    	hourString="0"+hour24;
						    }else{
						    	hourString=String.valueOf(hour24);
						    }
						    if(String.valueOf(min).length()==1){
						    	minString="0"+min;
						    }else{
						    	minString=String.valueOf(min);
						    }
						    if(String.valueOf(sec).length()==1){
						    	secString="0"+sec;
						    }else{
						    	secString=String.valueOf(sec);
						    }
						    if(String.valueOf(monthNumber).length()==1){
						    	
						    	if(monthNumber==9){
						    		monthNumberString=String.valueOf(monthNumber+1);
						    	}else{
						    		monthNumberString="0"+(monthNumber+1);	
						    	}
						    	
						    }else{
						    	monthNumberString=String.valueOf(monthNumber+1);
						    }
						    if(String.valueOf(date).length()==1){
						    	day="0"+date;
						    }else{
						    	day=String.valueOf(date);
						    	
						    }
						    dateTimeeditText
							.setText(  year+ "-" + monthNumberString + "-" +day+"   "+ hourString + ":" + minString+":"+secString);
						
						
									
									
									
										
										
					}

					@Override
					public void onCancel() {

					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		datetimePicket.set24HourFormat(false);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		datetimePicket.setDate(Calendar.getInstance());

	}
	
}
