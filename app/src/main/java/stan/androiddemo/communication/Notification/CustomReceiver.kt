package stan.androiddemo.communication.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class CustomReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,"Receive in my customBroadcastReceive",Toast.LENGTH_LONG).show()
        abortBroadcast()
    }
}
