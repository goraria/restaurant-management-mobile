package com.example.restaurantmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.Recipe
import com.example.restaurantmanagementapp.model.Ingredient
import com.example.restaurantmanagementapp.config.Database
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeTableFragment : Fragment() {
    private var menuId: Long = -1L
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeTableAdapter
    private var recipeList: List<Pair<Recipe, Ingredient?>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            menuId = it.getLong(ARG_MENU_ID, -1L)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_table, container, false)
        recyclerView = view.findViewById(R.id.recycler_recipe_table)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = RecipeTableAdapter(recipeList)
        recyclerView.adapter = adapter
        fetchRecipes()
        return view
    }

    private fun fetchRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val recipeResult = Database.client.postgrest["recipe"].select()
                val ingredientResult = Database.client.postgrest["ingredients"].select()
                val recipes = recipeResult.decodeList<Recipe>().filter { it.menu_id == menuId }
                val ingredients = ingredientResult.decodeList<Ingredient>()
                recipeList = recipes.map { recipe ->
                    val ingredient = ingredients.find { it.ingredient_id == recipe.ingredient_id }
                    recipe to ingredient
                }
                adapter.updateRecipes(recipeList)
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    class RecipeTableAdapter(private var recipes: List<Pair<Recipe, Ingredient?>>) : RecyclerView.Adapter<RecipeTableAdapter.ViewHolder>() {
        fun updateRecipes(newRecipes: List<Pair<Recipe, Ingredient?>>) {
            recipes = newRecipes
            notifyDataSetChanged()
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_table, parent, false)

            return ViewHolder(view)
        }
        override fun getItemCount() = recipes.size
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(recipes[position].first, recipes[position].second)
        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val tvIngredient: TextView = view.findViewById(R.id.tv_ingredient)
            private val tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
            private val tvUnit: TextView = view.findViewById(R.id.tv_unit)
            fun bind(recipe: Recipe, ingredient: Ingredient?) {
                tvIngredient.text = ingredient?.name ?: recipe.ingredient_id?.toString() ?: ""
                tvQuantity.text = recipe.quantity?.toString() ?: ""
                tvUnit.text = recipe.unit ?: ""
            }
        }
    }

    companion object {
        private const val ARG_MENU_ID = "menu_id"
        fun newInstance(menuId: Long) = RecipeTableFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_MENU_ID, menuId)
            }
        }
    }
}
