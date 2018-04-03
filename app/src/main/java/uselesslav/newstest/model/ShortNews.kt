package uselesslav.newstest.model

import com.google.gson.annotations.SerializedName

class ShortNews(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("shortDescription")
        val shortDescription: String)