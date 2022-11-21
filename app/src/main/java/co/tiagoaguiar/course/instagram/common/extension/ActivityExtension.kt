package co.tiagoaguiar.course.instagram.common.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.common.view.CustomDialog
import co.tiagoaguiar.course.instagram.register.view.FragmentAttachListener

fun Activity.hideKeyboard(){
    val service = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    var view: View? = currentFocus
    if (view == null) view = View(this)

    service.hideSoftInputFromWindow(view.windowToken, 0)
}

fun openDialogFromPhoto(context: Context, attachListener: FragmentAttachListener){
    val dialog = CustomDialog(context)
    dialog.setTitle(R.string.define_photo_profile)
    dialog.addButton(R.string.photo, R.string.gallery) {
        when (it.id) {
            R.string.photo -> {
                attachListener.goToCameraScreen()
            }
            R.string.gallery -> {
                attachListener.goToGalleryScreen()
            }
        }
    }
    dialog.show()
}

fun animationEnd(callback: () -> Unit) : AnimatorListenerAdapter{
    return object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            callback.invoke()
        }
    }
}

fun AppCompatActivity.replaceFragment(@IdRes id: Int, fragment: Fragment) {
    if (supportFragmentManager.findFragmentById(id) == null) {
        supportFragmentManager.beginTransaction().apply {
            add(id, fragment, fragment.javaClass.simpleName)
            commit()
        }
    } else {
        supportFragmentManager.beginTransaction().apply {
            replace(id, fragment, fragment.javaClass.simpleName)
            addToBackStack(null)
            commit()
        }
    }
}