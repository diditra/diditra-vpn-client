package com.diditra.vpn_client.ui

import android.Manifest
import android.content.*
import android.provider.Settings.Secure
import com.tbruyelle.rxpermissions.RxPermissions
import com.diditra.vpn_client.R
import com.diditra.vpn_client.util.AngConfigManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.diditra.vpn_client.extension.toast

class ScScannerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_none)
        importQRcode()
    }

    fun importQRcode(): Boolean {
        RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe {
                    if (it)
                        scanQRCode.launch(Intent(this, ScannerActivity::class.java))
                    else
                        toast(R.string.toast_permission_denied)
                }

        return true
    }

    private val scanQRCode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val deviceId = Secure.getString(contentResolver, Secure.ANDROID_ID);
            val count = AngConfigManager.importBatchConfig(it.data?.getStringExtra("SCAN_RESULT"), "", false, deviceId)
            if (count > 0) {
                toast(R.string.toast_success)
            } else {
                toast(R.string.toast_failure)
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
