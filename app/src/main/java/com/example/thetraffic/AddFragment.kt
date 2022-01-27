package com.example.thetraffic

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thetraffic.databinding.FragmentAddBinding
import com.example.thetraffic.db.MyDbHelper
import com.example.thetraffic.models.CameraModel
import com.github.florent37.runtimepermission.kotlin.askPermission
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var photoURI: Uri
    lateinit var myDbHelper: MyDbHelper
    val category = arrayOf("Qaysi turga kirishi","Ogohlantiruvchi","Imtiyozli","Ta'qiqlovchi","Buyuruvchi")
    lateinit var currentImagePath: String
    lateinit var binding: FragmentAddBinding
    private var fileAbsolutePath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater)
        myDbHelper = MyDbHelper(binding.root.context)

        val edit = arguments?.getString("edit")
        val add = arguments?.getString("add")
        val arrayAdapter = ArrayAdapter(binding.root.context,android.R.layout.simple_spinner_dropdown_item,category)
        binding.spinner.adapter = arrayAdapter


        if (edit!=null){
            setvalue()
            binding.imagee.setOnClickListener {
                dialog()
            }
            binding.save.setOnClickListener {
                val tahrir = arguments?.getSerializable("tahrir") as CameraModel
                val heading = binding.name.text.toString().trim()
                val descriptionim = binding.description.text.toString().trim()
                val kategoriya = binding.spinner.selectedItem.toString()


                if (fileAbsolutePath.isNullOrEmpty()){
                    fileAbsolutePath = tahrir.rasm
                    tahrir.nomi = heading
                    tahrir.malumot = descriptionim
                    tahrir.kategoriya = kategoriya
                    myDbHelper.updateCamera(tahrir)
                }else{
                    tahrir.nomi = heading
                    tahrir.rasm = fileAbsolutePath
                    tahrir.malumot = descriptionim
                    tahrir.kategoriya = kategoriya
                    myDbHelper.updateCamera(tahrir)
                }





                    findNavController().popBackStack()
                    Toast.makeText(binding.root.context, fileAbsolutePath, Toast.LENGTH_SHORT).show()
                }




            }

        if (add!=null){



            binding.imagee.setOnClickListener {
                val picturesDialog = AlertDialog.Builder(binding.root.context)
                val dialog = picturesDialog.create()
                picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
                    dialog.dismiss()
                })
                picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
                val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
                picturesDialog.setItems(DialogItems){
                        dialog, which ->
                    when(which){
                        0 -> permission_gallery()
                        1 -> permission_camera()
                    }
                }
                picturesDialog.show()
            }
        }



        binding.tooolbaradd.setNavigationOnClickListener {
            findNavController().popBackStack()
        }



        return binding.root
    }




    private fun setvalue() {
        val tahrir = arguments?.getSerializable("tahrir") as CameraModel
        binding.imagee.setImageURI(Uri.parse(tahrir.rasm))
        binding.name.setText(tahrir.nomi)
        binding.description.setText(tahrir.malumot)


        var indexCategory = -1
        for (i in 0 until category.size){
            if (category[i] == tahrir.kategoriya) {
                indexCategory = i
                break
            }
        }
        binding.spinner.setSelection(indexCategory)
    }

    private fun dialog() {
        val picturesDialog = AlertDialog.Builder(binding.root.context)
        val dialog = picturesDialog.create()
        picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
            dialog.dismiss()
        })
        picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
        val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
        picturesDialog.setItems(DialogItems){
                dialog, which ->
            when(which){
                0 -> edit_pick_gallery()
                1 -> edit_permission_camera()
            }
        }
        picturesDialog.show()
    }

    private fun edit_permission_camera() {
        askPermission(Manifest.permission.CAMERA) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(binding.root.context,BuildConfig.APPLICATION_ID,imageFile)
            getTakeeditImageContent.launch(photoURI)

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if(e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
    }



    private val getTakeeditImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.imagee.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
                val filesDir = binding.root.context.filesDir
                val contentResolver = activity?.contentResolver
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir,"image.jpg$format")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                val fileAbsolutePath = file.absolutePath


                binding.save.setOnClickListener {
                    val tahrir = arguments?.getSerializable("tahrir") as CameraModel
                    val heading = binding.name.text.toString().trim()
                    val descriptionim = binding.description.text.toString().trim()
                    val kategoriya = binding.spinner.selectedItem.toString()



                        tahrir.nomi = heading
                        tahrir.rasm = fileAbsolutePath
                        tahrir.malumot = descriptionim
                        tahrir.kategoriya = kategoriya
                        myDbHelper.updateCamera(tahrir)






                    findNavController().popBackStack()
                    Toast.makeText(binding.root.context, fileAbsolutePath, Toast.LENGTH_SHORT).show()






                }

            }

    }

    private fun edit_pick_gallery() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            geteditImageContent.launch("image/*")
        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if(e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }

    }
    private val geteditImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.imagee.setImageURI(uri)
            val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

            val filesDir = binding.root.context.filesDir
            val contentResolver = activity?.contentResolver
            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jp$format")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()


           fileAbsolutePath = file.absolutePath
            val fileInputStream = FileInputStream(file)

        }








    // pasda faqat add qilishga oid

    private fun permission_camera() {
        askPermission(Manifest.permission.CAMERA) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(binding.root.context,BuildConfig.APPLICATION_ID,imageFile)
            getTakeImageContent.launch(photoURI)

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if(e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
    }



    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.imagee.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
                val filesDir = binding.root.context.filesDir
                val contentResolver = activity?.contentResolver
                val openInputStream = contentResolver?.openInputStream(photoURI)
                val file = File(filesDir,"image.jpg$format")
                val fileOutputStream = FileOutputStream(file)
                openInputStream?.copyTo(fileOutputStream)
                openInputStream?.close()
                fileOutputStream.close()
                val fileAbsolutePath = file.absolutePath


                binding.save.setOnClickListener {

                    val heading = binding.name.text.toString().trim()
                    val descriptionim = binding.description.text.toString().trim()
                    val kategoriya = binding.spinner.selectedItem.toString()


                    if (heading.isNotEmpty() && descriptionim.isNotEmpty() && kategoriya != "Qaysi turga kirishi"){
                        val cameraModel = CameraModel(fileAbsolutePath,heading,descriptionim,kategoriya,R.drawable.ic_heart3,"unselected")
                        myDbHelper.insertCamera(cameraModel)
                        myDbHelper.getAllCamera()
                        findNavController().popBackStack()
                        Toast.makeText(binding.root.context, fileAbsolutePath, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(binding.root.context, "Iltimos ma'lumotlarni to'liq va aniq kiritganingizga ishonch hosil qiling", Toast.LENGTH_SHORT).show()
                    }





                }

            }
        }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format",".jpg",externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }

    private fun permission_gallery() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()

            }
            .onDeclined { e ->
                        if (e.hasDenied()) {
                            android.app.AlertDialog.Builder(binding.root.context)
                                .setMessage("Please accept our permissions")
                                .setPositiveButton("yes") { dialog, which ->
                                    e.askAgain();
                                } //ask again
                                .setNegativeButton("no") { dialog, which ->
                                    Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss();
                                }
                                .show();
                        }
                        if(e.hasForeverDenied()) {

                            // you need to open setting manually if you really need it
                            e.goToSettings();
                        }
                    }
    }

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.imagee.setImageURI(uri)
            val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

            val filesDir = binding.root.context.filesDir
            val contentResolver = activity?.contentResolver
            val openInputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir, "image.jp$format")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()




                val fileAbsolutePath = file.absolutePath
                val fileInputStream = FileInputStream(file)


                binding.save.setOnClickListener {

                    val heading = binding.name.text.toString().trim()
                    val descriptionim = binding.description.text.toString().trim()
                    val kategoriya = binding.spinner.selectedItem.toString()

                    if (heading.isNotEmpty() && descriptionim.isNotEmpty() && kategoriya != "Qaysi turga kirishi") {
                        val cameraModel =
                            CameraModel(fileAbsolutePath, heading, descriptionim, kategoriya,R.drawable.ic_heart3,"unselected")
                        myDbHelper.insertCamera(cameraModel)
                        myDbHelper.getAllCamera()
                        findNavController().popBackStack()
                        Toast.makeText(binding.root.context, fileAbsolutePath, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Iltimos ma'lumotlarni to'liq va aniq kiritganingizga ishonch hosil qiling",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}