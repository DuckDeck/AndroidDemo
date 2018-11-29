![Android学习](http://upload-images.jianshu.io/upload_images/1281203-f287f2af84a447ef.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

准备集成ffmpep和opencv
ffmpep和opencv集成OK，因为文件太大所以没有上传到github中。这样的话会导致下载这个项目太大了，所以打算放到网盘里面，并写文章来告诉大家怎么集成。需要去 https://pan.baidu.com/disk/home?#/all?vmode=list&path=%2F%E5%BC%80%E5%8F%91 下载需要的文件才能跑起来，怎么集成ffmpeg的文章https://www.jianshu.com/p/c93937977d2e  在这里。

刚开始时我们都是用Java开发Android，对于写了好几年`Swift`的我完全不能忍受Java的语法，没有元组(可以通过第三方实现)，没有高阶函数，没有扩展方法，不像Swift，接口(`Interface`)所有方法都要实现的，没有可选实现的。没有自定义运算符，句末要加分号等很多我在iOS开发过程中常用的语言特性都没有。直到后来Google宣布`Kotlin`为Android开发的一级语言。我就去随便了解下`Kotlin`的语法，发现`Kotlin`和`Swift`的语法实在是太像啦。两者相似度应该有80%以上吧，请参考[Swift和Kotlin语法比较](https://www.oschina.net/news/85013/swift-is-like-kotlin)。所以那还等什么？和同事协商后，确认可以在同一个项目里面可以同时使用Kotlin和Java开发，那么我果断换成`Kotlin`开发，`Kotlin`确实比Java简洁多了，不再需要`butterknife`框架，不需要写各种事件类，各种View也可直接从XML里面直接取出，再加上高阶函数和扩展方法，仿佛又回到了iOS开发APP的感觉。其开发体验和用Java开发完全不可同日而语。所以我在自己学习Android开发写的小项目也全是用`Kotlin`开发的。这里我就不再详细介绍`Kotlin`了，下面直接给出这些小项目吧。

![6个小项目](http://upload-images.jianshu.io/upload_images/1281203-63ebd9f6b502510d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这6个小项目从上下到依次是天气，小说，美图，五笔查询，花瓣和2048 AI版。
下面一个一个说起

### Weather


![天气APP](http://upload-images.jianshu.io/upload_images/1281203-6700d20278f37ccb.gif?imageMogr2/auto-orient/strip)


Weather是学习Android最经典的Android学习入门书《第一行代码》的最终实战，这个例子覆盖的知识点非常全面，有网络请求，Json转换成Model，抽屉布局，`DataSupport`数据库的使用还有`Service`服务，作为入门的实战APP实在是再适合不过了。书上是用Java写的，我在这里全部使用了`Kotlin`来实现。代码更加精简些。

### Novel 小说阅读



![搜索小说](http://upload-images.jianshu.io/upload_images/1281203-2ed6747ddf6ad36e.gif?imageMogr2/auto-orient/strip)



我有很多同事都是小说狂人，但是貌似很多小说APP都有广告(我不看小说不清楚)，于是让帮忙写个小说APP，他用都用iPhone，于是我写了个iOS版本的小说阅读APP [iOS小说阅读器](https://github.com/DuckDeck/novel)，然后我再想试试写一个Android版本的，于是就有了这个小项目，
![小说书签](http://upload-images.jianshu.io/upload_images/1281203-8e32a2053dcaf50c.gif?imageMogr2/auto-orient/strip)


`BaseQuickAdapter`真的很强大，解决了很多`RecyclerView`的实际需求，比较下拉刷新和加载更多等。书签用`DataSupport`保存，这个小项目没有使用到接口，是解析`HTML`实现的。使用了`Jsoup`来解析`HTML`，非常好用。另外在开发过程了还碰到了`GBK`问题，需要用到字符串`bytes`。问题不大。总之整个项目比较简单，很容易看懂。

### 美图

![美图](http://upload-images.jianshu.io/upload_images/1281203-c36a3a930184a3e4.gif?imageMogr2/auto-orient/strip)


![美图](http://upload-images.jianshu.io/upload_images/1281203-dd62ceeee2cbf379.gif?imageMogr2/auto-orient/strip)


![美图](http://upload-images.jianshu.io/upload_images/1281203-e7e27992eb42be93.gif?imageMogr2/auto-orient/strip)



无意中发现个网站，里面的图片质量还算不错，用来当壁纸比较合适。还可以用分辨率筛选。于是下载了他们官方的APP，但是官方APP有广告，而且好像只能看手机壁纸，不能看电脑的。这两点让我非常不爽。于是我就想自己写一个APP，无广告，可以随意收藏和下载高清壁纸。感觉很不错

开发的技术难点并不多，使用和小说阅读器一样的技术。图片显示用了`Fresco`，图片下载用了`Glide`。


### 五笔查询



![2017-08-28-09_13_25.gif](http://upload-images.jianshu.io/upload_images/1281203-b8d6be6cf4ddafae.gif?imageMogr2/auto-orient/strip)

笔者是一个五笔使用者，虽然用了很久五笔了，但还是有很多字不会打，所以我一般在我手机上安装了五笔反查这个APP，本来作为简单的查询APP，应该十分简洁的，但是里面的广告非常让我不爽，而且非常容易点到。体验也做得比较次。比如历史查询功能就做得很一般。所以我干脆自己写一个。

这个小项目没有特别的技术难点，唯一的难点度不在于APP，而是查询网站令人蛋疼的编码，全部采用`GBK`编码，而且对请求和请求体有一些特殊要求。请求时需要加上这些参数，查询的文字需要用`UrlEncode`进行`GBK`编码，然后再和key拼起来。探索这结东西花了些时间。

### 花瓣



![花瓣](http://upload-images.jianshu.io/upload_images/1281203-fb584fa0da520ec4.gif?imageMogr2/auto-orient/strip)



![花瓣](http://upload-images.jianshu.io/upload_images/1281203-1b7e37312ac8a70b.gif?imageMogr2/auto-orient/strip)




![花瓣](http://upload-images.jianshu.io/upload_images/1281203-f430ea302a8643dc.gif?imageMogr2/auto-orient/strip)



![花瓣](http://upload-images.jianshu.io/upload_images/1281203-60b70ae21a34631b.gif?imageMogr2/auto-orient/strip)


花瓣APP也算了个中型的APP了，我是按照[Github的花瓣 开源项目](https://github.com/LiCola/huabanDemo)来写的，主是要学习里面的架构写法。实现了收集，登录，喜欢，搜索等功能，原作者说全部采用**目前最新的和最热门技术**。所以还是有一定有学习价值的。你可以把我写的这个看成是Kotlin的实现版本。但是里面关于Fragment的实现机制不一样，我用了`BaseQuickAdapter`,所以会比较简单些。

技术方面用了`[RxJava/RxAndroid]`实现异步响应，简化了很多异步回调的代码。网络方面使用了`Retrofit`，搭配`RxJava`很实用，处理数据转化成`Model`一步到位。其他有兴趣的可以参考代码自己实现一次，就能明白里面的架构和技术了。


### 2048 AI版



![AI最后合成2048](http://upload-images.jianshu.io/upload_images/1281203-15f4e1876e2052e6.gif?imageMogr2/auto-orient/strip)


2048是一款具有魔力的游戏，很容易上瘾。一玩就停不下来。但是我水平很菜，从来没有合出来2048。既然我合不出来，就让AI来帮忙吧。于是我参考了2048的AI实现资料，目前网络上最主流的是算法是MixMax算法，请参考文章 [2048 AI 程序算法分析](http://blog.jobbole.com/64597/),里面详细地分析了这个算法，并且给出了js的实现。于是我也想在APP上实现这个算法，首先我要找到2048在安卓上在实现。我找到了这个[Android版2048游戏视频教程源码](https://github.com/plter/Android2048GameLesson),这个APP写得非常好，还有作弊和撤销功能。我就用这个APP的源码，并把它用Kotlin重新实现了一遍。然后再添加AI功能。AI的源代码我参考了[2048 AI](https://github.com/ovolve/2048-AI)的实现。发现里面的2048实现机制和安卓机制很像，在这个基础上加上AI代码应该不难。事实上确实如此，很快我就把AI代码移植过去并成功运行。经过简单的调试后再测试了很多次，合出2048豪无压力，只是.....我不明白为何一但AI合出2048后突然就智障了，后面的每一步都像自杀一样，豪无章法。很快就挂了。目前工作还比较忙，有时间我再看看为何会这样。

技术方面最主要就是2048的AI算法以及在Android上使用Kotlin的实现2048。这又是比较大的一块，我将专门写一篇文章来说明这个AI算法和实现代码。


1. 在 Android 平台绘制一张图片，使用至少 3 种不同的 API，ImageView，SurfaceView，自定义 View

2. 在 Android 平台使用 AudioRecord 和 AudioTrack API 完成音频 PCM 数据的采集和播放，并实现读写音频 wav 文件

3. 在 Android 平台使用 Camera API 进行视频的采集，分别使用 SurfaceView、TextureView 来预览 Camera 数据，取到 NV21 的数据回调

4. 学习 Android 平台的 MediaExtractor 和 MediaMuxer API，知道如何解析和封装 mp4 文件

5. 学习 Android 平台 OpenGL ES API，了解 OpenGL 开发的基本流程，使用 OpenGL 绘制一个三角形

6. 学习 Android 平台 OpenGL ES API，学习纹理绘制，能够使用 OpenGL 显示一张图片

7. 学习 MediaCodec API，完成音频 AAC 硬编、硬解

8. 学习 MediaCodec API，完成视频 H.264 的硬编、硬解

9. 串联整个音视频录制流程，完成音视频的采集、编码、封包成 mp4 输出

10. 串联整个音视频播放流程，完成 mp4 的解析、音视频的解码、播放和渲染

11. 进一步学习 OpenGL，了解如何实现视频的剪裁、旋转、水印、滤镜，并学习 OpenGL 高级特性，如：VBO，VAO，FBO 等等

12. 学习 Android 图形图像架构，能够使用 GLSurfaceviw 绘制 Camera 预览画面

13. 深入研究音视频相关的网络协议，如 rtmp，hls，以及封包格式，如：flv，mp4

14. 深入学习一些音视频领域的开源项目，如 webrtc，ffmpeg，ijkplayer，librtmp 等等

15. 将 ffmpeg 库移植到 Android 平台，结合上面积累的经验，编写一款简易的音视频播放器

16. 将 x264 库移植到 Android 平台，结合上面积累的经验，完成视频数据 H264 软编功能

17. 将 librtmp 库移植到 Android 平台，结合上面积累的经验，完成 Android RTMP 推流功能

18. 上面积累的经验，做一款短视频 APP，完成如：断点拍摄、添加水印、本地转码、视频剪辑、视频拼接、MV 特效等功能

android 目标
