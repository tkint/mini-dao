package com.thomaskint.minidao.utils;

import static com.thomaskint.minidao.utils.MDStringUtils.EMPTY;

public @interface MDIncomingFeature {

	String featureName() default EMPTY;

	String description() default EMPTY;
}
