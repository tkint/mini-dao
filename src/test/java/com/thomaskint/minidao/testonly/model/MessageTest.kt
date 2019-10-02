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

import com.thomaskint.minidao.annotation.MDEntity
import com.thomaskint.minidao.annotation.MDField
import com.thomaskint.minidao.annotation.MDId
import com.thomaskint.minidao.annotation.MDManyToOne

import java.math.BigDecimal

import com.thomaskint.minidao.enumeration.MDLoadPolicy.HEAVY
import com.thomaskint.minidao.enumeration.MDSQLAction.SELECT

/**
 * @author Thomas Kint
 */
@MDEntity(tableName = "message")
open class MessageTest(
        @MDId
        @MDField(fieldName = "id_message", allowedSQLActions = [SELECT])
        var idMessage: Long? = null,

        @MDField(fieldName = idAuthorFieldName)
        var idUser: Long? = null,

        @MDManyToOne(fieldName = idAuthorFieldName, targetFieldName = UserTest.idUserFieldName, target = UserTest::class, loadPolicy = HEAVY)
        var userTest: UserTest? = null,

        @MDField(fieldName = "content")
        var content: String? = null,

        @MDManyToOne(fieldName = messageTypeFieldName, targetFieldName = MessageTypeTest.valueFieldName, target = MessageTypeTest::class, loadPolicy = HEAVY)
        var messageType: MessageTypeTest? = null
) {

    companion object {
        const val idAuthorFieldName = "id_author"

        const val messageTypeFieldName = "message_type"
    }
}
