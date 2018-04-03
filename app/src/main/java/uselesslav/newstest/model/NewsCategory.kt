package uselesslav.newstest.model

import com.google.gson.annotations.SerializedName

/**
 * Категория новостей
 */
class NewsCategory(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String)