package stan.androiddemo.project.petal.Model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by stanhu on 11/8/2017.
 */
class PinsMainInfo() :Parcelable{
     var pin_id: Int = 0
     var user_id: Int = 0
     var board_id: Int = 0
     var file_id: Int = 0

     var file: PinsFileInfo? = null

     var imgRatio:Float = 1F
     get() {
          if (file != null) {
               var r = file!!.width.toFloat() / file!!.height.toFloat()
               if (r < 0.7) {
                    r = 0.7F
               }
               return r
          }
          return 1F
     }

     var media_type: Int = 0
     var source: String? = null
     var link: String? = null
     var raw_text: String? = null
     var via: Int = 0
     var via_user_id: Int = 0
     var original: Int = 0
     var created_at: Int = 0
     var like_count: Int = 0
     var seq: Int = 0
     var comment_count: Int = 0
     var repin_count: Int = 0
     var is_private: Int = 0
     var orig_source: String? = null
     var liked: Boolean = false

     var pinsUserInfo:PinsUserInfo? = null
     var pinsBoardInfo:PinsBoardInfo? = null

     constructor(parcel: Parcel) : this() {
          pin_id = parcel.readInt()
          user_id = parcel.readInt()
          board_id = parcel.readInt()
          file_id = parcel.readInt()
          file = parcel.readParcelable(PinsFileInfo::class.java.classLoader)
          media_type = parcel.readInt()
          source = parcel.readString()
          link = parcel.readString()
          raw_text = parcel.readString()
          via = parcel.readInt()
          via_user_id = parcel.readInt()
          original = parcel.readInt()
          created_at = parcel.readInt()
          like_count = parcel.readInt()
          seq = parcel.readInt()
          comment_count = parcel.readInt()
          repin_count = parcel.readInt()
          is_private = parcel.readInt()
          orig_source = parcel.readString()
          liked = parcel.readByte() != 0.toByte()
          pinsUserInfo = parcel.readParcelable(PinsUserInfo::class.java.classLoader)
          pinsBoardInfo = parcel.readParcelable(PinsBoardInfo::class.java.classLoader)
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeInt(pin_id)
          parcel.writeInt(user_id)
          parcel.writeInt(board_id)
          parcel.writeInt(file_id)
          parcel.writeParcelable(file, flags)
          parcel.writeInt(media_type)
          parcel.writeString(source)
          parcel.writeString(link)
          parcel.writeString(raw_text)
          parcel.writeInt(via)
          parcel.writeInt(via_user_id)
          parcel.writeInt(original)
          parcel.writeInt(created_at)
          parcel.writeInt(like_count)
          parcel.writeInt(seq)
          parcel.writeInt(comment_count)
          parcel.writeInt(repin_count)
          parcel.writeInt(is_private)
          parcel.writeString(orig_source)
          parcel.writeByte(if (liked) 1 else 0)
          parcel.writeParcelable(pinsUserInfo, flags)
          parcel.writeParcelable(pinsBoardInfo, flags)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<PinsMainInfo> {
          override fun createFromParcel(parcel: Parcel): PinsMainInfo {
               return PinsMainInfo(parcel)
          }

          override fun newArray(size: Int): Array<PinsMainInfo?> {
               return arrayOfNulls(size)
          }
     }
}