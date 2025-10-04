package com.seenu.dev.android.lazypizza.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.instrument_sans_bold
import lazypizza.composeapp.generated.resources.instrument_sans_medium
import lazypizza.composeapp.generated.resources.instrument_sans_regular
import lazypizza.composeapp.generated.resources.instrument_sans_semibold
import org.jetbrains.compose.resources.Font

val InstrumentSans: FontFamily
    @Composable get() = FontFamily(
        Font(
            resource = Res.font.instrument_sans_bold,
            weight = FontWeight.Bold
        ),
        Font(
            resource = Res.font.instrument_sans_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.instrument_sans_semibold,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = Res.font.instrument_sans_regular,
            weight = FontWeight.Normal
        )
    )

val LazyPizzaTypography: Typography
    @Composable get() = Typography()

val Typography.title1SemiBold
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp
    )

val Typography.title2
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )

val Typography.title3
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )

val Typography.label2
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )

val Typography.body1Regular
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )

val Typography.body1Medium
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )

val Typography.body3Regular
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val Typography.body3Medium
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

val Typography.body4Regular
    @Composable get() = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )