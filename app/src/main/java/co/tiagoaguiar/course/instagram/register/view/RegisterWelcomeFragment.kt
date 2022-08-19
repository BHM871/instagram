package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterWelcomeBinding

class RegisterWelcomeFragment : Fragment(R.layout.fragment_register_welcome) {

    private var binding: FragmentRegisterWelcomeBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    companion object{
        const val KEY_NAME = "key_name"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterWelcomeBinding.bind(view)

        val name = arguments?.getString(KEY_NAME)

        binding?.let{
            with(it){
                registerBtnNextInWelcome.isEnabled = true
                registerTxtWelcome.text = getString(R.string.welcome_to_instagram, name ?: "Desconhecido")

                registerBtnNextInWelcome.setOnClickListener {
                    fragmentAttachListener?.goToPhotoScreen()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    override fun onDestroy() {
        binding = null
        fragmentAttachListener = null
        super.onDestroy()
    }

}