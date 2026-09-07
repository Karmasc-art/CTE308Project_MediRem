package com.example.medirem.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.medirem.R
import com.example.medirem.data.Medicine
import com.example.medirem.data.MediRemDatabase
import com.example.medirem.notification.ReminderScheduler
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddMedicineFragment : Fragment(R.layout.fragment_add_medicine) {

    private var selectedTime: String = ""
    private var selectedDate: String = ""
    private var imageUri: Uri? = null
    private lateinit var imgMedicine: ImageView

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imgMedicine.setImageURI(imageUri)
        }
    }

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            imgMedicine.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etDosage = view.findViewById<EditText>(R.id.etDosage)
        val etNote = view.findViewById<EditText>(R.id.etNote)
        val btnDate = view.findViewById<Button>(R.id.btnDate)
        val btnTime = view.findViewById<Button>(R.id.btnTime)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        val btnTakePhoto = view.findViewById<Button>(R.id.btnTakePhoto)
        imgMedicine = view.findViewById(R.id.imgMedicine)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        btnTakePhoto.setOnClickListener {
            showImagePickerOptions()
        }

        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(requireContext(), { _, year, month, day ->
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day)
                btnDate.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                val amPmFormat = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
                btnTime.text = amPmFormat.format(cal.time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val dosage = etDosage.text.toString()
            val note = etNote.text.toString()

            if (name.isEmpty() || dosage.isEmpty() || selectedTime.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill name, dosage, date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicine = Medicine(
                name = name,
                dosage = dosage,
                note = note,
                time = selectedTime,
                date = selectedDate,
                photoUri = imageUri?.toString()
            )

            lifecycleScope.launch {
                val db = MediRemDatabase.getDatabase(requireContext())
                val id = db.medicineDao().insertMedicine(medicine)
                
                val savedMedicine = medicine.copy(id = id.toInt())
                ReminderScheduler.scheduleMedicine(requireContext(), savedMedicine)

                Toast.makeText(requireContext(), "Medicine Saved", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Medicine Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> selectImage.launch("image/*")
                }
            }
            .show()
    }

    private fun openCamera() {
        val storageDir = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        val photoFile = File(storageDir, "medicine_${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "com.example.medirem.fileprovider",
            photoFile
        )
        takePicture.launch(imageUri)
    }
}