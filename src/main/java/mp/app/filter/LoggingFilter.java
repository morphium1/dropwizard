package mp.app.filter;

import org.glassfish.jersey.message.MessageUtils;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

@Priority(1)
public class LoggingFilter implements ContainerResponseFilter, ContainerRequestFilter, WriterInterceptor {

    private static final Comparator<Map.Entry<String, List<String>>> COMPARATOR =
            (o1, o2) -> o1.getKey().compareToIgnoreCase(o2.getKey());

    private static final String REQUEST_PREFIX = "> ";
    private static final String RESPONSE_PREFIX = "< ";
    private static final int MAX_ENTITY_SIZE = 8 * 1024;
    private static final String ENTITY_LOGGER_PROPERTY = LoggingFilter.class.getName() + ".entityLogger";
    private static final String NOTIFICATION_PREFIX = "* ";

    private final Logger logger;


    public LoggingFilter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final StringBuilder b = new StringBuilder();

        printRequestLine(b, "Server has received a request", requestContext.getMethod(), requestContext.getUriInfo().getRequestUri());
        printPrefixedHeaders(b, REQUEST_PREFIX, requestContext.getHeaders());

        if (requestContext.hasEntity()) {
            requestContext.setEntityStream(
                    logInboundEntity(b, requestContext.getEntityStream(), MessageUtils.getCharset(requestContext.getMediaType())));
        }

        log(b);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final StringBuilder b = new StringBuilder();

        printResponseLine(b, "Server responded with a response", responseContext.getStatus());
        printPrefixedHeaders(b, RESPONSE_PREFIX, responseContext.getStringHeaders());

        if (responseContext.hasEntity()) {
            final OutputStream stream = new LoggingStream(b, responseContext.getEntityStream());
            responseContext.setEntityStream(stream);
            requestContext.setProperty(ENTITY_LOGGER_PROPERTY, stream);
            // not calling log(b) here - it will be called by the interceptor
        } else {
            log(b);
        }
    }

    private void printRequestLine(final StringBuilder b, final String note, final String method, final URI uri) {
        b.append(NOTIFICATION_PREFIX)
                .append(note)
//                .append(" on thread ").append(Thread.currentThread().getName())
                .append("\n");
        b.append(REQUEST_PREFIX).append(method).append(" ")
                .append(uri.toASCIIString()).append("\n");
    }

    private void printResponseLine(final StringBuilder b, final String note, final int status) {
        b.append(NOTIFICATION_PREFIX)
                .append(note)
//                .append(" on thread ").append(Thread.currentThread().getName())
                .append("\n");
        b.append(RESPONSE_PREFIX)
                .append("ReturnCode ").append(Integer.toString(status))
                .append("\n");
    }

    private void printPrefixedHeaders(final StringBuilder b,
                                      final String prefix,
                                      final MultivaluedMap<String, String> headers) {
        for (final Map.Entry<String, List<String>> headerEntry : getSortedHeaders(headers.entrySet())) {
            final List<?> val = headerEntry.getValue();
            final String header = headerEntry.getKey();

            if (val.size() == 1) {
                b.append(prefix).append(header).append(": ").append(val.get(0)).append("\n");
            } else {
                final StringBuilder sb = new StringBuilder();
                boolean add = false;
                for (final Object s : val) {
                    if (add) {
                        sb.append(',');
                    }
                    add = true;
                    sb.append(s);
                }
                b.append(prefix).append(header).append(": ").append(sb.toString()).append("\n");
            }
        }
    }

    @Override
    public void aroundWriteTo(final WriterInterceptorContext writerInterceptorContext)
            throws IOException, WebApplicationException {
        final LoggingStream stream = (LoggingStream) writerInterceptorContext.getProperty(ENTITY_LOGGER_PROPERTY);
        writerInterceptorContext.proceed();
        if (stream != null) {
            log(stream.getStringBuilder(MessageUtils.getCharset(writerInterceptorContext.getMediaType())));
        }
    }

    private Set<Map.Entry<String, List<String>>> getSortedHeaders(final Set<Map.Entry<String, List<String>>> headers) {
        final TreeSet<Map.Entry<String, List<String>>> sortedHeaders = new TreeSet<Map.Entry<String, List<String>>>(COMPARATOR);
        sortedHeaders.addAll(headers);
        return sortedHeaders;
    }

    private InputStream logInboundEntity(final StringBuilder b, InputStream stream, final Charset charset) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        stream.mark(MAX_ENTITY_SIZE + 1);
        final byte[] entity = new byte[MAX_ENTITY_SIZE + 1];
        final int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, MAX_ENTITY_SIZE), charset));
        if (entitySize > MAX_ENTITY_SIZE) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }


    private void log(final StringBuilder b) {
        if (logger != null) {
            logger.info(b.toString());
        }
    }

    private class LoggingStream extends FilterOutputStream {

        private final StringBuilder b;
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingStream(final StringBuilder b, final OutputStream inner) {
            super(inner);

            this.b = b;
        }

        StringBuilder getStringBuilder(final Charset charset) {
            // write entity to the builder
            final byte[] entity = baos.toByteArray();

            b.append(new String(entity, 0, Math.min(entity.length, MAX_ENTITY_SIZE), charset));
            if (entity.length > MAX_ENTITY_SIZE) {
                b.append("...more...");
            }
            b.append('\n');

            return b;
        }

        @Override
        public void write(final int i) throws IOException {
            if (baos.size() <= MAX_ENTITY_SIZE) {
                baos.write(i);
            }
            out.write(i);
        }
    }
}
