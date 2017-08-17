package stan.androiddemo.project.petal.Module.BoardDetail

/**
 * Created by stanhu on 17/8/2017.
 */
class BoardDetailBean{

    var board: BoardBean? = null

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
        var extra: Any? = null
        /**
         * user_id : 6285104
         * username : sai叔（saimme）
         * urlname : sai370419748
         * created_at : 1355974630
         * avatar : {"id":8607108,"farm":"farm1","bucket":"hbimg","key":"d556c1e555a56783df97f77fa9b2b4a0a9906040359ec-AzkrbO","type":"image/jpeg","width":600,"height":906,"frames":1}
         * pin_count : 27864
         * boards_like_count : 381
         * follower_count : 19875
         * board_count : 73
         * commodity_count : 42
         * following_count : 1052
         * creations_count : 0
         * like_count : 4881
         * explore_following_count : 5
         * profile : {"location":"上海","sex":"0","birthday":"","job":"2货 无脑 射基师","url":"","about":"sai叔一个爱设计的吃货女汉子 ，欢迎关注我的公众号sai叔的日常 每周推出美食和设计相关的内容ˊ_&gt;ˋ","show_verification":"weibo"}
         * status : {"newbietask":0,"lr":1459241229,"invites":0,"share":"0","default_board":3329094,"notificationsRead":[23],"past_shiji_guide":1,"past_design_welcome":1,"hide_douban":1,"douban_unsearchable":1}
         */

        var user: UserBean? = null
        var isFollowing: Boolean = false

        class UserBean {
            var user_id: Int = 0
            var username: String? = null
            var urlname: String? = null
            var created_at: Int = 0
            /**
             * id : 8607108
             * farm : farm1
             * bucket : hbimg
             * key : d556c1e555a56783df97f77fa9b2b4a0a9906040359ec-AzkrbO
             * type : image/jpeg
             * width : 600
             * height : 906
             * frames : 1
             */

            var avatar: String? = null
            var pin_count: Int = 0
            var boards_like_count: Int = 0
            var follower_count: Int = 0
            var board_count: Int = 0
            var commodity_count: Int = 0
            var following_count: Int = 0
            var creations_count: Int = 0
            var like_count: Int = 0
            var explore_following_count: Int = 0
            /**
             * location : 上海
             * sex : 0
             * birthday :
             * job : 2货 无脑 射基师
             * url :
             * about : sai叔一个爱设计的吃货女汉子 ，欢迎关注我的公众号sai叔的日常 每周推出美食和设计相关的内容ˊ_&gt;ˋ
             * show_verification : weibo
             */

            var profile: ProfileBean? = null
            /**
             * newbietask : 0
             * lr : 1459241229
             * invites : 0
             * share : 0
             * default_board : 3329094
             * notificationsRead : [23]
             * past_shiji_guide : 1
             * past_design_welcome : 1
             * hide_douban : 1
             * douban_unsearchable : 1
             */

            var status: StatusBean? = null

            class ProfileBean {
                var location: String? = null
                var sex: String? = null
                var birthday: String? = null
                var job: String? = null
                var url: String? = null
                var about: String? = null
                var show_verification: String? = null
            }

            class StatusBean {
                var newbietask: Int = 0
                var lr: Int = 0
                var invites: Int = 0
                var share: String? = null
                var default_board: Int = 0
                var past_shiji_guide: Int = 0
                var past_design_welcome: Int = 0
                var hide_douban: Int = 0
                var douban_unsearchable: Int = 0
                var notificationsRead: List<Int>? = null
            }
        }
    }
}