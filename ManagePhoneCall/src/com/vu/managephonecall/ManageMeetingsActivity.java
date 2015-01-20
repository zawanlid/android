package com.vu.managephonecall;

import static com.vu.managephonecall.util.GlobalManagePhoneCall.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vu.managephonecall.database.ManageAutoAnswerDao;
import com.vu.managephonecall.database.ManageMeetingsDao;

public class ManageMeetingsActivity extends Activity {
	String[] countryArray = { "No Records" };

	ManageMeetingsDao meetingsDao;

	private Spinner autoAnswer;

	private Spinner days;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_meetings);
		meetingsDao = new ManageMeetingsDao(getApplicationContext());

		manageMeetingList();

	}

	public void addManageMeetings(View view) {
		Intent intent = new Intent(getApplicationContext(),
				AddmanageMeetings.class);
		startActivity(intent);
	}

	private void manageMeetingList() {
		@SuppressWarnings("rawtypes")
		ArrayAdapter adapter;
		String[] descriptions = meetingsDao.getListOfMeetings();

		if (descriptions == null) {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, countryArray);

		} else {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, descriptions);

		}

		ListView listView = (ListView) findViewById(R.id.meeting_list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				TextView textView = (TextView) view;
				if (!"No Records".equalsIgnoreCase(textView.getText()
						.toString())) {
					alert(textView.getText().toString());
				}

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		manageMeetingList();
	}

	public void alert(String previousVal) {
		final String previousValue = previousVal;
		String[] values = meetingsDao.getEditDetails(previousVal);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// alert.setMessage("Phone Number");

		// Set an EditText view to get user input
		final EditText description = new EditText(this);
		description.setText(values[0]);

		final EditText startDate = new EditText(this);
		startDate.setText(values[1]);

		final EditText endTime = new EditText(this);
		endTime.setText(values[2]);

		days = new Spinner(getApplicationContext());
		ArrayAdapter<String> daysArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_selectable_list_item, getResources()
						.getStringArray(R.array.days_arrays));
		days.setAdapter(daysArrayAdapter);
		SelectSpinnerItemByValue(days, values[3]);

		autoAnswer = new Spinner(getApplicationContext());

		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_selectable_list_item,
				new ManageAutoAnswerDao(getApplicationContext())
						.getListOfAutoAnswer());
		autoAnswer.setAdapter(spinnerArrayAdapter);
		SelectSpinnerItemByValue(autoAnswer, values[4]);

		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(description);
		layout.addView(startDate);
		layout.addView(endTime);
		layout.addView(days);
		layout.addView(autoAnswer);

		alert.setView(layout);

		alert.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						String startDateStr = startDate.getText().toString().trim();
						String endTimeStr = endTime.getText().toString().trim();
						String desriptionStr = description.getText().toString();
						String dayStr = days.getSelectedItem().toString();
						String autoAns = autoAnswer.getSelectedItem().toString();
						
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.US);

						if (startDateStr.length() > 19) {
							startDateStr = startDateStr.substring(0, 19);
						}

						if (endTimeStr.length() > 19) {
							endTimeStr = endTimeStr.substring(0, 19);
						}
						boolean validRequest = false;
						try {
							simpleDateFormat.parse(startDateStr);
							simpleDateFormat.parse(endTimeStr);
							validRequest = true;
						} catch (ParseException e) {
							Toast.makeText(
									getApplicationContext(),
									"Invalid date entered.\nValid date format is (YYYY-MM-DD HH:MM:SS)",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

						Map<String, String> map = new HashMap<String, String>();
						map.put("Auto Answer", autoAns);
						map.put("Days", dayStr);
						map.put("End Date", endTimeStr);
						map.put("Start Date", startDateStr);
						map.put("Description", desriptionStr);

						validRequest = validate(getApplicationContext(), map);
						if (validRequest) {
							if (meetingsDao.update(previousValue, desriptionStr, startDateStr,
									endTimeStr, dayStr, autoAns)) {
								manageMeetingList();
								Toast.makeText(getApplicationContext(),
										"Updated successfully",
										Toast.LENGTH_LONG).show();

							} else {
								Toast.makeText(getApplicationContext(),
										"Update Fail", Toast.LENGTH_LONG)
										.show();

							}
						}

					}

				});

		alert.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (meetingsDao.delete(previousValue)) {
							manageMeetingList();
							Toast.makeText(getApplicationContext(),
									"Delete successfully", Toast.LENGTH_LONG)
									.show();

						} else {
							Toast.makeText(getApplicationContext(),
									"Delete Failed", Toast.LENGTH_LONG).show();

						}
					}
				});

		alert.show();
	}

	private void SelectSpinnerItemByValue(Spinner spnr, String value) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnr.getAdapter();
		for (int position = 0; position < adapter.getCount(); position++) {
			if (adapter.getItem(position).equals(value)) {
				spnr.setSelection(position);
				return;
			}
		}
	}
}
