import androidx.compose.ui.window.ComposeUIViewController
import compose.table.demo.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
