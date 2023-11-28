package datn.fpoly.myapplication.ui.searchService

import datn.fpoly.myapplication.core.ViewAction

sealed class SearchServiceViewAction : ViewAction {
    data class SearchService(val query: String): SearchServiceViewAction()
}