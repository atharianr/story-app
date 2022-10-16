package com.atharianr.storyapp.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.FragmentLoginBinding
import com.atharianr.storyapp.ui.auth.AuthViewModel
import com.atharianr.storyapp.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRegister.setOnClickListener {
                view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnLogin.setOnClickListener { login() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun login() {
        with(binding) {
            val loginRequest = LoginRequest(
                etEmail.text.toString(),
                etPassword.text.toString()
            )

            authViewModel.login(loginRequest).observe(viewLifecycleOwner) {
                when (it.status) {
                    StatusResponse.SUCCESS -> {
                        Log.d("cobawow", it.toString())
                        it.body?.message?.let { msg -> toast(requireActivity(), msg) }
                    }
                    StatusResponse.ERROR -> {
                        it.message?.let { msg -> toast(requireActivity(), msg) }
                    }
                }
            }
        }
    }
}