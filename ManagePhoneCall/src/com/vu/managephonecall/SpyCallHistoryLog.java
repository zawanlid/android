package com.vu.managephonecall;

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
		
		Cursor cursor = this.getContentResolver().query(CallLog.Calls.CONTENT_URI,
	            null, null, null, CallLog.Calls.DATE + " DESC");
		int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
		sb.append("Call Log :");
		int counter = 1;
		while (cursor.moveToNext()) {
			String phNumber = cursor.getString(number);
			String callType = cursor.getString(type);
			String callDate = cursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = cursor.getString(duration);
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
			if (counter == 100)
				break;
			else
				counter++;
		} 

		cursor.close();
		textView.setText(sb);
	}
}
