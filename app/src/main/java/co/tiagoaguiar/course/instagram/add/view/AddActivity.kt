package co.tiagoaguiar.course.instagram.add.view

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.add.Add
import co.tiagoaguiar.course.instagram.common.base.DependencyInjector
import co.tiagoaguiar.course.instagram.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity(), Add.View {

    private lateinit var binding: ActivityAddBinding
    private lateinit var uri: Uri

    override lateinit var presenter: Add.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.addToolbar)

        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        supportActionBar?.setHomeAsUpIndicator(drawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        try {
            uri = intent?.extras?.getParcelable("photoUri") ?: throw RuntimeException("photo not found")
        } catch (e: RuntimeException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.addImgPost.setImageURI(uri)

        presenter = DependencyInjector.mainAddPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_add_share -> {
                if (binding.addEditCaption.text.toString().isNotEmpty())
                    presenter.createPost(uri, binding.addEditCaption.text.toString())
                else
                    presenter.createPost(uri, "")
                true
            }
            else -> false
        }
    }

    override fun showProgress(enabled: Boolean) {
        binding.addProgress.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    override fun displayRequestSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    override fun displayRequestFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

}