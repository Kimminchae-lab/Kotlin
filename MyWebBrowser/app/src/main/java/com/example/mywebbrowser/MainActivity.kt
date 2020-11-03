package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // region 웹뷰 기본 설정
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
        // endregion

        // 만든 ConTextMenu를 띄울 View를 WebView로 지정
        registerForContextMenu(webView)

        // region 기본: Google
        webView.loadUrl("https://www.google.com")
        // endregion

        // region EditText에 URL을 입력하고 이동
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) { // 키패드의 돋보기(SEARCH) 버튼을 눌렀을 때)
                webView.loadUrl(urlEditText.text.toString())
                true
            } else {
                false
            }
        }
        // endregion

        registerForContextMenu(webView)

    }

    // region 뒤로가기 클릭 이벤트 메소드
    override fun onBackPressed() {
        if (webView.canGoBack()) { // 이전에 방문한 페이지가 있다면
            webView.goBack()
        } else { // 이전에 방문한 페이지가 없다면
            super.onBackPressed()
        }
    }
    // endregion

    // region 메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    // endregion

    // region 옵션 메뉴 선택 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("https://www.daum.net")
                return true
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                sendSMS("031-123-4567")
                return true
            }
            R.id.action_email -> {
                webView.url?.let { email("test@example.com", "좋은 사이트", it) }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // endregion

    // region 컨텍스트 메뉴 작성
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }
    // endregion

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                webView.url?.let { share(it) }
                return true
            }
            R.id.action_browser -> {
                webView.url?.let { browse(it) }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
