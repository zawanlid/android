package com.vu.managephonecall;

import static com.vu.managephonecall.util.GlobalManagePhoneCall.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vu.managephonecall.database.ManageAutoAnswerDao;
import com.vu.managephonecall.database.ManageMeetingsDao;

public class AddmanageMeetings extends Activity {

	// private EditText phonenumber;
	private EditText description;
	// private EditText dateTime;
	private Spinner days;
	private Spinner autoAnswer;
	// private Spinner autoAnswer;
	private EditText enddateTime;
	private EditText startDate;
	private Button startDateButton;
	private Button endDateButton;
	private CustomDateTimePicker custom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmanage_meetings);
		description = (EditText) findViewById(R.id.meeting_Decription);
		startDate = (EditText) findViewById(R.id.start_date);
		enddateTime = (EditText) findViewById(R.id.end_date);

		startDateButton = (Button) findViewById(R.id.select_file);
		endDateButton = (Button) findViewById(R.id.select_file_end);

		days = (Spinner) findViewById(R.id.meetings_days);

		autoAnswer = new Spinner(getApplicationContext());

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_selectable_list_item,
				new ManageAutoAnswerDao(getApplicationContext())
						.getListOfAutoAnswer());
		autoAnswer.setAdapter(spinnerArrayAdapter);

		startDateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDate(startDate);
				custom.showDialog();

			}
		});
		endDateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customDate(enddateTime);
				custom.showDialog();

			}
		});

	}

	public void saveMeetings(View view) {

		String startDateStr = startDate.getText().toString().trim();
		String endDateStr = enddateTime.getText().toString().trim();
		String desriptionStr = description.getText().toString();
		String dayStr = days.getSelectedItem().toString();
		String autoAns = autoAnswer.getSelectedItem().toString();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.US);
		if (startDateStr.length() > 19) {
			startDateStr = startDateStr.substring(0, 19);
		}
		if (endDateStr.length() > 19) {
			endDateStr = endDateStr.substring(0, 19);
		}
		boolean validRequest = false;
		try {
			simpleDateFormat.parse(startDateStr);
			simpleDateFormat.parse(endDateStr);
			validRequest = true;
		} catch (ParseException e) {
			Toast.makeText(
					getApplicationContext(),
					"Invalid date entered.\nValid date format is (YYYY-MM-DD HH:MM:SS)",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("Days", dayStr);
		map.put("End Date", endDateStr);
		map.put("Start Date", startDateStr);
		map.put("Description", desriptionStr);
		
		validRequest = validate(getApplicationContext(), map);

		if (validRequest) {
			ManageMeetingsDao meetingsDao = new ManageMeetingsDao(
					getApplicationContext());

			if (meetingsDao.savesMeetings(desriptionStr, startDateStr,
					endDateStr, dayStr, autoAns)) {
				Toast.makeText(getApplicationContext(),
						"Succussfully inserted", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "insertion failed",
						Toast.LENGTH_LONG).show();
			}

			finish();
		}

	}

	public void customDate(final EditText editText) {
		custom = new CustomDateTimePicker(this,
				new CustomDateTimePicker.ICustomDateTimeListener() {

					private String hourString;
					private String minString;
					private String secString;
					private String monthNumberString;
					private String day;

					// private SimpleDateFormat formatter;
					// private Date convertedDate;

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						Log.d("AM", "" + String.valueOf(hour24).length());
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

						editText.setText(year + "-" + monthNumberString + "-"
								+ day + " " + hourString + ":" + minString
								+ ":" + secString);

					}

					@Override
					public void onCancel() {

					}
				});
		/**
		 * Pass Directly current time format it will return AM and PM if you set
		 * false
		 */
		custom.set24HourFormat(true);
		/**
		 * Pass Directly current data and time to show when it pop up
		 */
		custom.setDate(Calendar.getInstance());

	}

}
