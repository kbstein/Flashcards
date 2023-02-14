package com.example.flashcards

import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flashcards.ui.theme.FlashCardsTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun App() {
    FlashCardsTheme() {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            Card()
        }
    }
}

@Composable
fun Card() {
    val flashCards = TheFlashCards.createCards()
    val rotation = remember { androidx.compose.animation.core.Animatable(0f) }
    val offset = remember { androidx.compose.animation.core.Animatable(0f) }
    var currentCardIndex by remember { mutableStateOf(0) }
    var currentText by remember { mutableStateOf(flashCards[currentCardIndex].frontSide) }
    var onFrontSide = true
    var toClamp = 0f
    var offsetX by remember { mutableStateOf(0f)}
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(modifier = Modifier
            .size(300.dp, 500.dp)
            .graphicsLayer {
                rotationY = rotation.value
                offsetX = offset.value
            }
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .pointerInput(Unit) {
                coroutineScope {
                    while (true) {
                        // Freezes animation when user touches
                        val pointerId = awaitPointerEventScope {
                            awaitFirstDown().id
                        }
                        rotation.stop()
                        awaitPointerEventScope {
                            horizontalDrag(pointerId) {
                                launch {
                                    if (onFrontSide) {
                                        if (it.positionChange().x > 0f  && currentCardIndex > 0) {
                                            toClamp = 0f
                                            rotation.snapTo(toClamp)
                                            offsetX += it.positionChange().x
                                        } else {
                                            toClamp =
                                                clampOnFront(rotation.value + (it.positionChange().x / 5f))
                                        }
                                    } else {
                                        if (it.positionChange().x < 0f) {
                                            toClamp = 0f
                                            rotation.snapTo(toClamp)
                                            offsetX += it.positionChange().x
                                        } else {
                                            toClamp =
                                                clampOnBack(rotation.value + (it.positionChange().x / 5f))
                                        }
                                    }
                                    if (toClamp > 90f && !onFrontSide) {
                                        currentText = flashCards[currentCardIndex].frontSide
                                        toClamp = -89f
                                        onFrontSide = !onFrontSide
                                    }
                                    if (toClamp < -90f && onFrontSide) {
                                        currentText = flashCards[currentCardIndex].backSide
                                        toClamp = 89f
                                        onFrontSide = !onFrontSide
                                    }
                                    if (toClamp != 0f) {
                                        rotation.snapTo(
                                            toClamp
                                        )
                                    }
                                }
                            }
                        }
                        // User stopped dragging
                        launch {
                            if (rotation.value != 0f) {
                                rotation.animateTo(
                                    0f,
                                    animationSpec = spring(1.5f, 500f),
                                    initialVelocity = if (rotation.value > 0) -500f else 500f
                                )
                            }
                            if (onFrontSide && currentCardIndex > 0) {
                                if ((offsetX / 5) < 25f) {
                                    offset.animateTo(
                                        0f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                } else {
                                    offset.animateTo(
                                        1000f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                    currentCardIndex -= 1
                                    currentText = flashCards[currentCardIndex].frontSide
                                    offset.snapTo(-1000f)
                                    offset.animateTo(
                                        0f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                }
                            }
                            if (!onFrontSide && currentCardIndex < 25) {
                                if ((offsetX / 5) > -25f) {
                                    offset.animateTo(
                                        0f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                } else {
                                    offset.animateTo(
                                        -1000f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                    currentCardIndex += 1
                                    currentText = flashCards[currentCardIndex].frontSide
                                    onFrontSide = true
                                    offset.snapTo(1000f)
                                    offset.animateTo(
                                        0f,
                                        animationSpec = spring(1.5f, 500f),
                                        initialVelocity = if (offset.value > 0) -500f else 500f
                                    )
                                }
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(15.dp),
            color = Color.Red
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SurfaceText(theText = currentText)
            }
        }
    }
}

fun clampOnFront(value: Float): Float {
    if (value > 1) return 1f
    return value
}

fun clampOnBack(value: Float): Float {
    if (value < -1) return -1f
    return value
}

@Composable
fun SurfaceText(theText: String) {
    Text(text = theText, fontSize = 25.sp, fontWeight = FontWeight(400), color = Color.White)
}


