package datn.fpoly.myapplication.ui.splashScreen

import datn.fpoly.myapplication.core.ViewAction

sealed class SplashViewAction : ViewAction {
    data class getStore(val idUser: String) : SplashViewAction()
    data class getUser(val idUser: String) : SplashViewAction()
}