package com.nicksnacs.smashultimateframedata.character_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nicksnacs.smashultimateframedata.R
import com.nicksnacs.smashultimateframedata.application.SUFDApplication
import com.nicksnacs.smashultimateframedata.databinding.ActivityCharacterBinding
import javax.inject.Inject

class CharacterActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : DetailedCharacterInformationModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DI
        (application as SUFDApplication).applicationComponent.getDetailedCharacterInformationModelComponentBuilder().build().inject(this)

        // Databinding
        (DataBindingUtil.setContentView(this, R.layout.activity_character) as ActivityCharacterBinding).apply {
            moveRecyclerView.layoutManager = LinearLayoutManager(this@CharacterActivity)
            moveRecyclerView.adapter = MoveAdapter().apply {
                // ViewModel
                val characterName: String
                val characterDataUrl: String
                this@CharacterActivity.intent.apply {
                    characterName = getStringExtra("character_name") ?: "No name"
                    characterDataUrl = getStringExtra("character_data_url") ?: "No name"
                }
                viewModel = viewModelFactory.create(DetailedCharacterInformationModel::class.java)
                viewModel.detailedCharacterInformation.observe(this@CharacterActivity, this)
                viewModel.loadCharacter(characterName, characterDataUrl)
            }
        }
    }
}