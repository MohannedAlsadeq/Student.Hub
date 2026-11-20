package com.studenthub.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Query("SELECT * FROM exams ORDER BY dateTime ASC") fun all(): Flow<List<Exam>>
    @Insert suspend fun insert(exam: Exam): Long
    @Update suspend fun update(exam: Exam)
    @Delete suspend fun delete(exam: Exam)
}

@Dao
interface AssignmentDao {
    @Query("SELECT * FROM assignments ORDER BY done ASC, dueDate ASC") fun all(): Flow<List<Assignment>>
    @Insert suspend fun insert(a: Assignment): Long
    @Update suspend fun update(a: Assignment)
    @Delete suspend fun delete(a: Assignment)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY done ASC, priority DESC") fun all(): Flow<List<Task>>
    @Insert suspend fun insert(t: Task): Long
    @Update suspend fun update(t: Task)
    @Delete suspend fun delete(t: Task)
}

@Dao
interface IdeaDao {
    @Query("SELECT * FROM ideas ORDER BY id DESC") fun all(): Flow<List<Idea>>
    @Insert suspend fun insert(i: Idea): Long
    @Update suspend fun update(i: Idea)
    @Delete suspend fun delete(i: Idea)
}
