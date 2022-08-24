package co.tiagoaguiar.course.instagram.add.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.add.Add
import co.tiagoaguiar.course.instagram.add.util.AddViewPageAdapter
import co.tiagoaguiar.course.instagram.common.base.BaseFragment
import co.tiagoaguiar.course.instagram.databinding.FragmentMainAddBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddFragment : Fragment(R.layout.fragment_main_add) {

    private var binding: FragmentMainAddBinding? = null

    companion object{
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("takeCameraKey") { _, bundle ->
            val uri = bundle.getParcelable<Uri>("takeUri")
            uri?.let {
                val intent = Intent(requireContext(), AddActivity::class.java)
                intent.putExtra("photoUri", uri)
                startActivity(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainAddBinding.bind(view)

        if (savedInstanceState == null) setupViews()
    }

    private fun setupViews() {
        val tabLayout =  binding?.mainAddTab
        val viewPager = binding?.mainAddViewpage
        val adapter = AddViewPageAdapter(requireActivity())
        viewPager?.adapter = adapter

        if (tabLayout != null && viewPager != null){
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.text == getString(adapter.tabs[0])){
                        starCamera()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

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
        setFragmentResult("cameraKey", bundleOf("startCamera" to true))
    }

    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            if (allPermissionsGranted()){
                starCamera()
            } else {
                Toast.makeText(requireContext(), R.string.permission_camera_denied, Toast.LENGTH_LONG).show()
            }
        }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}