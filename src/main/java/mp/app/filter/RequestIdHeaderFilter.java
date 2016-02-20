package mp.app.filter;

import org.jboss.logging.MDC;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.UUID;

import static mp.app.rest.Constants.REQUEST_ID;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Priority(0)
public class RequestIdHeaderFilter implements ContainerResponseFilter, ContainerRequestFilter {



    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (isEmpty(responseContext.getHeaderString(REQUEST_ID))) {
            responseContext.getHeaders().add(REQUEST_ID,MDC.get(REQUEST_ID));
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String requestId = requestContext.getHeaderString(REQUEST_ID);
        if (isEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        requestContext.getHeaders().add(REQUEST_ID,requestId);
        MDC.put(REQUEST_ID,requestId);
    }
}
