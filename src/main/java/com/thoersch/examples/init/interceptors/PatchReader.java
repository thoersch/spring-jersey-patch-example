package com.thoersch.examples.init.interceptors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.glassfish.jersey.message.MessageBodyWorkers;
import org.springframework.aop.framework.Advised;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Provider
@PATCH
public class PatchReader implements ReaderInterceptor {
    private UriInfo info;
    private MessageBodyWorkers workers;

    @Context
    public void setInfo(UriInfo info) {
        this.info = info;
    }

    @Context
    public void setWorkers(MessageBodyWorkers workers) {
        this.workers = workers;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext readerInterceptorContext) throws IOException, WebApplicationException {
        Object resource = info.getMatchedResources().get(0);

        Class<?> clazz = resource.getClass();
        // resolves spring's enhanced AOP wrapper
        if(Advised.class.isAssignableFrom(clazz)) {
            Advised advised = (Advised) resource;
            clazz = advised.getTargetSource().getTargetClass();
        }

        Method found = null;
        for (Method next : clazz.getMethods()) {
            if (next.getAnnotation(PATCH.class) != null) {
                found = next;
                break;
            }
        }

        if (found == null) {
            throw new IllegalArgumentException("No PATCH method on resource");
        }

        Object bean;
        try {
            bean = found.getReturnType().newInstance();
        } catch (Exception e) {
            throw new WebApplicationException(e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MessageBodyWriter<? super Object> bodyWriter = workers.getMessageBodyWriter(Object.class, bean.getClass(), new Annotation[0], MediaType.APPLICATION_JSON_TYPE);
        bodyWriter.writeTo(bean, bean.getClass(), bean.getClass(), new Annotation[0], MediaType.APPLICATION_JSON_TYPE, new MultivaluedHashMap<>(), baos);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode serverState = mapper.readValue(baos.toByteArray(), JsonNode.class);
        JsonNode patchAsNode = mapper.readValue(readerInterceptorContext.getInputStream(), JsonNode.class);
        JsonPatch patch = JsonPatch.fromJson(patchAsNode);

        try {
            JsonNode result = patch.apply(serverState);
            ByteArrayOutputStream resultAsByteArray = new ByteArrayOutputStream();
            mapper.writeValue(resultAsByteArray, result);
            readerInterceptorContext.setInputStream(new ByteArrayInputStream(resultAsByteArray.toByteArray()));

            return readerInterceptorContext.proceed();
        } catch (JsonPatchException e) {
            throw new WebApplicationException(Response.status(500).type("text/plain").entity(e.getMessage()).build());
        }
    }
}
