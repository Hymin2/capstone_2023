package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.PostActivity
import ac.kr.tukorea.capstone_android.adapter.CommunityAdapter
import ac.kr.tukorea.capstone_android.data.Post
import ac.kr.tukorea.capstone_android.data.Products
import android.content.Intent
import android.os.Bundle
import android.view.ContentInfo
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Community.newInstance] factory method to
 * create an instance of this fragment.
 */
class Community : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: CommunityAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postArrayList : ArrayList<Post>

    lateinit var postTitle : Array<String>
    lateinit var postImageId : Array<Int>
    lateinit var postWriter : Array<String>
    lateinit var postViews : Array<Int>
    lateinit var postDate : Array<String>
    lateinit var postContent : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_community, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Community.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Community().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.commu_recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = CommunityAdapter(postArrayList)
        recyclerView.adapter = adapter

        getUserData()
    }

    private fun dataInitialize() {

        postArrayList = arrayListOf<Post>()


        postTitle = arrayOf(
            "글 제목1",
            "글 제목2",
            "글 제목3",
            "글 제목4",
            "글 제목5",
            "글 제목6",
            "글 제목7"
            )

        postImageId = arrayOf(
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
            R.drawable.galaxys23,
            R.drawable.iphone14pro,
        )

        postWriter = arrayOf(
            "한국공학대학교",
            "한국공대",
            "tuk",
            "kpu",
            "한공이",
            "산기대",
            "한국산업기술대"
        )

        postViews = arrayOf(
            90100,
            4200,
            9300,
            2400,
            1500,
            11600,
            700,
        )

        postDate = arrayOf(
            "2022-01-02",
            "2022-02-02",
            "2022-03-03",
            "2022-04-04",
            "2022-05-05",
            "2022-06-06",
            "2022-07-07",
        )

        postContent = arrayOf(
            "1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n",
            "2\n2\n2\n2\n2\n2\n2\n2\n2\n2\n2\n2\n",
            "3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n",
            "4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n4\n",
            "5\n5\n5\n5\n5\n5\n5\n5\n5\n5\n5\n5\n",
            "6\n6\n6\n6\n6\n6\n6\n6\n6\n6\n6\n6\n",
            "7\n7\n7\n7\n7\n7\n7\n7\n7\n7\n7\n7\n"
        )
    }

    private fun getUserData() {
        for (i in postTitle.indices) {
            val post = Post(
                postTitle[i],
                postImageId[i],
                postWriter[i],
                postViews[i],
                postDate[i],
                postContent[i],
            )

            postArrayList.add(post)
        }

        var adapter = CommunityAdapter(postArrayList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : CommunityAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(context, PostActivity::class.java)
                intent.putExtra("postTitle",postArrayList[position].postTitle)
                intent.putExtra("postWriter",postArrayList[position].postWriter)
                intent.putExtra("postViews",postArrayList[position].postViews)
                intent.putExtra("postDate",postArrayList[position].postDate)
                intent.putExtra("postContent",postArrayList[position].postContent)

                startActivity(intent)
            }
        })
    }
}