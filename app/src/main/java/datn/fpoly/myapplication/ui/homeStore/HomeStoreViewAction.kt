package datn.fpoly.myapplication.ui.homeStore

import datn.fpoly.myapplication.core.ViewAction

sealed class HomeStoreViewAction : ViewAction {
    data class PostStoreActionList(val idStore: String) : HomeStoreViewAction()
    object GetListCategory : HomeStoreViewAction()
    data class GetStoreByIdUser(val idUser: String) : HomeStoreViewAction()
    data class deletePost(val idPost: String) : HomeStoreViewAction()
    data class GetDataOrderStore(val idStore : String, val sortOrder :String) : HomeStoreViewAction()
}