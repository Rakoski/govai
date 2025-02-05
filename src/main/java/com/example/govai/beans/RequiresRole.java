package com.example.govai.beans;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.*;

@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresRole {
    String[] value() default {};
}
