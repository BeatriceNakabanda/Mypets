package com.example.mypets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

class NewPetActivity : AppCompatActivity() {

    private lateinit var editPetNameView: EditText
    private lateinit var editPetBreedView: EditText
    private lateinit var editPetWeightView: EditText

    private lateinit var mGenderSpinner: Spinner

//    private val genderUnknown = 0
//    private val genderMale = 1
//    private val genderFemale = 2

    private val genderUnknown = "Unknown"
    private val genderMale = "Male"
    private val genderFemale = "Female"

    private var mGender = genderUnknown


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pet)


        editPetNameView = findViewById(R.id.edit_pet_name)
        editPetBreedView = findViewById(R.id.edit_pet_breed)
        editPetWeightView = findViewById(R.id.edit_pet_weight)
        mGenderSpinner = findViewById(R.id.spinner_gender)

        setUpGenderSpinner()

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val petName = editPetNameView.getText().toString().trim()
            val petBreed = editPetBreedView.getText().toString().trim()
            val petWeightString = editPetWeightView.getText().toString()

            val replyIntent = Intent()
            if (isEmpty() && !isinvalidGender(mGender)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {
                replyIntent.putExtra(EXTRA_NAME, petName)
                replyIntent.putExtra(EXTRA_BREED, petBreed)
                replyIntent.putExtra(EXTRA_WEIGHT, petWeightString)
                replyIntent.putExtra(EXTRA_GENDER, mGender)

                setResult(Activity.RESULT_OK, replyIntent)

                Toast.makeText(
                    this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT
                ).show()

            }
//            finish()
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_BREED = "extra_breed"
        const val EXTRA_WEIGHT = "extra_weight"
        const val EXTRA_GENDER = "extra_gender"
    }

    private fun setUpGenderSpinner(){
        if (mGenderSpinner != null ){
            //Create adapter for spinner
            val adapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options,
                android.R.layout.simple_spinner_item
            )
            // Specify dropdown layout style - simple list view with 1 item per line
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            // Apply the adapter to the spinner
            mGenderSpinner.adapter = adapter

            mGenderSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    val selection =
                        parent.getItemAtPosition(position) as String
                    if (!TextUtils.isEmpty(selection)) {
                        mGender = when (selection) {
                            getString(R.string.gender_male) -> {
                                genderMale
                            }
                            getString(R.string.gender_female) -> {
                                genderFemale
                            }
                            else -> {
                                genderUnknown
                            }
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    mGender
                }
            }
        }
    }
    private fun isEmpty(): Boolean{
        val petName = editPetNameView.getText().toString().trim()
        val petBreed = editPetBreedView.getText().toString().trim()
        if (TextUtils.isEmpty(petName) || TextUtils.isEmpty(petBreed)){

            val invalidNameText =
            findViewById<View>(R.id.invalid_pet_name) as TextView
            invalidNameText.visibility = View.VISIBLE

            val invalidBreedText =
                findViewById<View>(R.id.invalid_pet_breed) as TextView
            invalidBreedText.visibility = View.VISIBLE

            val invalidWeightText =
                findViewById<View>(R.id.invalid_weight_units) as TextView
            invalidWeightText.visibility = View.VISIBLE
            return true
        }
        return false
    }
    private fun isinvalidGender(gender: String): Boolean{
        if (gender == genderUnknown){
            val invalidGenderText =
                findViewById<View>(R.id.invalid_pet_gender) as TextView
            invalidGenderText.visibility = View.VISIBLE
            return true
        }
        return false
    }
}


