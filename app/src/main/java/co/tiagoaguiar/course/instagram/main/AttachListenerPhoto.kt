package co.tiagoaguiar.course.instagram.main

import co.tiagoaguiar.course.instagram.register.view.FragmentAttachListener

interface AttachListenerPhoto : FragmentAttachListener {

    override fun goToLoginScreen() {
    }

    override fun goToNameAndPasswordScreen(email: String) {
    }

    override fun goToWelcomeScreen(name: String) {
    }

    override fun goToPhotoScreen() {
    }

    override fun goToMainScreen() {
    }

    override fun goToGalleryScreen()

    override fun goToCameraScreen()

    fun openDialogForPhoto()

    fun goToFragmentCamera()

    fun gotoFragmentGallery()

}