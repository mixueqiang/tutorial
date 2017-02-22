package com.yike.web.api.v1;

import com.yike.Constants;
import com.yike.dao.mapper.UserRowMapper;
import com.yike.model.*;
import com.yike.service.WxITService;
import com.yike.service.WxYiKeService;
import com.yike.util.ResponseBuilder;
import com.yike.util.XmlUtil;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author ilakeyc
 * @since 07/02/2017
 */

@Path("/api/v1/wx")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiWxResource extends BaseResource {
    private static final Log LOG = LogFactory.getLog(ApiWxResource.class);

    @Resource
    protected WxITService wxITService;
    @Resource
    protected WxYiKeService wxYiKeService;

    @POST
    @Path("binding/phone")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Map<String, Object> bindingPhone(
            @FormParam("oid") String oid,
            @FormParam("phone") String phone,
            @FormParam("securityCode") String securityCode) {

        if (StringUtils.isEmpty(oid)) {
            LOG.error("binding phone could not without openId");
            return ResponseBuilder.error(70110, "发生未知错误，请稍后再试。");
        }
        if (StringUtils.isEmpty(phone)) {
            return ResponseBuilder.error(70100, "请输入手机号码。");
        }
        if (StringUtils.isNotEmpty(phone) && StringUtils.length(phone) != 11) {
            return ResponseBuilder.error(70104, "请输入有效的手机号码。");
        }
        if (StringUtils.isEmpty(securityCode)) {
            return ResponseBuilder.error(70105, "请输入验证码。");
        }

        try {

            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("type", 4);
            condition.put("phone", phone);
            condition.put("status", 1);
            Entity securityCodeEntity = entityDao.findOne("security_code", condition);
            if (securityCodeEntity == null || !StringUtils.equals(securityCode, securityCodeEntity.getString("securityCode"))) {
                return ResponseBuilder.error(70114, "无效的验证码。");
            }

            WxUser wxUser = wxYiKeService.wxUserService.getUser(oid);
            if (wxUser == null) {
                return ResponseBuilder.error(70404, "向微信请求信息失败，请稍后再试。");
            }

            if (wxUser.getUserId() != 0) {

                User user = entityDao.get(
                        "user",
                        wxUser.getUserId(),
                        UserRowMapper.getInstance());

                if (user != null) {
                    if (StringUtils.isNotEmpty(user.getPhone())) {
                        return ResponseBuilder.error(70111, "你的公众平台信息已经绑定过。");
                    }
                }
            }

            User user = entityDao.findOne("user", "phone", phone, UserRowMapper.getInstance());
            if (user != null) {
                wxYiKeService.wxUserService.updateByOpenId(oid, "userId", user.getId());
                if (StringUtils.isNotEmpty(user.getPassword())) {
                    wxYiKeService.sendBindingSuccessNoticeTemplateMessage(oid, user);
                    return ResponseBuilder.ok("n");
                }
            } else {

                long time = System.currentTimeMillis();
                // Save user.
                Entity userEntity = new Entity("user");
                userEntity.set("phone", phone);
                userEntity.set("username", "wx_" + phone.substring(7));
                userEntity.set("locale", "cn").set("roles", "user");
                userEntity.set("status", Constants.STATUS_ENABLED).set("createTime", time);
                userEntity = entityDao.saveAndReturn(userEntity);
                wxYiKeService.wxUserService.updateByOpenId(oid, "userId", userEntity.getId());
            }

            entityDao.update("security_code", "id", securityCodeEntity.getId(), "status", 0);

            return ResponseBuilder.ok("y");

        } catch (Throwable t) {
            t.printStackTrace();
            LOG.error("Failed to register user.", t);
            return ResponseBuilder.error(50000, "绑定失败，请稍后再试。");
        }
    }

    @POST
    @Path("binding/pwd")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_JSON)
    public Map<String, Object> bindingPwd(
            @FormParam("oid") String oid,
            @FormParam("username") String userName,
            @FormParam("password") String password
    ) {
        if (StringUtils.isEmpty(oid)) {
            LOG.error("binding phone could not without openId");
            return ResponseBuilder.error(70110, "发生未知错误，请稍后再试。");
        }

        if (StringUtils.isEmpty(password)) {
            return ResponseBuilder.error(70108, "请输入密码。");
        }
        if (StringUtils.isEmpty(userName)) {
            return ResponseBuilder.error(70106, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
        }
        String regex = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,20}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(userName).matches()) {
            return ResponseBuilder.error(70107, "昵称不能少于2个字，支持中文、字母、数字和下划线。");
        }

        WxUser wxUser = wxYiKeService.wxUserService.getUser(oid);
        if (wxUser == null) {
            return ResponseBuilder.error(70404, "向微信请求信息失败，请稍后再试。");
        }
        if (wxUser.getUserId() == 0) {
            return ResponseBuilder.error(70404, "你需要先绑定手机号码。");
        }
        try {
            User user = entityDao.get("user", wxUser.getUserId(), UserRowMapper.getInstance());
            if (user == null) {
                return ResponseBuilder.error(70404, "你需要先绑定手机号码。");
            }

            if (StringUtils.isNotEmpty(user.getPassword())) {
                return ResponseBuilder.error(
                        70606,
                        "密码已经设置，请到【一课上手】官方网站登录或找回密码。");
            }

            Map<String, Object> userUpdateValues = new HashMap<String, Object>();
            userUpdateValues.put("password", password);
            userUpdateValues.put("username", userName);
            userUpdateValues.put("updateTime", System.currentTimeMillis());

            entityDao.update("user", "id", user.getId(), userUpdateValues);

            wxYiKeService.sendBindingSuccessNoticeTemplateMessage(oid, user);

            // Save instructor
            Entity instructorEntity = new Entity(Instructor.SQL_TABLE_NAME);
            instructorEntity
                    .set(Instructor.SQL_USER_ID, user.getId())
                    .set(Instructor.SQL_NAME, userName)
                    .set(Instructor.SQL_CONTACTS, user.getPhone())
                    .set(Instructor.SQL_CREATE_TIME, System.currentTimeMillis())
                    .set(Instructor.SQL_STATUS, Constants.STATUS_OK);

            entityDao.save(instructorEntity);

            return ResponseBuilder.OK;

        } catch (Throwable t) {
            LOG.error("Failed to register user.", t);
            return ResponseBuilder.error(50000, "绑定失败，请稍后再试。");
        }
    }

    @GET
    @Path("it")
    @Produces(MediaType.TEXT_PLAIN)
    public String itTest(
            @DefaultValue("") @QueryParam("signature") String signature,
            @DefaultValue("") @QueryParam("timestamp") String timestamp,
            @DefaultValue("") @QueryParam("nonce") String nonce,
            @DefaultValue("") @QueryParam("echostr") String echostr) {
        return echostr;
    }

    @GET
    @Path("yike")
    @Produces(MediaType.TEXT_PLAIN)
    public String yikeTest(
            @DefaultValue("") @QueryParam("signature") String signature,
            @DefaultValue("") @QueryParam("timestamp") String timestamp,
            @DefaultValue("") @QueryParam("nonce") String nonce,
            @DefaultValue("") @QueryParam("echostr") String echostr) {
        return echostr;
    }

    @POST
    @Path("it")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_XML)
    public String receiveITMessages(String xml) {
        System.out.println(xml);

        LOG.info("wx receiveMessages : " + xml);

        WxMessage message = formatMessage(xml);

        wxITService.handleMessage(message);

        return null;
    }

    @POST
    @Path("yike")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_XML)
    public String receiveYiKeMessages(String xml) {
        System.out.println(xml);

        LOG.info("wx receiveMessages : " + xml);

        WxMessage message = formatMessage(xml);

        wxYiKeService.handleMessage(message);

        return null;
    }

    private WxMessage formatMessage(String xmlString) {

        WxMessage message = null;
        InputSource in = new InputSource(new StringReader(xmlString));
        in.setEncoding("UTF-8");
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(in);
            Element root = document.getRootElement();
            message = (WxMessage) XmlUtil.fromXmlToBean(root, WxMessage.class);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据解析错误");
        }
        return message;
    }

    private boolean check_signature(String signature, String timestamp, String nonce) {
        String[] array = new String[]{timestamp, nonce, WxITService.WX_TOKEN};
        Arrays.sort(array);
        String sortedString = array[0] + array[1] + array[2];
        String hexString;
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return false;
        }

        try {
            byte[] messageDigest = md.digest(sortedString.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    sb.append(0);
                }
                sb.append(shaHex);
            }
            hexString = sb.toString();
            return StringUtils.equals(signature, hexString);
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            return false;
        }
    }

}
