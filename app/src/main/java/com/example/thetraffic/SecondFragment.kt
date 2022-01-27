package com.example.thetraffic

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.example.thetraffic.adapters.Camera_adapter
import com.example.thetraffic.databinding.CamerLayoutBinding
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
    lateinit var myDbHelper: MyDbHelper
    lateinit var rvAdapter: Camera_adapter
    lateinit var list: ArrayList<CameraModel>
    lateinit var listkamera: ArrayList<CameraModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSecondBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)
        listkamera = myDbHelper.getAllCamera()
        list = ArrayList()

        for (camerav in listkamera){
            if (camerav.tanlangan == "selected"){
                list.add(camerav)
            }
        }
        

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
            var a = 100
            override fun onFavouriteClick(
                cameralayoutBinding: CamerLayoutBinding,
                cameraModel: CameraModel,
                position: Int
            ) {

                cameralayoutBinding.bookmarkBtn.setImageResource(R.drawable.ic_heart3)
                if (a == position) {
                    cameralayoutBinding.bookmarkBtn.setImageResource(R.drawable.ic_heart3)
                    cameraModel.like = R.drawable.ic_heart3
                    val cameraModel =
                        CameraModel(cameraModel.id, cameraModel.rasm,cameraModel.nomi,cameraModel.malumot,cameraModel.kategoriya,R.drawable.ic_heart3,"unselected")
                    myDbHelper.updateCamera(cameraModel)
                    rvAdapter.list = list
                    a++
                } else {
                    cameralayoutBinding.bookmarkBtn.setImageResource(R.drawable.ic_heart4)
                    cameraModel.like = R.drawable.ic_heart4
                    val cameraModel =
                        CameraModel(cameraModel.id, cameraModel.rasm,cameraModel.nomi,cameraModel.malumot,cameraModel.kategoriya,R.drawable.ic_heart4,"selected")
                    myDbHelper.updateCamera(cameraModel)
                    rvAdapter.list = list
                    a = position
                }
                rvAdapter.list = list
                list.clear()
                for (camerav in listkamera){
                    if (camerav.tanlangan == "selected"){
                        list.add(camerav)
                    }
                }
                rvAdapter.notifyDataSetChanged()
            }


        })
        binding.favouriteRv.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        for (camerav in listkamera){
            if (camerav.tanlangan == "selected"){
                list.add(camerav)
            }
        }
        rvAdapter.notifyDataSetChanged()
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