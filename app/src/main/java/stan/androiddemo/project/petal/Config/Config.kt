package stan.androiddemo.project.petal.Config

/**
 * Created by stanhu on 10/8/2017.
 */
class Config{
    companion object {
        val throttDuration = 300
        const val Authorization = "Authorization"
        const val LIMIT = 20

        const val LOGINTIME = "loginTime"
        const val USERACCOUNT = "userAccount"
        const val USERPASSWORD = "userPassword"
        val USERHEADKEY = "userHeadKey"
        val USEREMAIL = "userEmail"
        val OPERATEBOARDEXTRA = "recommend_tags"

        val USERNAME = "userName"
        val USERID = "userID"
        val TOKENACCESS = "TokenAccess"
        val TOKENREFRESH = "TokenRefresh"
        val TOKENTYPE = "TokenType"
        val TOKENEXPIRESIN = "TokeExpiresIn"
        val ISLOGIN = "isLogin"
        val HISTORYTEXT = "historyText"


        //board info
        val BOARDTILTARRAY = "boardTitleArray"
        val BOARDIDARRAY = "boardIdArray"


        val OPERATELIKE = "like"
        val OPERATEUNLIKE = "unlike"

        //用户对画板的关注操作字段
        val OPERATEFOLLOW = "follow"
        val OPERATEUNFOLLOW = "unfollow"
        //删除画板的操作符
        val OPERATEDELETEBOARD = "DELETE"


        var REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"

    }
}