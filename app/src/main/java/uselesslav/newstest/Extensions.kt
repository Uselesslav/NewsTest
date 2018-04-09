package uselesslav.newstest

import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Устанавливает время в нужном формате
 */
fun TextView.setTime(date: String) {

    val calendar = getDate(date)

    // Получение значений
    val hour = intToString(calendar[Calendar.HOUR])
    val minute = intToString(calendar[Calendar.MINUTE])
    val second = intToString(calendar[Calendar.SECOND])

    // Заполнение текстовых полей
    this.text = this.resources.getString(R.string.time, hour, minute, second)
}

/**
 * Устанавливает дату в нужном формате
 */
fun TextView.setDate(date: String) {

    val calendar = getDate(date)

    // Получение значений
    val year = intToString(calendar[Calendar.YEAR])
    val month = intToString(calendar[Calendar.MONTH])
    val day = intToString(calendar[Calendar.DAY_OF_MONTH])

    // Заполнение текстовых полей
    this.text = this.resources.getString(R.string.date, day, month, year)
}

/**
 * Добавляет нули в дату, если дата состоит из одной цифры
 */
private fun intToString(int: Int): String {
    return if (int < 10) {
        "0" + int.toString()
    } else {
        int.toString()
    }
}

/**
 * Получение даты в нужном формате
 */
private fun getDate(date: String): Calendar {

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    try {
        calendar.time = dateFormat.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return calendar
}