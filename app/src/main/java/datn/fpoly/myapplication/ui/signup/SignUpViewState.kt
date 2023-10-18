package datn.fpoly.myapplication.ui.signup

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

data class SignUpViewState (
    var stateSignUp: Async<String> = Uninitialized
): MvRxState