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

package com.thomaskint.minidao.connection

/**
 * @author Thomas Kint
 */
enum class MDDriver private constructor(
        val value: String,
        private val urlPart: String,
        private val databaseSeparator: String,
        private val complements: String? = null
) {
    MYSQL("com.mysql.cj.jdbc.Driver", "mysql://", "/", "?zeroDateTimeBehavior=convertToNull"),
    ORACLE("oracle.jdbc.driver.OracleDriver", "oracle:thin:@", ":"),
    H2("org.h2.Driver", "h2:", "/");

    open fun getCompleteUrl(url: String, port: String?, database: String): String {
        val completeUrlBuilder = StringBuilder("jdbc:")
        completeUrlBuilder.append(urlPart)
        completeUrlBuilder.append(url)
        if (this != H2) {
            completeUrlBuilder.append(":")
            completeUrlBuilder.append(port)
        }
        completeUrlBuilder.append(databaseSeparator)
        completeUrlBuilder.append(database)
        if (complements != null) {
            completeUrlBuilder.append(complements)
        }
        return completeUrlBuilder.toString()
    }

    companion object {
        val DEFAULT = MYSQL
    }
}
