package com.touchawesome.chronos

import android.net.Uri

/**
 * Created by scelus on 1/22/18
 */
data class CallLogEntry(val name: String?,
                        val number: String,
                        var photo: Uri?,
                        val timestamp: Long)