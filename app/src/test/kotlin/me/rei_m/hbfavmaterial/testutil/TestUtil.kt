package me.rei_m.hbfavmaterial.testutil

import me.rei_m.hbfavmaterial.entity.ArticleEntity
import me.rei_m.hbfavmaterial.entity.BookmarkEntity
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava.HttpException
import java.util.*

object TestUtil {

    fun createApiErrorResponse(statusCode: Int): HttpException {
        return HttpException(Response.error<HttpException>(statusCode, ResponseBody.create(MediaType.parse("application/json"), "")))
    }

    fun createTestBookmarkEntity(no: Int, description: String = ""): BookmarkEntity {
        return BookmarkEntity(
                articleEntity = ArticleEntity(
                        title = "ArticleEntity_title_$no",
                        url = "ArticleEntity_url_$no",
                        bookmarkCount = no,
                        iconUrl = "ArticleEntity_iconUrl_$no",
                        body = "ArticleEntity_body_$no",
                        bodyImageUrl = "ArticleEntity_bodyImageUrl_$no"
                ),
                description = description,
                creator = "BookmarkEntity_creator_$no",
                date = Date(),
                bookmarkIconUrl = "BookmarkEntity_bookmarkIconUrl_$no")
    }
}
