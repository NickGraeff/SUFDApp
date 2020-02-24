package com.nicksnacs.smashultimateframedata.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nicksnacs.smashultimateframedata.R
import com.nicksnacs.smashultimateframedata.application.SUFDApplication
import com.nicksnacs.smashultimateframedata.character_page.CharacterActivity
import com.nicksnacs.smashultimateframedata.databinding.ActivityMainBinding
import com.nicksnacs.smashultimateframedata.db.AppDatabase
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CharacterAdapter.ClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var roomDb: AppDatabase

    private lateinit var viewModel : CharactersViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DI
        (application as SUFDApplication).applicationComponent.getCharactersViewModelComponentBuilder().build().inject(this)

        // Databinding
        (DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding)
            .apply {

                characterRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                characterRecyclerView.adapter = CharacterAdapter(this@MainActivity).apply {
                    // ViewModel
                    viewModel = viewModelFactory.create(CharactersViewModel::class.java)
                    viewModel.characters.observe(this@MainActivity, this)
                    viewModel.loadCharacters()
                }

            }
    }

    override fun characterClicked(position: Int) {
        val intent = Intent(this, CharacterActivity::class.java)
        intent.putExtra("character_name", viewModel.characters.value?.get(position)?.name)
        intent.putExtra("character_data_url", viewModel.characters.value?.get(position)?.characterDataUrl)
        startActivity(intent)
    }
}
