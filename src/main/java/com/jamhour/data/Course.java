package com.jamhour.data;

import java.util.Comparator;

public record Course(String name, int id, int teacherId) implements Comparable<Course> {


    private static final Comparator<Course> COMPARATOR =
            Comparator
                    .comparingInt(Course::id)
                    .thenComparing(Course::teacherId)
                    .thenComparing(Course::name);


    @Override
    public int compareTo(Course other) {
        return COMPARATOR.compare(this, other);
    }
}
