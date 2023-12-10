/**
 * @Group 9
 * @author Emre Deniz (301371047)
 * @author Muindo Gituku (301372521)
 * @author Nkemjika Obi (301275091)
 * @date Nov 24, 2023
 * @description: Android Project
 */

package com.example.group9_mapd711_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.group9_mapd711_project.databinding.ActivityConfirmBinding
import com.example.group9_mapd711_project.databinding.ActivityCustomerBinding

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = applicationContext.getSharedPreferences("Build_Pizza_Order", 0) //initialize an instance of shared preference

        binding.orderReferenceNumber.text = "Ref No. #${pref.getString("order_reference_id","No Order Found")}"

        binding.copyReferenceButton.setOnClickListener {
            Toast.makeText(this,"Reference Copied", Toast.LENGTH_SHORT).show()
        }

        binding.goHomeButton.setOnClickListener {  }

        binding.trackOrderButton.setOnClickListener {
            startActivity(Intent(this, CustomerViewSingleOrderActivity::class.java))
        }
    }
}