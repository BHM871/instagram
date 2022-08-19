package co.tiagoaguiar.course.instagram.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.hideKeyboard
import co.tiagoaguiar.course.instagram.common.util.TxtWatcher
import co.tiagoaguiar.course.instagram.databinding.ActivityLoginBinding
import co.tiagoaguiar.course.instagram.login.Login
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.register.view.RegisterActivity

class LoginActivity : AppCompatActivity(), Login.View {

    private lateinit var binding: ActivityLoginBinding
    override lateinit var presenter: Login.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = DependencyInjector.loginPresenter(this)

        with(binding) {
            loginEditEmail.addTextChangedListener(watcher)
            loginEditPassword.addTextChangedListener(watcher)

            loginEditEmail.addTextChangedListener(TxtWatcher{displayEmailFailure(null)})
            loginEditPassword.addTextChangedListener(TxtWatcher{displayPasswordFailure(null)})


            loginBtnEnter.setOnClickListener {
                hideKeyboard()
                presenter.login(loginEditEmail.text.toString(), loginEditPassword.text.toString())
            }

            loginTextRegister.setOnClickListener {
                goToRegisterScreen()
            }
        }
    }

    private val watcher = TxtWatcher{
        binding.loginBtnEnter.isEnabled = binding.loginEditEmail.text.toString().isNotEmpty()
                && binding.loginEditPassword.text.toString().length >= 8
    }

    private fun goToRegisterScreen(){
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun showProgress(enabled: Boolean) {
        binding.loginBtnEnter.showProgress(enabled)
    }

    override fun displayEmailFailure(messageError: Int?) {
        binding.loginEditEmailInput.error = messageError?.let { getString(it) }
    }

    override fun displayPasswordFailure(messageError: Int?) {
        binding.loginEditPasswordInput.error = messageError?.let { getString(it) }
    }

    override fun onUserAuthenticated() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onUserUnauthorized(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}