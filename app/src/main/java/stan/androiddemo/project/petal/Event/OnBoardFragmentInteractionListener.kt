package stan.androiddemo.project.petal.Event

import android.view.View

/**
 * Created by stanhu on 17/8/2017.
 */
interface OnBoardFragmentInteractionListener<in T>{
    fun onClickBoardItemImage(bean: T, view: View)
}