package com.jamhour.data;

import com.jamhour.database.Table;
import com.jamhour.database.TableColumn;
import com.jamhour.database.TableColumnImpl;

import java.util.Comparator;
import java.util.Map;

public record Student(String name, String email, String phone, int id) implements Comparable<Student>, Table {

    private static final String TABLE_NAME = "student";

    private static final TableColumn<Integer> ID_TABLE_COLUMN = TableColumnImpl.of("id", Integer.class, true);
    private static final TableColumn<String> NAME_TABLE_COLUMN = TableColumnImpl.of("name", String.class);
    private static final TableColumn<String> EMAIL_TABLE_COLUMN = TableColumnImpl.of("email", String.class);
    private static final TableColumn<String> PHONE_TABLE_COLUMN = TableColumnImpl.of("phone", String.class);
    private static final Comparator<Student> COMPARATOR =
            Comparator
                    .comparingInt(Student::id)
                    .thenComparing(Student::phone)
                    .thenComparing(Student::name)
                    .thenComparing(Student::email);


    @Override
    public Map<TableColumn<?>, String> getTableColumns() {
        return Map.of(
                ID_TABLE_COLUMN, ID_TABLE_COLUMN.name(),
                NAME_TABLE_COLUMN, NAME_TABLE_COLUMN.name(),
                EMAIL_TABLE_COLUMN, EMAIL_TABLE_COLUMN.name(),
                PHONE_TABLE_COLUMN, PHONE_TABLE_COLUMN.name()
        );
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

}
