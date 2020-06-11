package com.example.mypets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil

class PetListAdapter internal constructor( private val context: Context)
    : RecyclerView.Adapter<PetListAdapter.PetViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pets = emptyList<Pet>() // Cached copy of pets


    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petItemView: TextView = itemView.findViewById(R.id.textView)
        val petBreedView: TextView = itemView.findViewById(R.id.breedtextView)
//        var petPosition = 0
//        val petWeightView: TextView = itemView.findViewById(R.id.edit_pet_weight)
//        val petGenderView: TextView = itemView.findViewById(R.id.spinner_gender)
        init{
            itemView.setOnClickListener {v: View ->
                val PET = "pet"
                val position: Int = adapterPosition

//                Toast.makeText(itemView.context, "You clicked on item # ${position + 1}", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, NewPetActivity::class.java )
                val bundle = Bundle()
                bundle.putSerializable(PET, pets[position])
                intent.putExtras(bundle)

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val current = pets[position]
        holder.petItemView.text = current.name
        holder.petBreedView.text = current.breed
//        holder.petPosition = position
//        holder.petGenderView.text = current.gender
//        holder.petWeightView.text = current.weight.toString()
//        holder.itemView.setOnClickListener { listener(current) }
    }

    internal fun setPets(pets: List<Pet>) {
//        this.pets.clear()
//        this.pets = pets as  MutableList<Pet>
//        notifyDataSetChanged()

        when(pets){
            null  -> {
                this.pets = pets
                notifyItemRangeInserted(0, pets.size)
            }
            else -> {
                val result : DiffUtil.DiffResult = DiffUtil.calculateDiff(object: DiffUtil.Callback(){

                    override fun getOldListSize():Int { return this@PetListAdapter.pets.size}
                    override fun getNewListSize():Int { return pets.size}

                    override fun areItemsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean {
                        return this@PetListAdapter.pets.get(oldItemPosition).getId() == pets.get(newItemPosition).getId()
                    }

                    override fun getChangePayload(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Any? {
                        return super.getChangePayload(oldItemPosition, newItemPosition)
                    }

                    override fun areContentsTheSame(
                        oldItemPosition: Int,
                        newItemPosition: Int
                    ): Boolean {
                        return this@PetListAdapter.pets[oldItemPosition] == pets[newItemPosition]
                    }

                } )
                this.pets = pets
                result.dispatchUpdatesTo(this)
            }

        }

    }

    override fun getItemCount() = pets.size

}