package com.example.restaurantmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {

    private lateinit var btnedit: Button
    private lateinit var btnlogout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnedit = view.findViewById(R.id.btnedit)
        btnedit.setOnClickListener {
            val intent = Intent(requireContext(), EditAcc::class.java)
            startActivity(intent)
        }


    }
}
