package stan.androiddemo.project.petal.Module.Login

/**
 * Created by stanhu on 14/8/2017.
 */
class UserMeAndOtherBean{
     var user_id: Int = 0
     var username: String? = null
     var urlname: String? = null
     var avatar: String? = null
     var email: String? = null
     var created_at: Int = 0
     var like_count: Int = 0
     var boards_like_count: Int = 0
     var following_count: Int = 0
     var commodity_count: Int = 0
     var board_count: Int = 0
     var follower_count: Int = 0
     var creations_count: Int = 0
     var pin_count: Int = 0

    //该用户是否已经关注 关注为1 否则没有对应的网络字段 int默认值为0
     var following: Int = 0

    var profile: ProfileBean? = null

    class ProfileBean {
        var location: String? = null
        var sex: String? = null
        var birthday: String? = null
        var job: String? = null
        var url: String? = null
        var about: String? = null
    }
}