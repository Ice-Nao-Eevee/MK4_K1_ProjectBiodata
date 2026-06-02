package com.danish.aplikasibiodata_percobaan

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class GalleryFragment : Fragment() {

    private lateinit var rvGallery: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter

    private var selectedImageUri: Uri? = null
    private var currentCaption: String = "Uploaded from Android"
    private var listGalleryData: List<GalleryItem> = ArrayList()
    private var userRole: String = "umum"

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            if (selectedImageUri != null) {
                uploadImageToCloudinary()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        userRole = prefs.getString("role", "umum") ?: "umum"

        rvGallery = view.findViewById(R.id.rvGallery)
        rvGallery.layoutManager = GridLayoutManager(requireContext(), 2)

        galleryAdapter = GalleryAdapter(listGalleryData) { item ->
            showImagePreviewDialog(item)
        }
        rvGallery.adapter = galleryAdapter

        val btnUpload = view.findViewById<TextView>(R.id.btnUploadMemory)
        val ivAddPhoto = view.findViewById<ImageView>(R.id.ivAddPhoto)

        if (userRole == "umum") {
            btnUpload?.visibility = View.GONE
            ivAddPhoto?.visibility = View.GONE
        } else {
            btnUpload?.visibility = View.VISIBLE
            ivAddPhoto?.visibility = View.VISIBLE

            btnUpload?.setOnClickListener { showCaptionInputDialog() }
            ivAddPhoto?.setOnClickListener { showCaptionInputDialog() }
        }

        fetchGalleryData()
    }

    private fun showCaptionInputDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tambah Caption Kenangan")

        val input = EditText(requireContext())
        input.hint = "Tulis moment seru di sini..."
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        builder.setView(input)

        builder.setPositiveButton("Pilih Foto") { dialog, _ ->
            currentCaption = input.text.toString().trim()
            if (currentCaption.isEmpty()) currentCaption = "Class Memory"
            dialog.dismiss()

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun fetchGalleryData() {
        val prefs = requireContext().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        RetrofitClient.instance.getGalleries("Bearer $token")
            .enqueue(object : Callback<GalleryResponse> {
                override fun onResponse(call: Call<GalleryResponse>, response: Response<GalleryResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            listGalleryData = it
                            galleryAdapter.updateData(it)
                        }
                    }
                }
                override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                    Log.e("GALLERY_DEBUG", "Fetch Failure: ${t.message}")
                }
            })
    }

    private fun showImagePreviewDialog(item: GalleryItem) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image, null)
        val ivPreview = view.findViewById<ImageView>(R.id.ivFullPreview)
        val tvCaption = view.findViewById<TextView>(R.id.tvPreviewCaption)
        val btnDelete = view.findViewById<ImageView>(R.id.btnDeleteImage)
        val btnDismiss = view.findViewById<ImageView>(R.id.btnDismissDialog)

        tvCaption.text = item.caption ?: "Class Memory"
        Glide.with(this).load(item.imageUrl).into(ivPreview)

        val dialog = AlertDialog.Builder(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            .setView(view)
            .create()

        if (userRole == "umum") {
            btnDelete.visibility = View.GONE
        } else {
            btnDelete.visibility = View.VISIBLE
            btnDelete.setOnClickListener {
                dialog.dismiss()
                deleteImageFromGallery(item.id ?: 0)
            }
        }

        // Action nutup dialog pakai tombol close bawaan layout premium baru
        btnDismiss.setOnClickListener { dialog.dismiss() }

        // Klik area background hitam luar juga tetep bisa nutup dialog
        view.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun uploadImageToCloudinary() {
        val uri = selectedImageUri ?: return
        val pd = ProgressDialog(requireContext())
        pd.setMessage("Mengirim ke Cloudinary...")
        pd.setCancelable(false)
        pd.show()

        val file = uriToFile(uri, requireContext())
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val caption = currentCaption.toRequestBody("text/plain".toMediaTypeOrNull())

        val prefs = requireContext().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = "Bearer ${prefs.getString("token", "")}"

        RetrofitClient.instance.uploadGallery(token, body, caption).enqueue(object : Callback<GalleryResponse> {
            override fun onResponse(call: Call<GalleryResponse>, response: Response<GalleryResponse>) {
                pd.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Memory Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
                    fetchGalleryData()
                } else {
                    Log.e("GALLERY_DEBUG", "Error: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                pd.dismiss()
                Log.e("GALLERY_DEBUG", "Upload Failure: ${t.message}")
            }
        })
    }

    private fun deleteImageFromGallery(id: Int) {
        if (id == 0) {
            Toast.makeText(context, "ID foto tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        val pd = ProgressDialog(requireContext())
        pd.setMessage("Menghapus foto...")
        pd.show()

        val prefs = requireContext().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        val token = "Bearer ${prefs.getString("token", "")}"

        RetrofitClient.instance.deleteGallery(token, id).enqueue(object : Callback<GalleryResponse> {
            override fun onResponse(call: Call<GalleryResponse>, response: Response<GalleryResponse>) {
                pd.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(context, "Foto berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    fetchGalleryData()
                } else {
                    Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                pd.dismiss()
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempFile).use { output -> input.copyTo(output) }
        }
        return tempFile
    }
}

// =========================================================================
// ADAPTER RECYCLERVIEW (SUDAH DISINKRONKAN DENGAN BINGKAI TEXTVIEW BARU)
// =========================================================================
class GalleryAdapter(
    private var items: List<GalleryItem>,
    private val onItemClick: (GalleryItem) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    fun updateData(newItems: List<GalleryItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_card, parent, false)
        return GalleryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = items[position]

        // Memasukkan teks caption dari database Laravel ke bingkai item_gallery_card
        holder.tvItemCaption.text = item.caption ?: "Class Memory"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.ivPhoto)

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPhoto: ImageView = view.findViewById(R.id.ivItemPhoto)
        val tvItemCaption: TextView = view.findViewById(R.id.tvItemCaption) // Berhasil di-link ke XML baru
    }
}