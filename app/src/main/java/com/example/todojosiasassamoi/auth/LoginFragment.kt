package com.example.todojosiasassamoi.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.todojosiasassamoi.MainActivity
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.FragmentAuthenticationBinding
import com.example.todojosiasassamoi.databinding.FragmentLoginBinding
import com.example.todojosiasassamoi.network.Api
import com.example.todojosiasassamoi.network.UserService
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun verificate(email : String,pass: String) : Boolean {

        return !pass.isEmpty() && !email.isEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener(){
            val email = binding.emailLogin.text.toString()
            val password = binding.passLogin.text.toString()
            if (verificate(email, password)) {
                    val newIntent = Intent(activity, MainActivity::class.java)
                    startActivity(newIntent)
            } else {
                Toast.makeText(context, "Nous avons besoin que vous remplissiez tous les champs 😫", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private var _binding: FragmentLoginBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}