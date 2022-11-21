package co.tiagoaguiar.course.instagram.post.view

import android.Manifest
import android.app.Activity
import android.content.Context
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
import androidx.viewpager2.widget.ViewPager2
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.add.view.AddActivity
import co.tiagoaguiar.course.instagram.databinding.FragmentMainAddBinding
import co.tiagoaguiar.course.instagram.post.util.AddViewPageAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddFragment : Fragment(R.layout.fragment_main_add) {

    private var binding: FragmentMainAddBinding? = null
    private var addListener: AddListener? = null

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: AddViewPageAdapter

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("uriKey") { _, bundle ->
            val uri = bundle.getParcelable<Uri>("uri")
            uri?.let {
                val intent = Intent(requireContext(), AddActivity::class.java)
                intent.putExtra("photoUri", uri)
                addActivityResult.launch(intent)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainAddBinding.bind(view)

        if (savedInstanceState == null) setupViews()
    }

    private fun setupViews() {
        tabLayout = binding?.mainAddTab!!
        viewPager = binding?.mainAddViewpage!!
        adapter = AddViewPageAdapter(requireActivity())
        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text == getString(adapter.tabs[0]))
                    starCamera()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager.currentItem = 1

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(adapter.tabs[position])
        }.attach()

        if (allPermissionsGranted()) {
            starCamera()
        } else {
            getPermission.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSIONS[0]
        ) == PermissionChecker.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSIONS[1]
        ) == PermissionChecker.PERMISSION_GRANTED

    private fun starCamera() {
        setFragmentResult("cameraKey", bundleOf("startCamera" to true))
    }

    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            if (allPermissionsGranted()) {
                starCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.permission_camera_denied,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private val addActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                addListener?.onPostCreated()
            }
        }

    interface AddListener {
        fun onPostCreated()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AddListener) {
            addListener = context
        }
    }

    override fun onDestroy() {
        binding = null
        addListener = null
        super.onDestroy()
    }

}