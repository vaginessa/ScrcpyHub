@file:JvmName("DeviceCardKt")

package view.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import model.entity.Device
import model.repository.ProcessStatus
import view.pages.devices.DeviceStatus
import view.resource.MainTheme
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun DeviceCard(
    deviceStatus: DeviceStatus,
    startScrcpy: ((Device.Context) -> Unit),
    stopScrcpy: ((Device.Context) -> Unit),
    goToDetail: ((Device.Context) -> Unit),
    takeScreenshot: ((Device.Context) -> Unit),
    startRecording: ((Device.Context) -> Unit),
    stopRecording: ((Device.Context) -> Unit),
    modifier: Modifier = Modifier
) {
    val (currentTime, setCurrentTime) = remember { mutableStateOf("") }
    LaunchedEffect(deviceStatus.processStatus) {
        when (val processStatus = deviceStatus.processStatus) {
            is ProcessStatus.Recording -> {
                coroutineScope {
                    while (true) {
                        val elapsedTime = createElapsedTime(processStatus.startDate, Date())
                        setCurrentTime(elapsedTime)
                        delay(1000)
                    }
                }
            }

            is ProcessStatus.Running -> {
                coroutineScope {
                    while (true) {
                        val elapsedTime = createElapsedTime(processStatus.startDate, Date())
                        setCurrentTime(elapsedTime)
                        delay(1000)
                    }
                }
            }

            else -> {
                setCurrentTime("")
            }
        }
    }

    Card(elevation = 4.dp, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = deviceStatus.context.displayName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.weight(1.0f, true).align(Alignment.CenterVertically)
                )

                Text(
                    text = currentTime,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.wrapContentWidth().align(Alignment.CenterVertically)
                )

                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Screenshot",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { takeScreenshot.invoke(deviceStatus.context) }
                        .align(Alignment.CenterVertically)
                )
                Icon(
                    imageVector = Icons.Default.ModeEdit,
                    contentDescription = "ModeEdit",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { goToDetail.invoke(deviceStatus.context) }
                        .align(Alignment.CenterVertically)
                )
            }

            ScrcpyButtons(
                deviceStatus = deviceStatus,
                startScrcpy = startScrcpy,
                stopScrcpy = stopScrcpy,
                startRecording = startRecording,
                stopRecording = stopRecording
            )
        }
    }
}

@Preview
@Composable
private fun DeviceCard_Preview_DARK() {
    val device = Device("00001")
    val context1 = Device.Context(device)
    val context2 = Device.Context(device, "CUSTOM_NAME1")
    val context3 = Device.Context(device, "CUSTOM_NAME2")

    MainTheme(isDarkMode = true) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DeviceCard(
                deviceStatus = DeviceStatus(context1, ProcessStatus.Idle),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )

            DeviceCard(
                deviceStatus = DeviceStatus(context2, ProcessStatus.Running()),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )

            DeviceCard(
                deviceStatus = DeviceStatus(context3, ProcessStatus.Recording()),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )
        }
    }
}

private fun createElapsedTime(startDate: Date, currentTime: Date): String {
    val totalMillis = currentTime.time - startDate.time
    val hour = TimeUnit.MILLISECONDS.toHours(totalMillis)
    val minute = TimeUnit.MILLISECONDS.toMinutes(totalMillis) % 60
    val second = TimeUnit.MILLISECONDS.toSeconds(totalMillis) % 60 % 60
    val hourString = hour.toString().padStart(2, '0')
    val minuteString = minute.toString().padStart(2, '0')
    val secondString = second.toString().padStart(2, '0')
    return "$hourString:$minuteString:$secondString"
}

@Preview
@Composable
private fun DeviceCard_Preview_Light() {
    val device = Device("00001")
    val context1 = Device.Context(device)
    val context2 = Device.Context(device, "CUSTOM_NAME1")
    val context3 = Device.Context(device, "CUSTOM_NAME2")

    MainTheme(isDarkMode = false) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            DeviceCard(
                deviceStatus = DeviceStatus(context1, ProcessStatus.Idle),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )

            DeviceCard(
                deviceStatus = DeviceStatus(context2, ProcessStatus.Running()),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )

            DeviceCard(
                deviceStatus = DeviceStatus(context3, ProcessStatus.Recording()),
                startScrcpy = {},
                stopScrcpy = {},
                goToDetail = {},
                takeScreenshot = {},
                startRecording = {},
                stopRecording = {},
                modifier = Modifier
            )
        }
    }
}
