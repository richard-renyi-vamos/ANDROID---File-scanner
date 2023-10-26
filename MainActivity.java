import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class FileScannerActivity : AppCompatActivity() {

    private val permissionCode = 101
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.fileListView)

        // Check for permission to read external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                permissionCode
            )
        } else {
            // Permission already granted, perform file scanning
            scanFiles(Environment.getExternalStorageDirectory())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionCode && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, perform file scanning
            scanFiles(Environment.getExternalStorageDirectory())
        }
    }

    private fun scanFiles(directory: File) {
        val largestFiles = directory.listFiles().asSequence()
            .filter { it.isFile }
            .sortedByDescending { it.length() }
            .take(10) // Change this number to display a different number of files

        val fileNames = largestFiles.map { it.name }.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileNames)
        listView.adapter = adapter
    }
}
