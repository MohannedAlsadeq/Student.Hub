package com.studenthub.app.ui

import com.studenthub.app.R
import com.studenthub.app.data.Task
import com.studenthub.app.databinding.DialogTaskBinding

class TasksFragment : ListFragment() {
    override val screenTitle = "Tasks"
    override val emptyText = "No tasks yet. Tap + to add one."
    private var tasks = emptyList<Task>()
    private val priorityNames = listOf("Low", "Medium", "High")
    private val priorityIds = listOf(R.id.rbLow, R.id.rbMed, R.id.rbHigh)

    override fun observe() {
        vm.tasks.observe(viewLifecycleOwner) { list ->
            tasks = list
            show(list.map { t ->
                CardItem(t.id, t.title, "Priority: ${priorityNames[t.priority]}", done = t.done)
            })
        }
    }

    override fun onAdd() = showDialog(null)
    override fun onItemClick(id: Int) = showDialog(tasks.firstOrNull { it.id == id })
    override fun onToggle(id: Int) {
        tasks.firstOrNull { it.id == id }?.let { vm.toggleTask(it) }
    }
    override fun onDelete(id: Int) {
        tasks.firstOrNull { it.id == id }?.let { vm.deleteTask(it) }
    }

    private fun showDialog(existing: Task?) {
        val d = DialogTaskBinding.inflate(layoutInflater)
        existing?.let {
            d.title.setText(it.title)
            d.priorityGroup.check(priorityIds[it.priority])
        }
        formDialog(if (existing == null) "New task" else "Edit task", d.root) {
            val title = d.title.text.toString().trim()
            val priority = priorityIds.indexOf(d.priorityGroup.checkedRadioButtonId).coerceAtLeast(0)
            if (title.isBlank()) {
                toast("Enter a task")
                false
            } else {
                if (existing == null) vm.addTask(title, priority)
                else vm.updateTask(existing.copy(title = title, priority = priority))
                true
            }
        }
    }
}
