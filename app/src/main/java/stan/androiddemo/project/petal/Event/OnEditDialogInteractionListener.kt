package stan.androiddemo.project.petal.Event

/**
 * Created by stanhu on 18/8/2017.
 */
interface OnEditDialogInteractionListener{
     fun onDialogPositiveClick(boardId: String, name: String, describe: String, selectType: String)

    fun onDialogNeutralClick(boardId: String, boardTitle: String)
}