package com.example.loverdiary.main
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
import com.example.loverdiary.*
import com.example.loverdiary.addnotes.AddNotesActivity
import com.example.loverdiary.data.Notes
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),
    MainContract.View {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

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
