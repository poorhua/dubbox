package com.telecomjs.resources;

import com.telecomjs.constants.EOPConstants;
import com.telecomjs.handlers.AsyncRequestMapHandler;
import com.telecomjs.handlers.MessageSender;
import com.telecomjs.handlers.ProductHandler;
import com.telecomjs.messages.RequestMessage;
import com.telecomjs.service.intf.CustomService;
import com.telecomjs.service.intf.ProductRestService;
import com.telecomjs.service.intf.ProductService;
import com.telecomjs.vo.EOPResponseRoot;
import com.telecomjs.vo.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zark on 16/11/10.
 */
@Component
@Path("/prod")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class ProductResource extends AbstractCommonResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private CustomService customService;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private ProductService productService;
    @SuppressWarnings("SpringJavaAutowiringInspection")


    /*@Path("/direct/{accNbr : \\w+}")
    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getProductByDirect(@PathParam("accNbr") final String accNbr) {
        new ProductHandler(new Object[]{customService,productService},null).callService();
    }*/

    @Path("/queue/{accNbr : \\w+}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON})
    public void getProduct(@Suspended final AsyncResponse asyncResponse, @PathParam("accNbr") final String accNbr){
        logger.debug("getProduct : "+accNbr);
        //获取时间戳，作为当前会话的key
        final String requestSequence = String.valueOf(System.currentTimeMillis());
        //设置异步响应的超时设置,并缓存异步响应实例
        configResponse(asyncResponse,requestSequence);
        //将请求送到消息队列，等待异步处理。 RequestMessage 是个自定义ObjectMessage
        Map params = new HashMap<String,String>();
        params.put(EOPConstants.M_CALL_QRY_CUST_CUSTINFO_BYNBR_PARAM1,accNbr);
        messageSender.send(new RequestMessage(EOPConstants.M_CALL_QRY_CUST_PRODUCT_BYNBR,null
                ,requestSequence,EOPConstants.ASYNC_REQUEST_MESSAGE_PARAM_TYPE_STRING,accNbr));
        final long timestamp = System.currentTimeMillis();
        logger.debug("messageSender.send duration : "+String.valueOf(timestamp - Long.parseLong(requestSequence))
                +",key="+requestSequence);

        /*asyncResponse.setTimeoutHandler(new TimeoutHandler() {

            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation time out.").build());
            }
        });
        asyncResponse.setTimeout(5, TimeUnit.SECONDS);
        taskExecutor.execute(new Thread(new Runnable() {

            @Override
            public void run() {
                logger.debug("start ProductHandler(customService).callService().getProduct"+accNbr);
                new ProductHandler(new Object[]{customService,productService},asyncResponse)
                        .resumeService(EOPConstants.M_CALL_QRY_CUST_PRODUCT_BYNBR,accNbr);
                //asyncResponse.resume(result);
            }

        }));*/
    }



}
