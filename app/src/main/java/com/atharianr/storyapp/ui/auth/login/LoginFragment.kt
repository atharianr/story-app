package com.atharianr.storyapp.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.atharianr.storyapp.R
import com.atharianr.storyapp.data.source.remote.request.LoginRequest
import com.atharianr.storyapp.data.source.remote.response.vo.StatusResponse
import com.atharianr.storyapp.databinding.FragmentLoginBinding
import com.atharianr.storyapp.ui.MainActivity
import com.atharianr.storyapp.ui.auth.AuthViewModel
import com.atharianr.storyapp.utils.Constant
import com.atharianr.storyapp.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        isLoading(true)

        with(binding) {
            val loginRequest = LoginRequest(
                etEmail.text.toString(), etPassword.text.toString()
            )

            authViewModel.login(loginRequest).observe(viewLifecycleOwner) {
                when (it.status) {
                    StatusResponse.SUCCESS -> {
                        it.body?.apply {
                            toast(requireActivity(), message)
                            saveToken(loginResult.token)
                            intentToMain()
                        }
                    }
                    StatusResponse.ERROR -> {
                        it.message?.let { msg -> toast(requireActivity(), msg) }
                    }
                }
                isLoading(false)
            }
        }
    }

    private fun saveToken(token: String) {
        val sharedPref =
            requireActivity().getSharedPreferences(Constant.USER_DATA, Context.MODE_PRIVATE)
                ?: return
        sharedPref.edit().putString(Constant.TOKEN, token).apply()
    }

    private fun intentToMain() {
        with(Intent(requireActivity(), MainActivity::class.java)) {
            startActivity(this)
            requireActivity().finish()
        }
    }

    private fun isLoading(loading: Boolean) {
        binding.apply {
            if (loading) {
                btnLogin.text = ""
                btnLogin.isEnabled = false
                progressBar.visibility = View.VISIBLE
            } else {
                btnLogin.text = getString(R.string.login)
                btnLogin.isEnabled = true
                progressBar.visibility = View.GONE
            }
        }
    }
}