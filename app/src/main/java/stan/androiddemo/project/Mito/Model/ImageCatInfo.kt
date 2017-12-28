package stan.androiddemo.project.Mito.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by stanhu on 28/12/2017.
 */
class ImageCatInfo() : Parcelable {
    var catName = ""
    var resulotions = arrayOf<Resolution>()

    constructor(parcel: Parcel) : this() {
        catName = parcel.readString()
        resulotions = parcel.readParcelableArray(ClassLoader.getSystemClassLoader()) as Array<Resolution>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(catName)
        parcel.writeParcelableArray(resulotions,flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageCatInfo> {
        override fun createFromParcel(parcel: Parcel): ImageCatInfo {
            return ImageCatInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageCatInfo?> {
            return arrayOfNulls(size)
        }
    }
}