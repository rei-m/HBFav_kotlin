package me.rei_m.hbfavmaterial.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import me.rei_m.hbfavmaterial.R
import me.rei_m.hbfavmaterial.entities.BookmarkEntity
import me.rei_m.hbfavmaterial.views.widgets.bookmark.BookmarkContentsLayout
import me.rei_m.hbfavmaterial.views.widgets.bookmark.BookmarkCountTextView
import me.rei_m.hbfavmaterial.views.widgets.bookmark.BookmarkHeaderLayout

class BookmarkFragment() : BaseFragment(), IFragmentAnimation {

    companion object {

        private val ARG_BOOKMARK = "ARG_BOOKMARK"

        fun newInstance(bookmarkEntity: BookmarkEntity): BookmarkFragment {
            return BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_BOOKMARK, bookmarkEntity)
                }
            }
        }
    }
    
    private val mBookmarkEntity: BookmarkEntity by lazy {
        arguments.getSerializable(ARG_BOOKMARK) as BookmarkEntity
    }

    override var mContainerWidth: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        val bookmarkHeaderLayout = view.findViewById(R.id.fragment_bookmark_layout_header) as BookmarkHeaderLayout

        val bookmarkContents = view.findViewById(R.id.layout_bookmark_contents) as BookmarkContentsLayout

        val bookmarkCountTextView = view.findViewById(R.id.fragment_bookmark_text_bookmark_count) as BookmarkCountTextView

        with(mBookmarkEntity) {
            bookmarkHeaderLayout.bindView(this)
            bookmarkContents.bindView(this)
            bookmarkCountTextView.bindView(this)
        }

        setContainerWidth(container!!)

        return view
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val animator = createAnimatorMoveSlide(transit, enter, nextAnim, activity)
        return animator ?: super.onCreateAnimation(transit, enter, nextAnim)
    }
}
