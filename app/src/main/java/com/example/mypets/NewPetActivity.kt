package com.example.mypets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewPetActivity : AppCompatActivity() {

    private lateinit var editPetNameView: EditText
    private lateinit var editPetBreedView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pet)

        editPetNameView = findViewById(R.id.edit_pet_name)
        editPetBreedView = findViewById(R.id.edit_pet_breed)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val petName = editPetNameView.getText().toString().trim()
            val petBreed = editPetBreedView.getText().toString().trim()

            val replyIntent = Intent()
            if (TextUtils.isEmpty(petName) || TextUtils.isEmpty(petBreed)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {

                replyIntent.putExtra(EXTRA_NAME, petName)
                replyIntent.putExtra(EXTRA_BREED, petBreed)

                setResult(Activity.RESULT_OK, replyIntent)

            }
            finish()
        }

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_BREED = "extra_breed"
    }
}


