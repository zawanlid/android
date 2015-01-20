package com.vu.managephonecall.util;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.widget.Toast;

public class GlobalManagePhoneCall {

	
	public static String path=null;
	
	public static boolean validate(Context context, Map<String,String> map) {
		
		Set<String> keySet = map.keySet();
		for (String key: keySet) {
			if (map.get(key) == null || map.get(key).trim().isEmpty()){
				Toast.makeText(context,	"Please enter value for " + key, Toast.LENGTH_LONG).show();
				return false;
			}
		}
		
		return true;
	}
}
