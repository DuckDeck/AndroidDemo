package stan.androiddemo.project.petal.Module.ImageDetail

/**
 * Created by stanhu on 14/8/2017.
 */
class GatherInfoBean{

    var warning: Int = 0
    var exist_pin: ExistPinBean? = null
    class ExistPinBean {
        var pin_id: Int = 0
        var user_id: Int = 0
        var board_id: Int = 0
        var file_id: Int = 0
        /**
         * farm : farm1
         * bucket : hbimg
         * key : 480ff4b6c1ee8f5a6facf64baf2138c6dcf3361c7cb2c-5cHQTC
         * type : image/jpeg
         * width : 638
         * height : 1136
         * frames : 1
         */

        var file: FileBean? = null
        var media_type: Int = 0
        var source: Any? = null
        var link: Any? = null
        var raw_text: String? = null
        var via: Int = 0
        var via_user_id: Int = 0
        var original: Int = 0
        var created_at: Int = 0
        var like_count: Int = 0
        var comment_count: Int = 0
        var repin_count: Int = 0
        var is_private: Int = 0
        var orig_source: Any? = null
        /**
         * board_id : 17891564
         * user_id : 15246080
         * title : 主画板修改
         * description :
         * category_id : beauty
         * seq : 3
         * pin_count : 559
         * follow_count : 107
         * like_count : 0
         * created_at : 1412310925
         * updated_at : 1462517187
         * deleting : 0
         * is_private : 0
         */

        var board: BoardBean? = null

        class FileBean {
            var farm: String? = null
            var bucket: String? = null
            var key: String? = null
            var type: String? = null
            var width: Int = 0
            var height: Int = 0
            var frames: Int = 0
        }

        class BoardBean {
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
        }
    }
}