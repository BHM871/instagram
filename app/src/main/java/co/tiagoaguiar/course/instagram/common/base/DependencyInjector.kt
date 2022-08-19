package co.tiagoaguiar.course.instagram.common.base

import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.data.HomeDataSourceFactory
import co.tiagoaguiar.course.instagram.home.data.HomeFeedCache
import co.tiagoaguiar.course.instagram.home.data.HomeRepository
import co.tiagoaguiar.course.instagram.home.presenter.HomePresenter
import co.tiagoaguiar.course.instagram.login.Login
import co.tiagoaguiar.course.instagram.login.data.LoginFakeDataSource
import co.tiagoaguiar.course.instagram.login.data.LoginRepository
import co.tiagoaguiar.course.instagram.login.presentation.LoginPresenter
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.*
import co.tiagoaguiar.course.instagram.profile.presenter.ProfilePresenter
import co.tiagoaguiar.course.instagram.register.RegisterEmail
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.RegisterPhoto
import co.tiagoaguiar.course.instagram.register.data.RegisterFakeDataSource
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository
import co.tiagoaguiar.course.instagram.register.presenter.RegisterEmailPresenter
import co.tiagoaguiar.course.instagram.register.presenter.RegisterNamePasswordPresenter
import co.tiagoaguiar.course.instagram.register.presenter.RegisterPhotoPresenter
import co.tiagoaguiar.course.instagram.splash.SplashScreen
import co.tiagoaguiar.course.instagram.splash.data.SplashFakeDataSource
import co.tiagoaguiar.course.instagram.splash.data.SplashRepository
import co.tiagoaguiar.course.instagram.splash.presenter.SplashPresenter

object DependencyInjector {

    fun splashRepository() : SplashRepository{
        return SplashRepository(SplashFakeDataSource())
    }

    fun splashPresenter(view: SplashScreen.View) : SplashScreen.Presenter{
        return SplashPresenter(view, splashRepository())
    }

    fun loginRepository() : LoginRepository{
        return LoginRepository(LoginFakeDataSource())
    }

    fun loginPresenter(view: Login.View) : LoginPresenter{
        return LoginPresenter(view, loginRepository())
    }

    fun registerRepository() : RegisterRepository {
        return RegisterRepository(RegisterFakeDataSource())
    }

    fun registerEmailPresenter(view: RegisterEmail.View) : RegisterEmail.Presenter{
        return RegisterEmailPresenter(view, registerRepository())
    }

    fun registerNamePasswordPresenter(view: RegisterNamePassword.View) : RegisterNamePassword.Presenter{
        return RegisterNamePasswordPresenter(view, registerRepository())
    }

    fun registerPhotoPresenter(view: RegisterPhoto.View) : RegisterPhoto.Presenter{
        return RegisterPhotoPresenter(view, registerRepository())
    }

    fun mainProfileRepository() : ProfileRepository{
        return ProfileRepository(ProfileDataSourceFactory(ProfileUserCache, ProfilePostsCache))
    }

    fun mainProfilePresenter(view: Profile.View) : Profile.Presenter{
        return ProfilePresenter(view, mainProfileRepository())
    }

    fun mainHomeRepository() : HomeRepository{
        return HomeRepository(HomeDataSourceFactory(HomeFeedCache))
    }

    fun mainHomePresenter(view: Home.View) : Home.Presenter{
        return HomePresenter(view, mainHomeRepository())
    }

}