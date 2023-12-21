package com.jamhour.data;

import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Map;

public record Student(String name, String email, String phone, int id) implements Comparable<Student>, Table {

    public static final String TABLE_NAME = "student";
    private static final Comparator<Student> COMPARATOR =
            Comparator
                    .comparingInt(Student::id)
                    .thenComparing(Student::phone)
                    .thenComparing(Student::name)
                    .thenComparing(Student::email);


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
    public int compareTo(Student other) {
        return COMPARATOR.compare(this, other);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Column {

        ID(TableColumnImpl.of("id", Integer.class, true)),
        NAME(TableColumnImpl.of("name", String.class)),
        EMAIL(TableColumnImpl.of("email", String.class)),
        PHONE(TableColumnImpl.of("phone", String.class));

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
                    Column.NAME.toEntry(),
                    Column.EMAIL.toEntry(),
                    Column.PHONE.toEntry()
            );
        }
    }
}
