package stan.androiddemo.UI

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by hugfo on 2017/7/23.
 */
class Separate: RecyclerView.ItemDecoration{

    companion object {
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }

    val ATRRS = intArrayOf(android.R.attr.listDivider)
    private  var context: Context
    private  var divider:Drawable
    private  var orientation:Int = Separate.HORIZONTAL_LIST
    constructor(context: Context,orientation: Int){

        this.context = context
        val ta = context.obtainStyledAttributes(ATRRS)
        this.divider = ta.getDrawable(0)
        ta.recycle()
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw IllegalArgumentException("invalid orientation")
        }
        this.orientation = orientation
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == HORIZONTAL_LIST){
            drawVertivalLine(c,parent,state)
        }
        else{
            drawHorizontalLine(c,parent,state)
        }
    }

    fun drawHorizontalLine(c:Canvas,parent:RecyclerView,state: RecyclerView.State){
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left,top,right,bottom)
            divider.draw(c)
        }
    }

    fun drawVertivalLine(c: Canvas,parent: RecyclerView,state: RecyclerView.State){
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount){
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicWidth
            divider.setBounds(left,top,right,bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect:Rect,view:View,parent: RecyclerView,state: RecyclerView.State){
        if (orientation == HORIZONTAL_LIST){
            outRect.set(0,0,0,divider.intrinsicHeight)
        }
        else{
            outRect.set(0,0,divider.intrinsicWidth,0)
        }
    }
}