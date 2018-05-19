package stan.androiddemo.tool

import android.media.Image
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

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

fun  String.ConvertUrl():String{
    return this.replace(".","_").replace(":","=").replace("/","-")
}

fun Image.Save(name:String){

    val buffer = this.planes[0].buffer
    val data = ByteArray(buffer.remaining())
    buffer.get(data)
    val filePath = Environment.getExternalStorageDirectory().path + "/DCIM/Camera"
    val photoPath = name + ".jpeg"
    val file = File(filePath,photoPath)
    try {
        //存到本地相册
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(data)
        fileOutputStream.close()
    }
    catch (e: FileNotFoundException){
        e.printStackTrace()
    }
    catch (e: IOException){
        e.printStackTrace()
    }
    finally {
        this.close()
    }
}