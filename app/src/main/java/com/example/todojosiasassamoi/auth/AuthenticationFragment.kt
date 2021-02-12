package com.example.todojosiasassamoi.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.FragmentAuthenticationBinding


class AuthenticationFragment : Fragment() {

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginButton.setOnClickListener(){
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        binding.SignUpButton.setOnClickListener(){
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
    }

    private var _binding: FragmentAuthenticationBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
