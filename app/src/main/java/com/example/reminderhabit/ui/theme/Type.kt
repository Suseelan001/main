package com.example.reminderhabit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.reminderhabit.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)


var RobotoRegularWithHEX31394f18Sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_regular)),
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    letterSpacing = TextUnit.Unspecified,
    color = HEX31394f
)
var RobotoBoldWithHEX31394f18Sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_bold)),
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    letterSpacing = TextUnit.Unspecified,
    color = HEX31394f
)
var RobotoItalicWithHEX989ba214sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_italic)),
    fontWeight = FontWeight.Light,
    fontSize = 14.sp,
    letterSpacing = TextUnit.Unspecified,
    color = HEX989ba2
)

var RobotoMediumWithHEX31394f18sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_medium)),
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    letterSpacing = TextUnit.Unspecified,
    color = HEX31394f
)
var RobotoRegularWithHEX45454518sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto_regular)),
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    letterSpacing = TextUnit.Unspecified,
    color = HEX454545
)