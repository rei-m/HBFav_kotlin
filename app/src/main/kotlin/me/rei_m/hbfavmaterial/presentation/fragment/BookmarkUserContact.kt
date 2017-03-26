package me.rei_m.hbfavmaterial.presentation.fragment

import me.rei_m.hbfavmaterial.domain.entity.BookmarkEntity
import me.rei_m.hbfavmaterial.constant.ReadAfterFilter

interface BookmarkUserContact {

    interface View {

        fun showBookmarkList(bookmarkList: List<BookmarkEntity>)

        fun hideBookmarkList()

        fun showNetworkErrorMessage()

        fun showProgress()

        fun hideProgress()

        fun startAutoLoading()

        fun stopAutoLoading()

        fun showEmpty()

        fun hideEmpty()

        fun navigateToBookmark(bookmarkEntity: BookmarkEntity)
    }

    interface Actions {

        var readAfterFilter: ReadAfterFilter

        fun onCreate(view: View,
                     isOwner: Boolean,
                     bookmarkUserId: String,
                     readAfterFilter: ReadAfterFilter)

        fun onResume()

        fun onPause()

        fun onRefreshList()

        fun onScrollEnd(nextIndex: Int)

        fun onOptionItemSelected(readAfterFilter: ReadAfterFilter)

        fun onClickBookmark(bookmarkEntity: BookmarkEntity)
    }
}
