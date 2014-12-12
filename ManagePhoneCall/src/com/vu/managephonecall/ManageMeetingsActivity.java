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
				alert(textView.getText().toString());

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		manageMeetingList();
	}

	public void alert(String PhoneNumbervalue) {
		final String previousValue = PhoneNumbervalue;
		String[] values = meetingsDao
				.getEditPhoneNumberDetails(PhoneNumbervalue);
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
		autoAnswer = new Spinner(getApplicationContext());

		if (new ManageAutoAnswerDao(getApplicationContext())
				.getListOfAutoAnswer() != null) {
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_selectable_list_item,
					new ManageAutoAnswerDao(getApplicationContext())
							.getListOfAutoAnswer());
			autoAnswer.setAdapter(spinnerArrayAdapter);
		}
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
						if (meetingsDao.update(previousValue, description
								.getText().toString(), startDate.getText()
								.toString(), endTime.getText().toString(),
								endTime.getText().toString(), days
										.getSelectedItem().toString())) {
							manageMeetingList();
							Toast.makeText(getApplicationContext(),
									"Updated successfully", Toast.LENGTH_LONG);

						} else {
							Toast.makeText(getApplicationContext(),
									"Update Fail", Toast.LENGTH_LONG);

						}

					}

				});

		alert.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (meetingsDao.delete(previousValue)) {
							manageMeetingList();
							Toast.makeText(getApplicationContext(),
									"Delete successfully", Toast.LENGTH_LONG);

						} else {
							Toast.makeText(getApplicationContext(),
									"Delete Failed", Toast.LENGTH_LONG);

						}
					}
				});

		alert.show();
	}

}
