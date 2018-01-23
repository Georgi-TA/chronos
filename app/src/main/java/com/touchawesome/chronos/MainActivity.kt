package com.touchawesome.chronos

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val REQUEST_READ_CALL_LOG = 15151
    private val data = ArrayList<CallLogEntry>()
    private val adapter = CallLogAdapter(data)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        call_log_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        call_log_list.adapter = adapter
        call_log_list.setHasFixedSize(true)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // request the permission.
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_READ_CALL_LOG)
        }
        else {
            loadCallLog()
        }
    }

    private fun loadCallLog() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            data.clear()

            val c = contentResolver.query(CallLog.Calls.CONTENT_URI, arrayOf(CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE), null, null, null)
            if (c.moveToFirst()) {
                for (i in 0..100) {
                    val item = CallLogEntry(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)),
                            c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)),
                            null,
                            c.getLong(c.getColumnIndex(CallLog.Calls.DATE)))

                    // Query for Photo if possible
                    val contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(item.number))

                    // querying contact data store
                    val phones = contentResolver.query(contactUri, arrayOf(ContactsContract.PhoneLookup.PHOTO_URI), null, null, null)
                    item.photo = Uri.parse(phones.getString(phones.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI)))
                    phones.close()

                    data.add(item)

                    // move to the next call log
                    c.moveToNext()
                    if (c.isAfterLast)
                        break
                }
            }
            c.close()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_READ_CALL_LOG -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadCallLog()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
