package com.example.mypets


import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.NewPetActivity.Companion.EXTRA_BREED
import com.example.mypets.NewPetActivity.Companion.EXTRA_NAME
import com.google.android.material.floatingactionbutton.FloatingActionButton




class MainActivity : AppCompatActivity() {

    private val newPetActivityRequestCode = 1
    private lateinit var petViewModel: PetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Add recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = PetListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Divider
        recyclerView.addItemDecoration(DividerItemDecoration(this,
            LinearLayoutManager.VERTICAL))

        //Set up live data observer
        petViewModel = ViewModelProvider(this).get(PetViewModel::class.java)
        petViewModel.allPets.observe(this, Observer { pets ->
            // Update the cached copy of the words in the adapter.
            pets?.let { adapter.setPets(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.add_pet_fab)
        fab.setOnClickListener {
            val intent = Intent(this, NewPetActivity::class.java)
            startActivityForResult(intent, newPetActivityRequestCode)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newPetActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
//                val word = Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY))

//                val newPet1 = Pet(data.getStringExtra(NewPetActivity.EXTRA_NAME), data.getStringExtra(NewPetActivity.EXTRA_BREED))


                val petName = data.getStringExtra(EXTRA_NAME)
                val petBreed = data.getStringExtra(EXTRA_BREED)
                val newPet = Pet(name = petName, breed = petBreed)

                petViewModel.insert(newPet)

            }
        }else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}
