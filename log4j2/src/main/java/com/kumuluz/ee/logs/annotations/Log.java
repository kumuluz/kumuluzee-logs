/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.annotations;

import com.kumuluz.ee.logs.annotations.enums.LogParams;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Rok on 14. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Log {
    LogParams[] value() default {};
    boolean methodCall() default true;
}