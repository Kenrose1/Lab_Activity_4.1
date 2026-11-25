package com.example.recipemanagementapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanagementapp.R
import com.example.recipemanagementapp.model.RecipeListItem

/** One adapter drives both row types: flavor headers and recipe rows. */
class RecipeAdapter(
    private val onRecipeClick: (RecipeListItem.RecipeRow) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<RecipeListItem> = emptyList()

    fun submitItems(newItems: List<RecipeListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun itemAt(position: Int): RecipeListItem = items[position]

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is RecipeListItem.FlavorHeader -> VIEW_FLAVOR
        is RecipeListItem.RecipeRow -> VIEW_RECIPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_FLAVOR) {
            FlavorViewHolder(inflater.inflate(R.layout.item_flavor, parent, false))
        } else {
            RecipeViewHolder(inflater.inflate(R.layout.item_recipe, parent, false), onRecipeClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is RecipeListItem.FlavorHeader -> (holder as FlavorViewHolder).bind(item)
            is RecipeListItem.RecipeRow -> (holder as RecipeViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    private class FlavorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flavorTitle: TextView = itemView.findViewById(R.id.item_flavor_title)

        fun bind(item: RecipeListItem.FlavorHeader) {
            flavorTitle.text = item.flavor.label
        }
    }

    private class RecipeViewHolder(
        itemView: View,
        onRecipeClick: (RecipeListItem.RecipeRow) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val recipeTitle: TextView = itemView.findViewById(R.id.item_recipe_title)
        private val recipeDescription: TextView = itemView.findViewById(R.id.item_recipe_description)

        init {
            itemView.setOnClickListener {
                val item = currentRecipe
                if (adapterPosition != RecyclerView.NO_POSITION && item != null) {
                    onRecipeClick(item)
                }
            }
        }

        // Set in bind() and read by the click listener.
        private var currentRecipe: RecipeListItem.RecipeRow? = null

        fun bind(item: RecipeListItem.RecipeRow) {
            currentRecipe = item
            recipeTitle.text = item.recipe.title
            recipeDescription.text = item.recipe.description
        }
    }

    companion object {
        private const val VIEW_FLAVOR = 0
        private const val VIEW_RECIPE = 1
    }
}
