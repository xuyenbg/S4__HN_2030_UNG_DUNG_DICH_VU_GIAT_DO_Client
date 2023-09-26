package datn.fpoly.myapplication.ui.login

import datn.fpoly.myapplication.core.ViewAction

sealed class LoginViewAction : ViewAction {
    data class LoginAction(val username: String, val password: String) : LoginViewAction()
}