package com.vu.managephonecall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vu.managephonecall.database.ManageCallBlockDao;

public class ManageCallBlockActivity extends Activity {
	String[] msgArray = { "No Blocked Numbers" };
	private String positiveButton;
	private String negativeButton;
	protected String phoneNumber;

	private ManageCallBlockDao manageCallBlockDao;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;
	protected String previousValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_manage_call);

		getlistItems();

	}

	public void getlistItems() {
		manageCallBlockDao = new ManageCallBlockDao(getApplicationContext());
		String[] blockedNumbers = manageCallBlockDao
				.getListOfBlockPhoneNumbers();

		if (blockedNumbers == null) {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, msgArray);

		} else {
			adapter = new ArrayAdapter<String>(this,
					R.layout.activity_listview, blockedNumbers);

		}

		ListView listView = (ListView) findViewById(R.id.block_list);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				TextView textView = (TextView) view;
				phoneNumber = textView.getText().toString();
				positiveButton = "Update";
				negativeButton = "Delete";

				previousValue = textView.getText().toString();
				// Toast.makeText(getApplicationContext(),
				// ""+textView.getText().toString(), Toast.LENGTH_LONG).show();

				alert();

			}
		});

	}

	public void addBlockNumber(View view) {

		positiveButton = "Save";
		negativeButton = "Cancel";
		alert();
	}

	public void alert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setMessage("Phone Number(Ex:+60123364045)");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		if (phoneNumber != null) {
			input.setText(phoneNumber);
			phoneNumber = null;
		}
		alert.setView(input);

		alert.setPositiveButton(positiveButton,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();

						if (positiveButton.equals("Save")) {
							if (!input.getText().toString().isEmpty()) {
								if (manageCallBlockDao
										.saveBlockedPhonenumber(value)) {
									getlistItems();
									Toast.makeText(getApplicationContext(),
											"Successfully Inserted",
											Toast.LENGTH_SHORT).show();

								} else {
									Toast.makeText(getApplicationContext(),
											"Insert Failed", Toast.LENGTH_SHORT)
											.show();

								}
							} else {
								Toast.makeText(getApplicationContext(),
										"Please enter phone number",
										Toast.LENGTH_SHORT).show();

							}

						} else {

							if (manageCallBlockDao.update(input.getText()
									.toString(), previousValue)) {
								getlistItems();
								Toast.makeText(getApplicationContext(),
										"Successfully Updated",
										Toast.LENGTH_SHORT).show();

							}

						}

					}

				});

		alert.setNegativeButton(negativeButton,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (negativeButton.equals("Delete")) {
							if (manageCallBlockDao.delete(input.getText()
									.toString().trim())) {
								getlistItems();
								Toast.makeText(getApplicationContext(),
										"Successfully Deleted",
										Toast.LENGTH_SHORT).show();

							}
						}
					}
				});

		alert.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// getlistItems();
	}
}
