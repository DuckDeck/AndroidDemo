package stan.androiddemo.project.Mito.Model

import android.os.Parcel
import android.os.Parcelable
import org.litepal.crud.DataSupport

/**
 * Created by stanhu on 29/12/2017.
 */
class Resolution(): DataSupport(), Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pixelX)
        parcel.writeInt(pixelY)
    }



    override fun describeContents(): Int {
        return 0
    }

    constructor(x:Int,y:Int):this(){
        pixelX = x
        pixelY = y
    }

    constructor(resolution: String):this(){
        setResolution(resolution)
    }

    fun setResolution(resolution: String){
        var res = resolution.split("x")
        if (resolution.contains('×')){
            res = resolution.split("×") // it's rediculous, because x and × is not the same
        }
        if (res.size == 1)
        {
            pixelX = res[0].toIntOrNull() ?: 0

        }
        else if (res.size == 2) {
            val s = res[0].toIntOrNull()
            pixelX = s ?: 0
            val y = res[1].toIntOrNull()
            pixelY = y ?: 0
        }
    }

    var pixelX = 0
    var pixelY = 0
    var device = ""
    var resolutionCode = ""
    constructor(parcel: Parcel) : this() {
        pixelX = parcel.readInt()
        pixelY = parcel.readInt()
    }

    override fun toString(): String {
        if (pixelX <0 && pixelY < 0){
            return  "全部"
        }
        return pixelX.toString() + "x" + pixelY.toString()
    }

    fun toUrlPara():String{
        if (pixelX == 0 && pixelY == 0){
            return "0"
        }
        else if(pixelX == 3840 && pixelY == 1200){
            return  "3440"
        }
        else if(pixelX == 3200 && pixelY == 2400){
            return  "3441"
        }
        else if(pixelX == 2880 && pixelY == 1800){
            return  "3442"
        }
        else if(pixelX == 2560 && pixelY == 1600){
            return  "3394"
        }
        else if(pixelX == 1920 && pixelY == 1200){
            return  "3395"
        }
        else if(pixelX == 1920 && pixelY == 1080){
            return  "3440"
        }
        else if(pixelX == 1680 && pixelY == 1050){
            return  "3397"
        }
        else if(pixelX == 1600 && pixelY == 1200){
            return  "3432"
        }
        else if(pixelX == 1600 && pixelY == 900){
            return  "3398"
        }
        else if(pixelX == 1440 && pixelY == 900){
            return  "3399"
        }
        else if(pixelX == 1366 && pixelY == 768){
            return  "3400"
        }
        else if(pixelX == 1280 && pixelY == 1024){
            return  "3401"
        }
        else if(pixelX == 1280 && pixelY == 800){
            return  "3402"
        }
        else if(pixelX == 1024 && pixelY == 768){
            return  "3427"
        }
        return "0"

    }

    companion object CREATOR : Parcelable.Creator<Resolution> {
        override fun createFromParcel(parcel: Parcel): Resolution {
            return Resolution(parcel)
        }

        override fun newArray(size: Int): Array<Resolution?> {
            return arrayOfNulls(size)
        }

        val standardComputerResolution = Resolution("1920x1080")
        val standardPhoneResolution = Resolution("640x940")
        val standardPadResolution = Resolution("1024x768")
        val standardEssentialResolution = Resolution("1920x1080")

        val wholeResolution =  Resolution(-1,-1)
    }
}