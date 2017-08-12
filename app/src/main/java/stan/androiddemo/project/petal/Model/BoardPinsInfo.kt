package stan.androiddemo.project.petal.Model

/**
 * Created by hugfo on 2017/8/12.
 */
class BoardPinsInfo{
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
     var deleting: Int = 0
     var is_private: Int = 0
     var extra: ExtraBean? = null
     var cover: ExtraBean.CoverBean? = null
     var user: UserBean? = null
     var following: Boolean = false
     var pins: List<PinsSimpleInfo>? = null
    class ExtraBean {
        var cover: CoverBean? = null
        class CoverBean {
            var pin_id: String? = null
        }
    }

    class UserBean {
        var user_id: Int = 0
        var username: String? = null
        var urlname: String? = null
        var created_at: Int = 0
        var avatar: String? = null
    }
}