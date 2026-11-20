package com.studenthub.app.ui

import com.studenthub.app.data.Assignment
import com.studenthub.app.databinding.DialogAssignmentBinding
import com.studenthub.app.util.formatDateTime
import com.studenthub.app.util.pickDateTime

class AssignmentsFragment : ListFragment() {
    override val screenTitle = "Assignments"
    override val emptyText = "No assignments yet. Tap + to add one."
    private var items = emptyList<Assignment>()

    override fun observe() {
        vm.assignments.observe(viewLifecycleOwner) { list ->
            items = list
            show(list.map { a ->
                CardItem(a.id, a.title, "${a.subject}  |  ${formatDateTime(a.dueDate)}", done = a.done)
            })
        }
    }

    override fun onAdd() = showDialog(null)
    override fun onItemClick(id: Int) = showDialog(items.firstOrNull { it.id == id })
    override fun onToggle(id: Int) {
        items.firstOrNull { it.id == id }?.let { vm.toggleAssignment(it) }
    }
    override fun onDelete(id: Int) {
        items.firstOrNull { it.id == id }?.let { vm.deleteAssignment(it) }
    }

    private fun showDialog(existing: Assignment?) {
        val d = DialogAssignmentBinding.inflate(layoutInflater)
        var due: Long? = existing?.dueDate
        existing?.let { d.title.setText(it.title); d.subject.setText(it.subject) }
        fun refreshButton() { d.btnDue.text = due?.let { formatDateTime(it) } ?: "Pick date & time" }
        refreshButton()
        d.btnDue.setOnClickListener { requireContext().pickDateTime { due = it; refreshButton() } }

        formDialog(if (existing == null) "New assignment" else "Edit assignment", d.root) {
            val title = d.title.text.toString().trim()
            val subject = d.subject.text.toString().trim()
            val t = due
            if (title.isBlank() || t == null) {
                toast("Enter a title and pick a date & time")
                false
            } else {
                if (existing == null) vm.addAssignment(title, subject, t)
                else vm.updateAssignment(existing.copy(title = title, subject = subject, dueDate = t))
                true
            }
        }
    }
}
