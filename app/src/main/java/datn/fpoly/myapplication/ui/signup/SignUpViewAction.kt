package datn.fpoly.myapplication.ui.signup

import datn.fpoly.myapplication.core.ViewAction

sealed class SignUpViewAction : ViewAction {
    data class SignUpAction(val phone: String, val passwd: String, val fullname: String,var idRole : String,val favouriteStores : List<String>?) : SignUpViewAction()
}