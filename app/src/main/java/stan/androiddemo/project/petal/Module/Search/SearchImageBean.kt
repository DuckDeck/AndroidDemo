package stan.androiddemo.project.petal.Module.Search

import stan.androiddemo.project.petal.Model.ErrorBaseInfo
import stan.androiddemo.project.petal.Model.FacetsInfo
import stan.androiddemo.project.petal.Model.PinsMainInfo

/**
 * Created by stanhu on 12/8/2017.
 */
class SearchImageBean: ErrorBaseInfo() {

    /**
     * text : 狗狗
     * type : pin
     */

     var query: QueryBean? = null
    /**
     * query : {"text":"狗狗","type":"pin"}
     * pin_count : 23924
     * board_count : 2312
     * people_count : 474
     * gift_count : 95
     * facets : {"missing":0,"total":23923,"other":0,"results":{"pets":6450,"funny":2284,"travel_places":1809,"other":1764,"photography":1760,"design":1652,"illustration":1529,"apparel":956,"web_app_icon":923,"home":779,"diy_crafts":584,"desire":567,"beauty":469,"industrial_design":419,"kids":298,"people":238,"anime":232,"film_music_books":218,"tips":191,"quotes":173,"food_drink":128,"wedding_events":123,"art":96,"modeling_hair":74,"games":55,"men":50,"architecture":30,"data_presentation":23,"education":20,"fitness":13,"geek":5,"sports":4,"digital":4,"cars_motorcycles":3}}
     * pins : [{"pin_id":613719457,"user_id":18165573,"board_id":28175104,"file_id":80120821,"file":{"farm":"farm1","bucket":"hbimg","key":"393e0b24e9e047e7d922b4277743d42a11bc563e8653-NqeAzU","type":"image/jpeg","width":440,"height":440,"frames":1},"media_type":0,"source":"weibo.com","link":"http://weibo.com/2968590850/DixnevliG","raw_text":"对可爱又听话的狗狗毫无抵抗力","text_meta":{},"via":7,"via_user_id":0,"original":null,"created_at":1455853492,"like_count":0,"comment_count":0,"repin_count":30,"is_private":0,"orig_source":null,"user":{"user_id":18165573,"username":"安頔Andy","urlname":"nmualcswwp","created_at":1449122560,"avatar":{"id":94431253,"farm":"farm1","bucket":"hbimg","key":"b036e8fb347fdfdd8428b1c1b93961aded1c563d77b9-nRf8ou","type":"image/jpeg","height":"440","frames":"1","width":"440"}},"board":{"board_id":28175104,"user_id":18165573,"title":"萌宠 萌宠 萌宠","description":"","category_id":"kids","seq":32,"pin_count":59,"follow_count":35,"like_count":0,"created_at":1455848727,"updated_at":1458096951,"deleting":0,"is_private":0,"extra":{"cover":{"pin_id":"631791256"}}}}]
     * page : 1
     * category : null
     * ads : {"fixedAds":[{"id":15,"link":"http://event.huaban.com/activity/4/slug/home/?md=homefeed","image":{"bucket":"hbimg-other","key":"92ebf6aacbcf7649e39458132c45b0f06dbf46e022659","width":236,"height":420},"type":2,"placement":"PC:fixed:home,PC:fixed:search","category":"CATEGORY_all","startAt":1458000000,"endAt":1459036800,"position":0}],"normalAds":[]}
     */

     var pin_count: Int = 0
     var board_count: Int = 0
     var people_count: Int = 0
     var gift_count: Int = 0

    /**
     * missing : 0
     * total : 23923
     * other : 0
     * results : {"pets":6450,"funny":2284,"travel_places":1809,"other":1764,"photography":1760,"design":1652,"illustration":1529,"apparel":956,"web_app_icon":923,"home":779,"diy_crafts":584,"desire":567,"beauty":469,"industrial_design":419,"kids":298,"people":238,"anime":232,"film_music_books":218,"tips":191,"quotes":173,"food_drink":128,"wedding_events":123,"art":96,"modeling_hair":74,"games":55,"men":50,"architecture":30,"data_presentation":23,"education":20,"fitness":13,"geek":5,"sports":4,"digital":4,"cars_motorcycles":3}
     */

     var facets: FacetsInfo? = null
     var page: Int = 0
     var category: Any? = null
     var ads: AdsBean? = null
     var pins: List<PinsMainInfo>? = null

    class AdsBean {
        /**
         * id : 15
         * link : http://event.huaban.com/activity/4/slug/home/?md=homefeed
         * image : {"bucket":"hbimg-other","key":"92ebf6aacbcf7649e39458132c45b0f06dbf46e022659","width":236,"height":420}
         * type : 2
         * placement : PC:fixed:home,PC:fixed:search
         * category : CATEGORY_all
         * startAt : 1458000000
         * endAt : 1459036800
         * position : 0
         */

        private var fixedAds: List<FixedAdsBean>? = null
    }


    class FixedAdsBean {
        private var id: Int = 0
        private var link: String? = null
        /**
         * bucket : hbimg-other
         * key : 92ebf6aacbcf7649e39458132c45b0f06dbf46e022659
         * width : 236
         * height : 420
         */

        private var image: ImageBean? = null
        private var type: Int = 0
        private var placement: String? = null
        private var category: String? = null
        private var startAt: Int = 0
        private var endAt: Int = 0
        private var position: Int = 0

        class ImageBean {
            private var bucket: String? = null
            private var key: String? = null
            private var width: Int = 0
            private var height: Int = 0
        }
    }

    class QueryBean {
        var text: String? = null
        var type: String? = null
    }
}