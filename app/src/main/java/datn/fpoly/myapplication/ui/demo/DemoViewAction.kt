package datn.fpoly.myapplication.ui.demo

import datn.fpoly.myapplication.core.ViewAction

sealed class DemoViewAction : ViewAction {
    object GetListPersonAction : DemoViewAction()
    data class SearchListPerson(val query: String) : DemoViewAction()
}