package com.kedzie.vbox;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kedzie.vbox.api.IMachine;
import com.kedzie.vbox.machine.MachineFragmentActivity;
import com.kedzie.vbox.soap.VBoxSvc;

/**
 * Listen for VirtualBox events and publish notifications
 */
public class EventNotificationService extends IntentService {

	private static final String TAG = EventNotificationService.class.getSimpleName();
	private static final int NOTIFICATION_ID=1;
	
	public EventNotificationService() {
		super("Event Notification Service");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Sending notification");
		IMachine eventMachine = BundleBuilder.getProxy(intent, IMachine.BUNDLE, IMachine.class);
		Intent i = new Intent(EventNotificationService.this, MachineFragmentActivity.class).putExtra(VBoxSvc.BUNDLE, eventMachine.getVBoxAPI());
		BundleBuilder.addProxy(i, IMachine.BUNDLE, eventMachine);
		Notification n =  new Notification.Builder(EventNotificationService.this)
				.setContentTitle(String.format("%1$s is %2$s", eventMachine.getName(), eventMachine.getState()))
				.setContentText(String.format("Virtual Machine %1$s changed state to [%2$s]", eventMachine.getName(), eventMachine.getState()))
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(R.drawable.ic_notif_vbox)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), ((VBoxApplication)getApplication()).getDrawable(eventMachine.getState())))
				.setContentIntent(PendingIntent.getActivity(EventNotificationService.this, 0, i, 0))
				.getNotification();
		((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, n);
	}
}