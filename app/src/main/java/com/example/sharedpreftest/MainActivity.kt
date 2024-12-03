package com.example.sharedpreftest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.startup.Initializer
import com.example.sharedpreftest.coroutines.WellDispatcher
import com.example.sharedpreftest.coroutines.WellDispatcherImpl
import com.example.sharedpreftest.storage.LocalStorageRepository
import com.example.sharedpreftest.storage.LocalStorageRepositoryImpl
import com.example.sharedpreftest.storage.PREF_DEEPLINK
import com.example.sharedpreftest.ui.theme.SharedPrefTestTheme
import com.google.firebase.perf.metrics.AddTrace
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedPrefTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: HomeViewModel = koinViewModel()
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = viewModel.resultText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(24.dp),
        )
    }
}

class HomeViewModel(
    private val localStorageRepository: LocalStorageRepository,
) : ViewModel() {
    var resultText by mutableStateOf("Calculating...")

    init {
        verifyNextScreen()
    }

    private fun verifyNextScreen() {
        viewModelScope.launch {
            Timer.start()
            for (i in 1..10) {
                localStorageRepository.getString(PREF_DEEPLINK, coroutineContext)
            }
            val first = Timer.log("10 times with default dispatcher")
            for (i in 1..10) {
                localStorageRepository.getString(PREF_DEEPLINK)
            }
            val second = Timer.log("10 times with io dispatcher")

            val result = "VM scope context took $first, but io dispatcher took $second"
            Log.e("asd", "--------------RESULT---------------")
            Log.e("asd", result)
            Log.e("asd", "-----------------------------------")
            resultText = result
        }
    }
}

val appModule =
    module {
        val SHARED_PREFERENCES = "com.example.sharedpreftest.shared_preferences_key"
        single<LocalStorageRepository> {
            LocalStorageRepositoryImpl(
                preferences =
                    androidContext().getSharedPreferences(
                        SHARED_PREFERENCES,
                        Context.MODE_PRIVATE,
                    ),
                wellDispatcher = get(),
            )
        }

        single<WellDispatcher> { WellDispatcherImpl() }

        viewModel {
            HomeViewModel(
                get(),
            )
        }
    }

class KoinInitializer : Initializer<Unit> {
    @AddTrace(name = "initializer_koin")
    override fun create(context: Context) {
        startKoin {
            androidContext(context)
            modules(appModule)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

object Timer {
    var start = System.currentTimeMillis()
    private var allStart = 0L

    fun start() {
        Log.e("asd", "timer started")
        start = System.currentTimeMillis()
        allStart = start
    }

    fun log(code: String): Int {
        val elapsed = (System.currentTimeMillis() - start).toInt()
        val total = (System.currentTimeMillis() - allStart).toInt()
        start = System.currentTimeMillis()
        Log.e("asd", "$code took: $elapsed, total: $total")
        return elapsed
    }
}
