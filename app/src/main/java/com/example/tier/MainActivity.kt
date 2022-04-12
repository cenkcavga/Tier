package com.example.tier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.tier.databinding.ActivityMainBinding
import com.example.tier.viewmodel.VehicleMapViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private  val vehicleMapViewModel: VehicleMapViewModel by viewModels()
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        vehicleMapViewModel.liveData.observe(this){

        }


    }



}