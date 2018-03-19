package com.thomaskint.minidao.annotation;

import com.thomaskint.minidao.enumeration.MDSQLAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.thomaskint.minidao.enumeration.MDSQLAction.*;

/**
 * @author Thomas Kint
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MDField {

	String fieldName();

	MDSQLAction[] allowedSQLActions() default {SELECT, INSERT, UPDATE};
}
