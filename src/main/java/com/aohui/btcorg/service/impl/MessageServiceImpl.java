package  com.aohui.btcorg.service.impl;

import com.alibaba.fastjson.JSON;
import com.aohui.btcorg.dao.*;
import com.aohui.btcorg.entity.*;
import com.aohui.btcorg.repo.AccountRepo;
import com.aohui.btcorg.service.MessageService;
import com.aohui.btcorg.util.*;
import com.google.common.base.Strings;
import com.mashape.unirest.http.exceptions.UnirestException;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aohui.btcorg.util.DateUtil.getCurrentTimesTamp;

@Service
public class MessageServiceImpl implements MessageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private long EXPIRY = 1000 * 60 * 15;

    private static String SUCCEED = "succeed";

    private static String BE_DEFEATED = "be defeated";

    private static String BACK_TO_LOGIN = "not";

    private ExpiryMap<String, String> emailCodeMap = new ExpiryMap<>(10);

    @Autowired
    private MessageMDao messageDao;

    @Resource
    private MessageMTDao messageINDao;

    @Resource
    private MessageFDao messageFDao;

    @Resource
    private MessageUIDao messageUIDao;

    @Resource
    private AccountRepo accountRepo;

    @Autowired
    private UserDao userDao;

    @Override
    public String messageInsert(MessageReq messageReq)
    {
        MessageTextEntity messageTextEntity = new MessageTextEntity();
        messageTextEntity.setId(UUIDUtill.getUUID());
        messageTextEntity.setMessage(messageReq.getMessage());
        messageTextEntity.setPd_ate(messageReq.getpDate());
        messageTextEntity.setStatus("1");
        if ("owner".equals(messageReq.getRecid()))
        {
            messageTextEntity.setRec_id("0");
            MessageTextEntity save = messageINDao.save(messageTextEntity);
            if (null != save){
                return SUCCEED;
            }else {
                return BE_DEFEATED;
            }
        }else {
            AccountEntity username = accountRepo.findByUsername(messageReq.getRecid());
            messageTextEntity.setRec_id(String.valueOf(username.getUid()));
            MessageTextEntity save = messageINDao.save(messageTextEntity);
            if (null != save){
                return SUCCEED;
            }else {
                return BE_DEFEATED;
            }
        }
    }

