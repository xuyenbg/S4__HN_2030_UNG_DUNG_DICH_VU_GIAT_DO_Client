package datn.fpoly.myapplication.ui.notification

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.NotifycationModel
import datn.fpoly.myapplication.data.model.StoreModel

data class NotifycationViewState(
    var stateNotify: Async<MutableList<NotifycationModel>> = Uninitialized
) : MvRxState