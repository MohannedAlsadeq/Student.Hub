package com.studenthub.app.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExamDao_Impl implements ExamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Exam> __insertionAdapterOfExam;

  private final EntityDeletionOrUpdateAdapter<Exam> __deletionAdapterOfExam;

  private final EntityDeletionOrUpdateAdapter<Exam> __updateAdapterOfExam;

  public ExamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExam = new EntityInsertionAdapter<Exam>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `exams` (`id`,`subject`,`room`,`dateTime`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exam entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getRoom());
        statement.bindLong(4, entity.getDateTime());
      }
    };
    this.__deletionAdapterOfExam = new EntityDeletionOrUpdateAdapter<Exam>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exams` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exam entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExam = new EntityDeletionOrUpdateAdapter<Exam>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exams` SET `id` = ?,`subject` = ?,`room` = ?,`dateTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exam entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getRoom());
        statement.bindLong(4, entity.getDateTime());
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Exam exam, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExam.insertAndReturnId(exam);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Exam exam, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExam.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Exam exam, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExam.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Exam>> all() {
    final String _sql = "SELECT * FROM exams ORDER BY dateTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<Exam>>() {
      @Override
      @NonNull
      public List<Exam> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfDateTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTime");
          final List<Exam> _result = new ArrayList<Exam>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exam _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final long _tmpDateTime;
            _tmpDateTime = _cursor.getLong(_cursorIndexOfDateTime);
            _item = new Exam(_tmpId,_tmpSubject,_tmpRoom,_tmpDateTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
