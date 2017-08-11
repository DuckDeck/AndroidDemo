package stan.androiddemo.tool

/**
 * Created by stanhu on 11/8/2017.
 */
class Base64Utils{
   companion object {
       private val mBasic = "Basic "
       private val mClientInfo = "MWQ5MTJjYWU0NzE0NGZhMDlkODg6Zjk0ZmNjMDliNTliNDM0OWExNDhhMjAzYWIyZjIwYzc="
       val mClientInto = mBasic + mClientInfo
   }
}