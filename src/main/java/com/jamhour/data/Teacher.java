package com.jamhour.data;

import com.jamhour.database.Schema;
import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

public record Teacher(String name, String phone, String email, String major, double salary, int experience,
                      LocalDate dateOfBirth, int id) implements Comparable<Teacher>, Table {
    public static final String TABLE_NAME = Schema.Tables.TEACHER.getTableName();
    private static final Comparator<Teacher> COMPARATOR =
            Comparator
                    .comparing(Teacher::id)
                    .thenComparing(Teacher::phone)
                    .thenComparing(Teacher::email)
                    .thenComparing(Teacher::dateOfBirth)
                    .thenComparing(Teacher::name)
                    .thenComparing(Teacher::experience)
                    .thenComparing(Teacher::salary);

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

    @Override
    public int compareTo(Teacher other) {
        return COMPARATOR.compare(this, other);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {

        NAME(TableColumnImpl.of("name", String.class)),
        PHONE(TableColumnImpl.of("phone", String.class)),
        EMAIL(TableColumnImpl.of("email", String.class)),
        MAJOR(TableColumnImpl.of("major", String.class)),
        SALARY(TableColumnImpl.of("salary", Double.class)),
        EXPERIENCE(TableColumnImpl.of("experience", Integer.class)),
        DATE_OF_BIRTH(TableColumnImpl.of("date_of_birth", LocalDate.class)),
        ID(TableColumnImpl.of("id", Integer.class, true));

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
                    Column.NAME.toEntry(),
                    Column.PHONE.toEntry(),
                    Column.EMAIL.toEntry(),
                    Column.MAJOR.toEntry(),
                    Column.SALARY.toEntry(),
                    Column.EXPERIENCE.toEntry(),
                    Column.DATE_OF_BIRTH.toEntry(),
                    Column.ID.toEntry()
            );
        }
    }
}
