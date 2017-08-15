package stan.androiddemo.project.petal.Module.ImageDetail

/**
 * Created by stanhu on 15/8/2017.
 */
class GatherResultBean{
    private var user_id: Int = 0
    private var board_id: Int = 0
    private var file_id: Int = 0
    /**
     * farm : farm1
     * bucket : hbimg
     * key : 6f6f10463b17caf2af3cdbf59853e5f67e19a30b94079-EvX8Ju
     * type : image/jpeg
     * height : 1918
     * frames : 1
     * width : 1280
     */

    private var file: FileBean? = null
    private var media_type: Int = 0
    private var source: String? = null
    private var link: String? = null
    private var raw_text: String? = null
    private var via: Int = 0
    private var via_user_id: Int = 0
    private var original: Int = 0
    private var created_at: Int = 0
    private var like_count: Int = 0
    private var comment_count: Int = 0
    private var repin_count: Int = 0
    private var is_private: Int = 0
    private var pin_id: Int = 0
    private var orig_source: String? = null
    /**
     * board_id : 17891564
     * user_id : 15246080
     * title : 主画板修改
     * description :
     * category_id : beauty
     * seq : 3
     * pin_count : 562
     * follow_count : 107
     * like_count : 0
     * created_at : 1412310925
     * updated_at : 1462533630
     * deleting : 0
     * is_private : 0
     */

    private var board: BoardBean? = null
    private var is_shiji: Boolean = false
    private var share_button: String? = null



    class FileBean {
        var farm: String? = null
        var bucket: String? = null
        var key: String? = null
        var type: String? = null
        var height: String? = null
        var frames: String? = null
        var width: String? = null
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