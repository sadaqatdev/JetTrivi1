package com.example.jettrivi.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.jettrivi.model.QuestionItem
import com.example.jettrivi.utils.AppColors


@Composable
fun TriviaHome(viewModel: QuestionViewModel?) {

    val data = viewModel?.data!!.value

    val questionList = data.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (data.isLoading!!) {
        CircularProgressIndicator()
    } else {
        if (questionList != null) {
            Log.d("TriviaHome", "TriviaHome question: ${questionList!!.first()}")

            val question = try {
                questionList.get(questionIndex.value)
            } catch (e: Exception) {
                null
            }
            if (question != null) {
                QuestionDisplay(
                    question = question,
                    onNextClicked = {

                        questionIndex.value = it + 1

                    },
                    viewModel = viewModel,
                    questionIndex = questionIndex
                )
            }


        }
    }

    Log.d("APP TAG", data.isLoading.toString())

    Log.d("APP TAG", questionList.toString())

}

@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionViewModel?,
    onNextClicked: (Int) -> Unit = {}
) {


    val choosesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {

        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choosesState[it] == question.answer
        }
    }

    val pathEffect =
        androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            ShowProgress(score = questionIndex.value)

            QuestionTracker(questionIndex.value, 100)

            DrawDotedLine(pathEffect)

            Column {

                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                choosesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        val anaotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red

                                    } else {
                                        AppColors.mOffWhite
                                    },
                                    fontSize = 17.sp
                                )
                            ) {
                                append(answerText)
                            }
                        }
                        Text(text = anaotatedString, modifier = Modifier.padding(6.dp))
                    }

                }

                Button(
                    onClick = {
                        onNextClicked(questionIndex.value)
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.mLightBlue)
                ) {

                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp,
                    )

                }


            }

        }
    }

}


@Composable
fun DrawDotedLine(pathEffect: androidx.compose.ui.graphics.PathEffect) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(10.dp), onDraw = {
        drawLine(
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            color = Color.Green
        )
    })
}

@Preview(showBackground = true)
@Composable
fun DotedLinePreview() {

    val pathEffect =
        androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    DrawDotedLine(pathEffect = pathEffect)
}


@Preview
@Composable
fun QuestionTracker(counter: Int = 1, outOff: Int = 0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = AppColors.mDarkPurple,
    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(text = buildAnnotatedString {
                withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                    withStyle(
                        style = SpanStyle(
                            color = AppColors.mLightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 27.sp
                        )
                    ) {
                        append("Question $counter/")
                        withStyle(
                            style = SpanStyle(
                                color = AppColors.mLightGray,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        ) {
                            append("$outOff")
                        }
                    }
                }
            })


        }

    }
}

@Composable
fun ShowProgress(score: Int = 12) {

    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )


    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)

    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(40.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50, topEndPercent = 50,
                    bottomEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}






