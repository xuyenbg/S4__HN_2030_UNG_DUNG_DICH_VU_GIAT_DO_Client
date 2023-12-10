package datn.fpoly.myapplication.utils

import android.app.Dialog
import android.content.Context
import datn.fpoly.myapplication.R

object DialogLoading {
    var dialog: Dialog?=null
    fun showDialog(conText: Context){
        dialog = Dialog(conText)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.window?.setBackgroundDrawableResource(R.color.tran)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()
    }
    fun hideDialog(){
        dialog?.dismiss()
    }
}