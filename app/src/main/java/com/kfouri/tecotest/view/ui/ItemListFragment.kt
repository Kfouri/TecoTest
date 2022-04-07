package com.kfouri.tecotest.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.tecotest.R
import com.kfouri.tecotest.adapter.CharacterAdapter
import com.kfouri.tecotest.api.CharacterService
import com.kfouri.tecotest.databinding.FragmentItemListBinding
import com.kfouri.tecotest.model.CharacterApiModel
import com.kfouri.tecotest.state.Status
import com.kfouri.tecotest.viewmodel.CharacterListViewModel
import com.kfouri.tecotest.viewmodel.ViewModelFactory

class ItemListFragment : Fragment() {

    private lateinit var binding: FragmentItemListBinding

    private lateinit var viewModel: CharacterListViewModel
    private val characterListAdapter by lazy {
        activity?.let { CharacterAdapter(it.applicationContext) { id: String -> itemClicked(id) } }
    }

    private var itemDetailFragmentContainer: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(
            CharacterApiModel(CharacterService())
        ))[CharacterListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setObservers()
        itemDetailFragmentContainer = view.findViewById(R.id.item_detail_nav_container)


        viewModel.getCharacterList()
    }

    private fun setRecyclerView() {
        binding.itemList.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = characterListAdapter
        }

        binding.itemList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                characterListAdapter?.itemCount.let { count ->
                    if (count != null) {
                        if (!binding.itemList.canScrollVertically(1) && count > 0) {
                            if (viewModel.currentPage <= viewModel.totalPages) {
                                viewModel.currentPage += 1
                                viewModel.getCharacterList()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setObservers() {
        activity?.let { it ->
            viewModel.characterListLiveData.observe(it, {
                when(it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        viewModel.totalPages = it.data?.info?.pages ?: 1
                        it.data?.results.let { list ->
                            list?.let { it1 -> characterListAdapter?.setData(it1) }
                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, "Error: " + it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun itemClicked(id: String) {

        val bundle = Bundle()
        bundle.putString(
            ItemDetailFragment.ARG_ITEM_ID,
            id
        )
        itemDetailFragmentContainer?.let {
            itemDetailFragmentContainer?.findNavController()
                ?.navigate(R.id.fragment_item_detail, bundle)
        } ?: run {
            findNavController().navigate(R.id.show_item_detail, bundle)
        }

    }
}