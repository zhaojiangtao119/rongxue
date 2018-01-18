package com.labelwall.mywall.util.qiniu;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2018-01-03.
 * 七牛云上传图片工具类
 */

public class QnUploadHelper {

    private static String AccessKey;
    private static String SecretKey;
    private static String Domain;
    private static String BucketName;
    private static Configuration configuration;

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    private static long delayTimes = 3029414400l;

    public static void init(String accessKey, String secretKey, String domain, String bucketName) {
        AccessKey = accessKey;
        SecretKey = secretKey;
        Domain = domain;
        BucketName = bucketName;
        configuration = new Configuration.Builder()
                .zone(FixedZone.zone1)
                .build();
    }

    /**
     * 上传图片
     *
     * @param path     文件路径
     * @param keys     标识文件key值
     * @param callBack 上传图片的监听回调
     */
    public static void uploadPic(final String path, final String keys, final UploadCallBack callBack) {
        try {
            // 1:第一种方式 构造上传策略
            JSONObject _json = new JSONObject();
            _json.put("deadline", delayTimes);
            _json.put("scope", BucketName);
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            final String _uploadToken = AccessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
            UploadManager uploadManager = new UploadManager(configuration);

            uploadManager.put(path, keys, _uploadToken,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info,
                                             JSONObject response) {
                            if (info.isOK()) {
                                String picUrl = Domain + keys;//构造图片访问的url，回调出去
                                callBack.success(picUrl);
                            } else
                                callBack.fail(key, info);
                        }
                    }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        javax.crypto.SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        // 完成 Mac 操作
        return mac.doFinal(text);
    }

    /**
     * 图片上传监听接口
     */
    public interface UploadCallBack {
        void success(String url);

        void fail(String key, ResponseInfo info);
    }

}
