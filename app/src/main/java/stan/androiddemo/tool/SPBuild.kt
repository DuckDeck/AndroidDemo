package stan.androiddemo.tool

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by stanhu on 11/8/2017.
 */
class SPBuild{
    private  var  editor: SharedPreferences.Editor

    constructor(context: Context) {
        this.editor = context.getSharedPreferences(SPUtils.FILE_NAME, SPUtils.MODE).edit()
    }

    fun addData(key: String, `object`: Any): SPBuild {
        SPUtils.putAdd(editor, key, `object`)
        return this
    }

    fun build() {
        this.editor.apply()
    }
}