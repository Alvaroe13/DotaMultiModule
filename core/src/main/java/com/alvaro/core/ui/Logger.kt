package com.alvaro.core.ui

class Logger(
    private val tag : String,
    private val isDebug : Boolean = true
){

    fun log(message : String){
        if (isDebug){
            //log locally
            printLogD(tag , message)
        }else{
            //crashlytics or any other crash report tool online
        }
    }


    companion object Factory{
        fun buildDebug(className : String) : Logger{
            return Logger( tag = className , isDebug = true)
        }

        fun buildRelease(className: String) : Logger{
            return Logger( tag = className , isDebug = false)
        }
    }

}

fun printLogD(tag : String , message : String){
    println("$tag , message = $message")
}