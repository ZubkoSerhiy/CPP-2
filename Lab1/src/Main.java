import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть шлях до текстового файлу: ");
        String filePath = scanner.nextLine();

        try {
            String content = readFile(filePath);
            analyzeText(content);

        } catch (FileNotFoundException e) {
            System.out.println("Файл не знайдено: " + e.getMessage());
        }
    }

    private static String readFile(String filePath) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        Scanner fileScanner = new Scanner(new File(filePath));
        while (fileScanner.hasNextLine()) {
            content.append(fileScanner.nextLine()).append(" ");
        }
        fileScanner.close();
        return content.toString().trim();
    }

    private static void analyzeText(String content) {
        int wordCount = 0;
        Set<String> uniqueWords = new HashSet<>();
        int totalWordLength = 0;

        Scanner wordScanner = new Scanner(content);
        while (wordScanner.hasNext()) {
            String word = cleanWord(wordScanner.next());
            if (!word.isEmpty()) {
                wordCount++;
                uniqueWords.add(word);
                totalWordLength += word.length();
            }
        }
        wordScanner.close();

        int sentenceCount = 0;
        for (char ch : content.toCharArray()) {
            if (ch == '.' || ch == '!' || ch == '?') {
                sentenceCount++;
            }
        }

        int punctuationCount = 0;
        for (char ch : content.toCharArray()) {
            if (",.!?;:\"'()[]{}".indexOf(ch) != -1) {
                punctuationCount++;
            }
        }

        double averageWordLength = wordCount == 0 ? 0 : (double) totalWordLength / wordCount;
        double averageSentenceLength = sentenceCount == 0 ? 0 : (double) wordCount / sentenceCount;

        Map<String, Integer> wordFrequency = new HashMap<>();
        wordScanner = new Scanner(content);
        while (wordScanner.hasNext()) {
            String word = cleanWord(wordScanner.next());
            if (!word.isEmpty()) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }
        wordScanner.close();

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFrequency.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<String> mostFrequentWords = new ArrayList<>();
        for (int i = 0; i < Math.min(10, sortedEntries.size()); i++) {
            mostFrequentWords.add(sortedEntries.get(i).getKey());
        }

        System.out.println("Кількість усіх слів: " + wordCount);
        System.out.println("Кількість оригінальних слів: " + uniqueWords.size());
        System.out.println("Кількість речень: " + sentenceCount);
        System.out.println("Кількість знаків пунктуації: " + punctuationCount);
        System.out.printf("Середня довжина слова: %.2f%n", averageWordLength);
        System.out.printf("Середня довжина речення: %.2f%n", averageSentenceLength);
        System.out.println("Перші 10 слів, які зустрічаються найчастіше: " + mostFrequentWords);
    }

    private static String cleanWord(String word) {
        return word.toLowerCase().replaceAll("[^\\p{L}]", "");
    }
}