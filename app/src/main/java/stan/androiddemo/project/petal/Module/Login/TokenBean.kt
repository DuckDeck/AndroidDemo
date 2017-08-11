package stan.androiddemo.project.petal.Module.Login

/**
 * Created by stanhu on 11/8/2017.
 */
class TokenBean{
     var access_token: String? = null
     var token_type: String? = null
     var expires_in: Int = 0
     var refresh_token: String? = null
    override fun toString(): String {
        return "TokenBean(access_token=$access_token, token_type=$token_type, expires_in=$expires_in, refresh_token=$refresh_token)"
    }


}