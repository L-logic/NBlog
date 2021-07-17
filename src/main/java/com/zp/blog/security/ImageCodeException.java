package com.zp.blog.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

public class ImageCodeException extends AuthenticationException {

    public ImageCodeException(String msg, Throwable t) {
        super (msg, t);
    }

    public ImageCodeException(String msg) {
        super (msg);
    }
}
