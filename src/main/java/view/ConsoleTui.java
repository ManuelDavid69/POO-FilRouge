package view;

import allShared.IConsoleTui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Petit renderer console sans dependance externe pour afficher
 * des scenarios de test sous forme de TUI ASCII.
 */
public final class ConsoleTui implements IConsoleTui {

    private static final int BOX_WIDTH = 136;
    private static final int[] CHECK_WIDTHS = {8, 44, 44, 24};

    @Override
    public void title(String title, String subtitle) {
        List<String> lines = new ArrayList<String>();
        if (subtitle != null && !subtitle.trim().isEmpty()) {
            lines.add(subtitle);
        }
        System.out.println(renderBox(title, lines, '='));
    }

    @Override
    public void section(String title) {
        System.out.println();
        System.out.println(renderBox(title, Collections.<String>emptyList(), '-'));
        System.out.println(buildTableBorder());
        System.out.println(buildRow("Type", "Operation", "Resultat", "Attendu"));
        System.out.println(buildTableBorder());
    }

    @Override
    public void check(String operation, Object result, String expected) {
        System.out.println(buildRow("CHECK", operation, stringify(result), defaultValue(expected)));
    }

    @Override
    public void info(String operation, Object result) {
        System.out.println(buildRow("INFO", operation, stringify(result), "-"));
    }

    @Override
    public void note(String title, String message) {
        System.out.println();
        System.out.println(renderBox(title, wrap(message, BOX_WIDTH - 4), '-'));
    }

    static String renderPanel(String title, List<String> lines) {
        return ConsoleTui.renderBox(title, lines, '-');
    }

    static String renderBanner(String title, String subtitle) {
        List<String> lines = subtitle == null || subtitle.trim().isEmpty()
                ? Collections.<String>emptyList()
                : List.of(subtitle);
        return ConsoleTui.renderBox(title, lines, '=');
    }
    private static String renderBox(String title, List<String> lines, char borderChar) {
        StringBuilder builder = new StringBuilder();
        builder.append(boxBorder(borderChar)).append('\n');
        builder.append(boxLine(title == null ? "" : title.toUpperCase())).append('\n');
        if (lines != null) {
            for (String line : lines) {
                for (String wrappedLine : wrap(line, BOX_WIDTH - 4)) {
                    builder.append(boxLine(wrappedLine)).append('\n');
                }
            }
        }
        builder.append(boxBorder(borderChar));
        return builder.toString();
    }

    private static String buildTableBorder() {
        StringBuilder builder = new StringBuilder("+");
        for (int width : CHECK_WIDTHS) {
            builder.append(repeat('-', width + 2)).append('+');
        }
        return builder.toString();
    }

    private static String buildRow(String type, String operation, String result, String expected) {
        return new StringBuilder()
                .append("| ").append(pad(type, CHECK_WIDTHS[0])).append(" | ")
                .append(pad(operation, CHECK_WIDTHS[1])).append(" | ")
                .append(pad(result, CHECK_WIDTHS[2])).append(" | ")
                .append(pad(expected, CHECK_WIDTHS[3])).append(" |")
                .toString();
    }

    private static String boxBorder(char borderChar) {
        return "+" + repeat(borderChar, BOX_WIDTH - 2) + "+";
    }

    private static String boxLine(String content) {
        return "| " + pad(content, BOX_WIDTH - 4) + " |";
    }

    private static String pad(String value, int width) {
        String safeValue = defaultValue(value).replace('\n', ' ').replace('\r', ' ').trim();
        if (safeValue.length() > width) {
            return safeValue.substring(0, Math.max(0, width - 3)) + "...";
        }
        StringBuilder builder = new StringBuilder(safeValue);
        while (builder.length() < width) {
            builder.append(' ');
        }
        return builder.toString();
    }

    private static String stringify(Object value) {
        return value == null ? "null" : String.valueOf(value);
    }

    private static String defaultValue(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value;
    }

    private static String repeat(char value, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, value);
        return new String(chars);
    }

    private static List<String> wrap(String text, int width) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.singletonList("");
        }

        List<String> lines = new ArrayList<String>();
        String[] rawLines = text.replace('\r', '\n').split("\n");
        for (String rawLine : rawLines) {
            if (rawLine.length() <= width) {
                lines.add(rawLine);
                continue;
            }

            String remaining = rawLine.trim();
            while (remaining.length() > width) {
                int splitAt = remaining.lastIndexOf(' ', width);
                if (splitAt <= 0) {
                    splitAt = width;
                }
                lines.add(remaining.substring(0, splitAt).trim());
                remaining = remaining.substring(splitAt).trim();
            }
            if (!remaining.isEmpty()) {
                lines.add(remaining);
            }
        }

        return lines;
    }
}
