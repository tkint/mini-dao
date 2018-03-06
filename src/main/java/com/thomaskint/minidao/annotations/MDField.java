/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.annotations;

import com.thomaskint.minidao.enumeration.MDParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.thomaskint.minidao.enumeration.MDParam.*;

/**
 * @author tkint
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MDField {

	String name();

	MDParam[] params() default {SELECT, INSERT, UPDATE};
}
