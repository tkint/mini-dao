package com.thomaskint.minidao.crud;

import com.thomaskint.minidao.connection.MDConnectionConfig;

abstract class MDCRUDBase {

	final MDConnectionConfig mdConnectionConfig;

	MDCRUDBase(MDConnectionConfig mdConnectionConfig) {
		this.mdConnectionConfig = mdConnectionConfig;
	}
}
