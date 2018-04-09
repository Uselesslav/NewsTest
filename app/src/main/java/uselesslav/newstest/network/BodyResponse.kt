package uselesslav.newstest.network

import com.google.gson.annotations.SerializedName

/**
 * Список элементов, получаемый с сервера
 */
class BodyResponseList<out T>(@SerializedName("list") val list: List<T>)

/**
 * Элемент, получаемый с сервера
 */
class BodyResponseObject<out T>(@SerializedName("news") val response: T)