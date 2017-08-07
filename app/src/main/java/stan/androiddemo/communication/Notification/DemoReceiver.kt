package stan.androiddemo.communication.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DemoReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw UnsupportedOperationException("Not yet implemented")
        Toast.makeText(context,"Boot Completed",Toast.LENGTH_LONG).show()
        //这个要重启手机才行，这个...还是算了吧
    }
}
