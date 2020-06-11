package com.example.mypets

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProvider


class NewPetActivity : AppCompatActivity() {

    private lateinit var editPetNameView: EditText
    private lateinit var editPetBreedView: EditText
    private lateinit var editPetWeightView: EditText
    private lateinit var mGenderSpinner: Spinner
    private lateinit var petViewModel: PetViewModel

    private val genderUnknown = 0
    private val genderMale = 1
    private val genderFemale = 2

    private var mGender = genderUnknown
    private var pet: Pet? = null
    private val petId = pet?.pid

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pet)

        pet = intent?.extras?.get("pet") as Pet?

        editPetNameView = findViewById(R.id.edit_pet_name)
        editPetBreedView = findViewById(R.id.edit_pet_breed)
        editPetWeightView = findViewById(R.id.edit_pet_weight)
        mGenderSpinner = findViewById(R.id.spinner_gender)

        editPetNameView.addTextChangedListener(nameTextWatcher)
        editPetBreedView.addTextChangedListener(breedTextWatcher)
        editPetWeightView.addTextChangedListener(weightTextWatcher)

        setUpGenderSpinner()

        if (pet == null){
            title = "Add Pet"

            savePet()
        }else{
            title = "Edit Pet"

            editPetNameView.setText(pet?.name)
            editPetBreedView.setText(pet?.breed)
            pet?.gender?.let { mGenderSpinner.setSelection(it) }
            editPetWeightView.setText(pet?.weight.toString())


            editPet()
        }

    }
    private val nameTextWatcher = object : TextWatcher{
        override fun afterTextChanged(editable: Editable?) {
            val invalidNameText =
                findViewById<View>(R.id.invalid_pet_name) as TextView
            if (editable != null && !editPetNameView.equals("")){
                invalidNameText.visibility = View.GONE
            }else{
                invalidNameText.visibility = View.VISIBLE

            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
    private val breedTextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val invalidBreedText =
                findViewById<View>(R.id.invalid_pet_breed) as TextView
            if (editable != null && !editPetBreedView.equals("")){
                invalidBreedText.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }
    private val weightTextWatcher = object : TextWatcher{
        override fun afterTextChanged(editable: Editable?) {
            val invalidWeightText =
                findViewById<View>(R.id.invalid_weight_units) as TextView
            if (editable != null && !editPetWeightView.equals("")){
                invalidWeightText.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_BREED = "extra_breed"
        const val EXTRA_WEIGHT = "extra_weight"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_ID = "extra_id"

    }
    private fun savePet(){
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val petName = editPetNameView.getText().toString().trim()
            val petBreed = editPetBreedView.getText().toString().trim()
            val petWeightString = editPetWeightView.getText().toString()

            val replyIntent = Intent()
            when {
                isEmpty() || isinvalidGender(mGender) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)

                }
                else -> {
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
            }
//            finish()
        }
    }
    private fun editPet(){
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val petName = editPetNameView.getText().toString().trim()
            val petBreed = editPetBreedView.getText().toString().trim()
            val petWeightString = editPetWeightView.getText().toString()

            val replyIntent = Intent()

            when {
                isEmpty() || isinvalidGender(mGender) -> {
                    setResult(Activity.RESULT_CANCELED, replyIntent)

                }
                else -> {
                    replyIntent.putExtra(EXTRA_NAME, petName)
                    replyIntent.putExtra(EXTRA_BREED, petBreed)
                    replyIntent.putExtra(EXTRA_WEIGHT, petWeightString)
                    replyIntent.putExtra(EXTRA_GENDER, mGender)
                    replyIntent.putExtra(EXTRA_ID, petId)

                    setResult(Activity.RESULT_OK, replyIntent)

                    Toast.makeText(
                        this,"name: $petName breed: $petBreed gender: $mGender weight: $petWeightString",
                        Toast.LENGTH_SHORT
                    ).show()

//                    Toast.makeText(
//                        this, getString(R.string.editor_update_pet_successful),
//                        Toast.LENGTH_SHORT
//                    ).show()

                }
            }

        }
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
                    val invalidGenderText =
                        findViewById<View>(R.id.invalid_pet_gender) as TextView
                    invalidGenderText.visibility = View.GONE

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
            val invalidBreedText =
                findViewById<View>(R.id.invalid_pet_breed) as TextView
            val invalidWeightText =
                findViewById<View>(R.id.invalid_weight_units) as TextView
            invalidNameText.visibility = View.VISIBLE
            invalidBreedText.visibility = View.VISIBLE
            invalidWeightText.visibility = View.VISIBLE
            return true
        }
        return false
    }
    private fun isinvalidGender(gender: Int): Boolean{
        if (gender == genderUnknown){
            val invalidGenderText =
                findViewById<View>(R.id.invalid_pet_gender) as TextView
            invalidGenderText.visibility = View.VISIBLE
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_editor, menu)
        return true
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        when (item.itemId) {

//            R.id.action_delete -> {
//
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}






