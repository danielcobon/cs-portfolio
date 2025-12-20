package com.teamacupcake.secretdiaryapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsAndConditionsScreen() {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = "Terms of Service",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = """
                    Our Terms of service were last updated on 10th February 2024.

                    ${"\u2022"} Introduction:
                    ${"\u2022\u2022"} Welcome to “My Secret Diary”, a day-to-day activity tracker and organizer focused on journaling with collaborative features. By accessing or using our app, you agree to be bound by these Terms of Service (“Terms”).

                    ${"\u2022"} Use License:
                    ${"\u2022\u2022"} You are granted a non-exclusive, non-transferable license to use the app for personal, non-commercial use purposes. You agree to not use the app for any unlawful purpose or in a manner that violates these terms.

                    ${"\u2022"} User Responsibilities:
                    ${"\u2022\u2022"} You are responsible for maintaining the confidentiality of your account and password and for restricting access to your device. You agree to use biometric authentication, invisible ink, and other privacy features responsibly and ethically.

                    ${"\u2022"} Content Ownership and Copyright:
                    ${"\u2022\u2022"} The content you create within the app is owned by you. By using the app, you grant us a license to access and use this content for the purpose of providing and improving the app’s services.

                    ${"\u2022"} Prohibited Uses:
                    ${"\u2022\u2022"} You may not use the app to engage in any illegal, fraudulent, or harmful behaviour or to infringe upon the rights of others.

                    ${"\u2022"} Termination:
                    ${"\u2022\u2022"} We reserve the right to terminate or suspend access to our app immediately, without prior notice or liability, for any breach of these Terms.
                """.trimIndent(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            Text(
                text = "Privacy Policy",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = """
                    ${"\u2022"} Introduction:
                    ${"\u2022\u2022"} Your privacy is important to us. This privacy policy outlines how “My Secret Diary" collects, uses, maintains and discloses information collected from users.

                    ${"\u2022"} Information Collection and Use:
                    ${"\u2022\u2022"} Personal Data: we collect personal data like biometrics for authentication, GPS location for location-based entries, and speech for text transcription. This information is used to provide and improve our services. Usage Data: we collect data on how the service is accessed and uses. This data includes your device's internet protocol (IP) address, browser type, browser version, our service pages that you visit, the time and date of your visit, the time spent on those pages, unique device identifiers, and other diagnostic data.

                    ${"\u2022"} Consent:
                    ${"\u2022\u2022"} By using our app, you consent to the collection and use of information in accordance with this policy. You will be informed of any personal data collection and will have the option to consent or refuse.

                    ${"\u2022"} Data Storage and Security:
                    ${"\u2022\u2022"} We implement a variety of security measures to maintain the safety of your personal information.

                    ${"\u2022"} Your Data Protection Rights Under GDPR:
                    ${"\u2022\u2022"} You have the right to access, update, delete, or restrict the use of your personal data. We will provide the means for you to exercise these rights.

                    ${"\u2022"} Data Sharing and Disclosure:
                    ${"\u2022\u2022"} We do not sell, trade, or rent users personal identification information to others.

                    ${"\u2022"} Changes to this privacy policy:
                    ${"\u2022\u2022"} We have the discretion to update this privacy policy at any time. When we do, we will post a notification on the main page of our app.

                    ${"\u2022"} Contact us:
                    ${"\u2022\u2022"} If you have any questions about these terms or privacy policy, please contact us at cupcakedevteam@outlook.com.
                """.trimIndent(),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


