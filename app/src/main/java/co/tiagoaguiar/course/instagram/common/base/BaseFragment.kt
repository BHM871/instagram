package co.tiagoaguiar.course.instagram.common.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<B, P : BasePresenter?>(
    @LayoutRes layoutId: Int,
    val bind: (View) -> B
) : Fragment(layoutId) {

    protected var binding: B? = null

    abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMenu()?.let { setHasOptionsMenu(true) }

        setupPresenter()
        getFragmentResult()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        getMenu()?.let { inflater.inflate(it, menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = bind(view)

        setupViews()
    }

    abstract fun setupPresenter()
    abstract fun setupViews()

    @MenuRes
    open fun getMenu(): Int? {
        return null
    }

    open fun getFragmentResult() {
    }

    override fun onDestroy() {
        binding = null
        presenter?.onDestroy()
        super.onDestroy()
    }

}