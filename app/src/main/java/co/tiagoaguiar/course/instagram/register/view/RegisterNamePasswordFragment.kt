package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import android.widget.Toast
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.hideKeyboard
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterNamePasswordBinding
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import java.io.IOException

class RegisterNamePasswordFragment : BaseFragment<FragmentRegisterNamePasswordBinding, RegisterNamePassword.Presenter>(
   R.layout.fragment_register_name_password,
   FragmentRegisterNamePasswordBinding::bind
), RegisterNamePassword.View {

    private var fragmentAttachListener: FragmentAttachListener? = null
    override lateinit var presenter: RegisterNamePassword.Presenter

    companion object {
        const val KEY_EMAIL = "key_email"
    }

    override fun setupPresenter() {
        presenter = DependencyInjector.registerNamePasswordPresenter(this)
    }

    override fun setupViews() {
        val email = arguments?.getString(KEY_EMAIL)

        binding?.let { binding ->
            with(binding) {
                registerTxtLogin.setOnClickListener {
                    fragmentAttachListener?.goToLoginScreen()
                }

                registerEditName.addTextChangedListener(watcher)
                registerEditUsername.addTextChangedListener(watcher)
                registerEditPassword.addTextChangedListener(watcher)
                registerEditConfirm.addTextChangedListener(watcher)

                registerEditName.addTextChangedListener (TxtWatcher{
                    displayNameFailure(null)
                })
                registerEditUsername.addTextChangedListener (TxtWatcher{
                    displayUsernameFailure(null)
                })
                registerEditPassword.addTextChangedListener (TxtWatcher{
                    displayPasswordFailure(null)
                    displayPasswordNotEquals(null)
                })
                registerEditConfirm.addTextChangedListener (TxtWatcher{
                    displayConfirmFailure(null)
                    displayPasswordNotEquals(null)
                })

                registerBtnNameContinue.setOnClickListener{
                    activity?.hideKeyboard()
                    presenter.create(
                        email = email ?: throw IOException("Email not found"),
                        name = registerEditName.text.toString(),
                        username = registerEditUsername.text.toString(),
                        password = registerEditPassword.text.toString(),
                        confirm = registerEditConfirm.text.toString()
                    )
                }
            }
        }
    }

    private val watcher = TxtWatcher {
        binding?.apply {
            registerBtnNameContinue.isEnabled = registerEditName.text.toString().isNotEmpty() &&
                        registerEditUsername.text.toString().isNotEmpty() &&
                        registerEditPassword.text.toString().length >= 8 &&
                        registerEditConfirm.text.toString().length >= 8
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding?.registerBtnNameContinue?.showProgress(enabled)
    }

    override fun displayNameFailure(message: Int?) {
        binding?.registerEditNameInput?.error = message?.let { getString(it) }
    }

    override fun displayUsernameFailure(message: Int?) {
        binding?.registerEditUsernameInput?.error = message?.let { getString(it) }
    }

    override fun displayPasswordFailure(message: Int?) {
        binding?.registerEditPasswordInput?.error = message?.let { getString(it) }
    }

    override fun displayConfirmFailure(message: Int?) {
        binding?.registerEditConfirmInput?.error = message?.let { getString(it) }
    }

    override fun displayPasswordNotEquals(message: Int?) {
        binding?.registerEditPasswordInput?.error = message?.let { getString(it) }
        binding?.registerEditConfirmInput?.error = message?.let { getString(it) }
    }

    override fun onCreateFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateSuccess(name: String) {
        fragmentAttachListener?.goToWelcomeScreen(name)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener){
            fragmentAttachListener = context
        }
    }

    override fun onDestroy() {
        fragmentAttachListener = null
        super.onDestroy()
    }

}