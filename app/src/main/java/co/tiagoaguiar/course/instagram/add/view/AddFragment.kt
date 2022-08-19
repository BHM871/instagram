package co.tiagoaguiar.course.instagram.add.view

import android.Manifest
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.add.Add
import co.tiagoaguiar.course.instagram.add.util.AddViewPageAdapter
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.databinding.FragmentMainAddBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.security.PermissionCollection
import java.security.Permissions

class AddFragment : BaseFragment<FragmentMainAddBinding, Add.Presenter>(
    R.layout.fragment_main_add,
    FragmentMainAddBinding::bind
) {

    override lateinit var presenter: Add.Presenter

    companion object{
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
    }

    override fun setupPresenter() {
    }

    override fun setupViews() {
        val tabLayout =  binding?.mainAddTab
        val viewPager = binding?.mainAddViewpage
        val adapter = AddViewPageAdapter(requireActivity())
        viewPager?.adapter = adapter

        if (tabLayout != null && viewPager != null){
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = getString(adapter.tabs[position])
            }.attach()
        }

        if (allPermissionsGranted()){
            starCamera()
        } else {
            getPermission.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSIONS) == PermissionChecker.PERMISSION_GRANTED

    private fun starCamera(){
        val a = "a"
    }

    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            if (allPermissionsGranted()){
                starCamera()
            } else {
                Toast.makeText(requireContext(), R.string.permission_camera_denied, Toast.LENGTH_LONG).show()
            }
        }

}