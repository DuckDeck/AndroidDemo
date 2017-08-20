package stan.androiddemo.project.Game2048

import android.content.Context
import android.view.View

/**
 * Created by hugfo on 2017/8/19.
 */
class MainView: View {

    companion object {
        internal val BASE_ANIMATION_TIME = 100000000

        internal val MERGING_ACCELERATION = (-0.5).toFloat()
        internal val INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4
    }

    constructor(context:Context):super(context){

    }
}