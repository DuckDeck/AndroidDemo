package stan.androiddemo.layout.RecyclerView

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_recycler_view.*
import stan.androiddemo.R
import stan.androiddemo.UI.Separate
import stan.androiddemo.layout.materialdesign.Fruit
import java.util.*

class RecyclerViewActivity : AppCompatActivity() {
    var arrFruits = ArrayList<Fruit>()
    var fruits =  ArrayList<Fruit>()
    val random = Random()
    lateinit var mAdapter:BaseQuickAdapter<Fruit,BaseViewHolder>
    lateinit var mAdapterGrid:BaseQuickAdapter<Fruit,BaseViewHolder>
    var layoutStatus = 0 //0 是linearLayout 1是Grid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }

        initFruits()
        for (i in 0 until 50){
            val index = random.nextInt(fruits.size)
            arrFruits.add(fruits[index])
        }
        mAdapter = object:BaseQuickAdapter<Fruit,BaseViewHolder>(R.layout.general_item1,arrFruits){
            override fun convert(helper: BaseViewHolder, item: Fruit) {
                Glide.with(this@RecyclerViewActivity).load(item.imageId).into(helper.getView(R.id.img_item))
                helper.setText(R.id.txt_item_name,item.name)
            }
        }

        mAdapterGrid = object:BaseQuickAdapter<Fruit,BaseViewHolder>(R.layout.fruit_item,arrFruits){
            override fun convert(helper: BaseViewHolder, item: Fruit) {
                Glide.with(this@RecyclerViewActivity).load(item.imageId).into(helper.getView(R.id.img_fruit))
                helper.setText(R.id.txt_fruit_name,item.name)
            }
        }

        txt_switch.setOnClickListener {
            if (layoutStatus == 0){
                recycler_view_demo.adapter = mAdapterGrid
                recycler_view_demo.layoutManager = GridLayoutManager(this,2)
                layoutStatus = 1
            }
            else{
                recycler_view_demo.adapter = mAdapter
                recycler_view_demo.layoutManager = LinearLayoutManager(this)
                layoutStatus = 0
            }

        }

        val itemTouchHelper = ItemTouchHelper(object:ItemTouchHelper.Callback(){
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//                如果是列表类型的 RecyclerView，拖拽只有 UP、DOWN 两个方向
//                如果是网格类型的则有 UP、DOWN、LEFT、RIGHT 四个方向
                if (recyclerView.layoutManager is GridLayoutManager){
                    val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    val swipe = 0
                    return makeMovementFlags(drag,swipe)
                }
                else{
                    val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    val swipe = ItemTouchHelper.START or  ItemTouchHelper.END
                    return makeMovementFlags(drag,swipe)
                }
//                dragFlags 是拖拽标志，
//                swipeFlags 是滑动标志，
//                swipeFlags 都设置为0，表示不滑动
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                //拖动的 item 的下标
                val fromPosttion = viewHolder.adapterPosition
                //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
                val toPosition = target.adapterPosition
                if (fromPosttion < toPosition){
                    for (i in fromPosttion until toPosition){
                        Collections.swap(mAdapter.data,i,i+1)
                    }
                }
                else{
                    for (i in fromPosttion downTo  toPosition){
                        Collections.swap(mAdapter.data,i,i-1)  //当到第一个会报错 需要修复
                    }
                }
                mAdapter.notifyItemMoved(fromPosttion,toPosition)
                return true
            }
            //这种左滑删除不是我想要的效果，直接就删除了
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
               val adapterPosition = viewHolder?.adapterPosition ?: return
                mAdapter.notifyItemRemoved(adapterPosition)
                mAdapter.data.removeAt(adapterPosition)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder?.itemView != null){
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY)
                }
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor( Color.WHITE)
            }
            //是否支持长按Drag，默认是支持的
            override fun isLongPressDragEnabled(): Boolean {
                 return super.isLongPressDragEnabled()
            }
        })
        itemTouchHelper.attachToRecyclerView(recycler_view_demo)
        recycler_view_demo.addItemDecoration(Separate(this,Separate.HORIZONTAL_LIST))
        recycler_view_demo.layoutManager = LinearLayoutManager(this)
        recycler_view_demo.adapter = mAdapter


    }



    fun initFruits(){
        var fruit = Fruit()
        fruit.name = "banana"
        fruit.imageId = R.mipmap.banana
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "apple"
        fruit.imageId = R.mipmap.apple
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "orange"
        fruit.imageId = R.mipmap.orange
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "papaya"
        fruit.imageId = R.mipmap.papaya
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "pineapple"
        fruit.imageId = R.mipmap.pineapple
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "watermelon"
        fruit.imageId = R.mipmap.watermelon
        fruits.add(fruit)
    }
}
