package datn.fpoly.myapplication.ui.dashboard

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.User

data class DashboardViewState(
    var stateLogin: Async<List<User>> = Uninitialized
): MvRxState
