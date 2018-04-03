package uselesslav.newstest.network

import com.google.gson.annotations.SerializedName

/**
 * Список элементов, получаемый с сервера
 */
class BodyResponse<T>(@SerializedName("list") val list: List<T>)