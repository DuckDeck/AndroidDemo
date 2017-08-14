package stan.androiddemo.project.petal.Event

import android.view.View
import stan.androiddemo.project.petal.Model.PinsMainInfo

/**
 * Created by hugfo on 2017/8/13.
 */
interface OnPinsFragmentInteractionListener {
    fun onClickPinsItemImage(bean: PinsMainInfo, view: View)

    fun onClickPinsItemText(bean: PinsMainInfo, view: View)
}