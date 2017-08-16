package stan.androiddemo.project.petal.Module.UserInfo

import stan.androiddemo.project.petal.Model.PinsSimpleInfo

/**
 * Created by stanhu on 16/8/2017.
 */
class UserBoardItemBean{
     var board_id: Int = 0
     var user_id: Int = 0
     var title: String? = null
     var description: String? = null
     var category_id: String? = null
     var seq: Int = 0
     var pin_count: Int = 0
     var follow_count: Int = 0
     var like_count: Int = 0
     var created_at: Int = 0
     var updated_at: Int = 0
     var deleting = 1
     var is_private: Int = 0
     var following: Boolean = false

     var pins: List<PinsSimpleInfo>? = null
}