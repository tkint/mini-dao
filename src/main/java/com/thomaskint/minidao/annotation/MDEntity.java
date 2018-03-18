/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.annotation;

import com.thomaskint.minidao.enumeration.MDVerb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.thomaskint.minidao.enumeration.MDVerb.*;

/**
 * @author Thomas Kint
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MDEntity {

	String tableName();

	MDVerb[] params() default {SELECT, INSERT, UPDATE, DELETE};
}
