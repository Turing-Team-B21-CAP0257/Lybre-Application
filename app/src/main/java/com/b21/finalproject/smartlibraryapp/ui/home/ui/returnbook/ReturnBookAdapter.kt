package com.b21.finalproject.smartlibraryapp.ui.home.ui.returnbook

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.b21.finalproject.smartlibraryapp.R
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookEntity
import com.b21.finalproject.smartlibraryapp.data.source.local.entity.BookWithDeadlineEntity
import com.b21.finalproject.smartlibraryapp.databinding.ItemListRecommendedBooksBinding
import com.b21.finalproject.smartlibraryapp.databinding.ItemListReturnBookBinding
import com.b21.finalproject.smartlibraryapp.ui.home.ui.detail.DetailBookActivity
import com.b21.finalproject.smartlibraryapp.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ReturnBookAdapter : RecyclerView.Adapter<ReturnBookAdapter.ViewHolder>() {

    private var allBooks = ArrayList<BookWithDeadlineEntity>()

    private lateinit var onBtnReturnClickCallback: OnBtnReturnClickCallback

    var IS_RETURN = false

    fun setOnBtnReturnClickCallback(onBtnReturnClickCallback: OnBtnReturnClickCallback) {
        this.onBtnReturnClickCallback = onBtnReturnClickCallback
    }

    fun setAllbooks(books: List<BookWithDeadlineEntity>) {
        if (allBooks.size != 0) {
            allBooks.clear()
            allBooks.addAll(books)
            notifyDataSetChanged()
        } else {
            allBooks.addAll(books)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListReturnBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allBooks[position])
    }

    override fun getItemCount(): Int = allBooks.size

    inner class ViewHolder(private val binding: ItemListReturnBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookEntity: BookWithDeadlineEntity) {
            with(binding) {
                val url = bookEntity.imageUrl_l.split("\"", "/").toTypedArray()
                val title = bookEntity.book_title.split("\"").toTypedArray()

                val circularProgressBar = CircularProgressDrawable(itemView.context).apply {
                    strokeWidth = 5f
                    centerRadius = 30f
                    start()
                }

                Glide.with(itemView.context)
                    .load("http://images.amazon.com/images/P/${url[6]}")
                    .apply(
                        RequestOptions.placeholderOf(circularProgressBar).error(R.drawable.ic_error)
                    )
                    .into(imgItemBook)

                tvBookTitle.text = title[1]
                tvDueDate.text = bookEntity.deadline

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailBookActivity::class.java)
                    intent.putExtra(DetailBookActivity.BOOK_ID, bookEntity.bookId.toInt())
                    itemView.context.startActivity(intent)
                }

                btnReturn.isEnabled = IS_RETURN != false

                btnReturn.setOnClickListener {
                    onBtnReturnClickCallback.onBtnItemClickCallback(bookEntity)
                }
            }
        }
    }

    interface OnBtnReturnClickCallback {
        fun onBtnItemClickCallback(book: BookWithDeadlineEntity)
    }
}
