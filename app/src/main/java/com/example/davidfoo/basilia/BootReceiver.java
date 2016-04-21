package com.example.davidfoo.basilia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author david.foo
 */
public class BootReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ServiceEngine.class);
        context.startService(startServiceIntent);
    }
}

