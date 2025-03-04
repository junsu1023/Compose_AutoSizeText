package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                AutoSizeTextView()
            }
        }
    }
}

@Composable
fun AutoSizeTextView() {
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .height(100.dp)
          .background(colorResource(R.color.beige)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var index by remember { mutableIntStateOf(1) }
        val dummyText = getDummyText()
        var text by remember { mutableStateOf(dummyText[0]) }

        LaunchedEffect(index) {
            text = dummyText[index]
        }

        val textSizeRange = FontSizeRange(min = 10.sp, max = 40.sp)
        Spacer(modifier = Modifier.width(10.dp))

        AutoSizeText(
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
                .background(colorResource(R.color.sage))
                .padding(horizontal = 10.dp),
            text = text,
            textStyle = TextStyle(
                fontSize = textSizeRange.max,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            textSizeRange = textSizeRange
        )

        Spacer(modifier = Modifier.width(10.dp))

        Image(
            modifier = Modifier
                .wrapContentSize()
                .padding(end = 10.dp)
                .clickable(
                    enabled = true,
                    onClick = {
                        index = (index + 1) % 3
                    }
                ),
            painter = painterResource(R.drawable.next),
            contentDescription = null
        )
    }
}

@Composable
fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    maxLines: Int = 2,
    textSizeRange: FontSizeRange
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val measurer = rememberTextMeasurer()
        val density = LocalDensity.current
        var textSize = with(density) { textSizeRange.max.toDp() }

        with(density) {
            var textWidth = measurer.measure(text, textStyle).size.width.toDp()

            while(textWidth > maxWidth) {
                textSize -= textSizeRange.step.toDp()
                textWidth = measurer.measure(text, textStyle.copy(fontSize = textSize.toSp())).size.width.toDp()
            }
        }

        Text(
            modifier = Modifier.wrapContentHeight(),
            text = text,
            style = textStyle.copy(fontSize = with(density) { textSize.toSp() }),
            maxLines = maxLines,
        )
    }
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_SIZE_STEP
) {
    companion object {
        private val DEFAULT_TEXT_SIZE_STEP = 2.sp
    }
}