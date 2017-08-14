package stan.androiddemo.project.petal.Module.ImageDetail

/**
 * Created by stanhu on 14/8/2017.
 */
class LikePinsOperateBean{
     var like: LikeBean? = null
    class LikeBean {
        var pin_id: Int = 0
        var user_id: Int = 0
        var seq: Int = 0
        var liked_at: Int = 0
    }
}