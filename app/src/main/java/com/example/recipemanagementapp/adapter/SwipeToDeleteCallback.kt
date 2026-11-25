package com.example.recipemanagementapp.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanagementapp.R
import com.example.recipemanagementapp.model.RecipeListItem

/** Swipe either way to delete a recipe. Flavor headers cannot be swiped. */
class SwipeToDeleteCallback(
    context: Context,
    private val adapter: RecipeAdapter,
    private val onDelete: (RecipeListItem.RecipeRow) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val background = ColorDrawable(Color.parseColor("#D32F2F"))
    private val deleteIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_delete)?.mutate()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val position = viewHolder.adapterPosition
        if (position == RecyclerView.NO_POSITION || adapter.itemAt(position) !is RecipeListItem.RecipeRow) {
            return 0
        }
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val icon = deleteIcon

        if (dX != 0f && icon != null && actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val backgroundBounds: Rect
            if (dX > 0) {
                backgroundBounds = Rect(
                    itemView.left,
                    itemView.top,
                    (itemView.left + dX).toInt().coerceAtMost(itemView.right),
                    itemView.bottom
                )
            } else {
                backgroundBounds = Rect(
                    (itemView.right + dX).toInt().coerceAtLeast(itemView.left),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            background.bounds = backgroundBounds
            background.draw(canvas)

            val iconHeight = icon.intrinsicHeight
            val iconWidth = icon.intrinsicWidth
            val iconTop = itemView.top + (itemView.height - iconHeight) / 2
            val iconBottom = iconTop + iconHeight
            val iconLeft: Int
            val iconRight: Int
            if (dX > 0) {
                iconLeft = itemView.left + icon.intrinsicWidth / 2
                iconRight = iconLeft + iconWidth
            } else {
                iconRight = itemView.right - icon.intrinsicWidth / 2
                iconLeft = iconRight - iconWidth
            }
            icon.bounds = Rect(iconLeft, iconTop, iconRight, iconBottom)
            icon.draw(canvas)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val item = adapter.itemAt(position)
            if (item is RecipeListItem.RecipeRow) {
                onDelete(item)
            }
        }
    }
}
