package com.hx.wjbaseproject.util;

import android.util.Base64;

import com.hx.wjbaseproject.api.Api;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import static com.hx.wjbaseproject.api.Api.DEFAULT_DES_KEY;

public class DesUtils {

    public static final String ALGORITHM_DES = "DES";



    /**
     * DES算法，加密
     * @param data 待加密字符串
     * @param key 加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encryptDES(String key, String data, String url) throws Exception {

        String defaultKeyUrl = "userMatchServiceI/getMatchList, " +
                "indexServiceI/getMatchNewsDetail, " +
                "userMatchServiceI/matchDetail, " +
                "userMatchServiceI/careFullyMatchList, " +
                "indexServiceI/getNoticeList, " +
                "indexServiceI/professorSpBall, " +
                "indexServiceI/indexTopic, " +
                "indexServiceI/getMatchNewsList, " +
                "schemeServiceI/professorSupportSchemeList, " +
                "schemeServiceI/matchScheme, " +
                "dataPredictorServiceI/preDatas, " +
                "spUserServiceI/getMenus, " +
                "schemeServiceI/hotScheme, " +
                "userMatchServiceI/careFullyMatchTypeList, " +
                "topicServiceI/getCommentList, " +
                "indexServiceI/browseNews, " +
                "spUserServiceI/userLogin, " +
                "spUserServiceI/userValidateCode, " +
                "weiXinUserServiceI/wxUserLogin, " +
                "systemConfigServiceI/getSystemConfigInfo, " +
                "actServiceI/actBasicInfo, " +
                "actServiceI/actJoinDetail, " +
                "actServiceI/checkActQualification, " +
                "actServiceI/userJoinActList, " +
                "actServiceI/userActSupportList, " +
                "actServiceI/supportAct, " +
                "indexServiceI/matchAnalyze, " +
                "indexServiceI/getSuspensionIcon, " +
                "indexServiceI/menuList, " +
                "systemConfigServiceI/getNewestVersion, " +
                "bigDataReportServiceI/getBigDataReport， " +
                "dataPredictorServiceI/preDatasNew，" +
                "indexServiceI/getFreeReportList, " +
                "schemeServiceI/getSchemeList, " +
                "professServiceI/getTopList, " +
                "matchNewsServiceI/getMatchNewsList, " +
                "matchNewsServiceI/matchNewsDetailInfo, " +
                "indexServiceI/hotMatchList, " +
                "userMatchServiceI/getMatchLeagueList, " +
                "userMatchServiceI/getBigDataMatchList," +
                "userMatchServiceI/getTodayMatchList, " +
                "equityPackageServiceI/queryModelList, " +
                "userMatchServiceI/teamLimitList, " +
                "dataPredictorServiceI/newsAppDarkhorseData, " +
                "bigDataReportServiceI/newsAppBigDataReport, ";


        if (url.startsWith(Api.ins().getBaseUrl())) {
            String transCode = url.substring(Api.ins().getBaseUrl().length());
            if (defaultKeyUrl.contains(transCode)
                    || transCode.startsWith("matchDetailDataServiceI/")
                    || transCode.startsWith("matchDetailOddsServiceI/")
                    || transCode.startsWith("matchDetailOutsServiceI/")
                    || transCode.startsWith("matchDataDetailServiceI/")) {
                key = DEFAULT_DES_KEY;
            }
        }

        if (StringUtils.isEmpty(key)) {
            key = DEFAULT_DES_KEY;
        }
        return encryptDES(key, data.getBytes());
    }


    /**
     * DES算法，加密
     * @param data 待加密字符串
     * @param key 加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String encryptDES(String key, String data) throws Exception {
        return encryptDES(key, data.getBytes());
    }
    /**
     * DES算法，加密
     * @param data  待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    private static String encryptDES(String key, byte[] data) throws Exception {
        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
            byte[] bytes = cipher.doFinal(data);
            return Base64.encodeToString(bytes, 0);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    /**
     * DES算法，解密
     * @param data  待解密字符串
     * @param key 解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception  异常
     */
    private static byte[] decryptDES(String key, byte[] data) throws Exception {
        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 解密.
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptDES(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            if (System.getProperty("os.name") != null
                    && (System.getProperty("os.name").equalsIgnoreCase("sunos") || System
                    .getProperty("os.name").equalsIgnoreCase("linux"))) {
                datas = decryptDES(key, Base64.decode(data, 0));
            } else {
                datas = decryptDES(key, Base64.decode(data, 0));
            }
            value = new String(datas);
        } catch (Exception e) {
            value = data;
        }
        return value;
    }
}
