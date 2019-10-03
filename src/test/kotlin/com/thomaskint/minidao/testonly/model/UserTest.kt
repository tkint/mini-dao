/*
 * MIT License
 *
 * Copyright (c) 2017 Thomas Kint
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.thomaskint.minidao.testonly.model

import java.math.BigDecimal
import java.util.ArrayList

import com.thomaskint.minidao.annotation.MDEntity
import com.thomaskint.minidao.annotation.MDField
import com.thomaskint.minidao.annotation.MDId
import com.thomaskint.minidao.annotation.MDManyToOne
import com.thomaskint.minidao.annotation.MDOneToMany

import com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY
import com.thomaskint.minidao.enumeration.MDSQLAction.SELECT
import com.thomaskint.minidao.testonly.model.MessageTest.Companion.idAuthorFieldName

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "user")
class UserTest(
        @MDId
        @MDField(fieldName = idUserFieldName, allowedSQLActions = [SELECT])
        var id: Long? = null,

        @MDField(fieldName = pseudoFieldName)
        var pseudo: String? = null,

        @MDField(fieldName = loginFieldName)
        var login: String? = null,

        @MDField(fieldName = "password")
        var password: String? = null,

        @MDOneToMany(fieldName = idUserFieldName, targetFieldName = idAuthorFieldName, target = MessageTest::class, loadPolicy = HEAVY)
        var messages: List<MessageTest>? = null,

        @MDManyToOne(fieldName = roleFieldName, targetFieldName = RoleTest.valueFieldName, target = RoleTest::class, loadPolicy = HEAVY)
        var role: RoleTest? = null
) {
    constructor(id: Long, pseudo: String, login: String, password: String) : this(id, pseudo, login, password, null, null)

    companion object {
        const val idUserFieldName = "id_user"

        const val pseudoFieldName = "pseudo"

        const val loginFieldName = "login"

        const val roleFieldName = "role"
    }
}
