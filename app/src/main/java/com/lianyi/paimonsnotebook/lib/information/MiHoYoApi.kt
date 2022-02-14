package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.bean.account.UserBean

class MiHoYoApi {
    companion object{
        //JSON数据获取
        const val JSON_DATA = "https://qoolianyi.github.io/PaimonsNotebook.github.io/"

        //首页公告
        const val OFFICIAL_RECOMMEND_POST = "https://bbs-api.mihoyo.com/post/wapi/getOfficialRecommendedPosts?gids=2"
        //首页活动
        const val BLACK_BOARD = "https://api-static.mihoyo.com/common/blackboard/ys_obc/v1/get_activity_calendar?app_sn=ys_obc"

        //获取详细信息,通过content_id
        const val LOAD_DETAIL_INFO = "https://bbs.mihoyo.com/ys/obc/content//detail?bbs_presentation_style=no_header"

        //首页信息
        const val HOME_PAGER_INFORMATION = "https://bbs-api-static.mihoyo.com/apihub/wapi/webHome?gids=2&page=1&page_size=20"

        //大地图
        const val MAP = "https://webstatic.mihoyo.com/app/ys-map-cn/index.html"

        //米游社登录
        const val LOGIN = "https://m.bbs.mihoyo.com/ys/#/login"

        //通过login_ticket 获取 itoken和stoken
        const val GET_MULTI_TOKEN = "https://api-takumi.mihoyo.com/auth/api/getMultiTokenByLoginTicket?"

        //通过Cookie获得玩家信息
        const val GET_GAME_ROLES_BY_COOKIE = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn"

        //当前月份签到奖励
        const val GET_CURRENT_MONTH_SIGN_REWARD_INFO = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=e202009291139501"
        //每日签到
        const val DAILY_SIGN = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign"

        const val GET_GACHA_LOG = "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog?"

        //获得角色列表详情 (post) requestBody ={"character_ids":[10000016,10000030...(拥有角色id)],"role_id":"","server":""}
        const val GET_CHARACTER_LIST_DETAIL = "https://api-takumi.mihoyo.com/game_record/app/genshin/api/character"

        //获得便笺url
        fun getDailyNoteUrl(gameUID:String,server:String):String{
            return "https://api-takumi-record.mihoyo.com/game_record/app/genshin/api/dailyNote?role_id=$gameUID&server=$server"
        }

        //获得旅行者札记url
        fun getMonthLedgerUrl(month:Int, gameUID: String, server: String):String{
            return "https://hk4e-api.mihoyo.com/event/ys_ledger/monthInfo?month=$month&bind_uid=$gameUID&bind_region=$server&bbs_presentation_style=fullscreen&bbs_auth+required=true&mys_source=GameRecord"
        }

        //获得本月签到信息url
        fun getCurrentMonthSignInfoUrl(gameUID: String, server: String):String{
            return "https://api-takumi.mihoyo.com/event/bbs_sign_reward/info?act_id=e202009291139501&uid=${gameUID}&region=${server}"
        }

        //获得祈愿记录url
        fun getGachaLogUrl(logUrl:String,gachaType: Int,page:Int,size:Int,end_id:String):String{
            val params = logUrl.split("?").last().dropLast(5)
            return "${GET_GACHA_LOG}${params}&gacha_type=${gachaType}&page=${page}&size=${size}&end_id=$end_id"
        }

        fun getAccountInformation(loginUid:String):String{
            return "https://bbs-api.mihoyo.com/user/api/getUserFullInfo?uid=$loginUid"
        }

        //获得帖子连接
        fun getArticleUrl(postId:String?):String{
            return if(postId?.contains("article") == true){
                "https://bbs.mihoyo.com/ys/article$postId"
            }else{
                postId?:""
            }
        }

        //获得Cookie
        fun getCookie(user: UserBean):String{
            return "ltuid=${user.loginUid};ltoken=${user.lToken};account_id=${user.loginUid};cookie_token=${user.cookieToken}"

        }

        //获得查询玩家信息的url
        fun getPlayerInfoUrl(gameUID:String, server: String):String{
            return "https://api-takumi.mihoyo.com/game_record/app/genshin/api/index?role_id=${gameUID}&server=$server"
        }

        //获得查询玩家深渊的url
        fun getAbyssUrl(gameUID: String,server: String):String{
            return "https://api-takumi.mihoyo.com/game_record/app/genshin/api/spiralAbyss?role_id=${gameUID}&schedule_type=1&server=$server"
        }

    }
}