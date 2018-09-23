package stan.androiddemo.Media.Audio

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.*
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_audio_record.*
import stan.androiddemo.R
import java.io.*


class AudioRecordActivity : AppCompatActivity() {

    val REQ_PERMISSION_AUDIO = 0x01
    lateinit var audioFile:File
    var mCaptureThread:Thread? = null
    var isRecording = false
    var isPlaying = false
    val frequence = 44100
    val channelConfig = AudioFormat.CHANNEL_IN_MONO
    val playChannelConfig = AudioFormat.CHANNEL_IN_STEREO
    val audioEncoding = AudioFormat.ENCODING_PCM_16BIT
    lateinit var  playerTask:PlayTask
    lateinit var  recordTask:RecordTask
    var audioStatus = AudioStatus.Normal
        set(value) {
            field = value
            updateStatus()
        }
    enum class AudioStatus{
        Normal,Recording,RecordingPausing, RecordCompleted, Playing,PlayingPausing,PlayCompleted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)
        audioStatus = AudioStatus.Normal


        btn_record.setOnClickListener {
            if (audioStatus == AudioStatus.Normal || audioStatus == AudioStatus.RecordingPausing){
                startAudioRecord()
            }
            else if(audioStatus == AudioStatus.Recording){
                //暂时不暂停
            }
        }

        btn_play.setOnClickListener {
            if(isPlaying){
                stopAudioPlay()
            }
            else{
                startAudioPlay()
            }
        }

        btn_stop.setOnClickListener {
            stopAudioRecord()
        }

    }

    private fun updateStatus(){
        when(audioStatus){
            AudioStatus.Normal-> {
                btn_play.isEnabled = false
                btn_delete.isEnabled = false
                btn_stop.isEnabled = false
            }
            AudioStatus.Recording->{
                btn_play.isEnabled = false
                btn_delete.isEnabled = false
                btn_record.text = "暂停"
                btn_stop.isEnabled = true
            }
            AudioStatus.RecordCompleted->{
                btn_delete.isEnabled = true
                btn_record.text = "录音"
                btn_stop.isEnabled = false
                btn_play.isEnabled = true
            }
            AudioStatus.Playing->{
                btn_record.isEnabled = false
                btn_delete.isEnabled = false
                btn_stop.isEnabled = false
            }
            AudioStatus.PlayCompleted->{
                btn_record.isEnabled = true
                btn_delete.isEnabled = true
            }
        }
    }


    private fun startAudioRecord(){
        if(checkPermission()){
            val packageManager = getPackageManager()
            if(!packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
                Toast.makeText(this,"This device do not have a MICROPHONE",Toast.LENGTH_SHORT).show()
                return
            }
            val path = File(Environment.getExternalStorageDirectory().absolutePath + "/AudioRecord")
            path.mkdirs()
            try {
                audioFile = File.createTempFile("recording",".pcm",path)
            }
            catch (e:IOException){
                Toast.makeText(this,"录音文件创建失败",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
            audioStatus = AudioStatus.Recording
            recordTask = RecordTask()
            recordTask.execute()
            Toast.makeText(this,"Recording start",Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermision()
        }
    }

    private fun stopAudioRecord(){
        isRecording = false
        audioStatus = AudioStatus.RecordCompleted
        Toast.makeText(this,"Recording completed",Toast.LENGTH_SHORT).show()
    }

    private fun startAudioPlay(){
        playerTask = PlayTask()
        playerTask.execute()
        audioStatus = AudioStatus.Playing
        Toast.makeText(this,"Recorded playing",Toast.LENGTH_SHORT).show()
    }

    private fun stopAudioPlay(){
        audioStatus = AudioStatus.PlayingPausing
        isPlaying = true
    }

    private fun checkPermission():Boolean{
        val result = ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.RECORD_AUDIO)
        return  result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermision(){

        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.RECORD_AUDIO),REQ_PERMISSION_AUDIO)
    }

    @SuppressLint("ShowToast")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQ_PERMISSION_AUDIO->{
                if(grantResults.count() > 0){
                    val storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val recordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (storagePermission && recordPermission){
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    inner  class RecordTask :AsyncTask<Void,Int,Void>(){
        override fun doInBackground(vararg p0: Void?): Void? {
            isRecording = true
            try {
                val dos = DataOutputStream(BufferedOutputStream(FileOutputStream(audioFile)))
                var bufferSize = AudioRecord.getMinBufferSize(frequence,channelConfig,audioEncoding)
                val record = AudioRecord(MediaRecorder.AudioSource.MIC,frequence,channelConfig,audioEncoding,bufferSize)
                val buffer = ShortArray(bufferSize)
                record.startRecording()
                var r = 0
                while (isRecording){
                    var bufferReadResult = record.read(buffer,0,buffer.count())
                    var i = 0
                    while (i<bufferReadResult){
                        dos.writeShort(buffer[i].toInt())
                    }
                    publishProgress(r)
                    r++
                }
                record.stop()
                Log.i("slack","::"+audioFile.length())
                dos.close()
            }
            catch (e:Exception){
                Log.e("slack","error: "+ e.message)
            }
            return  null
        }
    }

    inner class PlayTask: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            isRecording = true
            var bufferSize = AudioTrack.getMinBufferSize(frequence,playChannelConfig,audioEncoding)
            val buffer = ShortArray(bufferSize)
            try {
                val dis = DataInputStream(BufferedInputStream(FileInputStream(audioFile)))
                val track = AudioTrack(AudioManager.STREAM_MUSIC,frequence,playChannelConfig,audioEncoding,bufferSize,AudioTrack.MODE_STREAM)
                track.play()
                while (isPlaying && dis.available() > 0){
                    var i = 0
                    while (dis.available() > 0 && i < buffer.count()){
                        buffer[i] = dis.readShort()
                        i++
                    }
                    track.write(buffer,0,buffer.count())
                }
                track.stop()
                dis.close()
            }
            catch (e:Exception){
                Log.e("slack","error: "+ e.message)
            }
            return  null

        }
    }
}


