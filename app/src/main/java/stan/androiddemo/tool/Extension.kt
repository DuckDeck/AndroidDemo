package stan.androiddemo.tool

/**
 * Created by hugfo on 2017/8/5.
 */
fun Int.ToFixedInt(num:Int):String{
    if (this.toString().length > num){
        return this.toString()
    }
    else {
        var str = this.toString()
        while (str.length < num){
            str = "0" + str
        }
        return str
    }
}