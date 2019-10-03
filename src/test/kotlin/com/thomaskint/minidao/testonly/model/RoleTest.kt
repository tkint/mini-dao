package com.thomaskint.minidao.testonly.model

import com.thomaskint.minidao.annotation.MDEntity
import com.thomaskint.minidao.annotation.MDField
import com.thomaskint.minidao.annotation.MDId
import com.thomaskint.minidao.annotation.MDOneToMany

import com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "role")
class RoleTest(
        @MDId
        @MDField(fieldName = valueFieldName)
        var value: String? = null,

        @MDOneToMany(fieldName = valueFieldName, targetFieldName = UserTest.roleFieldName, target = UserTest::class, loadPolicy = HEAVY)
        var users: List<UserTest>? = null
) {
    companion object {
        const val valueFieldName = "value"
    }
}
