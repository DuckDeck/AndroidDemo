package stan.androiddemo.project.petal.Model

/**
 * Created by hugfo on 2017/8/12.
 */
class PinsSimpleInfo{
     var pin_id: Int = 0
     var user_id: Int = 0
     var board_id: Int = 0
     var file_id: Int = 0

     var file: FileBean? = null
     var media_type: Int = 0
     var source: String? = null
     var link: String? = null
     var raw_text: String? = null
     var via: Int = 0
     var via_user_id: Int = 0
     var original: Int = 0
     var created_at: Int = 0
     var like_count: Int = 0
     var comment_count: Int = 0
     var repin_count: Int = 0
     var is_private: Int = 0
     var orig_source: String? = null

    class FileBean {
        var farm: String? = null
        var bucket: String? = null
        var key: String? = null
        var type: String? = null
        var height: String? = null
        var frames: String? = null
        var width: String? = null
    }
}