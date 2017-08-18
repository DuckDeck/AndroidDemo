package stan.androiddemo.project.petal.Event

import android.view.View

/**
 * Created by stanhu on 18/8/2017.
 */
interface OnPeopleFragmentInteraction<in T>{
     fun onClickItemUser(bean: T, view: View)
}