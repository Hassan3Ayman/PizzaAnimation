package com.example.pizzaapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.pizzaapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        initHome()
    }

    fun onChangeSize(size: Size) {
        _state.update { it.copy(size = size) }
    }

    fun onIngredientClicked(currentPizza: Int, currentIngredient: Int, isSelected: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                pizzas = currentState.pizzas.toMutableList().apply {
                    val currentPizzaState = this[currentPizza]
                    val currentIngredients = currentPizzaState.ingredients.toMutableList()
                    val updatedIngredient = currentIngredients[currentIngredient].copy(isSelected = isSelected)
                    currentIngredients[currentIngredient] = updatedIngredient
                    this[currentPizza] = currentPizzaState.copy(ingredients = currentIngredients)
                }
            )
        }
    }


    private fun initHome() {
        _state.update { it.copy(pizzas = getInitPizzas())}
    }



     private fun getInitPizzas(): List<PizzaUiState> {
        return listOf(
            PizzaUiState(R.drawable.bread_1, ingredients = getInitIngredients()),
            PizzaUiState(R.drawable.bread_2, ingredients = getInitIngredients()),
            PizzaUiState(R.drawable.bread_3, ingredients = getInitIngredients()),
            PizzaUiState(R.drawable.bread_4, ingredients = getInitIngredients()),
            PizzaUiState(R.drawable.bread_5, ingredients = getInitIngredients()),
        )
    }

    private fun getInitIngredients(): List<IngredientUiState> {
        return listOf(
            IngredientUiState(imageId = R.drawable.basil_3),
            IngredientUiState(imageId = R.drawable.broccoli_1),
            IngredientUiState(imageId = R.drawable.onion_2),
            IngredientUiState(imageId = R.drawable.mushroom_6),
            IngredientUiState(imageId = R.drawable.sausage_1)
        )
    }
}
