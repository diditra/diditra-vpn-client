package com.diditra.vpn_client.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.zxing.WriterException
import com.diditra.vpn_client.R
import com.diditra.vpn_client.databinding.ActivityLogcatBinding
import com.diditra.vpn_client.extension.toast
import com.diditra.vpn_client.util.AngConfigManager

class UrlSchemeActivity : BaseActivity() {
    private lateinit var binding: ActivityLogcatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogcatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var shareUrl: String = ""
        try {
            intent?.apply {
                when (action) {
                    Intent.ACTION_SEND -> {
                        if ("text/plain" == type) {
                            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                                shareUrl = it
                            }
                        }
                    }
                    Intent.ACTION_VIEW -> {
                        val uri: Uri? = intent.data
                        shareUrl = uri?.getQueryParameter("url")!!
                    }
                }
            }
            toast(shareUrl)
            val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
            val count = AngConfigManager.importBatchConfig(shareUrl, "", false, deviceId)
            if (count > 0) {
                toast(R.string.toast_success)
            } else {
                toast(R.string.toast_failure)
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}