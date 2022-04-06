package fourtune.meeron.presentation.ui.common.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import fourtune.meeron.presentation.R

@Composable
fun TextGuideLineItem(title: String, text: String, guidelineFromStart: Float = 0.3f) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (left, right) = createRefs()
            val guideLine = createGuidelineFromStart(guidelineFromStart)
            Text(
                modifier = Modifier.constrainAs(left) {
                    start.linkTo(parent.start)
                },
                text = title,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray)
            )

            Text(
                modifier = Modifier.constrainAs(right) {
                    start.linkTo(guideLine)
                },
                text = text,
                fontSize = 14.sp,
                color = colorResource(id = R.color.dark_gray)
            )

        }
    }
}