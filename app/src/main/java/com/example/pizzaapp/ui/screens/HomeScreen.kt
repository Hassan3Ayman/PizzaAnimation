package com.example.pizzaapp.ui.screens

import android.widget.ToggleButton
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pizzaapp.R
import com.example.pizzaapp.ui.theme.SelectedItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    HomeContent(
        state = state,
        onChangeSize = viewModel::onChangeSize,
        onClickIngredient = viewModel::onIngredientClicked
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeContent(
    state: HomeUiState,
    onChangeSize: (Size) -> Unit,
    onClickIngredient: (Int, Int, Boolean) -> Unit
) {
    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBar()
        DishPager(state.size, onChangeSize, state.pizzas, pagerState = pagerState)
        Spacer(modifier = Modifier.height(32.dp))
        PizzaIngredients(
            ingredients = state.pizzas[pagerState.currentPage].ingredients,
            currentPizza = pagerState.currentPage,
            onClickIngredient = onClickIngredient
        )
        Spacer(modifier = Modifier.fillMaxHeight(.5f))
        BuyButton()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DishPager(
    size: Size,
    onChangeSize: (Size) -> Unit,
    pizzas: List<PizzaUiState>,
    pagerState: PagerState
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
    ) {
        Plate(modifier = Modifier.align(Alignment.Center))
        Pager(
            size = size,
            modifier = Modifier.align(Alignment.Center),
            pizzas = pizzas,
            pagerState = pagerState
        )
        SizesRow(
            selectedSize = size,
            onChangeSize = onChangeSize,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Plate(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .fillMaxSize(.6f)
            .aspectRatio(matchHeightConstraintsFirst = true, ratio = 1.01f),
        painter = painterResource(id = R.drawable.plate),
        contentDescription = "plate",
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun Pager(
    modifier: Modifier = Modifier,
    size: Size,
    pizzas: List<PizzaUiState>,
    pagerState: PagerState
) {

    val pizzaSize by animateFloatAsState(
        targetValue = when (size) {
            Size.SMALL -> 0.45f
            Size.LARGE -> 0.55f
            Size.MEDIUM -> 0.5f
        }
    )
    HorizontalPager(
        modifier = modifier
            .fillMaxSize(),
        count = pizzas.size,
        state = pagerState
    ) { page ->
        Box {
            Image(
                modifier = Modifier.fillMaxSize(pizzaSize),
                painter = painterResource(id = pizzas[page].image),
                contentDescription = ""
            )

            pizzas[page].ingredients.forEach {
                AnimatedVisibility(
                    visible = it.isSelected,
                    enter = scaleIn(initialScale = 80f),
                    exit = ExitTransition.None
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize(pizzaSize)
                            .align(Alignment.Center),
                        painter = painterResource(id = it.getFullImageId()),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun SizeButton(letter: String, size: Size, selectedSize: Size, onChangeSize: (Size) -> Unit) {

    val buttonElevation by animateDpAsState(targetValue = if (size == selectedSize) 5.dp else 0.dp)

    Button(
        modifier = Modifier
            .size(64.dp),
        onClick = { onChangeSize(size) },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(
            text = letter,
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun SizesRow(
    modifier: Modifier = Modifier,
    selectedSize: Size,
    onChangeSize: (Size) -> Unit
) {

    val offsetAnimation: Dp by animateDpAsState(
        when (selectedSize) {
            Size.SMALL -> 5.dp
            Size.MEDIUM -> 80.dp
            Size.LARGE -> 150.dp
            else -> 0.dp
        }, spring(stiffness = Spring.StiffnessLow), label = ""
    )

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .fillMaxSize()
                .absoluteOffset(x = offsetAnimation)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.CenterStart)
        )

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        )
        {
            SizeButton("S", Size.SMALL, selectedSize = selectedSize, onChangeSize)
            Spacer(modifier = Modifier.width(8.dp))
            SizeButton("M", Size.MEDIUM, selectedSize = selectedSize, onChangeSize)
            Spacer(modifier = Modifier.width(8.dp))
            SizeButton("L", Size.LARGE, selectedSize = selectedSize, onChangeSize)
        }
    }
}


@Composable
fun PizzaIngredients(
    ingredients: List<IngredientUiState>,
    currentPizza: Int,
    onClickIngredient: (Int, Int, Boolean) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = ingredients.size) {
            PizzaIngredient(
                ingredient = ingredients[it],
                index = it,
                currentPizza = currentPizza,
                onclick = onClickIngredient
            )
        }
    }
}

@Composable
fun PizzaIngredient(
    ingredient: IngredientUiState,
    currentPizza: Int,
    onclick: (Int, Int, Boolean) -> Unit,
    index: Int
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (ingredient.isSelected) SelectedItem else Color.Transparent, tween(300)
    )
    Image(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onclick(currentPizza, index, !ingredient.isSelected) }
            .padding(16.dp),
        painter = painterResource(id = ingredient.imageId),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun AppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")

        Text(
            text = "Pizza",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_favorite),
            contentDescription = "",
            tint = Color.DarkGray
        )
    }
}

@Composable
fun BuyButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth(.4f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_buy),
            contentDescription = "",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Add to Cart", color = Color.White, fontSize = 14.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun SizeRowPreview() {
    HomeScreen()
}