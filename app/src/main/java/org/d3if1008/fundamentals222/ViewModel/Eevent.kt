package org.d3if1008.fundamentals222.ViewModel

open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}