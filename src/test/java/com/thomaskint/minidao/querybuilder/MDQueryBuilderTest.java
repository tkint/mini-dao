package com.thomaskint.minidao.querybuilder;

import com.thomaskint.minidao.exception.MDException;
import org.junit.Test;

/**
 * @author Thomas Kint
 */
public class MDQueryBuilderTest {


	@Test(expected = MDException.class)
	public void should_not_construct_query_when_no_entity() throws MDException {
		// GIVEN
		MDQueryBuilder deleteBuilder = new MDDeleteBuilder();
		// WHEN
		deleteBuilder.build();
		// THEN exception
	}
}