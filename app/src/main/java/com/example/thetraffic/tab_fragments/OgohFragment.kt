package com.example.thetraffic.tab_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thetraffic.R
import com.example.thetraffic.adapters.Camera_adapter
import com.example.thetraffic.databinding.FragmentOgohBinding
import com.example.thetraffic.db.MyDbHelper
import com.example.thetraffic.models.CameraModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "liked"

/**
 * A simple [Fragment] subclass.
 * Use the [OgohFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OgohFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var categoryID: Int? = null
    private var liked: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryID = it.getInt(ARG_PARAM1)
            liked = it.getBoolean(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentOgohBinding
    lateinit var myDbHelper: MyDbHelper
    lateinit var cameraList: ArrayList<CameraModel>
    lateinit var list: ArrayList<CameraModel>
    lateinit var list2: ArrayList<CameraModel>
    lateinit var rvAdapter: Camera_adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentOgohBinding.inflate(layoutInflater)




        if (categoryID == 1){
            myDbHelper = MyDbHelper(binding.root.context)
            cameraList = myDbHelper.getAllCamera()
            list = ArrayList()
            cameraList.forEach{
                if (it.kategoriya == "Ogohlantiruvchi") {
                    list.add(it)
                }
            }
            list = myDbHelper.getAllCamera()

            rvAdapter = Camera_adapter(list)
            binding.cameraRv.adapter = rvAdapter
        }

        if (categoryID == 2){
            val kateg = "Imtiyozli"
            myDbHelper = MyDbHelper(binding.root.context)
            cameraList = myDbHelper.getAllCamera()
            list2 = ArrayList()
            cameraList.forEach{
                if (it.kategoriya == kateg) {
                    list2.add(it)
                }
            }
            list2 = myDbHelper.getAllCamera()

            rvAdapter = Camera_adapter(list2)
            binding.cameraRv.adapter = rvAdapter
        }


//        if (categoryID == 3){
//            cameraList.forEach{
//                if (it.kategoriya == "Ta'qiqlovchi") {
//                    list.add(it)
//                }
//            }
//            list = myDbHelper.getAllCamera()
//
//            rvAdapter = Camera_adapter(list)
//            binding.cameraRv.adapter = rvAdapter
//        }
//
//        if (categoryID == 4){
//            cameraList.forEach{
//                if (it.kategoriya == "Buyuruvchi") {
//                    list.add(it)
//                }
//            }
//            list = myDbHelper.getAllCamera()
//
//            rvAdapter = Camera_adapter(list)
//            binding.cameraRv.adapter = rvAdapter
//        }








        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OgohFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(categoryID: Int, liked: Boolean) =
            OgohFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, categoryID)
                    putBoolean(ARG_PARAM2, liked)
                }
            }
    }
}