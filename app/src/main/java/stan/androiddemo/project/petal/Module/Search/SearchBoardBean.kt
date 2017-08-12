package stan.androiddemo.project.petal.Module.Search

import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Model.ErrorBaseInfo
import stan.androiddemo.project.petal.Model.FacetsInfo

/**
 * Created by hugfo on 2017/8/12.
 */
class SearchBoardBean: ErrorBaseInfo() {

     var query: QueryBean? = null
    /**
     * query : {"text":"material design","type":"board"}
     * pin_count : 4032
     * board_count : 460
     * people_count : 0
     * gift_count : 0
     * facets : {"missing":260,"total":200,"other":0,"results":{"web_app_icon":159,"design":15,"architecture":12,"other":7,"home":3,"travel_places":1,"industrial_design":1,"illustration":1,"data_presentation":1}}
     */

     var pin_count: Int = 0
     var board_count: Int = 0
     var people_count: Int = 0
     var gift_count: Int = 0
    /**
     * missing : 260
     * total : 200
     * other : 0
     * results : {"web_app_icon":159,"design":15,"architecture":12,"other":7,"home":3,"travel_places":1,"industrial_design":1,"illustration":1,"data_presentation":1}
     */

     var facets: FacetsInfo? = null

     var boards: List<BoardPinsInfo>? = null

    class QueryBean {
        var text: String? = null
        var type: String? = null
    }
}