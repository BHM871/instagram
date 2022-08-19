package co.tiagoaguiar.course.instagram.add.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.FragmentMainAddGalleryBinding

class GalleryFragment : Fragment(R.layout.fragment_main_add_gallery) {

    private var binding: FragmentMainAddGalleryBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainAddGalleryBinding.bind(view)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}