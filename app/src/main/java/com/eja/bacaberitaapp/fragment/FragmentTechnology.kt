package com.eja.bacaberitaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentTechnology : Fragment() {

    companion object{
        const val API_KEY = Your News API
    }

    var strCategory = "technology"
    var strCountry: String? = null
    var modelArticle: MutableList<ModelArticle> = ArrayList()
    var newsAdapter : NewsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = "Baca Berita - Teknologi"

        rvListNews.layoutManager = LinearLayoutManager(context)
        rvListNews.setHasFixedSize(true)
        rvListNews.showShimmerAdapter()

        imageRefresh.setOnClickListener {
            rvListNews.showShimmerAdapter()

            getListNews()
        }

        getListNews()


    }

    private fun getListNews(){

        // set negara yang ingin di dapatkan beritanya
        strCountry = getCountry()

        val apiInterface = getApiClient().create(ApiInterface::class.java)
        val call = apiInterface.getTechnology(strCountry, strCategory, API_KEY)
        call.enqueue(object : Callback<ModelNews>{
            override fun onResponse(call: Call<ModelNews>, response: Response<ModelNews>) {
                if (response.isSuccessful && response.body() != null){
                    modelArticle = response.body()?.modelArticle as MutableList<ModelArticle>
                    newsAdapter = NewsAdapter(modelArticle, context!!)
                    rvListNews.adapter = newsAdapter
                    newsAdapter?.notifyDataSetChanged()
                    rvListNews.hideShimmerAdapter()
                }
            }

            override fun onFailure(call: Call<ModelNews>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}