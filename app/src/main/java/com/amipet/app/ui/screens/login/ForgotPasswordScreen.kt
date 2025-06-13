package com.amipet.app.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amipet.app.R
import com.amipet.app.ui.navigation.Screen
import com.amipet.app.viewmodel.AuthViewModel
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val forgotPasswordState by viewModel.forgotPasswordState.collectAsState()

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE2F986), Color(0xFFDCF64F))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated form card
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 600)) + fadeIn(),
            exit = fadeOut(animationSpec = tween(durationMillis = 600)) + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title
                    Text(
                        text = "Recuperar Senha",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                text = stringResource(R.string.label_email),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.DarkGray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Send Button with Animation
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                        exit = scaleOut(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    ) {
                        Button(
                            onClick = { viewModel.forgotPassword(email) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            enabled = email.isNotEmpty(),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 8.dp,
                                pressedElevation = 12.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8AE548),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            when (forgotPasswordState) {
                                is AuthViewModel.ForgotPasswordState.Loading -> CircularProgressIndicator(
                                    color = Color.White,
                                )
                                else -> Text(
                                    text = "Enviar",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Error Feedback
                    if (forgotPasswordState is AuthViewModel.ForgotPasswordState.Error) {
                        Text(
                            text = (forgotPasswordState as AuthViewModel.ForgotPasswordState.Error).message,
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        )
                    }

                    // Navigation Link
                    Text(
                        text = stringResource(R.string.login),
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .clickable {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.ForgotPassword.route) { inclusive = false }
                                }
                            }
                    )
                }
            }
        }
    }

    // Navigate on Success
    if (forgotPasswordState is AuthViewModel.ForgotPasswordState.Success) {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.ForgotPassword.route) { inclusive = true }
        }
    }
}