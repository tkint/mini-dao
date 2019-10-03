package com.thomaskint.minidao.model

import java.util.ArrayList

class MDCallStack<T> {

    private val stack: MutableList<T>

    val isEmpty: Boolean
        get() = this.stack.isEmpty()

    init {
        this.stack = ArrayList()
    }

    fun push(item: T): T {
        this.stack.add(item)
        return item
    }

    fun pop(): T {
        val item = this.stack[this.stack.size - 1]
        this.stack.remove(item)
        return item
    }

    fun search(item: T): Int {
        var index = -1
        var i = 0
        while (i < stack.size && index == -1) {
            if (stack[i] == item) {
                index = i
            }
            i++
        }
        return index
    }

    fun isPresent(item: T): Boolean {
        return search(item) != -1
    }
}
