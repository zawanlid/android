package com.android.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.app.R;
import com.android.app.constants.Constants;
import com.android.app.db.DatabaseContract.User;
import com.android.app.db.DatabaseHelper;
import com.android.app.model.SignUp;
import com.android.app.service.MainService;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.android.app.MESSAGE";

	DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
	String msg = "Android : ";

	SignUp signup = new SignUp();
	List<SignUp> signupList = new ArrayList<SignUp>();
	SignupAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(msg, "The onCreate() event");
		super.onCreate(savedInstanceState);
		// set they screen layout defined in activity_main.xml
		setContentView(R.layout.activity_main);

		ListView list = (ListView) findViewById(R.id.users);
		adapter = new SignupAdapter();
		list.setAdapter(adapter);

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor c = db.rawQuery("select " + User.COLUMN_USER_NAME + ","
				+ User.COLUMN_EMAIL + "," + User.COLUMN_GENDER + " from "
				+ User.TABLE_NAME, null);

		SignUp signUpOb = null;
		while (c.moveToNext()) {
			signUpOb = new SignUp();
			signUpOb.setUsername(c.getString(0));
			signUpOb.setEmail(c.getString(1));
			signUpOb.setGender(c.getString(2));
			adapter.add(signUpOb);
		}
		c.close();
		db.close();

		// implicit and explicit intents
		Button startBrowser = (Button) findViewById(R.id.start_browser);
		startBrowser.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse("http://fx-rate.net/MYR/PKR/"));
				startActivity(i);
			}
		});
		Button startPhone = (Button) findViewById(R.id.start_phone);
		startPhone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
						.parse("tel:0142970820"));
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		Log.d(msg, "The onCreateOptionsMenu() event");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/** Called when the activity is about to become visible. */
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(msg, "The onStart() event");

	}

	// user events
	public void startService(View view) {
		startService(new Intent(getBaseContext(), MainService.class));
	}

	// user events
	public void stopService(View view) {
		stopService(new Intent(getBaseContext(), MainService.class));
	}

	/** Called when the activity has become visible. */
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(msg, "The onResume() event");
	}

	/** Called when another activity is taking focus. */
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(msg, "The onPause() event");
	}

	/** Called when the activity is no longer visible. */
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(msg, "The onStop() event");

	}

	/** Called just before the activity is destroyed. */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(msg, "The onDestroy() event");
	}

	/** Called when the user clicks the Sign Up button */
	public void signup(View view) {

		// get view values using intent
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.username);
		signup.setUsername(editText.getText().toString());
		editText = (EditText) findViewById(R.id.email);
		signup.setEmail(editText.getText().toString());
		editText = (EditText) findViewById(R.id.pwd);
		signup.setPwd(editText.getText().toString());

		RadioGroup gender = (RadioGroup) findViewById(R.id.gender);
		switch (gender.getCheckedRadioButtonId()) {
		case R.id.male:
			signup.setGender(Constants.GENDER_MALE);
			break;
		case R.id.female:
			signup.setGender(Constants.GENDER_FEMALE);
			break;
		}

		if (signup.getUsername().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter Username!",
					Toast.LENGTH_SHORT).show();
		} else if (signup.getEmail().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter E-Mail!",
					Toast.LENGTH_SHORT).show();
		} else if (signup.getPwd().length() == 0) {
			Toast.makeText(getApplicationContext(), "Please enter password!",
					Toast.LENGTH_SHORT).show();
		} else if (signup.getGender() == null) {
			Toast.makeText(getApplicationContext(), "Please select Gender!",
					Toast.LENGTH_SHORT).show();
		} else {

			// check if user name already exists in the DB
			// Gets the data repository in write mode
			SQLiteDatabase db = mDbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("select distinct " + User.COLUMN_USER_NAME
					+ " from " + User.TABLE_NAME + " where "
					+ User.COLUMN_USER_NAME + " = '" + signup.getUsername()
					+ "'", null);

			if (c.moveToFirst()
					&& signup.getUsername().equalsIgnoreCase(c.getString(0))) {
				Toast.makeText(getApplicationContext(),
						"[" + signup.getUsername() + "] User Already Exists!",
						Toast.LENGTH_SHORT).show();

			} else {
				// Create a new map of values, where column names are the keys
				ContentValues values = new ContentValues();
				values.put(User.COLUMN_USER_NAME, signup.getUsername());
				values.put(User.COLUMN_EMAIL, signup.getEmail());
				values.put(User.COLUMN_PASSWORD, signup.getPwd());
				values.put(User.COLUMN_GENDER, signup.getGender());

				// Insert the new row, returning the primary key value of the
				// new row
				long newRowId;
				newRowId = db
						.insert(User.TABLE_NAME, User.COLUMN_EMAIL, values);

				Toast.makeText(getApplicationContext(),
						"[" + newRowId + "] Record(s) Successfull Added",
						Toast.LENGTH_SHORT).show();
				intent.putExtra(EXTRA_MESSAGE, signup.getEmail());
				startActivity(intent);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Log.d(msg, "The onOptionsItemSelected() event");
		switch (item.getItemId()) {
		case R.id.action_search:
			openSearch();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSearch() {
		Toast.makeText(this, "Search button pressed", Toast.LENGTH_SHORT)
				.show();
	}

	private void openSettings() {
		startActivityForResult(new Intent(
				android.provider.Settings.ACTION_SETTINGS), 0);
	}

	class SignupAdapter extends ArrayAdapter<SignUp> {

		SignupAdapter() {
			super(MainActivity.this, android.R.layout.simple_list_item_1,
					signupList);
		}

		/*
		 * (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View _view, ViewGroup parent) {
			View rowView = _view;
			SignupHolder holder = null;
			if (rowView == null) {
				LayoutInflater inflater = getLayoutInflater();
				rowView = inflater.inflate(R.layout.row, parent, false);
				holder = new SignupHolder(rowView);
				rowView.setTag(holder);
			} else {
				holder = (SignupHolder) rowView.getTag();
			}
			holder.populateFrom(signupList.get(position));
			return (rowView);
		}
	}

	static class SignupHolder {
		private TextView username = null;
		private TextView email = null;
		private ImageView genderIcon = null;
		private View row = null;

		SignupHolder(View _rowView) {
			row = _rowView;
			username = (TextView) row.findViewById(R.id.title);
			email = (TextView) row.findViewById(R.id.description);
			genderIcon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(SignUp r) {
			username.setText(r.getUsername());
			username.setTextColor(Color.RED);
			email.setText(r.getEmail());
			if (r.getGender().equals(Constants.GENDER_MALE)) {
				genderIcon.setImageResource(R.drawable.ic_launcher);
			} else if (r.getGender().equals(Constants.GENDER_FEMALE)) {
				genderIcon.setImageResource(R.drawable.my_app_launcher);
			}
		}
	}
}
