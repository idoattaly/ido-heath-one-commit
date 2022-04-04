package com.dchealth.util;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/25.
 */
public class SmsSendUtil {
    private static SmsSendUtil instance = null;
    private static ResourceLoader loader = ResourceLoader.getInstance();
    private static final String DEFAULT_CONFIG_FILE = "dchealth.properties";
    private static Properties prop = null;
    private static ConcurrentMap<String, String> paramMap = new ConcurrentHashMap<String, String>();
    private String  secretKey = "bjjmyrj1bjjmyrj2";
    public static final String register = "register";
    public static final String delPationt = "delPationt";
    public static final String pictureCode = "pictureCode";
    public static final String pictureCodeToRegister = "pictureCodeToRegister";
    private ExecutorService service;

    private SmsSendUtil(){
        this.service = Executors.newFixedThreadPool(15);
//        this.service = new ThreadPoolExecutor(5, 10, 3,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.CallerRunsPolicy());
    }
    public static SmsSendUtil getInstance(){
        if(instance==null){
            synchronized (SmsSendUtil.class){
                if(instance==null){
                    instance = new SmsSendUtil();
                }
            }
        }
        return instance;
    }
    class sendCodeHandler implements Runnable{
        private String mobile;
        private String veryCode;
        private String type;
        public sendCodeHandler(String mobile,String veryCode,String type){
            this.mobile = mobile;
            this.veryCode = veryCode;
            this.type = type;
        }
        @Override
        public void run() {
            sendVeryCode(mobile,veryCode,type);
        }
    }
    public String execSendCode(String mobile,String type) {
        Integer veryCodeNum = getStringByKey("veryCodeNum")==null?6:Integer.valueOf(getStringByKey("veryCodeNum"));
        String veryCode = getRandNum(veryCodeNum);
        this.service.submit(new sendCodeHandler(mobile,veryCode,type));
        return veryCode;
    }
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    private static void sendVeryCode(String phone,String veryCode,String type){
        String accessKeyId = getStringByKey("accessKeyId");
        String accessKeySecret = getStringByKey("accessKeySecret");
        String mnsEndpoint = getStringByKey("mnsEndpoint");
        String topicName = getStringByKey("topic");
        String signName = getStringByKey("signName");
        String templateCode = getStringByKey("templateCode");
        if(register.equals(type)){
            templateCode = getStringByKey("registerTemplateCode")==null?getStringByKey("templateCode"):getStringByKey("registerTemplateCode");
        }
        CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, mnsEndpoint);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(topicName);
        //设置SMS消息体（必须）
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        //Step 3. 生成SMS消息属性
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(signName);
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(templateCode);
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("no",veryCode);
        // 3.4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            //System.out.println("MessageId: " + ret.getMessageId());
            //System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }

    /**
     * 生成随机码
     * @param charCount 随机码位数
     * @return
     */
    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }
    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static String getStringByKey(String key, String propName) {
        try {
            prop = loader.getPropFromProperties(propName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        key = key.trim();
        if (!paramMap.containsKey(key)) {
            if (prop.getProperty(key) != null) {
                paramMap.put(key, prop.getProperty(key));
            }
        }
        return paramMap.get(key);
    }

    public static String getStringByKey(String key) {
        return getStringByKey(key, DEFAULT_CONFIG_FILE);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        Pattern p = Pattern.compile(REGEX_MOBILE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    public static void main(String args[]){
        //getInstance().execSendCode("18710026153");
        System.out.println(isMobile("18710026153"));
    }
}
