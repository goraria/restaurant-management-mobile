package com.example.restaurantmanagementapp.ui.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantmanagementapp.CategoryManagementFragment
import com.example.restaurantmanagementapp.MenuFragment
import com.example.restaurantmanagementapp.MenuManagementFragment
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.RecipeManagementFragment
import com.example.restaurantmanagementapp.TableManagementFragment
import com.example.restaurantmanagementapp.databinding.FragmentGalleryBinding
import com.google.android.material.navigation.NavigationView

class ManagementFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_management, container, false)

        // Khởi tạo các view
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navigationView = view.findViewById(R.id.nav_view)
        toolbar = view.findViewById(R.id.toolbar)

        // Thiết lập toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        
        // Thiết lập toggle cho drawer
        toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Xử lý sự kiện click menu
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_1 -> {
                    updateToolbarTitle("Thực đơn")
                    loadFragment(MenuManagementFragment())
                }
                R.id.nav_menu_2 -> {
                    updateToolbarTitle("Danh mục")
                    loadFragment(CategoryManagementFragment())
                }
                R.id.nav_menu_3 -> {
                    updateToolbarTitle("Công thức")
                    loadFragment(RecipeManagementFragment())
                }
                R.id.nav_menu_4 -> {
                    updateToolbarTitle("Bàn ăn")
                    loadFragment(TableManagementFragment())
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Load fragment mặc định
        if (savedInstanceState == null) {
            updateToolbarTitle("Thực đơn")
            loadFragment(MenuManagementFragment())
        }

        return view
    }

    private fun updateToolbarTitle(title: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = title
    }

    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        drawerLayout.removeDrawerListener(toggle)
    }
}

//class ManagementFragment : Fragment() {
//
//    private var _binding: FragmentGalleryBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val managementViewModel =
//            ViewModelProvider(this).get(ManagementViewModel::class.java)
//
//        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textGallery
//        managementViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}