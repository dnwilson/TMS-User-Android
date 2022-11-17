package com.takemysigns.takemysigns.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.takemysigns.takemysigns.R

val openSans = FontFamily(
    Font(R.font.open_sans_regular),
    Font(R.font.open_sans_bold, FontWeight.Bold),
)

val poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = openSans,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    h1 = TextStyle(
        fontFamily = poppins,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    h2 = TextStyle(
        fontFamily = poppins,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    h3 = TextStyle(
        fontFamily = poppins,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    h4 = TextStyle(
        fontFamily = poppins,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    h5 = TextStyle(
        fontFamily = poppins,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    h6 = TextStyle(
        fontFamily = poppins,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
)