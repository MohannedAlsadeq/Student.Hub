package com.studenthub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studenthub.app.databinding.FragmentListBinding
import com.studenthub.app.viewmodel.MainViewModel

/** Shared screen for Exams, Assignments, Tasks and Ideas: heading + list + "+" button. */
abstract class ListFragment : Fragment() {
    protected val vm: MainViewModel by activityViewModels()
    private var _b: FragmentListBinding? = null
    protected val b get() = _b!!
    private lateinit var adapter: CardAdapter

    protected abstract val screenTitle: String
    protected abstract val emptyText: String
    protected abstract fun observe()
    protected abstract fun onAdd()
    protected abstract fun onItemClick(id: Int)
    protected abstract fun onDelete(id: Int)
    protected open fun onToggle(id: Int) {}

    protected fun show(items: List<CardItem>) {
        adapter.submitList(items)
        b.empty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _b = FragmentListBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        b.heading.text = screenTitle
        b.empty.text = emptyText
        adapter = CardAdapter({ onItemClick(it) }, { onToggle(it) }, { onDelete(it) })
        b.recycler.layoutManager = LinearLayoutManager(requireContext())
        b.recycler.adapter = adapter
        b.fab.setOnClickListener { onAdd() }
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
