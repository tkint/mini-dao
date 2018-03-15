/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.annotation;

import com.thomaskint.minidao.enumeration.MDLoadPolicy;
import com.thomaskint.minidao.utils.MDIncomingFeature;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.thomaskint.minidao.enumeration.MDLoadPolicy.LAZY;

/**
 * @author Thomas Kint
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MDOneToMany {

	String name();

	Class entity();

	@MDIncomingFeature(
			featureName = "Control of load policy",
			description = "The load of the parent entity will be affected by that value")
	MDLoadPolicy loadPolicy() default LAZY;
}
