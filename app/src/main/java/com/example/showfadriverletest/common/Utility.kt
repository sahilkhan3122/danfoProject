package com.example.showfadriverletest.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utility {
    var nameOfEvent = ArrayList<String>()
    var startDates = ArrayList<String>()
    var endDates = ArrayList<String>()
    var descriptions = ArrayList<String>()
    private var toast: Toast? = null
    // read the calendar events based on the query and return the list of event to requested class
    /*public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
            if (cursor.getString(4) != null) {
                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
            }
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        return nameOfEvent;
    }*/
    /**
     * This is method for get the current date with format(yyyy-MM-dd)
     *
     *
     * <P>
     * This is method for get the current date with format(yyyy-MM-dd)
    </P> *
     *
     * @param
     * @return String date
     */
    /*public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }*/
    // convert the input stream data to string
    fun inputStreamToString(`is`: InputStream?): StringBuilder {
        var rLine: String? = ""
        val answer = StringBuilder()
        val rd = BufferedReader(InputStreamReader(`is`))
        try {
            while (rd.readLine().also { rLine = it } != null) {
                answer.append(rLine)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return answer
    }

    fun logMessage(key: String?, veryLongString: String) {
        val maxLogSize = 1000
        for (i in 0..veryLongString.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = Math.min(end, veryLongString.length)
            Log.v(key, veryLongString.substring(start, end))
        }
    }

    fun getValidValue(data: String): String {
        val value: String
        val i = data.toInt()
        value = if (i <= 9) {
            "0$i"
        } else {
            i.toString()
        }
        return "$value.00"
    }

    fun showToast(context: Context?, message: String?) {
        if (toast != null) {
            toast!!.cancel()
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getDateFormat(time: Date?): String {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        return sdf.format(time)
    }
}
