package com.example.restaurantmanagementapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantmanagementapp.model.Recipe
import com.example.restaurantmanagementapp.model.Ingredient
import com.example.restaurantmanagementapp.model.Menu
import com.example.restaurantmanagementapp.config.Database
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeTableActivity : AppCompatActivity() {
    private var menuId: Long = -1L
    private var menuName: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeTableAdapter
    private var recipeList: List<Pair<Recipe, Ingredient?>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_table)
        menuId = intent.getLongExtra(EXTRA_MENU_ID, -1L)
        menuName = intent.getStringExtra(EXTRA_MENU_NAME) ?: ""
        val tvTitle = findViewById<TextView>(R.id.tv_recipe_table_title)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        tvTitle.text = menuName
        btnBack.setOnClickListener { finish() }
        recyclerView = findViewById(R.id.recycler_recipe_table)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipeTableAdapter(recipeList)
        recyclerView.adapter = adapter
        fetchRecipes()
    }

    private fun fetchRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val recipeResult = Database.client.postgrest["recipe"].select()
                val ingredientResult = Database.client.postgrest["ingredients"].select()
                val recipes = recipeResult.decodeList<Recipe>().filter { it.menu_id == menuId }
                val ingredients = ingredientResult.decodeList<Ingredient>()
                recipeList = if (recipes.isEmpty()) {
                    // Nếu không có công thức, trả về 1 dòng trống
                    listOf()
                } else {
                    recipes.map { recipe ->
                        val ingredient = ingredients.find { it.ingredient_id == recipe.ingredient_id }
                        recipe to ingredient
                    }
                }
                adapter.updateRecipes(recipeList)
            } catch (e: Exception) {
                // Nếu lỗi, cũng trả về bảng trống
                recipeList = listOf()
                adapter.updateRecipes(recipeList)
            }
        }
    }

    class RecipeTableAdapter(private var recipes: List<Pair<Recipe, Ingredient?>>) : RecyclerView.Adapter<RecipeTableAdapter.ViewHolder>() {
        fun updateRecipes(newRecipes: List<Pair<Recipe, Ingredient?>>) {
            recipes = newRecipes
            notifyDataSetChanged()
        }
        override fun getItemCount() = if (recipes.isEmpty()) 1 else recipes.size
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_table, parent, false)
            return ViewHolder(view)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (recipes.isEmpty()) {
                holder.bindEmpty()
            } else {
                holder.bind(recipes[position].first, recipes[position].second)
            }
        }
        class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
            private val tvIngredient: TextView = view.findViewById(R.id.tv_ingredient)
            private val tvQuantity: TextView = view.findViewById(R.id.tv_quantity)
            private val tvUnit: TextView = view.findViewById(R.id.tv_unit)
            fun bind(recipe: Recipe, ingredient: Ingredient?) {
                tvIngredient.text = ingredient?.name ?: recipe.ingredient_id?.toString() ?: ""
                tvQuantity.text = recipe.quantity?.toString() ?: ""
                tvUnit.text = recipe.unit ?: ""
            }
            fun bindEmpty() {
                tvIngredient.text = "Không có công thức"
                tvQuantity.text = ""
                tvUnit.text = ""
            }
        }
    }

    companion object {
        private const val EXTRA_MENU_ID = "menu_id"
        private const val EXTRA_MENU_NAME = "menu_name"
        fun start(context: Context, menuId: Long, menuName: String) {
            val intent = Intent(context, RecipeTableActivity::class.java)
            intent.putExtra(EXTRA_MENU_ID, menuId)
            intent.putExtra(EXTRA_MENU_NAME, menuName)
            context.startActivity(intent)
        }
    }
}
