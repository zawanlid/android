package com.vu.managephonecall;

import static com.vu.managephonecall.util.GlobalManagePhoneCall.validate;

import java.util.HashMap;
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
		manageAutoAnswerDao = new ManageAutoAnswerDao(getApplicationContext());
		autoAnswersList();
	}

	private void autoAnswersList() {
		@SuppressWarnings("rawtypes")
		ArrayAdapter adapter;

		String[] descriptions = manageAutoAnswerDao.getListOfAutoAnswer();

		if (descriptions == null) {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, countryArray);

		} else {

			if (descriptions.length > 0
					&& "default".equalsIgnoreCase(descriptions[0])) {
				adapter = new ArrayAdapter<String>(this,
						R.layout.activity_listview, countryArray);

			} else {
				
				String [] descList= new String[descriptions.length-1];
				int count = 0;
				for (String desc:descriptions) {
					if (!"default".equalsIgnoreCase(desc)){
						descList[count] = desc;
						count++;
					}
					
				}
				adapter = new ArrayAdapter<String>(this,
						R.layout.activity_listview, descList);
			}

		}

		ListView listView = (ListView) findViewById(R.id.auto_answer_list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				
				TextView textView = (TextView) view;

				if (!"No records".equalsIgnoreCase(textView.getText().toString())) {
					alert(textView.getText().toString());
				}

			}
		});

	}

	public void alert(String PhoneNumbervalue) {
		final String previousValue = PhoneNumbervalue;
		String[] values = manageAutoAnswerDao
				.getEditPhoneNumberDetails(PhoneNumbervalue);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		// alert.setMessage("Phone Number");

		// Set an EditText view to get user input
		final EditText title = new EditText(this);
		title.setText(values[0]);

		final EditText description = new EditText(this);
		description.setText(values[1]);

		final EditText msgBody = new EditText(this);
		msgBody.setText(values[2]);
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(title);
		layout.addView(description);
		layout.addView(msgBody);

		alert.setView(layout);

		alert.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						Map<String, String> map = new HashMap<String, String>();
						map.put("Message Body", msgBody.getText().toString());						
						map.put("Description", description.getText().toString());
						map.put("Title", title.getText().toString());						
						
						boolean validRequest = validate(getApplicationContext(), map);
						
						if (validRequest) {
							if (manageAutoAnswerDao.update(previousValue,
									title.getText().toString(),
									description.getText().toString(),
									msgBody.getText().toString())) {
								autoAnswersList();
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

						if (manageAutoAnswerDao.delete(previousValue)) {
							autoAnswersList();
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
