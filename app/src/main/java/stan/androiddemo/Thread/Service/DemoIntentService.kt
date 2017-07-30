package stan.androiddemo.Thread.Service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class DemoIntentService : IntentService("DemoIntentService") {
    val LOG = DemoIntentService::class.java.canonicalName
    override fun onHandleIntent(intent: Intent?) {
      Log.e(LOG,"Thread id is " + Thread.currentThread().id)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(LOG,"onDestroy executed")
    }




}
