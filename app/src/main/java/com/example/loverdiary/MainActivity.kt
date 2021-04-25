package com.example.loverdiary
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainContract.View {

    //싱글톤
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    //코루틴 CoroutineScope 사용
    //쓰레드는 일반적으로 IO쓰레드와 Main 쓰레드가 있다. 안드로이드 환경에서는 IO는 백그라운드 잡을 말하고 Main에서는 UI thread
    // UI thread : 안드로이드는 UI 작업(업데이트)시에는 특정 UI Thread에서 사용하도록 되어있다. 그래서 UI Thread가 아닌 Thread에서 사용하면 오류가 발생한다.

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var presenter: MainPresenter //늦은 초기화 하기
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this) //초기화 하기

        rvNotes.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(this@MainActivity, RecyclerView.VERTICAL))
            adapter = mainAdapter
        }

        fabAdd.setOnClickListener {
            AddNotesActivity.start(this)
        }

        uiScope.launch {
            presenter.getAllNotes()
        }

    }

    override fun showNotes(data: List<Notes>) {
        mainAdapter.setData(data)
    }

    override fun showLoading() {
        pbMain.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbMain.visibility = View.GONE
    }

    override fun failed(error: String) {
        Log.d("TAG", "failed: $error")
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}
