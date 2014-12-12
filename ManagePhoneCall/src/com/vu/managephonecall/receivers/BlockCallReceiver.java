package com.vu.managephonecall.receivers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vu.managephonecall.database.ManageAutoAnswerDao;
import com.vu.managephonecall.database.ManageCallBlockDao;
import com.vu.managephonecall.database.ManageMeetingsDao;

public class BlockCallReceiver extends BroadcastReceiver {
	Context context;
	private Class<?> c;
	private Timestamp ts;
	private Timestamp startTimeStamp;
	private Timestamp endTimeStamp;
	private String message = "Please call me later,I am in meeting";
	private String incomingNumber;
	private Intent intent;
	private ManageMeetingsDao meetingsDao;
	private ManageAutoAnswerDao answerDao;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			this.intent=intent;
			 answerDao=new ManageAutoAnswerDao(context);
				
			ManageCallBlockDao manageCallBlockDao = new ManageCallBlockDao(
					context);
			String[] blockNumbers = manageCallBlockDao
					.getListOfBlockPhoneNumbers();
			 incomingNumber = intent
						.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			if (blockNumbers != null) {
			
				System.out.println("--------------my number---------"
						+ incomingNumber);
				ArrayList<String> arrayList = new ArrayList<String>(
						Arrays.asList(blockNumbers));

				arrayList.contains(incomingNumber);
					blockNumber();

				} else {
					 meetingsDao = new ManageMeetingsDao(
							context);
					Map<String, String> meetingTimes = meetingsDao
							.getListOfMeetingsTimings();

					Iterator<Map.Entry<String, String>> entries = meetingTimes
							.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String, String> entry = entries.next();

						String text = entry.getKey();
						startTimeStamp = Timestamp.valueOf(text);

						String text1 = entry.getValue();
						endTimeStamp = Timestamp.valueOf(text1);

						if(sendMessage(startTimeStamp, endTimeStamp,
								incomingNumber)){
							break;
						}
						System.out.println("Key = " + ts + ", Value = "
								+ entry.getValue());

					}

				}
			

		} catch (Exception ex) { // Many things can go wrong with reflection
									// calls
			ex.printStackTrace();
		}

	}

	public boolean sendMessage(Timestamp startTime, Timestamp endTime,
			String incomingNumber) {

		java.util.Date date = new java.util.Date();
		Timestamp currentTime = new Timestamp(date.getTime());

		

		if (currentTime.after(startTime) && currentTime.before(endTime)) {
		
			
			String dayandauto=meetingsDao.getDay(startTime.toString(), endTime.toString());
			//String[] allDays=dayandauto.split("@");
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			Log.d("ALL DAYS--",dayandauto+day);
			if(dayandauto.equals("week days")){
				
				if(day==1||day==7){
					blockNumber(); 
					
					callMes();
					
					}
				
			}else if (dayandauto.equals("Today")) {
				blockNumber(); 
				
				callMes();
				
				
			}else{
				blockNumber(); 
				
				callMes();
				
				}
		
				
		

		}
		return false;
		

	}
   
	public void blockNumber() {
		try {
			String serviceManagerName = "android.os.ServiceManager";
			String serviceManagerNativeName = "android.os.ServiceManagerNative";
			String telephonyName = "com.android.internal.telephony.ITelephony";
			Class<?> telephonyClass;
			Class<?> telephonyStubClass;
			Class<?> serviceManagerClass;
			Class<?> serviceManagerNativeClass;
			Method telephonyEndCall;
			Object telephonyObject;
			Object serviceManagerObject;
			telephonyClass = Class.forName(telephonyName);
			telephonyStubClass = telephonyClass.getClasses()[0];
			serviceManagerClass = Class.forName(serviceManagerName);
			serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
			Method getService = // getDefaults[29];
			serviceManagerClass.getMethod("getService", String.class);
			Method tempInterfaceMethod = serviceManagerNativeClass.getMethod(
					"asInterface", IBinder.class);
			Binder tmpBinder = new Binder();
			tmpBinder.attachInterface(null, "fake");
			serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
			IBinder retbinder = (IBinder) getService.invoke(
					serviceManagerObject, "phone");
			Method serviceMethod = telephonyStubClass.getMethod("asInterface",
					IBinder.class);
			telephonyObject = serviceMethod.invoke(null, retbinder);
			telephonyEndCall = telephonyClass.getMethod("endCall");
			telephonyEndCall.invoke(telephonyObject);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean callMes(){
		try {
			Log.d("CALL", "Before");
			 if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				 
				
				 
				 
		            // This code will execute when the phone has an incoming call
		             
			Log.d("CALL", "After"+incomingNumber);
					SmsManager sms = SmsManager.getDefault();
					

					sms.sendTextMessage(incomingNumber, null, answerDao.getMsgBody(), null, null);
		             
		        }
			
		
			
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
}
