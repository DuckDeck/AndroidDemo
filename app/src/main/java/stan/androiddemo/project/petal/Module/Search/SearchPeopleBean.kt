package stan.androiddemo.project.petal.Module.Search

import stan.androiddemo.project.petal.Model.ErrorBaseInfo

/**
 * Created by hugfo on 2017/8/12.
 */
class SearchPeopleBean: ErrorBaseInfo() {


     var query: QueryBean? = null
    /**
     * query : {"text":"特立独行的猫","type":"people"}
     * pin_count : 138
     * board_count : 2
     * people_count : 34
     * gift_count : 0
     * users : [{"user_id":16211815,"username":"特立独行的猫2014","urlname":"teliduxingdemao","created_at":1415252603,"avatar":"图片地址","follower_count":2787,"following":0},{"user_id":188174,"username":"一只特立独行的猫","urlname":"vivi0507","created_at":1333682701,"avatar":"图片地址","follower_count":178,"following":null}]
     */

     var pin_count: Int = 0
     var board_count: Int = 0
     var people_count: Int = 0
     var gift_count: Int = 0
    /**
     * user_id : 16211815
     * username : 特立独行的猫2014
     * urlname : teliduxingdemao
     * created_at : 1415252603
     * avatar : 图片地址
     * follower_count : 2787
     * following : 0
     */

     var users: List<UsersBean>? = null


    class QueryBean {
        var text: String? = null
        var type: String? = null
    }

    class UsersBean {
        var user_id: Int = 0
        var username: String? = null
        var urlname: String? = null
        var created_at: Int = 0
        var avatar: String? = null
        var follower_count: Int = 0
        var following: Int = 0
    }
}