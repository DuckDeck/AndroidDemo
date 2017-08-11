package stan.androiddemo.project.petal.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by stanhu on 11/8/2017.
 */
class PinsBoardInfo() :Parcelable{
     var board_id: Int = 0
     var user_id: Int = 0
     var title: String? = null
     var description: String? = null
     var category_id: String? = null
     var seq: Int = 0
     var pin_count: Int = 0
     var follow_count: Int = 0
     var like_count: Int = 0
     var created_at: Int = 0
     var updated_at: Int = 0
     var deleting: Int = 0
     var is_private: Int = 0

     constructor(parcel: Parcel) : this() {
          board_id = parcel.readInt()
          user_id = parcel.readInt()
          title = parcel.readString()
          description = parcel.readString()
          category_id = parcel.readString()
          seq = parcel.readInt()
          pin_count = parcel.readInt()
          follow_count = parcel.readInt()
          like_count = parcel.readInt()
          created_at = parcel.readInt()
          updated_at = parcel.readInt()
          deleting = parcel.readInt()
          is_private = parcel.readInt()
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeInt(board_id)
          parcel.writeInt(user_id)
          parcel.writeString(title)
          parcel.writeString(description)
          parcel.writeString(category_id)
          parcel.writeInt(seq)
          parcel.writeInt(pin_count)
          parcel.writeInt(follow_count)
          parcel.writeInt(like_count)
          parcel.writeInt(created_at)
          parcel.writeInt(updated_at)
          parcel.writeInt(deleting)
          parcel.writeInt(is_private)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<PinsBoardInfo> {
          override fun createFromParcel(parcel: Parcel): PinsBoardInfo {
               return PinsBoardInfo(parcel)
          }

          override fun newArray(size: Int): Array<PinsBoardInfo?> {
               return arrayOfNulls(size)
          }
     }
}