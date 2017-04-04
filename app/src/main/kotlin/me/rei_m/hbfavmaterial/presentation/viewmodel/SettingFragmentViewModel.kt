package me.rei_m.hbfavmaterial.presentation.viewmodel

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.view.View
import me.rei_m.hbfavmaterial.domain.model.UserModel
import me.rei_m.hbfavmaterial.domain.service.HatenaService
import me.rei_m.hbfavmaterial.domain.service.TwitterService
import me.rei_m.hbfavmaterial.presentation.event.FailToConnectionEvent
import me.rei_m.hbfavmaterial.presentation.event.RxBus
import me.rei_m.hbfavmaterial.presentation.event.ShowEditHatenaIdDialogEvent
import me.rei_m.hbfavmaterial.presentation.event.StartAuthoriseTwitterEvent
import me.rei_m.hbfavmaterial.presentation.helper.Navigator

class SettingFragmentViewModel(private val userModel: UserModel,
                               private val hatenaService: HatenaService,
                               private val twitterService: TwitterService,
                               private val rxBus: RxBus,
                               private val navigator: Navigator) : AbsFragmentViewModel() {

    val userId: ObservableField<String> = ObservableField("")

    val isAuthorisedHatena: ObservableBoolean = ObservableBoolean(false)

    val isAuthorisedTwitter: ObservableBoolean = ObservableBoolean(false)

    override fun onStart() {
        super.onStart()
        registerDisposable(userModel.user.subscribe {
            userId.set(it.id)
        }, userModel.completeUpdateUserEvent.subscribe {
            userId.set(it.id)
        }, hatenaService.confirmAuthorisedEvent.subscribe {
            isAuthorisedHatena.set(it)
        }, twitterService.confirmAuthorisedEvent.subscribe {
            isAuthorisedTwitter.set(it)
        })
    }

    override fun onResume() {
        super.onResume()
        userModel.getUser()
        hatenaService.confirmAuthorised()
        twitterService.confirmAuthorised()
    }

    fun onClickHatenaId(view: View) {
        rxBus.send(ShowEditHatenaIdDialogEvent())
    }

    fun onClickHatenaAuthStatus(view: View) {
        navigator.navigateToOAuth()
    }

    fun onAuthoriseHatena(isDone: Boolean, isAuthorise: Boolean) {
        if (isDone) {
            isAuthorisedHatena.set(isAuthorise)
        } else {
            // 認可を選択せずにresultCodeが設定された場合はネットワークエラーのケース.
            rxBus.send(FailToConnectionEvent())
        }
    }

    fun onClickTwitterAuthStatus(view: View) {
        rxBus.send(StartAuthoriseTwitterEvent())
    }
}
