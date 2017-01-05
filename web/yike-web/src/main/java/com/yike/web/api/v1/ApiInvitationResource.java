package com.yike.web.api.v1;

import com.yike.Constants;
import com.yike.model.Entity;
import com.yike.model.Invitation;
import com.yike.util.ResponseBuilder;
import com.yike.web.BaseResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 05/01/2017
 */

@Path("/api/v1/invitation")
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ApiInvitationResource extends BaseResource {
  private static final Log LOG = LogFactory.getLog(ApiInvitationResource.class);


  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(APPLICATION_JSON)
  public Map<String, Object> post(
          @FormParam("name") String name,
          @FormParam("contacts") String contacts,
          @FormParam("content") String content
  ) {

    if (StringUtils.isEmpty(name)) {
      return ResponseBuilder.error(100101, "请输入你的姓名。");
    }
    if (StringUtils.isEmpty(contacts)) {
      return ResponseBuilder.error(100102, "请输入你的联系方式。");
    }
    if (StringUtils.isEmpty(content)) {
      return ResponseBuilder.error(100103, "请写下想要对我们说的话。");
    }

    long time = System.currentTimeMillis();

    try {
      Entity entity = new Entity(Invitation.SQL_TABLE_NAME);
      entity.set(Invitation.SQL_NAME, name)
              .set(Invitation.SQL_CONTACTS, contacts)
              .set(Invitation.SQL_CONTENT, content)
              .set(Invitation.SQL_CREATE_TIME, time)
              .set(Invitation.SQL_STATUS, Constants.STATUS_NOT_READY);
      entityDao.save(entity);

      return ResponseBuilder.OK;
    } catch (Throwable t) {
      LOG.error("unable to save an invitation", t);
      return ResponseBuilder.error(50000, "暂时无法提交，请稍后再试。");
    }
  }

}
