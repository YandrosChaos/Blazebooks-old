package com.blazebooks.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.blazebooks.R


/**
 * A simple [Fragment] subclass.
 */
class BookPageFragment : Fragment() {

   companion object {
       private const val BASE_URL= "baseUrl";
       private const val DATA= "data";
       private const val POSITION= "position";
       private const val ENCODING = "UTF-8"
       private const val MIME_TYPE = "text/html"

       fun newInstance(baseUrl: String, data: String, position: Int): BookPageFragment {
           val args = Bundle()
           args.putString(BASE_URL, baseUrl);
           args.putString(DATA, data);
           args.putInt(POSITION, position);
           val fragment = BookPageFragment()
           fragment.arguments = args
           return fragment
       }
   }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_a, container, false)
        val webViewReader = rootView.findViewById<WebView>(R.id.webViewReader)
        (activity as ReaderActivity?)?.updatePageTextView(arguments?.getInt(POSITION)!!-1)
        webViewReader.loadDataWithBaseURL(
            arguments?.getString(BASE_URL),
            arguments?.getString(DATA),
            MIME_TYPE,
            ENCODING,
            null
        )

        return rootView
    }

    override fun onResume() {
        super.onResume()
        (activity as ReaderActivity?)?.updatePageTextView(arguments?.getInt(POSITION)!!-1)
    }


}
