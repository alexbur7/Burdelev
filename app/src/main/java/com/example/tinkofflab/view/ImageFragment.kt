package com.example.tinkofflab.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.tinkofflab.ContentModel
import com.example.tinkofflab.viewmodel.ImageViewModel
import com.example.tinkofflab.utils.MyViewModelFactory
import com.example.tinkofflab.R

class ImageFragment: Fragment() {

    companion object{
        private const val POSITION_KEY = "position_key"
        fun newInstance(position:Int): ImageFragment {
            val args = Bundle()
            args.putInt(POSITION_KEY,position)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val contentList = mutableListOf<ContentModel>()
    private lateinit var downloadBar:ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var nextButton:AppCompatImageButton
    private lateinit var backButton:AppCompatImageButton
    private lateinit var repeatText:TextView
    private lateinit var errorText:TextView
    private var currentPosition = 0
    private lateinit var imageViewModel: ImageViewModel
    private val observer = Observer<Int> {
        when(it){
            ImageViewModel.ERROR_NETWORK_KEY -> errorNetwork()
            ImageViewModel.GONE_DOWNLOAD_BAR_KEY -> downloadBar.gone()
            ImageViewModel.SHOW_VIEW_KEY -> showView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageViewModel = ViewModelProvider(this, MyViewModelFactory()).get(ImageViewModel::class.java)
        imageViewModel.getData().observe(this, observer)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val position = arguments?.getInt(POSITION_KEY)?:0
        val view = inflater.inflate(R.layout.fragment_image,container,false)
        setUpAllView(view,position)
        imageViewModel.downloadImage(requireContext(),position,imageView,currentPosition,
            contentList)
        return view
    }

    private fun setUpAllView(view: View,position: Int) {
        initializationView(view)
        backButton.isEnabled = false
        repeatText.setOnClickListener {
            imageViewModel.downloadImage(requireContext(),position,imageView,currentPosition,
                contentList)
        }
        backButton.setOnClickListener {
                if (currentPosition == 1) {
                    it.isEnabled = false
                }
                currentPosition--
                Glide.with(this).asDrawable().load(contentList[currentPosition].url)
                    //.transition(GenericTransitionOptions.with(R.anim.zoom_in_anim))
                    .into(imageView)
        }
        nextButton.setOnClickListener{
            currentPosition++
            if (contentList.isNotEmpty()) {
                backButton.isEnabled = true
            }
            downloadBar.show()
            if (currentPosition >= contentList.size){
                it.isEnabled = false
                imageViewModel.downloadImage(requireContext(),position,imageView,currentPosition,
                    contentList)
            }
            else{
                imageViewModel.downloadNextGif(contentList[currentPosition],requireContext(),imageView)
            }
        }
    }

    private fun initializationView(view: View) {
        repeatText = view.findViewById(R.id.repeat)
        errorText = view.findViewById(R.id.error_with_network)
        downloadBar = view.findViewById(R.id.download_bar)
        imageView = view.findViewById(R.id.gif_image)
        nextButton = view.findViewById(R.id.next_button)
        backButton = view.findViewById(R.id.back_button)
    }

    private fun showView() {
        imageView.show()
        nextButton.show()
        backButton.show()
        errorText.gone()
        repeatText.gone()
        downloadBar.gone()
        nextButton.isEnabled = true
    }

    private fun errorNetwork() {
        downloadBar.gone()
        imageView.gone()
        backButton.gone()
        nextButton.gone()
        errorText.show()
        repeatText.show()
        Toast.makeText(context,context?.getString(R.string.check_network),Toast.LENGTH_SHORT).show()
    }

    private fun View.gone(){
        this.visibility = View.GONE
    }


    private fun View.show(){
        this.visibility = View.VISIBLE
    }
}