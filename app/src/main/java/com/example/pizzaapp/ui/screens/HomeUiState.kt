package com.example.pizzaapp.ui.screens

import com.example.pizzaapp.R

data class HomeUiState(
    val pizzas: List<PizzaUiState> = emptyList(),
    val size: Size = Size.MEDIUM
)

data class PizzaUiState(
    val image: Int,
    val price: Int = 0,
    val ingredients: List<IngredientUiState> = emptyList()
)

data class IngredientUiState(
    val name: String = "",
    var isSelected: Boolean = false,
    val imageId : Int = 0
){
    fun getFullImageId(): Int{
        return when(imageId){
            R.drawable.basil_3 -> R.drawable.basil
            R.drawable.broccoli_1 -> R.drawable.broccoli
            R.drawable.onion_2 -> R.drawable.onion
            R.drawable.mushroom_6 -> R.drawable.mushroom
            R.drawable.sausage_1 -> R.drawable.sausage
            else -> R.drawable.sausage
        }
    }
}

data class PizzaIngredientUiState(
    val name: String = "",
    val imageId: Int
)


enum class Size{
    SMALL,
    LARGE,
    MEDIUM
}

fun IngredientUiState.toPizzaIngredient() = PizzaIngredientUiState(imageId = this.getFullImageId())

//R.drawable.basil_3,
//R.drawable.broccoli_1,
//R.drawable.onion_2,
//R.drawable.mushroom_6,
//R.drawable.sausage_1
