package com.cxt.patrolapp.mvp.presenter

import com.cxt.patrolapp.R
import com.cxt.patrolapp.enums.NetType
import com.cxt.patrolapp.extend.assignSchedulers
import com.cxt.patrolapp.mvp.model.UserRepository
import com.cxt.patrolapp.mvp.view.contract.LoginView
import io.reactivex.rxkotlin.subscribeBy
import org.apache.shiro.crypto.hash.Md5Hash

class LoginPresenter(view: LoginView) : BasePresenter<LoginView>(view) {

    private val LOGIN_SUCCESS_CODE = 20001

    fun login(account: String, password: String) {
        var errorMessage: String? = null
        if (account.isEmpty()) {
            errorMessage = context.getString(R.string.error_account_is_empty)
        } else if (password.isEmpty()) {
            errorMessage = context.getString(R.string.error_password_is_empty)
        } else if (!NetType.isConnected) {
            errorMessage = context.getString(R.string.error_net_is_unavailable)
        }
        if (errorMessage != null) {
            view.onError(errorMessage)
            return
        }
        view.showProgressDialog()
        UserRepository.getSalt(account)
                .flatMap {
                    val salt = it.data
                    val md5 = Md5Hash(password, salt).toHex()
                    UserRepository.login(account, md5)
                }
                .assignSchedulers()
                .subscribeBy(
                        onNext = { response ->
                            view.dismissProgressDialog()
                            when (response.statusCode) {
                                LOGIN_SUCCESS_CODE -> {
                                    view.loginSuccess()
                                    response.data?.let { UserRepository.uid = it }
                                }
                                else -> view.onError(response.message)
                            }
                        },
                        onError = {
                            view.onError(throwable = it)
                            view.dismissProgressDialog()
                        })
    }
}