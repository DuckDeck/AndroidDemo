package stan.androiddemo.project.petal.Model

/**
 * Created by stanhu on 11/8/2017.
 */
class PinsMainInfo{
     var pin_id: Int = 0
     var user_id: Int = 0
     var board_id: Int = 0
     var file_id: Int = 0

     var file: PinsFileInfo? = null
     var media_type: Int = 0
     var source: String? = null
     var link: String? = null
     var raw_text: String? = null
     var via: Int = 0
     var via_user_id: Int = 0
     var original: Int = 0
     var created_at: Int = 0
     var like_count: Int = 0
     var seq: Int = 0
     var comment_count: Int = 0
     var repin_count: Int = 0
     var is_private: Int = 0
     var orig_source: String? = null
     var liked: Boolean = false

     var pinsUserInfo:PinsUserInfo? = null
     var pinsBoardInfo:PinsBoardInfo? = null
}