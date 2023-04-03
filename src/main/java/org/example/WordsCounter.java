package org.example;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordsCounter {
    public static Map<String, Integer> countWords(String filename) {
        File file = new File(filename);
        Map<String, Integer> countWords = null;
        try {
            Reader is = new FileReader(file);
            try {
                is = new BufferedReader(is);
                countWords = countWordsInFile(is);
            } finally {
                is.close();
            }
        } catch (IOException ex) {
            System.out.println("Проблемы с чтением файла. Проверьте наличие файла.");
        }
        return countWords;
    }

    public static StreamTokenizer initStreamTokenizer(Reader reader) {
        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

        streamTokenizer.lowerCaseMode(true);
        streamTokenizer.whitespaceChars(0, 46);
        streamTokenizer.whitespaceChars(0, 1000000);

        streamTokenizer.wordChars(45, 45);
        streamTokenizer.wordChars('А', 'Я');
        streamTokenizer.wordChars('а', 'я');
        streamTokenizer.wordChars('Ё', 'Ё');
        streamTokenizer.wordChars('ё', 'ё');
        streamTokenizer.wordChars('A', 'Z');
        streamTokenizer.wordChars('a', 'z');


        return streamTokenizer;
    }

    public static Map<String, Integer> countWordsInFile(Reader reader) throws IOException {
        final int QUOTE_CHARACTER = '\'';
        final int DOUBLE_QUOTE_CHARACTER = '"';
        StreamTokenizer streamTokenizer = initStreamTokenizer(reader);

        Map<String, Integer> countWords = new LinkedHashMap<>();
        int currentToken = streamTokenizer.nextToken();
        while (currentToken != StreamTokenizer.TT_EOF) {
            if (streamTokenizer.ttype == StreamTokenizer.TT_WORD
                    || streamTokenizer.ttype == QUOTE_CHARACTER
                    || streamTokenizer.ttype == DOUBLE_QUOTE_CHARACTER) {
                String currentWord = streamTokenizer.sval;
                if ("—".equals(currentWord) || currentWord.contains("…")) {
                    currentToken = streamTokenizer.nextToken();
                    continue;
                }
                int count = countWords.getOrDefault(currentWord, 0);
                countWords.put(streamTokenizer.sval, count + 1);
            }

            currentToken = streamTokenizer.nextToken();
        }

        return countWords;
    }

    public static void main(String[] args) {

        Map<String, Integer> words = countWords(args[0]);

        List<Map.Entry<String, Integer>> entries = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

        System.out.println("Top 10 words:");
        entries.stream().limit(10).forEach(System.out::println);

        System.out.println("\nLast 10 words:");
        entries.stream().skip(Math.max(0, words.size() - 10)).forEach(System.out::println);
    }
}
