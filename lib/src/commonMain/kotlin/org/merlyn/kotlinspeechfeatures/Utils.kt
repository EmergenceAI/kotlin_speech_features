package org.merlyn.kotlinspeechfeatures

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

suspend fun <T> Array<T>.asyncForEachIndexed(function: (Int, T) -> Unit): List<Unit> {
    return mapIndexed { index, element ->
        GlobalScope.async(dispatcherDefault()) {
            function(index, element)
        }
    }.awaitAll()
}

private fun dispatcherDefault() : CoroutineContext {
    val crashHandler = CoroutineExceptionHandler {
            _, exception -> println("CoroutineCrash: ${exception.message}\n${exception.stackTraceToString()}")
    }
    return Dispatchers.Default + crashHandler
}