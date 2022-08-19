package co.tiagoaguiar.course.instagram.add.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentMainAddCameraBinding

class CameraFragment : Fragment(R.layout.fragment_main_add_camera) {

    private var binding: FragmentMainAddCameraBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainAddCameraBinding.bind(view)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}