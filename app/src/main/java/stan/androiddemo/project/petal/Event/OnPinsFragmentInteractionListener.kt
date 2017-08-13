package stan.androiddemo.project.petal.Event

import android.view.View
import stan.androiddemo.project.petal.Model.PinsMainInfo

/**
 * Created by hugfo on 2017/8/13.
 */
interface OnPinsFragmentInteractionListener {
    abstract fun onClickPinsItemImage(bean: PinsMainInfo, view: View)

    abstract fun onClickPinsItemText(bean: PinsMainInfo, view: View)
}