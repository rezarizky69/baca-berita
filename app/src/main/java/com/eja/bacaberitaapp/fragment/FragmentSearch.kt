package com.eja.bacaberitaapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eja.bacaberitaapp.R
import com.eja.bacaberitaapp.adapter.NewsAdapter
import com.eja.bacaberitaapp.model.ModelArticle
import com.eja.bacaberitaapp.model.ModelNews
import com.eja.bacaberitaapp.networking.ApiEndpoint.getApiClient
import com.eja.bacaberitaapp.networking.ApiInterface
import com.eja.bacaberitaapp.util.Utils.getCountry
import kotlinx.android.synthetic.main.fragement_news.*
import kotlinx.android.synthetic.main.fragement_news.rvListNews
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentSearch : Fragment() {

    companion object{
        const val API_KEY = Your News API
    }

    var strKeywords: String = ""
    var modelArticle: MutableList<ModelArticle> = ArrayList()
    var newsAdapter: NewsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvListNews.layoutManager = LinearLayoutManager(context)
        rvListNews.setHasFixedSize(true)
        rvListNews.hideShimmerAdapter()
        imageClear.visibility = View.GONE
        linearNews.visibility = View.GONE

        imageClear.setOnClickListener {
            etSearchView.text.clear()
            modelArticle.clear()
            linearNews.visibility = View.GONE
            imageClear.visibility = View.GONE
        }

        //action search
        etSearchView.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                strKeywords = etSearchView.text.toString()
                if (strKeywords.isEmpty()) {
                    Toast.makeText(context, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                } else {
                    getListNews(strKeywords)
                }
                val inputManager =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getListNews(strKeywords: String) {
        rvListNews.showShimmerAdapter()
        modelArticle.clear()

        //set api
        val apiInterface = getApiClient().create(ApiInterface::class.java)
        val call = apiInterface.getNewsSearch(strKeywords, "id", API_KEY)
        call.enqueue(object : Callback<ModelNews> {
            override fun onResponse(call: Call<ModelNews>, response: Response<ModelNews>) {
                if (response.isSuccessful && response.body() != null) {
                    modelArticle = response.body()?.modelArticle as MutableList<ModelArticle>
                    newsAdapter = NewsAdapter(modelArticle, context!!)
                    rvListNews.adapter = newsAdapter
                    newsAdapter?.notifyDataSetChanged()
                    rvListNews.hideShimmerAdapter()
                    linearNews.visibility = View.VISIBLE
                    imageClear.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ModelNews>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}