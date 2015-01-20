package com.vu.managephonecall;

import static com.vu.managephonecall.util.GlobalManagePhoneCall.validate;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vu.managephonecall.database.ManageAutoAnswerDao;
import com.vu.managephonecall.util.GlobalManagePhoneCall;

public class AddAutoAnswer extends Activity {

	private EditText title;
	private EditText descrption;
	private EditText message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_auto_answer);

		title = (EditText) findViewById(R.id.title);
		descrption = (EditText) findViewById(R.id.description);
		message = (EditText) findViewById(R.id.audio_file);

	}

	@Deprecated
	public void selectFile(View view) {
		Intent intent = new Intent(getApplicationContext(),
				AndroidExplorer.class);
		startActivity(intent);
	}

	public void saveAutoAnswer(View view) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("Message Body", message.getText().toString());
		map.put("Description", descrption.getText().toString());
		map.put("Title", title.getText().toString());
		
		boolean validRequest = validate(getApplicationContext(), map);

		if (validRequest) {
			ManageAutoAnswerDao answerDao = new ManageAutoAnswerDao(
					getApplicationContext());

			if (answerDao.savesAutoAnswer(title.getText().toString(),
					descrption.getText().toString(), message.getText()
							.toString())) {
				Toast.makeText(getApplicationContext(),
						"Succussfully inserted", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "insertion failed",
						Toast.LENGTH_LONG).show();
			}
			finish();

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (GlobalManagePhoneCall.path != null) {
			message.setText(GlobalManagePhoneCall.path);

			Log.d("TAG FILE", GlobalManagePhoneCall.path);
		}
	}

}
