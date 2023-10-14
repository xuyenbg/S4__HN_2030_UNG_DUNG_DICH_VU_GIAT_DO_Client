package datn.fpoly.myapplication.ui.demo

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import datn.fpoly.myapplication.data.model.Person
import datn.fpoly.myapplication.data.model.User

data class DemoViewState (
    var stateGetListPerson: Async<List<Person>> = Uninitialized
): MvRxState