package com.vu.managephonecall;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;

public class SpyCallHistoryLog extends Activity {
	String[] countryArray = { "4654754", "465747685", "6576547", "654765486859" };
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spy_call_history_log);
		textView = (TextView) findViewById(R.id.textview_call);
		  getCallDetails();
	}

	private void getCallDetails() {
		StringBuffer sb = new StringBuffer();
		@SuppressWarnings("deprecation")
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
				null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		sb.append("Call Log :");
		managedCursor.moveToLast();
		while (managedCursor.moveToPrevious()) {
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString(duration);
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;
			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;
			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			sb.append("\nPhone Number:" + phNumber + " \nCall Type: "
					+ dir + " \nCall Date:" + callDayTime
					+ " \nCall duration in sec :" + callDuration);
			sb.append("\n----------------------------------");
			 textView.setText(sb);
		} // managedCursor.close(); textView.setText(sb); }

	}
}
