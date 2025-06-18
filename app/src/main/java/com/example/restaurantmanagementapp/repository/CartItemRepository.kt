import com.example.restaurantmanagementapp.config.Database
import com.example.restaurantmanagementapp.model.CartItem
import com.example.restaurantmanagementapp.model.Food
import com.example.restaurantmanagementapp.repository.FoodRepository
import com.example.restaurantmanagementapp.repository.TableRepository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CartItemRepository {

    suspend fun getCartItem(): List<CartItem> = withContext(Dispatchers.IO) {
        val result = Database.client.from("cart_items").select()
        result.decodeList<CartItem>()
    }

    suspend fun addCartItem(cart_item: CartItem): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items").insert(cart_item)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getCartItemById(cartItemId: Long): CartItem? = withContext(Dispatchers.IO) {
        val result = Database.client.from("cart_items")
            .select {
                filter { eq("cart_item_id", cartItemId) }
            }
            .decodeList<CartItem>()
        result.firstOrNull()
    }

    suspend fun getCartItemByTableAndPaid(tableId: Long, paid: Boolean): List<CartItem> = withContext(Dispatchers.IO) {
        val result = Database.client
            .from("cart_items")
            .select {
                filter {
                    eq("table_id", tableId)
                    eq("paid", paid)
                }
            }
            .decodeList<CartItem>()
        result
    }

    suspend fun getFoodsInCartByTableLoop(tableId: Long): List<Food> = withContext(Dispatchers.IO) {
        val cartItems = getCartItemByTableAndPaid(tableId, false)
        cartItems.mapNotNull { it.menu_id }
            .mapNotNull { FoodRepository.getFoodById(it.toInt()) }
    }

    suspend fun deleteCartItem(cartItemId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items")
                .delete {
                    filter { eq("cart_item_id", cartItemId) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateCartItem(cartItem: CartItem): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items")
                .update(cartItem) {
                    filter { eq("cart_item_id", cartItem.cart_item_id ?: -1) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateCartItemQuantity(cartItemId: Long, newQuantity: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items")
                .update(mapOf("quantity" to newQuantity)) {
                    filter { eq("cart_item_id", cartItemId) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateCartItemPaid(cartItemId: Long, paid: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            Database.client.from("cart_items")
                .update(mapOf("paid" to paid)) {
                    filter { eq("cart_item_id", cartItemId) }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateTableStatusByCart(tableId: Long) {
        val cartCount = getCartItemByTableAndPaid(tableId.toLong(), false).size
        val status = cartCount > 0
        TableRepository.updateTableStatus(tableId, status)
    }
}