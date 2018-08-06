package com.cxt.patrolapp.mvp.view.activity

import android.os.Bundle
import com.cxt.patrolapp.mvp.view.contract.LoginView
import com.cxt.patrolapp.R
import com.cxt.patrolapp.extend.showToast
import com.cxt.patrolapp.extend.startActivity
import com.cxt.patrolapp.mvp.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseEditTextActivity(), LoginView {

    private val presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button.setOnClickListener { presenter.login(account_edit.text.toString(), password_edit.text.toString()) }
    }

    override fun loginSuccess() {
        showToast(getString(R.string.login_success))
        startActivity(MainActivity::class.java)
        finish()
    }
}