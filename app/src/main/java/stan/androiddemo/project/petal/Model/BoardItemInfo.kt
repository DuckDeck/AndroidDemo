package stan.androiddemo.project.petal.Model

/**
 * Created by stanhu on 14/8/2017.
 */
class BoardItemInfo{
     var board_id: Int = 0
     var user_id: Int = 0
     var title: String? = null
     var is_private: Int = 0
     var category_id: String? = null
     var pin_count: Int = 0
     var follow_count: Int = 0
     var description: String? = null
     var recommend_tags: List<String>? = null
}