//    @Scheduled(cron="0 */1 * * * ? ")
    @Override
    public void messageFind() throws ParseException {
        String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        System.out.println("111111111111111111111");
        List<MessageTextEntity> bySend_timeBetween = messageINDao.findByPd_ate(string,
                string);
        List<AccountEntity> allBy = userDao.findAllBy();
        for (MessageTextEntity messageList : bySend_timeBetween)
        {
            String recid = messageList.getRec_id();
            if ("0".equals(recid))
            {
                for (AccountEntity user :allBy)
                {
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setId(UUIDUtill.getUUID());
                    messageEntity.setMessage_id(String.valueOf(messageList.getId()));
                    messageEntity.setRec_id(String.valueOf(user.getUid()));
                    messageEntity.setSend(messageList.getSend());
                    messageEntity.setStatue("1");
                    messageDao.save(messageEntity);
                }
                messageINDao.updateMT("5",String.valueOf(messageList.getId()));
            }
            if ("1".equals(recid))
            {
                UserInformationEntity userInformationEntity = new UserInformationEntity();
                userInformationEntity.setId(UUIDUtill.getUUID());
                userInformationEntity.setMessage_id(String.valueOf(messageList.getId()));
                userInformationEntity.setRec_id(messageList.getRec_id());
                userInformationEntity.setSend(messageList.getSend());
                userInformationEntity.setStatus("1");
                messageUIDao.save(userInformationEntity);
                messageINDao.updateUT("5",String.valueOf(messageList.getId()));
            }
        }
    }

    @Override
    public String userInformation(String uid) {
        List<String> MessageS = new ArrayList<>();
        List<MessageReq> MessageList = messageFDao.findUserInfo(uid);
        List<MessageReq> userInfo = messageFDao.findUserInfo(uid);
        if (!(MessageList.size() <= 0))
        {
            for (MessageReq Message : MessageList)
            {
                messageUIDao.updateMStatus(Message.getStatue(),Message.getRecid());
                MessageS.add(Message.getMessage());
            }
        }
        if (!(userInfo.size() <= 0)){
            for (MessageReq Message : userInfo)
            {
                messageFDao.updateUIStatus(Message.getStatue(),Message.getRecid());
                MessageS.add(Message.getMessage());
            }
        }
        return JSON.toJSONString(MessageS);
    }

    @Override
    public String accountStatus(String email) {
        AccountEntity byUid = userDao.findByEmail(email);
        String status = byUid.getStatus();
        if ("1".equals(status))
        {
            return BACK_TO_LOGIN;
        }else {
            return "2";
        }


    }

    @Override
    public String updateStatus(String emailCode) {
        String email = emailCodeMap.get(emailCode);
        if (StringUtil.isEmpty(email)){
            return "code is null";
        }
        int status = userDao.updateStatus("2", email);
        if (0 != status)
        {
            emailCodeMap.remove(emailCode);
            return "activated";
        }else {
            emailCodeMap.remove(emailCode);
            return "nonactivated";
        }

    }

    @Override
    public String activate(EmailReq emailReq) {
        String s = null;
        try {
            s = MailgunClient.sendSimpleMessage(emailReq.getDomainName(),
                    emailReq.getFromEmail(),
                    emailReq.getFromNickName(),
                    emailReq.getToEmail(),
                    emailReq.getSubject(),
                    emailReq.getText(),
                    emailReq.getHtml());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public String queryUserName(String uid) {
        AccountEntity byUid = userDao.findByUid(uid);
        return byUid.getUsername();
    }


//    public static void main(String[] args) {
//        String date1="2013-06-24 12:30:30"; //
//        String date2="2013-06-26 12:30:31";
//        try {
//            if(jisuan(date1,date2)){
//                System.out.println("111111");
//
//            }else{
//                System.out.println("2222222");
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
    @Override
    public String association(String email) {
        Map<String,String> emailTime = new HashMap<>(16);
        Integer number = 0;
        userDao.updateEmailNumber(String.valueOf(number),email);
        AccountEntity byEmail = userDao.findByEmail(email);
        number = Integer.parseInt(byEmail.getEmailnumber()) + 1;
        userDao.updateEmailNumber(String.valueOf(number),email);
        String key = email.concat(byEmail.getEmailnumber());
        emailTime.put(key,DateUtil.getCurrentTimesTamp());
        System.out.println(DateUtil.getCurrentTimesTamp());
        System.out.println(emailTime.get(key));
        try {
            if (DateUtil.jisuan(emailTime.get(email.concat("0")),emailTime.get(key))){
                emailTime.clear();
                userDao.updateEmailNumber("0",email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(byEmail.getEmailnumber()) >= 20){
            return "exceed";
        }
        String md5email = MD5Util.md5password(email);
        emailCodeMap.put(md5email,email,EXPIRY);
        String enamilHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\n" +
                "\t</head>\n" +
                "\t<style>\n" +
                "\t\t.zhuce{\n" +
                "\t\t\twidth: 900px;\n" +
                "\t\t\ttop: 0px;\n" +
                "\t\t\tbottom: 0px;\n" +
                "\t\t\tleft: 0px;\n" +
                "\t\t\tright: 0px;\n" +
                "\t\t\tmargin: 0 auto;\n" +
                "\t\t\tmargin-top: 50px;\n" +
                "\t\t}\n" +
                "\t\t.dingbu{\n" +
                "\t\t\t\n" +
                "\t\t\tmargin: 0 auto;\n" +
                "\t\t\twidth: 204px;\n" +
                "\t\t\theight: 69px;\n" +
                "\t\t\tmargin-left:342px ;\n" +
                "\t\t}\n" +
                "\t\t.text1{\n" +
                "\t\t\tmargin-left: 50px;\n" +
                "\t\t}\n" +
                "\t\t.text2{\n" +
                "\t\t\tmargin-left: 50px;\n" +
                "\t\t}\n" +
                "\t\t.text3{\n" +
                "\t\t\tmargin-left: 50px;\n" +
                "\t\t}\n" +
                "\t\t.text4{\n" +
                "\t\t\tmargin-left: 50px;\n" +
                "\t\t}\n" +
                "\t\t.textall{\n" +
                "\t\t\twidth:580px;\n" +
                "\t\t\theight:430px;\n" +
                "\t\t\tbackground:rgba(255,255,255,1);\n" +
                "\t\t\tmargin-left: 200px;\n" +
                "\t\t\tmargin-top: 50px;\n" +
                "\t\t}\n" +
                "\t\t.body{\n" +
                "\t\t\tbackground: rgba(250, 250, 250, 1);\n" +
                "\t\t}\n" +
                "\t\t.anniu{\n" +
                "\t\t\twidth:171px;\n" +
                "\t\t\theight:48px;\n" +
                "\t\t\tbackground:rgba(41,47,136,1);\n" +
                "\t\t\tborder-radius:1px;\n" +
                "\t\t\tmargin-left: 205px;\n" +
                "\t\t\tmargin-top: 40px;\n" +
                "\t\t\tcolor: white;\n" +
                "\t\t}\n" +
                "\t\t.address{\n" +
                "\t\t\tmargin-left: 100px;\n" +
                "\t\t\tmargin-top: 20px;\n" +
                "\t\t}\n" +
                "\t\tbody{\n" +
                "\t\t\t background:rgba(250,250,250,1);\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"zhuce\">\n" +
                "\t\t\t<img src=\"https://austatichost.oss-ap-southeast-2.aliyuncs.com/accmail/promotional/email1.png\"  class=\"dingbu\"/>\n" +
                "\t\t\t<div class=\"textall\">\n" +
                "\t\t\t<div style=\"height: 50px;\"></div>\n" +
                "\t\t\t<div class=\"text1\" >Account created</div>\n" +
                "\t\t\t<div class=\"text2\">Dear user,</div>\n" +
                "\t\t\t<div class=\"text3\">Welcome,</div>\n" +
                "\t\t\t<div class=\"text4\">Please click the link below to confirm your email:</div>\n" +
                "\t\t\t\n" +
                "\t\t\t<button class=\"anniu\" ><a href=\"https://auscrypto.org/#/emailsOK/"+md5email+"\" style=\"color: #FFFFFF;\">Verify Email address</a></button>\n" +
                "\t\t\t<div class=\"address\">https://auscrypto.org/#/emailsOK/"+md5email+"</div>\n" +
                "\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>";

        String s = null;
        try {
            s = MailgunClient.sendSimpleMessage("auscrypto.org",
                    "no-reply@auscrypto.org",
                    "AAC",
                    email,
                    "Welcome to AAC",
                    "",
                    enamilHtml);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 修改密码
     * @param password
     * @param code
     * @return
     */
    @Override
    public String forgetThePassword(String password, String code) {
        if (!isPassword(password))
        {
            return "Passwords must be at least 8 characters, at least one letter and one number";
        }
        String email = emailCodeMap.get(code);
        if (Strings.isNullOrEmpty(email)) {
            return "url is expired";
        }
//        AccountEntity byEmail = userDao.findByEmail(email);
//        if (!byEmail.equals(email)){
//            return "unregistered";
//        }
        if (StringUtil.isEmpty(password))
        {
            return "The password you entered is wrong";
        }
        AccountEntity rec = accountRepo.findByEmail(email);
        if (rec != null) {
            rec.setPassword(MD5HEXUtil.INSTANCE.encodeMd5(password));
            accountRepo.save(rec);
        }else {
            return "user not found";
        }
//        int updatePassword = userDao.updatePassword(encodeMd5, email);
//        System.out.println(updatePassword);
//        if (updatePassword != 1){
//            emailCodeMap.remove(code);
//            return "fail to modify";
//        }
        emailCodeMap.remove(code);
        return "modify successfully";
    }

    @Override
    public String changePassword(String email) {
        String md5email = MD5Util.md5password(email);
        emailCodeMap.put(md5email,email,EXPIRY);
        String emailHtml = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"utf-8\" />\n" +
                "\t\t<title></title>\n" +
                "\t</head>\n" +
                "\t<style>\n" +
                "\t\tbody {\n" +
                "\t\t\tbackground: rgba(250, 250, 250, 1);\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.emailall {\n" +
                "\t\t\twidth: 583px;\n" +
                "\t\t\theight: 680px;\n" +
                "\t\t\tbackground: rgba(255, 255, 255, 1);\n" +
                "\t\t\tborder: 1px solid rgba(238, 238, 238, 1);\n" +
                "\t\t\ttop: 0px;\n" +
                "\t\t\tbottom: 0px;\n" +
                "\t\t\tleft: 0px;\n" +
                "\t\t\tright: 0px;\n" +
                "\t\t\tmargin: auto;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.img1 {\n" +
                "\t\t\twidth: 224px;\n" +
                "\t\t\theight: 60px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.imgall {\n" +
                "\t\t\ttop: 0px;\n" +
                "\t\t\tbottom: 0px;\n" +
                "\t\t\tleft: 0px;\n" +
                "\t\t\twidth: 224px;\n" +
                "\t\t\theight: 30px;\n" +
                "\t\t\tright: 0px;\n" +
                "\t\t\tmargin: 0 auto;\n" +
                "\t\t\tmargin-top: 59px;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.anniu {\n" +
                "\t\t\twidth: 171px;\n" +
                "\t\t\theight: 48px;\n" +
                "\t\t\tbackground: black;\n" +
                "\t\t\tborder-radius: 1px;\n" +
                "\t\t\tcolor: white;\n" +
                "\t\t\tmargin-top: 12px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext{\n" +
                "\t\t\tmargin-top: 25px;\n" +
                "\t\t\tmargin-left: 35px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext1{\n" +
                "\t\t\tfont-size:18px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tcolor:rgba(51,51,51,1);\n" +
                "\t\t}\n" +
                "\t\t.emailtext2{\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tcolor:rgba(102,102,102,1);\n" +
                "\t\t\tmargin-top: 27px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext3{\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tcolor:rgba(102,102,102,1);\n" +
                "\t\t\tmargin-top: 25px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext4{\n" +
                "\t\t\tfont-size:16px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tcolor:rgba(102,102,102,1);\n" +
                "\t\t}\n" +
                "\t\t.emailtext5{\n" +
                "\t\t\tfont-size:18px;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tcolor:rgba(237,55,53,1);\n" +
                "\t\t\tmargin-top: 20px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext6{\n" +
                "\t\t\tfont-size:15px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tmargin-top: 11px;\n" +
                "\t\t\tline-height: 25px;\n" +
                "\t\t\tcolor:rgba(102,102,102,1);\n" +
                "\t\t}\n" +
                "\t\t.emailtext7{\n" +
                "\t\t\tfont-size:15px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tmargin-top: 28px;\n" +
                "\t\t\tcolor:rgba(102,102,102,1);\n" +
                "\t\t}\n" +
                "\t\t.emailall-7 {\n" +
                "\t\t\twidth: 165px;\n" +
                "\t\t\theight: 103px;\n" +
                "\t\t\tposition: absolute;\n" +
                "\t\t}\n" +
                "\t\t\n" +
                "\t\t.emailall-8 {\n" +
                "\t\t\twidth: 370px;\n" +
                "\t\t\tfont-size: 12px;\n" +
                "\t\t\tfont-weight: 400;\n" +
                "\t\t\tcolor: rgba(102, 102, 102, 1);\n" +
                "\t\t\tmargin-left: 175px;\n" +
                "\t\t}\n" +
                "\t\t.emailtext9{\n" +
                "\t\t\tfont-size:14px;\n" +
                "\t\t\tfont-weight:400;\n" +
                "\t\t\tmargin-top: 50px;\n" +
                "\t\t\tcolor:rgba(153,153,153,1);\n" +
                "\t\t}\n" +
                "\t\t.emailtext8{\n" +
                "\t\t\t\tfont-size:16px;\n" +
                "\t\t\t}\n" +
                "\t</style>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"imgall\"><img src=\"https://austatichost.oss-ap-southeast-2.aliyuncs.com/accmail/promotional/email1.png\" class=\"img1\" /></div>\n" +
                "\t\t<div style=\"height: 43px;\"></div>\n" +
                "\t\t<div class=\"emailall\">\n" +
                "\t\t\t<div class=\"emailtext\">\n" +
                "\t\t\t\t<div class=\"emailtext1\">Forget Password</div>\n" +
                "\t\t\t\t<div class=\"emailtext2\">\n" +
                "\t\t\t\t\tWe have received a request to reset the password associated with your AussieBit account \n" +
                "\t\t\t\t\t<span style=\"color:blue\">(" + email + ").</span>\n" +
                "\t\t\t\t\tTo confirm this request and assign a new password, please click the button below:\n" +
                "\n" +
                "\t\t\t\t<div></div>\n" +
                "\t\t\n" +
                "\t\t\t<div ><a style='font-size:2em' href=\"https://auscrypto.org/#/passwordisOK/"+md5email+"\" style=\"color: black;\">Reset Password</a></div>\n" +
                "\t\t\t<div class=\"emailtext3\">If you are unable to confirm by clicking the button above, you may copy the link below into your browser's address bar to confirm:</div>\n" +
                "\t\t\t<div class=\"emailtext4\"><a href=\"https://auscrypto.org/#/passwordisOK/"+md5email+"\">https://auscrypto.org/#/passwordisOK/"+md5email+"</a></div>\n" +
                "\t\t<div style=\"margin-top: 74px;\"><img src=\"\n" +
                "\t\thttps://austatichost.oss-ap-southeast-2.aliyuncs.com/accmail/promotional/email3.png\"\n" +
                "\t\t\t\t\t\t class=\"emailall-7\">\n" +
                "\t\t\t\t\t\t<div class=\"emailall-8\">The Association of Australian cryptocurrency (AAC) is a nonprofit professional association\n" +
                "\t\t\t\t\t\t\twith its headquarters located in Sydney, Australia. With responsible use and proper regulatory support, AAC sees\n" +
                "\t\t\t\t\t\t\tcryptocurrency applications and distributed ledgers benefiting everyone.</div>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t\t\t</div>\n" +
                "\t</body>\n" +
                "</html>";
        String sendEmail = null;
        try {
            sendEmail = MailgunClient.sendSimpleMessage("auscrypto.org",
                    "no-reply@auscrypto.org",
                    "AAC",
                    email,
                    "Welcome to AAC",
                    "",
                    emailHtml);
        } catch (UnirestException e) {
            logger.error(e.getMessage());
        }
        logger.info("MessageServiceImpl changePassword sendEmail = " + sendEmail);
        return "ok";
    }

    public static boolean isPassword(String password) {
        if (null == password || "".equals(password)) {
            return false;
        }
        String regEx1 = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
}
