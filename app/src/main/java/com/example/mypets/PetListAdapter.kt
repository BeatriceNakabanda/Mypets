package com.example.mypets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PetListAdapter internal constructor( context: Context)
    : RecyclerView.Adapter<PetListAdapter.PetViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var pets = emptyList<Pet>() // Cached copy of pets

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petItemView: TextView = itemView.findViewById(R.id.textView)
        val petBreedView: TextView = itemView.findViewById(R.id.breedtextView)
//        val petWeightView: TextView = itemView.findViewById(R.id.edit_pet_weight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return PetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val current = pets[position]
        holder.petItemView.text = current.name
        holder.petBreedView.text = current.breed
//        holder.petGenderView.text = current.gender
//        holder.petWeightView.text = current.weight
    }

    internal fun setPets(pets: List<Pet>) {
        this.pets = pets
        notifyDataSetChanged()
    }

    override fun getItemCount() = pets.size

}