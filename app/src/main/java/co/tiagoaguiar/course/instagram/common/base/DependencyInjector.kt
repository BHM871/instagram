package co.tiagoaguiar.course.instagram.common.base

import android.content.Context
import co.tiagoaguiar.course.instagram.add.Add
import co.tiagoaguiar.course.instagram.add.data.AddFakeRemoteDtaSource
import co.tiagoaguiar.course.instagram.add.data.AddLocalDataSource
import co.tiagoaguiar.course.instagram.add.data.AddRepository
import co.tiagoaguiar.course.instagram.add.presenter.AddPresenter
import co.tiagoaguiar.course.instagram.common.util.PostsCache
import co.tiagoaguiar.course.instagram.common.util.UserCache
import co.tiagoaguiar.course.instagram.home.Home
import co.tiagoaguiar.course.instagram.home.data.HomeDataSourceFactory
import co.tiagoaguiar.course.instagram.home.data.HomeFeedCache
import co.tiagoaguiar.course.instagram.home.data.HomeRepository
import co.tiagoaguiar.course.instagram.home.presenter.HomePresenter
import co.tiagoaguiar.course.instagram.login.Login
import co.tiagoaguiar.course.instagram.login.data.LoginFakeDataSource
import co.tiagoaguiar.course.instagram.login.data.LoginRepository
import co.tiagoaguiar.course.instagram.login.presentation.LoginPresenter
import co.tiagoaguiar.course.instagram.post.Post
import co.tiagoaguiar.course.instagram.post.data.PostLocalDataSource
import co.tiagoaguiar.course.instagram.post.data.PostRepository
import co.tiagoaguiar.course.instagram.post.presenter.PostPresenter
import co.tiagoaguiar.course.instagram.profile.Profile
import co.tiagoaguiar.course.instagram.profile.data.ProfileDataSourceFactory
import co.tiagoaguiar.course.instagram.profile.data.ProfileRepository
import co.tiagoaguiar.course.instagram.profile.presenter.ProfilePresenter
import co.tiagoaguiar.course.instagram.register.RegisterEmail
import co.tiagoaguiar.course.instagram.register.RegisterNamePassword
import co.tiagoaguiar.course.instagram.register.RegisterPhoto
import co.tiagoaguiar.course.instagram.register.data.RegisterFakeDataSource
import co.tiagoaguiar.course.instagram.register.data.RegisterLocalDataSource
import co.tiagoaguiar.course.instagram.register.data.RegisterRepository
import co.tiagoaguiar.course.instagram.register.presenter.RegisterEmailPresenter
import co.tiagoaguiar.course.instagram.register.presenter.RegisterNamePasswordPresenter
import co.tiagoaguiar.course.instagram.register.presenter.RegisterPhotoPresenter
import co.tiagoaguiar.course.instagram.splash.SplashScreen
import co.tiagoaguiar.course.instagram.splash.data.SplashFakeDataSource
import co.tiagoaguiar.course.instagram.splash.data.SplashRepository
import co.tiagoaguiar.course.instagram.splash.presenter.SplashPresenter

object DependencyInjector {

    private fun splashRepository() : SplashRepository{
        return SplashRepository(SplashFakeDataSource())
    }

    fun splashPresenter(view: SplashScreen.View) : SplashScreen.Presenter{
        return SplashPresenter(view, splashRepository())
    }

    private fun loginRepository() : LoginRepository{
        return LoginRepository(LoginFakeDataSource())
    }

    fun loginPresenter(view: Login.View) : LoginPresenter{
        return LoginPresenter(view, loginRepository())
    }

    private fun registerRepository() : RegisterRepository {
        return RegisterRepository(RegisterLocalDataSource(UserCache), RegisterFakeDataSource())
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

    private fun mainProfileRepository() : ProfileRepository{
        return ProfileRepository(ProfileDataSourceFactory(UserCache, PostsCache))
    }

    fun mainProfilePresenter(view: Profile.View) : Profile.Presenter{
        return ProfilePresenter(view, mainProfileRepository())
    }

    private fun mainHomeRepository() : HomeRepository{
        return HomeRepository(HomeDataSourceFactory(HomeFeedCache))
    }

    fun mainHomePresenter(view: Home.View) : Home.Presenter{
        return HomePresenter(view, mainHomeRepository())
    }

    private fun mainAddRepository() : AddRepository {
        return AddRepository(AddLocalDataSource(), AddFakeRemoteDtaSource())
    }

    fun mainAddPresenter(view: Add.View) : Add.Presenter {
        return AddPresenter(view, mainAddRepository())
    }

    private fun postRepository(context: Context) : PostRepository{
        return PostRepository(PostLocalDataSource(context))
    }

    fun postPresenter(view: Post.View, context: Context) : Post.Presenter{
        return PostPresenter(view, postRepository(context))
    }

}