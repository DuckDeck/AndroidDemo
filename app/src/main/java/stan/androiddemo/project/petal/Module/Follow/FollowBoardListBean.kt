package stan.androiddemo.project.petal.Module.Follow

import stan.androiddemo.project.petal.Model.BoardPinsInfo

/**
 * Created by stanhu on 17/8/2017.
 */
class FollowBoardListBean{
    var filter: String? = null
    var page: Int = 0
    var following_count: Int = 0
    /**
     * follower_count : 114
     * board_count : 2
     * pin_count : 742
     */

    var user_info: UserInfoBean? = null

    var boards: List<BoardPinsInfo>? = null

    class UserInfoBean {
        var follower_count: Int = 0
        var board_count: Int = 0
        var pin_count: Int = 0
    }
}