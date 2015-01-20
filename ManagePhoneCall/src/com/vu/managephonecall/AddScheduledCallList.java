package com.vu.managephonecall;

import static com.vu.managephonecall.util.GlobalManagePhoneCall.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vu.managephonecall.database.SchcheduledCallListDao;
import com.vu.managephonecall.receivers.AlarmReceiver;

@SuppressLint("SimpleDateFormat")
public class AddScheduledCallList extends Activity {

	private EditText phonenumberEditText;
	private EditText descriptionEditText;
	private EditText dateTimeeditText;
	// private Spinner alertTypeSpinner;
	SchcheduledCallListDao schcheduledCallListDao;
	private com.vu.managephonecall.CustomDateTimePicker datetimePicker;
	private Button shceduleButton;

	private static Logger log = Logger.getLogger(AddScheduledCallList.class.getName());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		log.info("Processing onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_scheduled_call_list);

		phonenumberEditText = (EditText) findViewById(R.id.phone_number);
		descriptionEditText = (EditText) findViewById(R.id.description);
		dateTimeeditText = (EditText) findViewById(R.id.date_time);
		dateTimeeditText.setInputType(InputType.TYPE_NULL);
		shceduleButton = (Button) findViewById(R.id.schedule_time);
		customDate();
		schcheduledCallListDao = new SchcheduledCallListDao(
				getApplicationContext());

		shceduleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				datetimePicker.showDialog();

			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	public void saveScheduledCall(View view) {

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:SS");
			String schedleTimeStr = dateTimeeditText.getText().toString();
			Date scheduleTime = simpleDateFormat.parse(schedleTimeStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(scheduleTime);
			calendar.set(Calendar.SECOND, 0);
			scheduleTime = calendar.getTime(); 
			schedleTimeStr = scheduleTime.toString();
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("Description", descriptionEditText.getText().toString());
			map.put("Phone Number", phonenumberEditText.getText().toString());
			boolean validate = validate(getApplicationContext(), map);
			
			if (validate) {
			Intent intentAlarm = new Intent(this, AlarmReceiver.class);
			intentAlarm.putExtra("com.vu.managephonecall.notification.call", descriptionEditText.getText().toString());
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, scheduleTime.getTime(), PendingIntent
					.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

			if (schcheduledCallListDao.savescheduledPhonenumber(
					phonenumberEditText.getText().toString(),
					descriptionEditText.getText().toString(), simpleDateFormat.format(calendar.getTime()))) {

				Toast.makeText(getApplicationContext(),
						"Successfully Inserted", Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(getApplicationContext(), "Insertion Failed",
						Toast.LENGTH_SHORT).show();
			}
			finish();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void customDate() {
		datetimePicker = new CustomDateTimePicker(this,
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

						if (String.valueOf(hour24).length() == 1) {
							hourString = "0" + hour24;
						} else {
							hourString = String.valueOf(hour24);
						}
						if (String.valueOf(min).length() == 1) {
							minString = "0" + min;
						} else {
							minString = String.valueOf(min);
						}
						if (String.valueOf(sec).length() == 1) {
							secString = "0" + sec;
						} else {
							secString = String.valueOf(sec);
						}
						if (String.valueOf(monthNumber).length() == 1) {

							if (monthNumber == 9) {
								monthNumberString = String
										.valueOf(monthNumber + 1);
							} else {
								monthNumberString = "0" + (monthNumber + 1);
							}

						} else {
							monthNumberString = String.valueOf(monthNumber + 1);
						}
						if (String.valueOf(date).length() == 1) {
							day = "0" + date;
						} else {
							day = String.valueOf(date);

						}
						dateTimeeditText.setText(year + "-" + monthNumberString
								+ "-" + day + "   " + hourString + ":"
								+ minString + ":" + secString);

					}

					@Override
					public void onCancel() {

					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		datetimePicker.set24HourFormat(false);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		datetimePicker.setDate(Calendar.getInstance());

	}

}
