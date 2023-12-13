package datn.fpoly.myapplication.ui.favoriteStore

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityFavoriteStoreBinding
import datn.fpoly.myapplication.ui.detailstore.DetailStoreActivity
import datn.fpoly.myapplication.utils.Common
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class FavoriteStoreActivity : BaseActivity<ActivityFavoriteStoreBinding>(),FavoriteStoreViewModel.Factory {
    @Inject
    lateinit var listFavoriteFactory: FavoriteStoreViewModel.Factory
    private val viewModel: FavoriteStoreViewModel by viewModel()
    private lateinit var adapter: FavoriteStoreAdapter
    private val account = Hawk.get<AccountModel>("Account",null)
    private var idUser = account.id.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        viewModel.handle(FavoriteStoreViewAction.GetDetailUser(idUser))
        adapter = FavoriteStoreAdapter(){
            val intent = Intent(this, DetailStoreActivity::class.java)
            intent.putExtra(Common.KEY_ID_STORE,it.id)
            startActivity(intent)
        }


        views.rcvListFavoriteStore.adapter = adapter
        adapter.setListener(object :FavoriteStoreAdapter.FavoriteListener{
            override fun onClickFavorite(id: String?) {
                val accountModel = AccountModel()
                accountModel.favoriteStores = ArrayList()
                (accountModel.favoriteStores as ArrayList<String>)?.add(id.toString())
                viewModel.removeFavoriteStore(idUser,accountModel)
            }

            override fun onClickFavoriteRe(id: String?) {
                val accountModel = AccountModel()
                accountModel.favoriteStores = ArrayList()
                (accountModel.favoriteStores as ArrayList<String>)?.add(id.toString())
                viewModel.addFavoriteStore(idUser,accountModel)
            }

        })
        viewModel.subscribe(this){
            getDetailUser(it)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun getDetailUser(state: FavoriteStoreViewState){
        when(state.stateFavoriteStore){
            is Success->{
                runBlocking {
                    launch {
                        state.stateFavoriteStore.invoke()?.favoriteStores.let {
                            if (it != null) {
                                adapter.updateDataFavorite(it)
                                Timber.tag("AAAAAAAAA").e("getDetailUser: Detail user size: "+it.size )
                            }
                        }
                    }
                }
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("getDetailUser: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("getDetailUser: Call Fail")
            }
            else->{

            }
        }

    }

    override fun getBinding(): ActivityFavoriteStoreBinding {
        return ActivityFavoriteStoreBinding.inflate(layoutInflater)
    }

    override fun create(initialState: FavoriteStoreViewState): FavoriteStoreViewModel {
        return listFavoriteFactory.create(initialState)
    }
}