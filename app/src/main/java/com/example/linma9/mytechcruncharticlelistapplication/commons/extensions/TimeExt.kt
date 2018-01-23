package com.example.linma9.mytechcruncharticlelistapplication.commons.extensions

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun Long.getFriendlyTime(): String {
    val dateTime = Date(this)
    //val dateTime = Date(this * 1000)
    val sb = StringBuffer()
    val current = Calendar.getInstance().time
    var diffInSeconds = ((current.time - dateTime.time) / 1000).toInt()

    val sec = if (diffInSeconds >= 60) (diffInSeconds % 60).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 60
    val min = if (diffInSeconds >= 60) (diffInSeconds % 60).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 60
    val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 24
    val days = if (diffInSeconds >= 30) (diffInSeconds % 30).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 30
    var months = if (diffInSeconds >= 12) (diffInSeconds % 12).toInt() else diffInSeconds.toInt()
    diffInSeconds = diffInSeconds / 12
    val years = diffInSeconds.toInt()

    //val parser2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX") //ISO 8601

    val convertdate = Date(this)
    var strDate = SimpleDateFormat("yyyyy-MM-dd HH:mm:ss").format(convertdate)
    try {
        strDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").format(convertdate)
    } catch (e: Exception) {
        //Log.e("eee888", "+++ !!! getFriendlyTime convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ssX\"), ${e.toString()}")

        try {
            strDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(convertdate)
        } catch(e: Exception) {
            //Log.e("eee888", "+++ !!! getFriendlyTime convert exception SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\"), ${e.toString()}")
            try {
                strDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(convertdate)

            } catch(e: Exception) {
                //Log.e("eee888", "+++ !!! getFriendlyTime convert exception SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\"), ${e.toString()}")
            }
        }
    }


    if (years > 0) {
        sb.append(strDate+"  ")

        if (years == 1) {
            sb.append("a year")
        } else {
            sb.append("$years years")
        }
        if (years <= 6 && months > 0) {
            if (months == 1) {
                sb.append(" and a month")
            } else {
                sb.append(" and $months months")
            }
        }
    } else if (months > 0) {
        sb.append(strDate+"  ")

        if (months == 1) {
            sb.append("a month")
        } else {
            sb.append("$months months")
        }
        if (months <= 6 && days > 0) {
            if (days == 1) {
                sb.append(" and a day")
            } else {
                sb.append(" and $days days")
            }
        }
    } else if (days > 0) {

        if (days > 7) {
            sb.append(strDate+"  ")
        }

        if (days == 1) {
            sb.append("a day")
        } else {
            sb.append("$days days")
        }
        if (days <= 3 && hrs > 0) {
            if (hrs == 1) {
                sb.append(" and an hour")
            } else {
                sb.append(" and $hrs hours")
            }
        }

    } else if (hrs > 0) {
        if (hrs == 1) {
            sb.append("an hour")
        } else {
            sb.append("$hrs hours")
        }
        if (min > 1) {
            sb.append(" and $min minutes")
        }
    } else if (min > 0) {
        if (min == 1) {
            sb.append("a minute")
        } else {
            sb.append("$min minutes")
        }
        if (sec > 1) {
            sb.append(" and $sec seconds")
        }
    } else {
        if (sec <= 1) {
            sb.append("about a second")
        } else {
            sb.append("about $sec seconds")
        }
    }

    sb.append(" ago")

    return sb.toString()
}