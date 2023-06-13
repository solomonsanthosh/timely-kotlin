package com.example.timelynew.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timelynew.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.grp.setOnClickListener {
            var intent = Intent(requireActivity(),ViewGroups::class.java)
            requireActivity().startActivity(intent)
        }


        return root
    }


}