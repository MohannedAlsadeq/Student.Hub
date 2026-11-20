package com.studenthub.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.studenthub.app.data.*
import com.studenthub.app.notifications.ReminderScheduler
import com.studenthub.app.util.formatDateTime
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val ctx get() = getApplication<Application>()

    val exams = db.examDao().all().asLiveData()
    val assignments = db.assignmentDao().all().asLiveData()
    val tasks = db.taskDao().all().asLiveData()
    val ideas = db.ideaDao().all().asLiveData()

    // ----- Exams -----
    fun addExam(subject: String, room: String, dateTime: Long) = viewModelScope.launch {
        val id = db.examDao().insert(Exam(subject = subject, room = room, dateTime = dateTime))
        val where = if (room.isBlank()) "" else " in $room"
        ReminderScheduler.schedule(ctx, "exam_$id", "Exam: $subject",
            "${formatDateTime(dateTime)}$where", dateTime)
    }

    fun updateExam(e: Exam) = viewModelScope.launch {
        db.examDao().update(e)
        ReminderScheduler.cancel(ctx, "exam_${e.id}")
        val where = if (e.room.isBlank()) "" else " in ${e.room}"
        ReminderScheduler.schedule(ctx, "exam_${e.id}", "Exam: ${e.subject}",
            "${formatDateTime(e.dateTime)}$where", e.dateTime)
    }

    fun deleteExam(e: Exam) = viewModelScope.launch {
        db.examDao().delete(e)
        ReminderScheduler.cancel(ctx, "exam_${e.id}")
    }

    // ----- Assignments -----
    fun addAssignment(title: String, subject: String, due: Long) = viewModelScope.launch {
        val id = db.assignmentDao().insert(Assignment(title = title, subject = subject, dueDate = due))
        ReminderScheduler.schedule(ctx, "assignment_$id", "Assignment due: $title",
            "$subject - ${formatDateTime(due)}", due)
    }

    fun updateAssignment(a: Assignment) = viewModelScope.launch {
        db.assignmentDao().update(a)
        ReminderScheduler.cancel(ctx, "assignment_${a.id}")
        if (!a.done) ReminderScheduler.schedule(ctx, "assignment_${a.id}", "Assignment due: ${a.title}",
            "${a.subject} - ${formatDateTime(a.dueDate)}", a.dueDate)
    }

    fun toggleAssignment(a: Assignment) = viewModelScope.launch {
        db.assignmentDao().update(a.copy(done = !a.done))
        if (!a.done) ReminderScheduler.cancel(ctx, "assignment_${a.id}")
        else ReminderScheduler.schedule(ctx, "assignment_${a.id}", "Assignment due: ${a.title}",
            "${a.subject} - ${formatDateTime(a.dueDate)}", a.dueDate)
    }

    fun deleteAssignment(a: Assignment) = viewModelScope.launch {
        db.assignmentDao().delete(a)
        ReminderScheduler.cancel(ctx, "assignment_${a.id}")
    }

    // ----- Tasks -----
    fun addTask(title: String, priority: Int) = viewModelScope.launch {
        db.taskDao().insert(Task(title = title, priority = priority))
    }
    fun updateTask(t: Task) = viewModelScope.launch { db.taskDao().update(t) }
    fun toggleTask(t: Task) = viewModelScope.launch { db.taskDao().update(t.copy(done = !t.done)) }
    fun deleteTask(t: Task) = viewModelScope.launch { db.taskDao().delete(t) }

    // ----- Ideas -----
    fun addIdea(title: String, text: String) = viewModelScope.launch {
        db.ideaDao().insert(Idea(title = title, text = text))
    }
    fun updateIdea(i: Idea) = viewModelScope.launch { db.ideaDao().update(i) }
    fun deleteIdea(i: Idea) = viewModelScope.launch { db.ideaDao().delete(i) }
}
