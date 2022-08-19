package co.tiagoaguiar.course.instagram.splash.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.common.extension.animationEnd
import co.tiagoaguiar.course.instagram.databinding.ActivitySplashBinding
import co.tiagoaguiar.course.instagram.login.view.LoginActivity
import co.tiagoaguiar.course.instagram.main.view.MainActivity
import co.tiagoaguiar.course.instagram.splash.SplashScreen

class SplashActivity : AppCompatActivity(), SplashScreen.View {

    private lateinit var binding: ActivitySplashBinding
    override lateinit var presenter: SplashScreen.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = DependencyInjector.splashPresenter(this)
        fadeIn()
    }

    private fun fadeIn() {
        binding.splashImg.animate().apply {
            setListener(animationEnd{presenter.authenticate()})
            duration = 1000
            alpha(1.0f)
            start()
        }
    }

    private fun fadeOut(actionListener: () -> Unit) {
        binding.splashImg.animate().apply {
            setListener(animationEnd{actionListener.invoke()})
            duration = 1000
            startDelay = 1000
            alpha(1.0f)
            start()
        }
    }

    override fun goToMainScreen() {
        fadeOut(fun(){
            val intent = Intent (baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
    }

    override fun goToLoginScreen() {
        fadeOut(fun(){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}