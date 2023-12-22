package com.jamhour.data;

import com.jamhour.database.Schema;
import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

public record Enrollment(EnrollmentStatus status, boolean payed, int courseId, int studentId)
        implements Comparable<Enrollment>, Table {

    public static final String TABLE_NAME = Schema.Tables.ENROLLMENT.getTableName();
    private static final Comparator<Enrollment> COMPARATOR =
            Comparator
                    .comparingInt(Enrollment::studentId)
                    .thenComparingInt(Enrollment::courseId)
                    .thenComparing(Enrollment::payed)
                    .thenComparing(Enrollment::status);


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
        return studentId;
    }

    @Override
    public int compareTo(Enrollment other) {
        return COMPARATOR.compare(this, other);
    }

    @Getter
    @RequiredArgsConstructor
    public enum EnrollmentStatus {

        ACTIVE("active"),
        COMPLETED("completed"),
        DROPPED("dropped"),
        WITHDRAWN("withdrawn");

        private final String status;

    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {

        STATUS(TableColumnImpl.of("enrollment_status", EnrollmentStatus.class)),
        PAYED(TableColumnImpl.of("payed", Boolean.class)),
        COURSE_ID(TableColumnImpl.of("course_id", Integer.class, true)),
        STUDENT_ID(TableColumnImpl.of("student_id", Integer.class, true));

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
                    Column.STATUS.toEntry(),
                    Column.PAYED.toEntry(),
                    Column.COURSE_ID.toEntry(),
                    Column.STUDENT_ID.toEntry()
            );
        }
    }
}
