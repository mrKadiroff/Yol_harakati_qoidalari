package com.example.thetraffic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.thetraffic.adapters.Camera_adapter
import com.example.thetraffic.databinding.FragmentOgohBinding
import com.example.thetraffic.databinding.FragmentSecondBinding
import com.example.thetraffic.db.MyDbHelper
import com.example.thetraffic.models.CameraModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "liked"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
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
    lateinit var binding: FragmentSecondBinding
    lateinit var cameraList: ArrayList<CameraModel>
    lateinit var myDbHelper: MyDbHelper
    lateinit var rvAdapter: Camera_adapter
    lateinit var list: ArrayList<CameraModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSecondBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)

        cameraList = myDbHelper.getAllCamera()
        list = ArrayList()
        for (like in cameraList){
            if (like.like == "liked"){
                list.add(like)
            }
        }

        rvAdapter = Camera_adapter(list, object : Camera_adapter.OnItemClickListener{
            override fun onItemClick(cameraModel: CameraModel) {
                TODO("Not yet implemented")
            }

            override fun onEditClick(cameraModel: CameraModel, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onDeleteClick(cameraModel: CameraModel, position: Int) {
                TODO("Not yet implemented")
            }

//            override fun onFavouriteClick(
//                cameraModel: CameraModel,
//                position: Int,
//                checkBox: CheckBox
//            ) {
//                TODO("Not yet implemented")
//            }

        })
        binding.favouriteRv.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(categoryID: Int, liked: Boolean) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, categoryID)
                    putBoolean(ARG_PARAM2, liked)
                }
            }
    }
}