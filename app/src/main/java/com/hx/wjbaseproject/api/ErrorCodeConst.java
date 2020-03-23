package com.hx.wjbaseproject.api;

/**
 * @描述: 错误码定义类
 * @文件名: ErrorCodeConst
 * @创建人: potter
 * @创建时间: 2018/2/1 下午2:50
 * @修改人:
 * @修改备注: Copyright 北京和信金谷科技有限公司 2018/2/1
 */
public class ErrorCodeConst {

    /**
     * 成功
     */
    public static final String SUCCESS = "0000";
    /**
     * 系统错误
     */
    public static final String SYSYTEM_ERROR = "9999";
    /**
     * 未实现该协议
     */
    public static final String NO_TRACSCODE = "9998";
    /**
     * 没有返回数据错误
     */
    public static final String NO_RETURN_MSG = "9997";
    /**
     * 系统忙，请稍后再试
     */
    public static final String SYSTEM_BUSY = "9996";

    /**
     * 部分数据存在
     */
    public static final String DATA_PART_EXISTED = "8999";

    /**
     * 部分数据错误
     */
    public static final String DATA_PART_ERROR = "8998";

    /**
     * 数据不存在
     */
    public static final String DATA_NOT_EXISTED = "8997";

    /**
     * 参数不全或者为空
     */
    public static final String PARAM_IS_NULL = "8996";

    /**
     * 更新错误
     */
    public static final String UPDATE_ERROR = "8995";

    /**
     * 插入错误
     */
    public static final String INSERT_ERROR = "8994";


    /**
     * 验证码错误
     */
    public static final String SMS_CODE_ERROR = "8993";

    /**
     * 验证码频繁
     */
    public static final String SMS_CODE_BUSY = "9639";

    //*************************************************项目专用错误码*****************************************************************
    /**
     * 原始赛事不存在
     */
    public static final String ORG_LEAGUE_NOT_EXISTED = "7999";

    /**
     * 赛事不存在
     */
    public static final String LEAGUE_NOT_EXISTED = "7998";
    /**
     * 国家不存在
     */
    public static final String COUNTRY_NOT_EXISTED = "7997";
    /**
     * 比赛不存在
     */
    public static final String MATCH_NOT_EXISTED = "7996";
    /**
     * 球队已存在
     */
    public static final String TEAM_EXISTED = "7995";
    /**
     * 球队原始信息已存在
     */
    public static final String TEAM_ORG_EXISTED = "7994";
    /**
     * 球队不存在
     */
    public static final String TEAM_NOT_EXISTED = "7993";
    /**
     * 比赛原始信息已存在
     */
    public static final String MATCH_ORG_EXISTED = "7992";


    //**********nlbNick start**********-->
    /**
     * 用户被禁用
     */
    public static final String USER_FREEZE = "3999";

    /**
     * 用户不存在
     */
    public static final String USER_ISNO = "3998";

    /**
     * 支付宝账户有误
     */
    public static final String USER_ALIPAY_FORMAT_ERROR = "3997";

    /**
     * 今日已签到
     */
    public static final String TODAY_ALREADY_SIGN = "3996";

    /**
     * 今日查看独家数据次数已达上限
     */
    public static final String TODAY_VIEW_DATA_REACH_LIMIT = "3995";

    /**
     * 用户已关注此场比赛
     */
    public static final String USER_ALREADY_FAVORITE_MATCH = "3994";
    /**
     * 用户已取关此场比赛
     */
    public static final String USER_ALREADY_EXIT_FAVORITE_MATCH = "3993";


    //**********nlbNick end**********-->

    //*****************wangL start***********************/
    /**
     * 计划不存在
     */
    public static final String PLAN_NOT_EXIT = "5999";
    /**
     * 方案不存在
     */
    public static final String SCHEME_NOT_EXIT = "5998";
    /**
     * 专家不存在
     */
    public static final String PROFESSOR_NOT_EXIT = "5997";
    /**
     * 当前计划未跟单该方案
     */
    public static final String PLAN_DETAIL_NOT_EXIT = "5996";
    /**
     * 支付金额错误
     */
    public static final String PAY_MONEY_FAIL = "5995";
    /**
     * 用户不是VIP
     */
    public static final String USER_NOT_VIP = "5994";
    /**
     * 方案未购买
     */
    public static final String SCHEME_NOT_BUY = "5993";
    /**
     * 免费看单次数用完
     */

    public static final String SCHEME_FREE_COUNT_OVER = "5992";
    /*****************wangL end***********************/


    /**
     * 比赛支持失败
     */
    public static final String MATCH_SUPPORT_FAIL = "6999";
    /**
     * vip信息不存在
     */
    public static final String VIP_INFO_NOT_EXIST = "6998";

    /**
     * 支付金额错误
     */
    public static final String PAY_MONEY_INCORRECT = "6997";

    /**
     * vip 等级错误
     */
    public static final String VIP_LEVEL_FAIL = "6996";

    /**
     * source不能为空
     */
    public static final String SOURCE_NOT_EMPTY = "6995";


    /**
     * 请绑定手机号
     */
    public static final String NEED_BIND_PHONE = "3998";
}
