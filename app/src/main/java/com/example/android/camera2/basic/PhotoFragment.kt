package com.example.android.camera2.basic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

/**
 * A simple image viewer fragment with delete and share buttons
 */
class PhotoFragment : Fragment() {
    private val TAG = "PhotoFragment"
    private val args: PhotoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoView = requireActivity().findViewById<ImageView>(R.id.photo_image)
        val shareButton = requireActivity().findViewById<ImageButton>(R.id.share_button)
        val deleteButton = requireActivity().findViewById<ImageButton>(R.id.delete_button)
        val photoUri = Uri.parse(args.imageUri)


        shareButton.setOnClickListener {
            val photoUri = Uri.parse(args.imageUri)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_STREAM, photoUri)
                setDataAndType(photoUri, "image/jpeg")

            }
            try {
                startActivity(sendIntent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start share activity")
            }
        }

        deleteButton.setOnClickListener {
            try {
                requireActivity().contentResolver.delete(photoUri, null, null)
                findNavController().popBackStack()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to delete image $photoUri")
            }
        }

        Glide
            .with(this)
            .load(photoUri)
            .into(photoView)
    }
}