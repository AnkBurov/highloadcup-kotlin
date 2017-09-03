package ru.highloadcup.util

import org.springframework.http.HttpStatus
import javax.servlet.AsyncContext
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

inline fun AsyncContext.doAsync(crossinline callBack: (HttpServletResponse) -> Unit) {
    this.start {
        try {
            callBack(this.response as HttpServletResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            HttpUtil.createResponse(HttpStatus.BAD_REQUEST, this.response as HttpServletResponse)
        } finally {
            this.complete()
        }
    }
}

/*
inline fun doAsync(asyncContext: AsyncContext, crossinline callBack: (HttpServletResponse) -> Unit) {
    asyncContext.start {
        try {
            callBack(asyncContext.response as HttpServletResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            HttpUtil.createResponse(HttpStatus.BAD_REQUEST, asyncContext.response as HttpServletResponse)
        } finally {
            asyncContext.complete()
        }
    }
}*/
