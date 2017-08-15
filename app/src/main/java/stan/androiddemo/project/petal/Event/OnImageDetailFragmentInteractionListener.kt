package stan.androiddemo.project.petal.Event

/**
 * Created by stanhu on 15/8/2017.
 */
interface OnImageDetailFragmentInteractionListener:OnPinsFragmentInteractionListener{
     fun onClickBoardField(key: String, title: String)

     fun onClickUserField(key: String, title: String)

     fun onClickImageLink(link: String)
}