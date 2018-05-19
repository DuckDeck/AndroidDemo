package stan.androiddemo.Media.TakePhoto

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.ImageReader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.TextureView
import stan.androiddemo.R
import android.util.SparseIntArray
import android.view.Surface
import kotlinx.android.synthetic.main.activity_take_photo.*
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import java.util.*
import kotlin.Comparator
import android.support.v4.app.ActivityCompat
import kotlin.collections.ArrayList
import android.hardware.camera2.CameraDevice
import android.opengl.Visibility
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class TakePhotoActivity : AppCompatActivity() {

    private var cameraId:String = "0"
    private val RESULT_CODE_CAMERA = 1
    var cameraDevice:CameraDevice? = null
    lateinit var previewSession:CameraCaptureSession
    lateinit var captureRequestBuilder:CaptureRequest.Builder
    lateinit var mCaptureRequestBuilder:CaptureRequest.Builder
    lateinit var  captureRequest: CaptureRequest
    private lateinit var imageReader: ImageReader
    var height = 0
    var width = 0
    var isShowImage = false
    var cameraList = ArrayList<String>()
    private lateinit var previewSize:Size

    private val ORIENTATIONS = SparseIntArray()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);

        setContentView(R.layout.activity_take_photo)

        btn_Take.setOnClickListener {
            takePhoto()
        }

        btn_Switch.setOnClickListener {
            switchCamera()
        }

        tv.surfaceTextureListener = surfaceTextureListener
    }

    private fun takePhoto(){
        if (isShowImage){
            startCamera()
            iv.visibility = View.GONE
            isShowImage = false
            btn_Take.text = "拍照"
        }
        else{
            try {
                if(cameraDevice == null){
                    return
                }
                // 创建拍照请求
                captureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                // 设置自动对焦模式
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                // 将imageReader的surface设为目标
                captureRequestBuilder.addTarget(imageReader.surface)
                // 获取设备方向
                val rotation = windowManager.defaultDisplay.rotation
                // 根据设备方向计算设置照片的方向
                captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation))
                // 停止连续取景
                previewSession.stopRepeating()
                //拍照
                val captureRequest = captureRequestBuilder.build()
                previewSession.capture(captureRequest,captureCallback,null)
            }
            catch (e:CameraAccessException){
                e.printStackTrace()
            }
        }

    }


    fun switchCamera(){

    }

    override fun onPause(){
        super.onPause()
        if (cameraDevice != null){
            stopCamera()
        }
    }

     override fun onResume() {
        super.onResume()
        startCamera()
    }

    /**TextureView的监听 */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
        }
        //可用
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            this@TakePhotoActivity.width = width
            this@TakePhotoActivity.height = height
            openCamera()
        }
        //释放
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            stopCamera()
            return true
        }
    }

    /**摄像头状态的监听 */
    private val stateCallback = object : CameraDevice.StateCallback() {
        // 摄像头被打开时触发该方法
        override fun onOpened(cameraDevice: CameraDevice) {
            this@TakePhotoActivity.cameraDevice = cameraDevice
            // 开始预览
            takePreview()
        }

        // 摄像头断开连接时触发该方法
        override fun onDisconnected(cameraDevice: CameraDevice) {
            this@TakePhotoActivity.cameraDevice?.close()
            this@TakePhotoActivity.cameraDevice = null

        }

        // 打开摄像头出现错误时触发该方法
        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraDevice.close()
        }
    }


    val captureCallback = object:CameraCaptureSession.CaptureCallback(){
        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            try {
                previewSession.setRepeatingRequest(captureRequest,null,null)
            }
            catch (e:CameraAccessException){
                e.printStackTrace()
            }
        }
    }
    private var imageAvailableListener = object:ImageReader.OnImageAvailableListener{
        // 当照片数据可用时激发该方法
        override fun onImageAvailable(reader: ImageReader) {

            //先验证手机是否有sdcard
            val status = Environment.getExternalStorageState()
            if (status != Environment.MEDIA_MOUNTED){
                Toast.makeText(applicationContext,"你的SD卡不可用",Toast.LENGTH_SHORT).show()
                return
            }
            // 获取捕获的照片数据
            val image = reader.acquireNextImage()
            val buffer = image.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            val bitmap = BitmapFactory.decodeByteArray(data,0,data.count(),options)
            iv.setImageBitmap(bitmap)
            iv.visibility = View.VISIBLE
            stopCamera()
            isShowImage = true
            btn_Take.text = "再拍"
        }

    }
    private fun startCamera(){
        if (tv.isAvailable){
            if (cameraDevice == null){
                openCamera()
            }
        }
        else{
            tv.surfaceTextureListener = surfaceTextureListener
        }
    }


    fun openCamera()
    {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        this.setCameraCharacteristics(manager)
        try {
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf("android.permission.CAMERA"),RESULT_CODE_CAMERA)
            }
            else{
                manager.cameraIdList.toCollection(cameraList)
                for (id in cameraList){
                    Log.d(id,"cameraId")
                    cameraId = id
                    break
                }
                manager.openCamera(cameraId,stateCallback,null)
            }
        }
        catch (e:CameraAccessException){
            e.printStackTrace()
        }

    }

    fun takePreview(){
        val surfaceTexture = tv.surfaceTexture
        //设置TextureView的缓冲区大小
        surfaceTexture.setDefaultBufferSize(previewSize.width,previewSize.height)
        //获取Surface显示预览数据
        val surface = Surface(surfaceTexture)
        try {
            //创建预览请求
            mCaptureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            // 设置自动对焦模式
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            //设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(surface)
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            cameraDevice?.createCaptureSession(arrayOf(surface,imageReader.surface).asList(),object:CameraCaptureSession.StateCallback(){
                override fun onConfigureFailed(p0: CameraCaptureSession?) {
                    Log.d("我的三星手机在这里失败了","后摄像头不行，前面的可以")
                }

                override fun onConfigured(session: CameraCaptureSession) {
                  try {
                      captureRequest = mCaptureRequestBuilder.build()
                      previewSession = session
                      previewSession.setRepeatingRequest(captureRequest,null,null)
                  }
                  catch (e:CameraAccessException){
                      e.printStackTrace()
                  }
                }

            },null)
        }
        catch (e:CameraAccessException){
            e.printStackTrace()
        }
    }

    private fun setCameraCharacteristics(manager:CameraManager){
        try {
            // 获取指定摄像头的特性
            val characteristics = manager.getCameraCharacteristics(cameraId)
            //获取摄像头支持的配置属性
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val largest = Collections.max(
                    map.getOutputSizes(ImageFormat.JPEG).asList(), CompareSizesByArea())
            imageReader = ImageReader.newInstance(largest.width,largest.height,ImageFormat.JPEG,2)
            imageReader.setOnImageAvailableListener(imageAvailableListener,null)
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),width,height,largest)

        }
        catch (e:CameraAccessException){
            e.printStackTrace()
        }
        catch (e:NullPointerException){
            e.printStackTrace()
        }
    }

    fun stopCamera(){
        if (cameraDevice != null){
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            RESULT_CODE_CAMERA->{
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted){
                    openCamera()
                }
                else{
                    Toast.makeText(this,"请开启应用拍照权限",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        fun chooseOptimalSize(choices:Array<Size>,width:Int,height:Int,aspectRatio:Size):Size{
            val bigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices){
                if (option.height == option.width * h / w && option.width >= width && option.height >= height){
                    bigEnough.add(option)
                }
            }
            return if (bigEnough.size > 0){
                Collections.min(bigEnough,CompareSizesByArea())
            } else{
                choices[0]
            }
        }
    }

    class CompareSizesByArea:Comparator<Size>{
        override fun compare(lhs: Size, rhs: Size): Int {
            val res = lhs.width  * lhs.height - rhs.width  * rhs.height

            return java.lang.Long.signum(res.toLong())
        }
    }
}
