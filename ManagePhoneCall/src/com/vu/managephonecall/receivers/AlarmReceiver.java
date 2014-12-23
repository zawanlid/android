package com.vu.managephonecall.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.vu.managephonecall.R;

public class AlarmReceiver extends BroadcastReceiver {

	@SuppressWarnings("unused")
	private NotificationManager NM;

	@Override
	public void onReceive(Context context, Intent intentArg) {

		// define sound URI, the sound to be played when there's a notification
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// intent triggered, you can add other intent for other actions
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the
		// first param to 0
		Notification mNotification = new Notification.Builder(context)

		.setContentTitle("Scheduled Call!")
				.setContentText(intentArg.getStringExtra("com.vu.managephonecall.notification.call"))
				.setSmallIcon(R.drawable.ninja).setContentIntent(pIntent)
				.setSound(soundUri)
				.build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// If you want to hide the notification after it was selected, do the
		// code below
		// myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, mNotification);

		/*
		 * Uri soundUri =
		 * RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 * 
		 * String title ="ScheduleCall"; String subject = "@kk"; String body =
		 * "call"; NM=(NotificationManager)context.getSystemService(Context.
		 * NOTIFICATION_SERVICE); Notification notify=new
		 * Notification(android.R.drawable.
		 * stat_notify_more,title,System.currentTimeMillis()); PendingIntent
		 * pending=PendingIntent.getActivity( context
		 * .getApplicationContext(),0, new Intent(),0);
		 * notify.setLatestEventInfo
		 * (context.getApplicationContext(),subject,body,pending); NM.notify(0,
		 * notify);
		 */

	}

}