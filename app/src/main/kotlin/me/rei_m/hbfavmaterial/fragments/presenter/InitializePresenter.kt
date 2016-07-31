package me.rei_m.hbfavmaterial.fragments.presenter

import android.content.Context
import android.support.v4.app.Fragment
import me.rei_m.hbfavmaterial.entities.UserEntity
import me.rei_m.hbfavmaterial.extensions.getAppContext
import me.rei_m.hbfavmaterial.fragments.BaseFragment
import me.rei_m.hbfavmaterial.repositories.UserRepository
import me.rei_m.hbfavmaterial.service.UserService
import retrofit2.adapter.rxjava.HttpException
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.HttpURLConnection
import javax.inject.Inject

class InitializePresenter(private val view: InitializeContact.View) : InitializeContact.Actions {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userService: UserService

    private val appContext: Context
        get() = (view as Fragment).getAppContext()

    private var isLoading = false

    init {
        (view as BaseFragment).component.inject(this)
    }

    override fun clickButtonSetId(userId: String): Subscription? {

        if (isLoading) return null

        isLoading = true
        view.showProgress()

        return userService.confirmExistingUserId(userId)
                .doOnUnsubscribe {
                    isLoading = false
                    view.hideProgress()
                }
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onConfirmExistingUserIdSuccess(it, userId)
                }, {
                    onConfirmExistingUserIdFailure(it)
                })
    }

    private fun onConfirmExistingUserIdSuccess(isValid: Boolean, userId: String) {
        if (isValid) {
            userRepository.store(appContext, UserEntity(userId))
            view.navigateToMain()
        } else {
            view.displayInvalidUserIdMessage()
        }
    }

    private fun onConfirmExistingUserIdFailure(e: Throwable) {
        println(e)
        if (e is HttpException) {
            if (e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                view.displayInvalidUserIdMessage()
                return
            }
        }
        view.showNetworkErrorMessage()
    }
}
