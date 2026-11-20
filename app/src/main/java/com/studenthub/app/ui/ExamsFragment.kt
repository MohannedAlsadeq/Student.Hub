package com.studenthub.app.ui

import com.studenthub.app.data.Exam
import com.studenthub.app.databinding.DialogExamBinding
import com.studenthub.app.util.formatDateTime
import com.studenthub.app.util.pickDateTime

class ExamsFragment : ListFragment() {
    override val screenTitle = "Exams"
    override val emptyText = "No exams yet. Tap + to add one."
    private var exams = emptyList<Exam>()

    override fun observe() {
        vm.exams.observe(viewLifecycleOwner) { list ->
            exams = list
            show(list.map { e ->
                val room = if (e.room.isBlank()) "" else "  |  ${e.room}"
                CardItem(e.id, e.subject, formatDateTime(e.dateTime) + room)
            })
        }
    }

    override fun onAdd() = showDialog(null)
    override fun onItemClick(id: Int) = showDialog(exams.firstOrNull { it.id == id })
    override fun onDelete(id: Int) {
        exams.firstOrNull { it.id == id }?.let { vm.deleteExam(it) }
    }

    private fun showDialog(existing: Exam?) {
        val d = DialogExamBinding.inflate(layoutInflater)
        var time: Long? = existing?.dateTime
        existing?.let { d.subject.setText(it.subject); d.room.setText(it.room) }
        fun refreshButton() { d.btnTime.text = time?.let { formatDateTime(it) } ?: "Pick date & time" }
        refreshButton()
        d.btnTime.setOnClickListener { requireContext().pickDateTime { time = it; refreshButton() } }

        formDialog(if (existing == null) "New exam" else "Edit exam", d.root) {
            val subject = d.subject.text.toString().trim()
            val room = d.room.text.toString().trim()
            val t = time
            if (subject.isBlank() || t == null) {
                toast("Enter a subject and pick a date & time")
                false
            } else {
                if (existing == null) vm.addExam(subject, room, t)
                else vm.updateExam(existing.copy(subject = subject, room = room, dateTime = t))
                true
            }
        }
    }
}
