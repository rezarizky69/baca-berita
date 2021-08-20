package com.eja.bacaberitaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.eja.bacaberitaapp.R
import com.eja.bacaberitaapp.fragment.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setItemSelected(R.id.menuHeadline, true)

        setupFragment(FragmentHeadline())

        //open fragment
        bottomNavigation.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.menuHeadline -> setupFragment(FragmentHeadline())
                    R.id.menuSports -> setupFragment(FragmentSports())
                    R.id.menuTechnology -> setupFragment(FragmentTechnology())
                    R.id.menuBusiness -> setupFragment(FragmentBusiness())
                    R.id.menuHealth -> setupFragment(FragmentHealth())
                    R.id.menuEntertaiment -> setupFragment(FragmentEntertainment())
                    R.id.menuSearch -> setupFragment(FragmentSearch())
                }
            }
        })
    }

    private fun setupFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentLayout, fragment)
        transaction.commit()
    }

}