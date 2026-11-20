package com.studenthub.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.studenthub.app.data.Assignment
import com.studenthub.app.data.Exam
import com.studenthub.app.data.Task
import com.studenthub.app.databinding.FragmentHomeBinding
import com.studenthub.app.util.formatDateTime
import com.studenthub.app.viewmodel.MainViewModel

class HomeFragment : Fragment() {
    private val vm: MainViewModel by activityViewModels()
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!

    private var exams = emptyList<Exam>()
    private var assignments = emptyList<Assignment>()
    private var tasks = emptyList<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _b = FragmentHomeBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, state: Bundle?) {
        val adapter = CardAdapter({}, {}, {})
        b.upcoming.layoutManager = LinearLayoutManager(requireContext())
        b.upcoming.adapter = adapter

        fun refresh() {
            val now = System.currentTimeMillis()
            val items = (
                exams.filter { it.dateTime > now }.map { Triple("Exam", it.subject, it.dateTime) } +
                assignments.filter { !it.done && it.dueDate > now }.map { Triple("Assignment", it.title, it.dueDate) }
            ).sortedBy { it.third }.take(5).mapIndexed { i, t ->
                CardItem(i, t.second, "${t.first}  |  ${formatDateTime(t.third)}", deletable = false)
            }
            adapter.submitList(items)
            b.empty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            b.openTasks.text = tasks.count { !it.done }.toString()
        }

        vm.exams.observe(viewLifecycleOwner) { exams = it; refresh() }
        vm.assignments.observe(viewLifecycleOwner) { assignments = it; refresh() }
        vm.tasks.observe(viewLifecycleOwner) { tasks = it; refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
