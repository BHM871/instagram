package co.tiagoaguiar.course.instagram.register.view

import android.content.Context
import androidx.core.widget.addTextChangedListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.hideKeyboard
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.FragmentRegisterEmailBinding
import co.tiagoaguiar.course.instagram.register.RegisterEmail

class RegisterEmailFragment : BaseFragment<FragmentRegisterEmailBinding, RegisterEmail.Presenter>(
    R.layout.fragment_register_email,
    FragmentRegisterEmailBinding::bind
), RegisterEmail.View {

    private var fragmentAttachListener: FragmentAttachListener? = null
    override lateinit var presenter: RegisterEmail.Presenter

    override fun setupPresenter() {
        presenter = DependencyInjector.registerEmailPresenter(this)
    }

    override fun setupViews() {
        binding?.let { binding ->
            with(binding) {
                registerTxtLogin.setOnClickListener {
                    fragmentAttachListener?.goToLoginScreen()
                }

                registerEditEmail.addTextChangedListener(watcher)
                registerEditEmail.addTextChangedListener {
                    displayEmailFailure(null)
                }

                registerBtnNext.setOnClickListener {
                    activity?.hideKeyboard()
                    presenter.create(registerEditEmail.text.toString())
                }
            }
        }
    }

    private val watcher = TxtWatcher {
        binding?.registerBtnNext?.isEnabled =
            binding?.registerEditEmail?.text.toString().isNotEmpty()
    }

    override fun showProgress(enabled: Boolean) {
        binding?.registerBtnNext?.showProgress(enabled)
    }

    override fun goToNamePasswordScreen(email: String) {
        fragmentAttachListener?.goToNameAndPasswordScreen(email)
//        (activity as RegisterActivity).openFragment(RegisterNamePasswordFragment())
    }

    override fun displayEmailFailure(messageError: Int?) {
        binding?.registerEditEmailInput?.error = messageError?.let { getString(it) }
    }

    override fun onEmailError(message: String) {
        binding?.registerEditEmailInput?.error = message
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