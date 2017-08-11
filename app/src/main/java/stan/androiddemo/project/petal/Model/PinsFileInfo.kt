package stan.androiddemo.project.petal.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by stanhu on 11/8/2017.
 */
class PinsFileInfo() :Parcelable{
     var farm: String? = null
     var bucket: String? = null
     var key: String? = null
     var type: String? = null
     var width: Int = 0
     var height: Int = 0
     var frames: Int = 0

    constructor(parcel: Parcel) : this() {
        farm = parcel.readString()
        bucket = parcel.readString()
        key = parcel.readString()
        type = parcel.readString()
        width = parcel.readInt()
        height = parcel.readInt()
        frames = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(farm)
        parcel.writeString(bucket)
        parcel.writeString(key)
        parcel.writeString(type)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeInt(frames)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PinsFileInfo> {
        override fun createFromParcel(parcel: Parcel): PinsFileInfo {
            return PinsFileInfo(parcel)
        }

        override fun newArray(size: Int): Array<PinsFileInfo?> {
            return arrayOfNulls(size)
        }
    }
}