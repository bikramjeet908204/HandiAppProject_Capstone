import android.app.AlertDialog
import android.content.Context

class AlertDialogHelper constructor(val context: Context) {

    fun create(
        title: String = "",
        message: String = "",
        cancellable: Boolean = false
    ): AlertDialog.Builder {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(cancellable)
    }
}