package datn.fpoly.myapplication.ui.address


import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.orhanobut.hawk.Hawk
import datn.fpoly.myapplication.AppApplication
import datn.fpoly.myapplication.core.BaseActivity
import datn.fpoly.myapplication.data.model.account.AccountModel
import datn.fpoly.myapplication.databinding.ActivityAddressBinding
import datn.fpoly.myapplication.ui.addAddress.AddAddressActivity
import datn.fpoly.myapplication.ui.updateAddress.UpdateAddressActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class AddressActivity : BaseActivity<ActivityAddressBinding>(),AddressViewModel.Factory {
    @Inject
    lateinit var listAddressFactory: AddressViewModel.Factory
    private val viewModel: AddressViewModel by viewModel()
    private lateinit var adapter: AddressAdapter
    private val account = Hawk.get<AccountModel>("Account",null)
    private var idUser = account.id.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as AppApplication).appComponent.inject(this);
        super.onCreate(savedInstanceState)
        viewModel.handle(AddressViewAction.GetListAddress(idUser))
        adapter = AddressAdapter()
        views.rcvAddress.adapter = adapter

        adapter.setListener(object :AddressAdapter.AddressListener{
            override fun onClickAddress(id: String?) {
                if (id != null) {
                    val intent = Intent(this@AddressActivity, UpdateAddressActivity::class.java)
                    intent.putExtra("idAddress",id)
                    startActivity(intent)
                }
            }

//            override fun onClickAddressDefault(idAddress: String?, idUser: String?) {
//                idUser?.let { idAddress?.let { it1 -> AddressViewAction.PutDefaultAddress(it1, it) } }
//                    ?.let { viewModel.handle(it) }
//            }

        })
        views.tvAddAddress.setOnClickListener {
            startActivity(Intent(this@AddressActivity,AddAddressActivity::class.java))
        }
        viewModel.subscribe(this){
            getListAddress(it)
        }
        views.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.handle(AddressViewAction.GetListAddress(idUser))
    }

    private fun getListAddress(it: AddressViewState){
        when(it.stateAddress){
            is Success->{
                runBlocking {
                    launch {
                        it.stateAddress.invoke()?.let { addresses ->
                            val sortedAddresses = addresses.sortedByDescending { it.isDefault }
                            adapter.updateDataAddress(sortedAddresses.toMutableList())
                        }
                    }
                }
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("ListAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("ListAddress: Call Fail")
            }
            else->{

            }
        }
        when(it.stateAddressDefault){
            is Success->{
                runBlocking {
                    launch {
                        it.stateAddress.invoke()?.let { addresses ->
                            val sortedAddresses = addresses.sortedByDescending { it.isDefault }
                            adapter.updateDataAddress(sortedAddresses.toMutableList())
                        }
                    }
                }
            }
            is Loading ->{
                Timber.tag("AAAAAAAAA").e("ListAddress: Loading")
            }
            is Fail -> {
                Timber.tag("AAAAAAAAA").e("ListAddress: Call Fail")
            }
            else->{

            }
        }
    }
    override fun getBinding(): ActivityAddressBinding {
        return ActivityAddressBinding.inflate(layoutInflater)
    }

    override fun create(initialState: AddressViewState): AddressViewModel {
        return listAddressFactory.create(initialState)
    }
}