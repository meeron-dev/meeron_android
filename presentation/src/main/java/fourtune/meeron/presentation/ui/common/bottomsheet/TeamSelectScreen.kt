package fourtune.meeron.presentation.ui.common.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MerronButton

@Composable
fun TeamSelectScreen() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height((LocalConfiguration.current.screenHeightDp * 0.9).dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 46.dp),
                text = stringResource(R.string.team_select_title),
                fontSize = 18.sp,
                color = colorResource(id = R.color.dark_gray)
            )
        }
        MerronButton(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter),
            {},
            {}
        )
    }

}