package com.jamhour.data;

import com.jamhour.database.Schema;
import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

public record Exam(String name, String description, LocalDateTime examDateTime, int id,
                   int courseId) implements Comparable<Exam>, Table {
    public static final String TABLE_NAME = Schema.Tables.EXAM.getTableName();
    private static final Comparator<Exam> COMPARATOR =
            Comparator
                    .comparingInt(Exam::id)
                    .thenComparing(Exam::courseId)
                    .thenComparing(Exam::examDateTime)
                    .thenComparing(Exam::name)
                    .thenComparing(Exam::description);

    @Override
    public int compareTo(Exam o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Column.toMap();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public int getPrimaryKey() {
        return id();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {
        ID(TableColumnImpl.of("id", Integer.class, true)),
        COURSE_ID(TableColumnImpl.of("course_id", Integer.class)),
        NAME(TableColumnImpl.of("name", String.class)),
        DESCRIPTION(TableColumnImpl.of("description", String.class)),
        EXAM_DATE_TIME(TableColumnImpl.of("date_time", LocalDateTime.class));

        private final TableColumn<?> tableColumn;

        private TableColumn<?> getTableColumn() {
            return tableColumn;
        }

        public Class<?> getType() {
            return tableColumn.getType();
        }

        public String getName() {
            return tableColumn.name();
        }

        public boolean isPrimaryKey() {
            return tableColumn.isPrimaryKey();
        }

        public boolean isNullable() {
            return tableColumn.isNullable();
        }

        private Map.Entry<TableColumn<?>, String> toEntry() {
            return Map.entry(getTableColumn(), getName());
        }

        private static Map<TableColumn<?>, String> toMap() {
            return Map.ofEntries(
                    Column.ID.toEntry(),
                    Column.COURSE_ID.toEntry(),
                    Column.NAME.toEntry(),
                    Column.DESCRIPTION.toEntry(),
                    Column.EXAM_DATE_TIME.toEntry()
            );
        }
    }
}
