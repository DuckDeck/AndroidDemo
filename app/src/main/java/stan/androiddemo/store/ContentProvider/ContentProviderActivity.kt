package stan.androiddemo.store.ContentProvider

import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_content_provider.*
import stan.androiddemo.R

class ContentProviderActivity : AppCompatActivity() {
    lateinit var adapter: ArrayAdapter<String>
    var arrContactList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_provider)
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrContactList)
        list_view_contact.adapter = adapter
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_CONTACTS),1)
        }
        else{
            readContacts()
        }
    }

    fun readContacts(){
        var cursor: Cursor? = null
        var  phoneCursor:Cursor?
        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
            if (cursor != null){
                while (cursor.moveToNext()){
                    val sb = StringBuilder()
                    val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                    val number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                    arrContactList.add(name + "\n" + number)
                    //真不知道你这电话号码读取API是怎么设计的
                    //书上是错的，读取电话号要新建一个cursor
                    phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,null,null)
                    while(phoneCursor.moveToNext()){
                        val num = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        sb.append("Phone=").append(num)
                    }
                    phoneCursor.close()
                    arrContactList.add(name + "\n" + sb.toString())

                }
                adapter.notifyDataSetChanged()
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        finally {
            if (cursor!=null){
                cursor.close()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1->
                if (grantResults.isNotEmpty() &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }
                else {
                    Toast.makeText(this,"You denied the perssion",Toast.LENGTH_LONG).show()
                }
        }
    }

}
