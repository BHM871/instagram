package co.tiagoaguiar.course.instagram.splash.data

class SplashRepository(private val data: SplashDataSource) {

    fun session(callback: SplashCallback){
        data.session(callback)
    }

}