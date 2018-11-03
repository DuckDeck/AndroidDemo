package stan.androiddemo.tool

import android.graphics.Bitmap
import android.media.Image
import android.os.Environment
import android.widget.ImageView
import okhttp3.internal.Util
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
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
            str = "0$str"
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
    val photoPath = "$name.jpeg"
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

fun ImageView.setMat(mat:Mat){
    val bitmat = Bitmap.createBitmap(mat.width(),mat.height(),Bitmap.Config.ARGB_8888)
    val matResult = Mat()
//    Imgproc.cvtColor(mat,matResult,Imgproc.COLOR_BGR2RGBA)
    Utils.matToBitmap(mat,bitmat)
    setImageBitmap(bitmat)
    matResult.release()
}

fun ImageView.getBitmap():Bitmap{
    this.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(this.drawingCache)
    this.isDrawingCacheEnabled = false
    return  bitmap
}
fun ImageView.getMat():Mat{
    val bitmap = getBitmap()
    val mat = Mat()
    Utils.bitmapToMat(bitmap,mat)
    return  mat
}
