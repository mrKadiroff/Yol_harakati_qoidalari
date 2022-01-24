package com.example.thetraffic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.thetraffic.adapters.TrafficMainAdapter

import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_first.view.*
import kotlinx.android.synthetic.main.tab_item.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {
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
    lateinit var root: View
    private var adapter: TrafficMainAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_first, container, false)

        root.view_pager.setOffscreenPageLimit(1);

        root.tooolbar.inflateMenu(R.menu.add_menu)
        root.tooolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.add){
                findNavController().navigate(R.id.addFragment)
            }
            true
        }

        loadAdapters()
        setTabs()


        return root
    }

    private fun loadAdapters() {
        adapter = TrafficMainAdapter(childFragmentManager)
        root.view_pager.adapter = adapter
        root.tab_layout.setupWithViewPager(root.view_pager)
    }

    @SuppressLint("SetTextI18n")
    private fun setTabs() {

        val tabCount = root.tab_layout.tabCount

        for (i in 0 until tabCount) {
            val tabBind = LayoutInflater.from(root.context).inflate(R.layout.tab_item, null, false)
            val tab = root.tab_layout?.getTabAt(i)
            tab?.customView = tabBind

            when (i) {
                0 -> tabBind.title_tv.text = "Ogohlantiruvchi"
                1 -> tabBind.title_tv.text = "Imtiyozli"
                2 -> tabBind.title_tv.text = "Ta'qiqlovchi"
                3 -> tabBind.title_tv.text = "Buyuruvchi"
            }
            if (i == 0) {
                tabBind.round.setImageResource(R.drawable.tab_corner_selected)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.main_color))
            } else {
                tabBind.round.setImageResource(R.drawable.tab_corner)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.white))
            }
        }



        root.tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.round?.setImageResource(R.drawable.tab_corner_selected)
                customView?.title_tv?.setTextColor(resources.getColor(R.color.main_color))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.round?.setImageResource(R.drawable.tab_corner)
                customView?.title_tv?.setTextColor(resources.getColor(R.color.white))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })}

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}