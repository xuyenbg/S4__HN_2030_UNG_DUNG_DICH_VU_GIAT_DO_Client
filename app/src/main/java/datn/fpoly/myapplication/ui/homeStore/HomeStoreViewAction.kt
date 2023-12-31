package datn.fpoly.myapplication.ui.homeStore

import datn.fpoly.myapplication.core.ViewAction

sealed class HomeStoreViewAction : ViewAction {
    data class PostStoreActionList(val idStore: String) : HomeStoreViewAction()
    object GetListCategory : HomeStoreViewAction()
    data class GetStatisticalByToday (val idStore: String): HomeStoreViewAction()
    data class GetStatisticalByMonth(val idStore: String, val month : Int) : HomeStoreViewAction()
    data class GetStatisticalByWeek(val idStore: String, val week : Int) : HomeStoreViewAction()

    data class GetStoreByIdUser(val idUser: String) : HomeStoreViewAction()
    data class deletePost(val idPost: String) : HomeStoreViewAction()
    data class GetDataOrderStore(val idStore: String, val sortOrder: String) : HomeStoreViewAction()
    data class getListServiceByStore(val idStore: String) : HomeStoreViewAction()
    data class GetDataOrderStoreDate(
        val idStore: String,
        val status: Int,
        val sortOrder: String
    ) : HomeStoreViewAction()

    data class GetDataOrderStoreDateWashing(
        val idStore: String,
        val status: Int,
        val sortOrder: String
    ) : HomeStoreViewAction()

    data class GetDataOrderStoreDateComplete(
        val idStore: String,
        val status: Int,
        val sortOrder: String
    ) : HomeStoreViewAction()

    data class GetDataOrderStoreDateCompleteMission(
        val idStore: String,
        val status: Int,
        val sortOrder: String
    ) : HomeStoreViewAction()

    data class UpdateStatus(val idOrder: String, val status: Int) : HomeStoreViewAction()
    data class UpdateStatusWashing(val idOrder: String, val status: Int) : HomeStoreViewAction()
    data class UpdateStatusComplete(val idOrder: String, val status: Int) : HomeStoreViewAction()
    data class OpendCloseStore(val idStore: String, val status: Int) : HomeStoreViewAction()
    data class FilterOrder(
        val idStore: String,
        val startDate: String,
        val endDate: String,
        val status: Int
    ) : HomeStoreViewAction()
}