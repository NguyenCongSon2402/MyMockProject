package com.example.mymockproject.helper

import timber.log.Timber

class LogUtil : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}: ${element.lineNumber})"
    }
}