package me.rei_m.hbfavmaterial.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.squareup.picasso.Picasso
import me.rei_m.hbfavmaterial.databinding.FragmentBookmarkBinding
import me.rei_m.hbfavmaterial.entities.BookmarkEntity
import me.rei_m.hbfavmaterial.events.EventBusHolder
import me.rei_m.hbfavmaterial.events.ui.BookmarkClickedEvent
import me.rei_m.hbfavmaterial.events.ui.BookmarkCountClickedEvent
import me.rei_m.hbfavmaterial.events.ui.BookmarkUserClickedEvent
import me.rei_m.hbfavmaterial.extensions.hide
import me.rei_m.hbfavmaterial.extensions.show
import me.rei_m.hbfavmaterial.extensions.toggle
import me.rei_m.hbfavmaterial.utils.BookmarkUtil
import me.rei_m.hbfavmaterial.views.widgets.graphics.RoundedTransformation

class BookmarkFragment : Fragment(), IFragmentAnimation {

    lateinit private var mBookmarkEntity: BookmarkEntity

    override var mContainerWidth: Float = 0.0f

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBookmarkEntity = arguments.getSerializable(ARG_BOOKMARK) as BookmarkEntity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        // 全体
        with(binding) {
            bookmarkEntity = mBookmarkEntity

            // コンテンツ部分を押した時のイベント
            layoutBookmarkContents.setOnClickListener {
                EventBusHolder.EVENT_BUS.post(BookmarkClickedEvent(mBookmarkEntity))
            }

            // ブックマークユーザー数を押した時のイベント
            fragmentBookmarkTextBookmarkCount.setOnClickListener {
                EventBusHolder.EVENT_BUS.post(BookmarkCountClickedEvent(mBookmarkEntity))
            }
        }

        // ヘッダ部分
        with(binding.fragmentBookmarkLayoutHeader) {
            Picasso.with(context)
                    .load(mBookmarkEntity.bookmarkIconUrl)
                    .transform(RoundedTransformation())
                    .into(layoutBookmarkHeaderImageUserIcon)
            root.setOnClickListener {
                EventBusHolder.EVENT_BUS.post(BookmarkUserClickedEvent(mBookmarkEntity.creator))
            }
        }

        // ブックマーク部分
        with(binding.layoutBookmarkContentsLayoutBookmark) {
            layoutBookmarkTextDescription.toggle(!mBookmarkEntity.description.isEmpty())
            Picasso.with(context)
                    .load(mBookmarkEntity.articleEntity.iconUrl)
                    .transform(RoundedTransformation())
                    .into(layoutBookmarkImageArticleIcon)
        }

        // 記事部分
        with(binding.layoutBookmarkContentsLayoutArticle) {
            if (mBookmarkEntity.articleEntity.bodyImageUrl.isEmpty()) {
                layoutArticleImageBody.hide()
            } else {
                layoutArticleImageBody.show()
                Picasso.with(context)
                        .load(mBookmarkEntity.articleEntity.bodyImageUrl)
                        .into(layoutArticleImageBody)
            }
            layoutArticleTextAddBookmarkTiming.text = BookmarkUtil.getPastTimeString(mBookmarkEntity.date)
        }

        setContainerWidth(container!!)

        return binding.root
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val animator = createAnimatorMoveSlide(transit, enter, nextAnim, activity)
        return animator ?: super.onCreateAnimation(transit, enter, nextAnim)
    }
}
