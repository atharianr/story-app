package com.atharianr.storyapp.ui.auth.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.request.RegisterRequest
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse.ERROR
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse.SUCCESS
import com.atharianr.storyapp.databinding.FragmentRegisterBinding
import com.atharianr.storyapp.ui.auth.AuthViewModel
import com.atharianr.storyapp.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding as FragmentRegisterBinding

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnLogin.setOnClickListener {
                view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            btnRegister.setOnClickListener { register() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun register() {
        with(binding) {
            val registerRequest = RegisterRequest(
                etName.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString()
            )

            authViewModel.register(registerRequest).observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        Log.d("cobawow", it.toString())
                        it.message?.let { msg -> toast(requireActivity(), msg) }
                    }
                    ERROR -> {
                        it.message?.let { msg -> toast(requireActivity(), msg) }
                    }
                }
            }
        }
    }
}