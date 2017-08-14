package stan.androiddemo.project.petal.Module.Login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_petal_login.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import stan.androiddemo.R

import stan.androiddemo.project.petal.API.TokenAPI
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardItemInfo
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.NetUtils
import stan.androiddemo.tool.SPBuild
import stan.androiddemo.tool.SPUtils
import java.util.concurrent.TimeUnit

class PetalLoginActivity : BasePetalActivity() {

    lateinit var mTokenBean:TokenBean
    lateinit var mUserBean:UserMeAndOtherBean
    lateinit var emailAdapter:ArrayAdapter<String>
    companion object {
        fun launch(activity: Activity){
            val intent = Intent(activity,PetalLoginActivity::class.java)
            activity.startActivity(intent)
        }

        fun launch(activity: Activity,message:String){
            val intent = Intent(activity,PetalLoginActivity::class.java)
            intent.putExtra("message",message)
            activity.startActivity(intent)
        }
    }

    override fun getTag(): String {
       return this.toString()
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_petal_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val message = intent.getStringExtra("message")
        if (!message.isNullOrEmpty()){
            NetUtils.showSnackBar(scroll_petal_login,message)
        }
        addUsernameAutoComplete()
    }

    override fun initResAndListener() {
        super.initResAndListener()

        RxTextView.textChanges(auto_text_username)
                .subscribe({
                    emailAdapter.notifyDataSetChanged()
                })

        RxTextView.editorActions(edit_password, Func1 {
            it == EditorInfo.IME_ACTION_DONE
        }).throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe(Action1 {
                    attempLogin()
                })
        RxView.clicks(btn_login).throttleFirst(500,TimeUnit.MILLISECONDS).subscribe(Action1 {
            attempLogin()
        })

        RxView.clicks(btn_register).throttleFirst(500,TimeUnit.MILLISECONDS).subscribe(Action1 {
            netRegister()
        })
    }

    fun addUsernameAutoComplete(){
        val emailArray = arrayListOf("qq.com","163.com","126.com")
        emailAdapter = ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,emailArray)
        auto_text_username.setAdapter(emailAdapter)
    }

    fun attempLogin(){
        auto_text_username.error = null
        edit_password.error = null
        val username = auto_text_username.text.toString().trim()
        val password = edit_password.text.toString().trim()

        var cancel = false
        var focusView:View? = null
        if (password.isNullOrEmpty()){
            edit_password.error = resources.getString(R.string.error_empty_password)
            focusView = edit_password
            cancel = true
        }
        if (!password.isNullOrEmpty() && password.length < 6){
            edit_password.error = resources.getString(R.string.error_invalid_password)
            focusView = edit_password
            cancel = true
        }
        if (username.isNullOrEmpty()){
            edit_password.error = resources.getString(R.string.error_field_required)
            focusView = auto_text_username
            cancel = true
        }
        if (!username.matches(Regex(Config.REGEX_EMAIL))){
            edit_password.error = resources.getString(R.string.error_invalid_username)
            focusView = auto_text_username
            cancel = true
        }
        if (cancel){
            focusView?.requestFocus()
        }
        else{
            httpLogin(username,password)
        }
    }

    fun netRegister(){
        val uri = Uri.parse(resources.getString(R.string.urlRegister))
        val int = Intent(Intent.ACTION_VIEW,uri)
        if (int.resolveActivity(this@PetalLoginActivity.packageManager) != null){
            startActivity(int)
        }
        else{
            Logger.d("checkResolveIntent = null")
        }

    }

    fun httpLogin(username:String,password:String){
        RetrofitClient.createService(TokenAPI::class.java).httpsGetTokenRx(mAuthorization,"password",username,password)
                .flatMap( {
                    mTokenBean = it
                    mAuthorization = it.token_type + " " + it.access_token
                     RetrofitClient.createService(UserAPI::class.java).httpsUserRx(mAuthorization)
                }).flatMap { mUserBean = it
                    RetrofitClient.createService(UserAPI::class.java).httpsBoardListInfo(mAuthorization,Config.OPERATEBOARDEXTRA)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<BoardListInfo>(){
                    override fun onStart() {
                        super.onStart()
                        showProgrss(true)
                    }
                    override fun onNext(t: BoardListInfo?) {
                        showProgrss(false)
                        NetUtils.showSnackBar(scroll_petal_login,resources.getString(R.string.snack_message_login_success))
                                .setCallback(object:Snackbar.Callback(){
                                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                        super.onDismissed(transientBottomBar, event)
                                        TODO("login to user activity")
                                    }
                                })
                        saveUserInfo(mUserBean,mTokenBean,username,password,t!!.boards!!)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        showProgrss(false)
                    }

                })
    }

    fun saveUserInfo(infoResult:UserMeAndOtherBean,tokenBean:TokenBean,userAccount:String,
                     password: String,boardList:List<BoardItemInfo>){
        SPUtils.clear(applicationContext)
        val boardTitle = StringBuilder()
        val boardId = StringBuilder()
        for (i in 0 until boardList.size){
            boardTitle.append(boardList[i].title)
            boardId.append(boardList[i].board_id)
            if (i == boardList.size - 1){
                boardTitle.append(",")
                boardId.append(",")
            }
        }

        SPBuild(applicationContext).addData(Config.ISLOGIN,true)
                .addData(Config.ISLOGIN,true)
                .addData(Config.LOGINTIME,System.currentTimeMillis())
                .addData(Config.USERACCOUNT,userAccount)
                .addData(Config.USERPASSWORD,password)
                .addData(Config.TOKENACCESS,tokenBean.access_token!!)
                .addData(Config.TOKENREFRESH,tokenBean.refresh_token!!)
                .addData(Config.TOKENTYPE,tokenBean.token_type!!)
                .addData(Config.TOKENEXPIRESIN,tokenBean.expires_in)
                .addData(Config.USERNAME,infoResult.username!!)
                .addData(Config.USERID,infoResult.user_id)
                .addData(Config.USERHEADKEY,infoResult.avatar!!)
                .addData(Config.USEREMAIL,infoResult.email!!)
                .addData(Config.BOARDTILTARRAY,boardTitle)
                .addData(Config.BOARDIDARRAY,boardId)
                .build()
    }

    private fun showProgrss(show: Boolean) {
        val showAnimTime = resources.getInteger(R.integer.config_tooltipAnimTime)
        progress_login.visibility = if(show) View.VISIBLE else View.INVISIBLE
        progress_login.animate().setDuration(showAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                progress_login.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }
}
