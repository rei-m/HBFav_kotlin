package me.rei_m.hbfavkotlin.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.AdapterView
import android.widget.ListView
import com.squareup.otto.Subscribe
import me.rei_m.hbfavkotlin.R
import me.rei_m.hbfavkotlin.events.EventBusHolder
import me.rei_m.hbfavkotlin.events.HotEntryLoadedEvent
import me.rei_m.hbfavkotlin.managers.ModelLocator
import me.rei_m.hbfavkotlin.models.HotEntryModel
import me.rei_m.hbfavkotlin.views.adapters.EntryListAdapter
import me.rei_m.hbfavkotlin.events.HotEntryLoadedEvent.Companion.Type as EventType
import me.rei_m.hbfavkotlin.managers.ModelLocator.Companion.Tag as ModelTag

public class HotEntryFragment : Fragment(), FragmentAnimationI {

    private var mListAdapter: EntryListAdapter? = null

    override var mContainerWidth: Float = 0.0f

    companion object {
        fun newInstance(): HotEntryFragment {
            return HotEntryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = EntryListAdapter(activity, R.layout.list_item_entry)
    }

    override fun onDestroy() {
        super.onDestroy()
        mListAdapter = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_bookmark_list, container, false)

        val listView = view.findViewById(R.id.list_bookmark) as ListView
        
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //            val bookmarkEntity = parent?.adapter?.getItem(position) as BookmarkEntity
            //            EventBusHolder.EVENT_BUS.post(BookmarkListClickEvent(bookmarkEntity))
        }

        listView.adapter = mListAdapter

        setContainer(container!!)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        // EventBus登録
        EventBusHolder.EVENT_BUS.register(this);

        val hotEntryModel = ModelLocator.get(ModelTag.HOT_ENTRY) as HotEntryModel

        val displayedCount = mListAdapter?.count!!

        if (displayedCount != hotEntryModel.entryList.size) {
            // 表示済の件数とModel内で保持している件数をチェックし、
            // 差分があれば未表示のブックマークがあるのでリストに表示する
            mListAdapter?.clear()
            mListAdapter?.addAll(hotEntryModel.entryList)
            mListAdapter?.notifyDataSetChanged()
        } else if (displayedCount === 0) {
            // 1件も表示していなければお気に入りのブックマーク情報を取得する
            hotEntryModel.fetch()
        }
    }

    override fun onPause() {
        super.onPause()

        // EventBus登録解除
        EventBusHolder.EVENT_BUS.unregister(this);
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val animator = createAnimatorMoveSlide(transit, enter, nextAnim, activity)
        return animator ?: super.onCreateAnimation(transit, enter, nextAnim)
    }

    @Subscribe
    @SuppressWarnings("unused")
    public fun onHotEntryLoadedEvent(event: HotEntryLoadedEvent) {
        when (event.type) {
            HotEntryLoadedEvent.Companion.Type.COMPLETE -> {
                val hotEntryModel = ModelLocator.get(ModelTag.HOT_ENTRY) as HotEntryModel
                mListAdapter?.clear()
                mListAdapter?.addAll(hotEntryModel.entryList)
                mListAdapter?.notifyDataSetChanged()
            }
            HotEntryLoadedEvent.Companion.Type.ERROR -> {
                // TODO エラー表示
            }
        }
    }
}
