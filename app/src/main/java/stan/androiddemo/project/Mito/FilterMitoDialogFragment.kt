package stan.androiddemo.project.Mito

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageCatInfo


/**
 * Created by stanhu on 25/8/2017.
 */
class FilterMitoDialogFragment: AppCompatDialogFragment(){

    companion object {
        private val KEYIMAGECAT = "keyAuthorization"
        fun create(resolutionArray:ImageCatInfo): FilterMitoDialogFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEYIMAGECAT,resolutionArray)

            val fragment = FilterMitoDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_resolution_filter,container)
    }

}