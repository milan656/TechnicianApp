package com.example.walkinslatestapp.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Common {

    companion object{
        var url: String? = "https://staging-backend.jkadvantage.co.in/api/"

        var urlStaging: String? = "http://qa-picture.jktyrecrm.in/#/sM1I8A"
        var urlRelease: String? = "https://picture.jktyrecrm.in/#/sM1I8A"

        fun timeStampaTimeago(time1: String): String? {
            var time: String = time1
            try {

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                format.timeZone = TimeZone.getTimeZone("UTC")
                val past = format.parse(time)
                val now = Date()
                val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
                val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
                var seconds = Math.abs(seconds1)
                var minutes = Math.abs(minutes1)
                var hours = Math.abs(hours1)
                var days = Math.abs(days1)
                if (seconds < 60) {
                    println("$seconds seconds ago")
                    time = "$seconds seconds ago"
                } else if (minutes < 60) {
                    println("$minutes minutes ago")
                    time = "$minutes minutes ago"
                } else if (hours < 24) {
                    println("$hours hours ago")
                    time = "$hours hours ago"
                } else if (days < 30) {

                    if ("$days".equals("1")) {
                        time = "$days day ago"
                    } else {
                        time = "$days days ago"
                    }
                } else if (days < 365) {
                    var month = days / 30
                    time = "$month month ago"
                } else if (days >= 365) {
                    time = (days / 365).toString() + " year ago"
                } else {
                    println("$days days ago")
                    time = "$days days ago"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return time
        }

        fun dateDiffrence(past: Date?, now: Date?): String? {

            var time: String? = null
            val now = Date()
            val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past?.time!!)
            val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past?.time!!)
            val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past?.time!!)
            val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past?.time!!)
            var seconds = Math.abs(seconds1)
            var minutes = Math.abs(minutes1)
            var hours = Math.abs(hours1)
            var days = Math.abs(days1)
            if (seconds < 60) {
                println("$seconds seconds")
                time = "$seconds seconds"
            } else if (minutes < 60) {
                println("$minutes minutes")
                time = "$minutes minutes"
            } else if (hours < 24) {
                println("$hours hours")
                time = "$hours hours"
            } else if (days < 30) {

                if ("$days".equals("1")) {
                    time = "$days day"
                } else {
                    time = "$days days"
                }
            } else if (days < 365) {
                var month = days / 30
                time = "$month month"
            } else if (days >= 365) {
                time = (days / 365).toString() + " year"
            } else {
                println("$days days")
                time = "$days days"
            }
            return time
        }

    }



}