package stan.androiddemo.project.Mito

import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import stan.androiddemo.R

/**
 * Created by stanhu on 25/8/2017.
 */
class FilterMitoDialogFragment: AppCompatDialogFragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_resolution_filter,container)
    }

}