package com.example.blingo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blingo.adapter.NotificationAdapter
import com.example.blingo.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Fragment : BottomSheetDialogFragment(){
    private lateinit var binding:FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notifications = listOf("Bravo! Your order has been placed", "Order has been picked up","We regret your cancellation" )
        val notificationImages= listOf(R.drawable.congrats,R.drawable.truck,R.drawable.sademoji)
        val adapter=NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )
        binding.notificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}