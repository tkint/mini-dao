package com.thomaskint.minidao.enumeration;

import com.thomaskint.minidao.utils.MDIncomingFeature;

/**
 * @author Thomas Kint
 */
@MDIncomingFeature(
		featureName = "Control of load policy",
		description = "LAZY: Load targeted target when needed | HEAVY: Always load targeted target")
public enum MDLoadPolicy {
	LAZY,
	HEAVY;

	@Override
	public String toString() {
		return name();
	}
}
