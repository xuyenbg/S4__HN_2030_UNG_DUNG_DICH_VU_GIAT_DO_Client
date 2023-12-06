package datn.fpoly.myapplication.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = spacing
        outRect.left = spacing/2
        outRect.right = spacing/2
        // Đặt khoảng dưới theo giá trị spacing bạn muốn
    }
}