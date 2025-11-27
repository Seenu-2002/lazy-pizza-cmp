package com.seenu.dev.android.lazypizza.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seenu.dev.android.lazypizza.firebase.LazyPizzaAuth
import com.seenu.dev.android.lazypizza.presentation.design_system.LazyPizzaTextButton
import com.seenu.dev.android.lazypizza.presentation.design_system.LoginNumberCard
import com.seenu.dev.android.lazypizza.presentation.navigation.Route
import com.seenu.dev.android.lazypizza.presentation.theme.body3Regular
import com.seenu.dev.android.lazypizza.presentation.theme.surfaceHighest
import com.seenu.dev.android.lazypizza.presentation.theme.textSecondary
import com.seenu.dev.android.lazypizza.presentation.theme.title1Medium
import com.seenu.dev.android.lazypizza.presentation.theme.title3
import kotlinx.coroutines.delay
import lazypizza.composeapp.generated.resources.Res
import lazypizza.composeapp.generated.resources.btn_confirm
import lazypizza.composeapp.generated.resources.btn_continue
import lazypizza.composeapp.generated.resources.continue_as_guest
import lazypizza.composeapp.generated.resources.enter_phone
import lazypizza.composeapp.generated.resources.request_new_code
import lazypizza.composeapp.generated.resources.resend_code
import lazypizza.composeapp.generated.resources.welcome_msg
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun LoginScreen(
    auth: LazyPizzaAuth,
    onLoginSuccess: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()
    val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(LoginIntent.SetAuth(auth))
    }

    LaunchedEffect(loginUiState.loginState) {
        when (loginUiState.loginState) {
            LoginState.Success -> {
                onLoginSuccess()
            }

            is LoginState.Error -> {
                // Handle error state
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .widthIn(max = 450.dp)
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.welcome_msg),
                style = MaterialTheme.typography.title1Medium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.enter_phone),
                style = MaterialTheme.typography.body3Regular
            )
            Spacer(modifier = Modifier.height(20.dp))

            val isInOtpMode =
                loginUiState.loginState is LoginState.OtpSent || loginUiState.loginState == LoginState.ValidatingOtp

            LoginNumberCard(
                number = loginUiState.phoneNumber,
                otpState = loginUiState.otpState,
                onValueChange = {
                    viewModel.onIntent(LoginIntent.OnNumberChange(it))
                },
                isInOtpValidationMode = isInOtpMode,
                onOtpAction = { action ->
                    viewModel.onIntent(LoginIntent.OnOtpAction(action))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            val enableButton = if (isInOtpMode) {
                loginUiState.otpState.code.filterNotNull().size == 6
            } else {
                true
            }

            LaunchedEffect(loginUiState.loginState) {
                if (loginUiState.loginState is LoginState.OtpSent) {
                    val focusIndex = loginUiState.otpState.code.indexOfFirst { it == null }
                    if (focusIndex != -1) {
                        loginUiState.otpState.focusRequesters[focusIndex].requestFocus()
                    }
                }
            }

            val isLoading = loginUiState.otpState.isLoading ||
                    loginUiState.loginState == LoginState.GuestLogin

            LazyPizzaTextButton(
                enabled = enableButton && loginUiState.isNumberValid,
                onClick = {
                    if (isLoading) return@LazyPizzaTextButton

                    val intent = if (isInOtpMode) {
                        val otpState = loginUiState.loginState as LoginState.OtpSent
                        LoginIntent.OnValidateOtp(
                            otp = loginUiState.otpState.code.joinToString(separator = ""),
                            verificationId = otpState.verificationId
                        )
                    } else {
                        LoginIntent.OnLoginClick(number = loginUiState.phoneNumber)
                    }
                    viewModel.onIntent(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.surfaceHighest
                    )
                } else {
                    val text = if (isInOtpMode) {
                        stringResource(Res.string.btn_confirm)
                    } else {
                        stringResource(Res.string.btn_continue)
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.continue_as_guest),
                style = MaterialTheme.typography.title3,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.onIntent(LoginIntent.OnContinueAsGuestClick)
                    }
                    .padding(8.dp)
            )

            if (loginUiState.loginState is LoginState.OtpSent) {
                Spacer(modifier = Modifier.height(12.dp))

                val requestAgainInSeconds = 60
                val otpSentState = loginUiState.loginState as LoginState.OtpSent
                val otpSentAt = otpSentState.sentAt

                var secondsLeft by remember(otpSentAt) {
                    mutableIntStateOf(
                        requestAgainInSeconds - ((Clock.System.now()
                            .toEpochMilliseconds() / 1000 - otpSentAt)).toInt()
                    )
                }

                if (secondsLeft > 0) {
                    LaunchedEffect(Unit) {
                        while (secondsLeft > 0) {
                            delay(1000)
                            secondsLeft -= 1
                        }
                    }
                    RequestOtpDurationText(
                        timeRemaining = secondsLeft,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.resend_code),
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            viewModel.onIntent(LoginIntent.OnResendCodeClick)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun RequestOtpDurationText(
    timeRemaining: Int,
    modifier: Modifier = Modifier
) {

    val formatted =
        "00:".let { if (timeRemaining > 10) it + timeRemaining else "$it:0$timeRemaining" }

    Text(
        text = stringResource(Res.string.request_new_code, formatted),
        modifier = modifier,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.textSecondary,
        style = MaterialTheme.typography.body3Regular,
        textAlign = TextAlign.Center
    )
}

