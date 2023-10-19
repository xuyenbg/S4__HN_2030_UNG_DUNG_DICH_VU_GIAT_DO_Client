package datn.fpoly.myapplication.ui.login

import datn.fpoly.myapplication.core.ViewAction

sealed class LoginViewAction : ViewAction {
    data class LoginAction(val phone: String, val userId: String) : LoginViewAction()
}