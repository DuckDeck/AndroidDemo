package stan.androiddemo.project.petal.Event

/**
 * Created by stanhu on 17/8/2017.
 */
interface OnBoardDetailFragmentInteractionListener:OnPinsFragmentInteractionListener{
    fun onClickUserField(key: String, title: String)

    fun onHttpBoardAttentionState(userId: String, isAttention: Boolean)
}