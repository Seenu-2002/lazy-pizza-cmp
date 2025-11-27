package com.seenu.dev.android.lazypizza.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.seenu.dev.android.lazypizza.data.repository.LazyPizzaCartRepository
import com.seenu.dev.android.lazypizza.data.repository.UserRepository
import com.seenu.dev.android.lazypizza.domain.util.PhoneNumberValidator
import com.seenu.dev.android.lazypizza.firebase.AuthListener
import com.seenu.dev.android.lazypizza.firebase.LazyPizzaAuth
import com.seenu.dev.android.lazypizza.presentation.design_system.OtpAction
import com.seenu.dev.android.lazypizza.presentation.state.OtpState
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@KoinViewModel
class LoginViewModel constructor(
    private val loginRepository: UserRepository,
    private val cartRepository: LazyPizzaCartRepository,
    private val phoneNumberValidator: PhoneNumberValidator
) : ViewModel() {

    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(
        LoginUiState()
    )
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private var auth: LazyPizzaAuth? = null

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.SetAuth -> {
                setAuth(intent.auth)
            }

            is LoginIntent.OnNumberChange -> {
                val number = if (intent.number.isNotEmpty() && intent.number[0] != '+') {
                    "+${intent.number}"
                } else {
                    intent.number
                }
                _loginUiState.value = _loginUiState.value.copy(
                    phoneNumber = phoneNumberValidator.format(number),
                    isNumberValid = phoneNumberValidator.isValid(intent.number)
                )
            }

            is LoginIntent.OnValidateOtp -> {
                viewModelScope.launch {
                    validateOtp(intent.otp, intent.verificationId)
                }
            }

            is LoginIntent.OnResendCodeClick -> {
                resendOtp()
            }

            LoginIntent.OnContinueAsGuestClick -> {
                loginAsGuest()
            }

            is LoginIntent.OnOtpAction -> {
                when (val action = intent.action) {
                    is OtpAction.OnEnterNumber -> {
                        val num = _loginUiState.value.otpState.code
                        val focusRequesters = _loginUiState.value.otpState.focusRequesters
                        val (value, index) = action
                        num[index] = value
                        if (index < 5 && value != null) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }

                    is OtpAction.OnFocused -> {
                        val num = _loginUiState.value.otpState.code
                        val focusRequesters = _loginUiState.value.otpState.focusRequesters
                        val focusedFieldIndex = action.index
                        if (num[focusedFieldIndex] != null) {
                            focusRequesters[focusedFieldIndex].requestFocus()
                            return
                        }

                        var hasEmpty = false
                        for ((index, num) in num.withIndex()) {
                            if (num == null) {
                                focusRequesters[index].requestFocus()
                                hasEmpty = true
                                break
                            }
                        }

                        if (!hasEmpty) {
                            focusRequesters[focusedFieldIndex].requestFocus()
                        }
                    }

                    is OtpAction.OnKeyboardBack -> {
                        val num = _loginUiState.value.otpState.code
                        val focusRequesters = _loginUiState.value.otpState.focusRequesters
                        val index = action.index
                        if (num[index] != null) {
                            num[index] = null
                        } else if (index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                }
            }

            is LoginIntent.OnLoginClick -> {
                login(intent.number)
            }
        }
    }

    private fun setAuth(auth: LazyPizzaAuth) {
        this.auth = auth
    }

    @OptIn(ExperimentalTime::class)
    private fun login(number: String) {
        val authListener = object : AuthListener {
            override fun onCodeAutoRetrievalTimeOut(str: String) {
                Logger.d { "Code auto-retrieval timed out for number: $number" }
            }

            override fun onCodeSent(verificationId: String) {
                Logger.d { "OTP sent to number: $number" }
                _loginUiState.value = _loginUiState.value.copy(
                    loginState = LoginState.OtpSent(
                        verificationId = verificationId,
                        sentAt = Clock.System.now().epochSeconds
                    ),
                    otpState = _loginUiState.value.otpState.copy(isLoading = false)
                )
            }

            override fun onVerificationCompleted(cred: PhoneAuthCredential) {
                Logger.d { "Phone verification completed for number: $number" }
                viewModelScope.launch {
                    cartRepository.syncCartItems()
                    _loginUiState.value = _loginUiState.value.copy(
                        loginState = LoginState.Success,
                        otpState = _loginUiState.value.otpState.copy(isLoading = false)
                    )
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Logger.e { "Phone authentication failed: ${e.message}" }
                _loginUiState.value = _loginUiState.value.copy(
                    loginState = LoginState.Error(e.message ?: "Verification failed"),
                    otpState = _loginUiState.value.otpState.copy(isLoading = false)
                )
            }
        }

        auth?.login(number, authListener)
        _loginUiState.value = _loginUiState.value.copy(
            otpState = _loginUiState.value.otpState.copy(isLoading = true)
        )
    }

    private suspend fun validateOtp(otp: String, verificationId: String) {
        _loginUiState.value = _loginUiState.value.copy(
            loginState = LoginState.ValidatingOtp
        )

        try {
            val user = auth?.getCredentials(verificationId, otp)
            if (user != null) {
                cartRepository.syncCartItems()
                _loginUiState.value = _loginUiState.value.copy(
                    loginState = LoginState.Success
                )
                Logger.d { "OTP validation successful for user: $user" }
            } else {
                _loginUiState.value = _loginUiState.value.copy(
                    loginState = LoginState.Error("Invalid OTP"),
                    otpState = _loginUiState.value.otpState.copy(
                        isLoading = false,
                        isValid = false
                    )
                )
                Logger.d { "OTP validation failed: Invalid OTP" }
            }
        } catch (exp: Exception) {
            _loginUiState.value = _loginUiState.value.copy(
                loginState = LoginState.Error(exp.message ?: "OTP validation failed"),
                otpState = _loginUiState.value.otpState.copy(
                    isLoading = false,
                    isValid = false
                )
            )
        }
    }

    private fun resendOtp() {
        val currentState = _loginUiState.value
        login(currentState.phoneNumber)
    }

    private fun loginAsGuest() {
        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(
                otpState = _loginUiState.value.otpState.copy(isLoading = true),
                loginState = LoginState.GuestLogin
            )
            val user = loginRepository.loginAsGuest()
            Logger.d { "Guest login successful: $user" }
            _loginUiState.value = _loginUiState.value.copy(
                loginState = LoginState.Success
            )
        }
    }
}

data class LoginUiState(
    val phoneNumber: String = "",
    val isNumberValid: Boolean = false,
    val loginState: LoginState = LoginState.Idle,
    val otpState: OtpState = OtpState()
)

sealed interface LoginState {
    data object Idle : LoginState
    data class OtpSent(
        val verificationId: String,
        val sentAt: Long
    ) : LoginState

    data object GuestLogin : LoginState
    data object ValidatingOtp : LoginState
    data object Success : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface LoginIntent {
    data class SetAuth(val auth: LazyPizzaAuth) : LoginIntent
    data class OnNumberChange(val number: String) : LoginIntent
    data class OnValidateOtp(val otp: String, val verificationId: String) : LoginIntent
    data class OnOtpAction(val action: OtpAction) : LoginIntent
    data object OnContinueAsGuestClick : LoginIntent
    data class OnLoginClick(val number: String) : LoginIntent
    data object OnResendCodeClick : LoginIntent
}