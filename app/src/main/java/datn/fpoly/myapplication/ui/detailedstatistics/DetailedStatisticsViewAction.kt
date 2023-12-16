package datn.fpoly.myapplication.ui.detailedstatistics

import datn.fpoly.myapplication.core.ViewAction
import datn.fpoly.myapplication.ui.home.HomeViewAction

sealed class DetailedStatisticsViewAction: ViewAction {
    data class GetDetailedStatistics(val idStore: String): DetailedStatisticsViewAction()

}