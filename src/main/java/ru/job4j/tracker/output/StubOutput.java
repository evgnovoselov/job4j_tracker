package ru.job4j.tracker.output;

import java.util.ArrayList;
import java.util.List;

public class StubOutput implements Output {
    private final List<String> lines = new ArrayList<>();

    @Override
    public void println(Object obj) {
        lines.add(obj.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
