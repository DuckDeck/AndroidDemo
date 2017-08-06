package stan.androiddemo.tool

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by hugfo on 2017/8/6.
 */
class KeyboardTool{
    companion object {
        fun hideKeyboard(activity:Activity){
            val view = activity.currentFocus
            if (view != null){
                val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
        fun hideKeyboard(context:Context,viewList:List<View>?){
           if (viewList == null){
               return
           }
           val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
           for (v in viewList!!){
               inputMethodManager.hideSoftInputFromWindow(v.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
           }
        }

        fun showKeyboard(activity:Activity){
            val im = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }

    }
}