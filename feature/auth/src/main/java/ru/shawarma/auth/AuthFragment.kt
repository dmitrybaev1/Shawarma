package ru.shawarma.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shawarma.auth.databinding.FragmentAuthBinding
import ru.shawarma.auth.navigation.AuthNavigation
import ru.shawarma.auth.viewmodels.AuthUIState
import ru.shawarma.auth.viewmodels.AuthViewModel
import ru.shawarma.core.data.utils.Errors

class AuthFragment : Fragment() {

    private var binding: FragmentAuthBinding? = null
    private val viewModel: AuthViewModel by viewModels()

    /*Here we should implement observers like triggers or commands in fragment lifecycle (not view
    because view will be recreated and reset consumers). Only LiveData works with value caching and
    doesn't invoke duplicate value observing when we move back here from other fragments and switch
    in STARTED state again*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navCommand.observe(this){
            findNavController().navigate(R.id.actionAuthToRegister)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAuthBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        this.binding = binding
        arguments?.let{bundle ->
            viewModel.setRefreshTokenError(bundle.getString("error") ?: Errors.REFRESH_TOKEN_ERROR)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.authState.filterNotNull().stateIn(this)
                    .collect{state ->
                        handleAuthState(state)
                    }
            }
        }

    }

    private fun handleAuthState(state: AuthUIState){
        when(state){
            is AuthUIState.Success -> {
                (requireActivity() as AuthNavigation).navigateToMenu(state.authData)
            }
            is AuthUIState.Error -> {
                when(val message = state.message){
                    Errors.EMPTY_INPUT_ERROR -> binding!!.authErrorTextView.text = resources.getString(R.string.empty_input_error)
                    Errors.NETWORK_ERROR -> binding!!.authErrorTextView.text = resources.getString(R.string.unknown_error)
                    Errors.REFRESH_TOKEN_ERROR -> binding!!.authErrorTextView.text = resources.getString(R.string.refresh_token_error)
                    else -> binding!!.authErrorTextView.text = message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}