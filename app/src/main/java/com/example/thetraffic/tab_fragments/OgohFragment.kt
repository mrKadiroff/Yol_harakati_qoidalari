package com.example.thetraffic.tab_fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thetraffic.R
import com.example.thetraffic.adapters.Camera_adapter
import com.example.thetraffic.databinding.FragmentOgohBinding
import com.example.thetraffic.db.MyDbHelper
import com.example.thetraffic.models.CameraModel
import java.io.File

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
    lateinit var rvAdapter: Camera_adapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentOgohBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)


        setRv()
        connectRv()



        return binding.root
    }



    private fun setRv() {
        cameraList = myDbHelper.getAllCamera()
        list = ArrayList()

        if (categoryID == 1) {

            cameraList.forEach {
                if (it.kategoriya == "Ogohlantiruvchi") {
                    list.add(it)
                }
            }



        }

        if (categoryID == 2){
            val kateg = "Imtiyozli"
            list = ArrayList()
            cameraList.forEach{
                if (it.kategoriya == kateg) {
                    list.add(it)
                }
            }


        }


        if (categoryID == 3){
            cameraList.forEach{
                if (it.kategoriya == "Ta'qiqlovchi") {
                    list.add(it)
                }
            }



        }

        if (categoryID == 4){
            cameraList.forEach{
                if (it.kategoriya == "Buyuruvchi") {
                    list.add(it)
                }
            }


        }
    }

    private fun connectRv() {
        rvAdapter = Camera_adapter(list, object : Camera_adapter.OnItemClickListener{
            override fun onItemClick(cameraModel: CameraModel) {
                var bundle = Bundle()
                bundle.putSerializable("data",cameraModel)
                findNavController().navigate(R.id.resultFragment,bundle)
            }

            override fun onEditClick(cameraModel: CameraModel, position: Int) {
                var bundle = Bundle()
                bundle.putString("edit","edit")
                bundle.putSerializable("tahrir",cameraModel)
                findNavController().navigate(R.id.addFragment,bundle)
            }

            override fun onDeleteClick(cameraModel: CameraModel, position: Int) {
                myDbHelper.deleteCamera(cameraModel)
                list.remove(cameraModel)
                rvAdapter.notifyItemRemoved(position)
                rvAdapter.notifyItemRangeChanged(position, list.size)



                val filesDir = binding.root.context.filesDir

                if (filesDir.isDirectory){
                    val listFiles = filesDir.listFiles()

                    for (listFile in listFiles) {
                        if (listFile.absolutePath == cameraModel.rasm){
                            listFile.delete()
                        }


                    }
                }


            }

//            override fun onFavouriteClick(cameraModel: CameraModel, position: Int, checkBox: CheckBox) {
//                checkBox.setOnCheckedChangeListener { checkBox, isChecked ->
//
//                    if (isChecked) {
//                        showToast("Item added to list")
//                        cameraModel.like = "liked"
//                        myDbHelper.updateCamera(cameraModel)
//                    } else {
//                        showToast("Item removed from wishlist")
//                        cameraModel.like = "not_liked"
//                        myDbHelper.updateCamera(cameraModel)
//                    }
//                }
//            }

        })
        binding.cameraRv.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

    }

    private fun showToast(str:String) {
        Toast.makeText(binding.root.context, str, Toast.LENGTH_SHORT).show()

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