package uselesslav.newstest.network

import com.google.gson.annotations.SerializedName

/**
 * Список элементов, получаемый с сервера
 */
class BodyResponseList<T>(@SerializedName("list") val list: List<T>)

/**
 * Элемент, получаемый с сервера
 */
class BodyResponseObject<T>(@SerializedName("news") val response: T)