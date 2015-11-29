package me.rei_m.hbfavkotlin.utils

import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
public class BookmarkUtilsTest {

    @Test
    public fun testGetAddBookmarkTimeString(){

        val now = Calendar.getInstance()
        now.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        now.set(2015, Calendar.OCTOBER, 15, 10, 0, 0)

        // 秒前
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("Asia/Tokyo")
        cal.set(2015, Calendar.OCTOBER, 15, 9, 59, 58)

        assertEquals(BookmarkUtil.getPastTimeString(cal.time, now.time), "2秒前")
    }

}