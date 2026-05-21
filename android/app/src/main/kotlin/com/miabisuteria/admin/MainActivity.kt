package com.miabisuteri.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miabisuteri.admin.ui.navigation.MiaNavGraph
import com.miabisuteri.admin.ui.theme.MiaAdminTheme
import com.miabisuteri.admin.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiaAdminTheme {
                MiaNavGraph(sessionManager = sessionManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sessionManager.onAppForegrounded()
    }
}
