package com.studenthub.app.ui

import com.studenthub.app.data.Idea
import com.studenthub.app.databinding.DialogIdeaBinding

class IdeasFragment : ListFragment() {
    override val screenTitle = "Ideas"
    override val emptyText = "No ideas yet. Tap + to jot one down."
    private var ideas = emptyList<Idea>()

    override fun observe() {
        vm.ideas.observe(viewLifecycleOwner) { list ->
            ideas = list
            show(list.map { CardItem(it.id, it.title, it.text) })
        }
    }

    override fun onAdd() = showDialog(null)
    override fun onItemClick(id: Int) = showDialog(ideas.firstOrNull { it.id == id })
    override fun onDelete(id: Int) {
        ideas.firstOrNull { it.id == id }?.let { vm.deleteIdea(it) }
    }

    private fun showDialog(existing: Idea?) {
        val d = DialogIdeaBinding.inflate(layoutInflater)
        existing?.let { d.title.setText(it.title); d.details.setText(it.text) }
        formDialog(if (existing == null) "New idea" else "Edit idea", d.root) {
            val title = d.title.text.toString().trim()
            val text = d.details.text.toString().trim()
            if (title.isBlank()) {
                toast("Enter a title")
                false
            } else {
                if (existing == null) vm.addIdea(title, text)
                else vm.updateIdea(existing.copy(title = title, text = text))
                true
            }
        }
    }
}
