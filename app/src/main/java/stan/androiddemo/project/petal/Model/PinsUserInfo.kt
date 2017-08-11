package stan.androiddemo.project.petal.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by stanhu on 11/8/2017.
 */
class PinsUserInfo() :Parcelable{
     var user_id: Int = 0
     var username: String? = null
     var urlname: String? = null
     var created_at: Int = 0
     var avatar: String? = null

    constructor(parcel: Parcel) : this() {
        user_id = parcel.readInt()
        username = parcel.readString()
        urlname = parcel.readString()
        created_at = parcel.readInt()
        avatar = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(user_id)
        parcel.writeString(username)
        parcel.writeString(urlname)
        parcel.writeInt(created_at)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PinsUserInfo> {
        override fun createFromParcel(parcel: Parcel): PinsUserInfo {
            return PinsUserInfo(parcel)
        }

        override fun newArray(size: Int): Array<PinsUserInfo?> {
            return arrayOfNulls(size)
        }
    }
}