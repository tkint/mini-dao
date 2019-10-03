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

import com.thomaskint.minidao.exception.MDException
import com.thomaskint.minidao.model.MDPair

import java.sql.*

/**
 * @author Thomas Kint
 */
class MDConnectionKT @Throws(MDException::class)
private constructor(connectionConfig: MDConnectionConfig) {

    private var connectionConfig: MDConnectionConfig? = null

    private var connection: Connection? = null

    private var statement: Statement? = null

    init {
        try {
            Class.forName(connectionConfig.driver.value)

            val completeUrl = connectionConfig.completeUrl
            val login = connectionConfig.login
            val password = connectionConfig.password

            this.connection = DriverManager.getConnection(completeUrl, login, password)
            this.connectionConfig = connectionConfig
        } catch (ex: ClassNotFoundException) {
            throw MDException(ex)
        } catch (ex: SQLException) {
            throw MDException(ex)
        }

    }

    @Throws(SQLException::class)
    private fun prepareStatement(query: String, idFieldName: String?): PreparedStatement? {
        if (idFieldName != null) {
            this.statement = this.connection!!.prepareStatement(query, arrayOf(idFieldName))
        } else {
            this.statement = this.connection!!.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        }
        return statement as PreparedStatement?
    }

    @Throws(SQLException::class)
    private fun createStatement(): Statement? {
        this.statement = this.connection?.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
        return statement
    }

    companion object {
        lateinit var instance: MDConnectionKT

        @JvmStatic
        @Throws(MDException::class)
        private fun getInstance(connectionConfig: MDConnectionConfig): MDConnectionKT? {
            try {
                if (!::instance.isInitialized || connectionConfig != instance.connectionConfig || instance.connection!!.isClosed) {
                    instance = MDConnectionKT(connectionConfig)
                }
            } catch (e: SQLException) {
                throw MDException(e)
            }
            return instance
        }

        @JvmStatic
        @Throws(MDException::class)
        fun executeQuery(connectionConfig: MDConnectionConfig, query: String): ResultSet? {
            try {
                return getInstance(connectionConfig)?.prepareStatement(query, null)?.executeQuery()
            } catch (e: SQLException) {
                throw MDException(e)
            }

        }

        @JvmStatic
        @Throws(MDException::class)
        fun execute(connectionConfig: MDConnectionConfig, sql: String): Boolean {
            try {
                return getInstance(connectionConfig)?.createStatement()!!.execute(sql)
            } catch (e: SQLException) {
                throw MDException(e)
            }

        }

        @JvmStatic
        @Throws(MDException::class)
        fun executeUpdate(connectionConfig: MDConnectionConfig, query: String, idFieldName: String?): MDPair<Statement?, Int?> {
            try {
                val statement = getInstance(connectionConfig)?.prepareStatement(query, idFieldName)
                val lines = statement?.executeUpdate()
                return MDPair(statement, lines)
            } catch (e: SQLException) {
                throw MDException(e)
            }
        }

        @JvmStatic
        @Throws(MDException::class)
        fun close() {
            try {
                if (::instance.isInitialized) {
                    if (instance.statement != null && !instance.statement!!.isClosed) {
                        if (instance.statement!!.resultSet != null && !instance.statement!!.resultSet.isClosed) {
                            instance.statement!!.resultSet.close()
                        }
                        instance.statement!!.close()
                    }
                    if (instance.connection != null && !instance.connection!!.isClosed) {
                        instance.connection!!.close()
                    }
                }
            } catch (e: SQLException) {
                throw MDException(e)
            }

        }
    }
}
