package com.example.recipemanagementapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemanagementapp.adapter.RecipeAdapter
import com.example.recipemanagementapp.adapter.SwipeToDeleteCallback
import com.example.recipemanagementapp.model.Flavor
import com.example.recipemanagementapp.model.Recipe
import com.example.recipemanagementapp.model.RecipeListItem

class MainActivity : AppCompatActivity() {

    private val savoryRecipes = mutableListOf<Recipe>()
    private val sweetRecipes = mutableListOf<Recipe>()
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleInput = findViewById<EditText>(R.id.main_recipe_title)
        val descriptionInput = findViewById<EditText>(R.id.main_recipe_description)

        adapter = RecipeAdapter(::showRecipeDialog)
        findViewById<RecyclerView>(R.id.main_recipes_list).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = this@MainActivity.adapter
            ItemTouchHelper(
                SwipeToDeleteCallback(
                    this@MainActivity,
                    this@MainActivity.adapter,
                    ::deleteRecipe,
                )
            ).attachToRecyclerView(this)
        }

        findViewById<Button>(R.id.main_add_savory).setOnClickListener {
            addRecipe(Flavor.SAVORY, titleInput, descriptionInput)
        }
        findViewById<Button>(R.id.main_add_sweet).setOnClickListener {
            addRecipe(Flavor.SWEET, titleInput, descriptionInput)
        }

        refreshList()
    }

    private fun buildItems(): List<RecipeListItem> = buildList {
        add(RecipeListItem.FlavorHeader(Flavor.SAVORY))
        savoryRecipes.forEach { add(RecipeListItem.RecipeRow(it, Flavor.SAVORY)) }
        add(RecipeListItem.FlavorHeader(Flavor.SWEET))
        sweetRecipes.forEach { add(RecipeListItem.RecipeRow(it, Flavor.SWEET)) }
    }

    private fun refreshList() {
        adapter.submitItems(buildItems())
    }

    private fun recipesFor(flavor: Flavor): MutableList<Recipe> =
        if (flavor == Flavor.SAVORY) savoryRecipes else sweetRecipes

    private fun addRecipe(
        flavor: Flavor,
        titleInput: EditText,
        descriptionInput: EditText
    ) {
        val title = titleInput.text?.toString()?.trim().orEmpty()
        val description = descriptionInput.text?.toString()?.trim().orEmpty()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, R.string.enter_recipe, Toast.LENGTH_SHORT).show()
            return
        }

        recipesFor(flavor).add(Recipe(title, description))
        titleInput.text?.clear()
        descriptionInput.text?.clear()
        titleInput.requestFocus()
        refreshList()
    }

    private fun deleteRecipe(row: RecipeListItem.RecipeRow) {
        recipesFor(row.flavor).remove(row.recipe)
        refreshList()
    }

    private fun showRecipeDialog(row: RecipeListItem.RecipeRow) {
        AlertDialog.Builder(this)
            .setTitle(row.recipe.title)
            .setMessage(row.recipe.description)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
