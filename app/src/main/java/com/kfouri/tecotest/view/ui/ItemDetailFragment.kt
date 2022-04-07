package com.kfouri.tecotest.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.kfouri.tecotest.R
import com.kfouri.tecotest.api.CharacterService
import com.kfouri.tecotest.api.model.CharacterDetailResponse
import com.kfouri.tecotest.databinding.FragmentItemDetailBinding
import com.kfouri.tecotest.model.CharacterApiModel
import com.kfouri.tecotest.state.Status
import com.kfouri.tecotest.viewmodel.CharacterDetailViewModel
import com.kfouri.tecotest.viewmodel.ViewModelFactory

class ItemDetailFragment : Fragment() {

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private lateinit var viewModel: CharacterDetailViewModel
    private lateinit var characterId: String
    private lateinit var binding: FragmentItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                characterId = it.getString(ARG_ITEM_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(
            CharacterApiModel(CharacterService())
        ))[CharacterDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        with(binding) {
            if (characterId.isNotEmpty()) {
                imageViewLogo.visibility = View.GONE
                characterDetailGroup.visibility = View.VISIBLE
                viewModel.getCharacterDetail(characterId)
            }
        }

//        if (!resources.getBoolean(R.bool.isTablet)) {
//            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        }
    }

    private fun setObservers() {
        activity?.let { it ->
            viewModel.characterDetailLiveData.observe(it, {
                when(it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        showData(it.data)
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, "Error: " + it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun showData(data: CharacterDetailResponse?) {

        data?.let {
            with(binding) {

                textViewName.text = data.name

                context?.let { context ->
                    textViewStatus.text = context.getString(R.string.status, data.species, data.status, data.gender)
                    textViewLastKnownLocationText.text = context.getString(R.string.last_know_location_detail, data.location.name)
                    textViewOriginText.text = context.getString(R.string.origin_detail, data.origin.name)
                    textViewEpisodes.text = context.getString(R.string.episodes_detail, data.episode.size.toString())
                    GlideApp.with(context.applicationContext)
                        .load(data.image)
                        .into(imageViewThumbnail)
                }
            }
        }
    }
}