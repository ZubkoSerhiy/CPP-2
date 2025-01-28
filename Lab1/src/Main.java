import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть шлях до текстового файлу: ");
        String filePath = scanner.nextLine();

        try
        {
            String content = readFile(filePath);
            int wordCount = countWords(content);
            int uniqueWordCount = countUniqueWords(content);
            int sentenceCount = countSentences(content);
            int punctuationCount = countPunctuation(content);
            double averageWordLength = calculateAverageWordLength(content);
            double averageSentenceLength = calculateAverageSentenceLength(content);
            List<String> mostFrequentWords = findMostFrequentWords(content, 10);

            System.out.println("Кількість усіх слів: " + wordCount);
            System.out.println("Кількість оригінальних слів: " + uniqueWordCount);
            System.out.println("Кількість речень: " + sentenceCount);
            System.out.println("Кількість знаків пунктуації: " + punctuationCount);
            System.out.printf("Середня довжина слова: %.2f%n", averageWordLength);
            System.out.printf("Середня довжина речення: %.2f%n", averageSentenceLength);
            System.out.println("Перші 10 слів, які зустрічаються найчастіше: " + mostFrequentWords);

        }
        catch (FileNotFoundException e)
        {
            System.out.println("Файл не знайдено: " + e.getMessage());
        }
    }

    private static String readFile(String filePath) throws FileNotFoundException
    {
        StringBuilder content = new StringBuilder();
        Scanner fileScanner = new Scanner(new File(filePath));
        while (fileScanner.hasNextLine()) {
            content.append(fileScanner.nextLine()).append(" ");
        }
        fileScanner.close();
        return content.toString().trim();
    }

    private static int countWords(String content)
    {
        Scanner wordScanner = new Scanner(content);
        int count = 0;
        while (wordScanner.hasNext())
        {
            wordScanner.next();
            count++;
        }
        wordScanner.close();
        return count;
    }

    private static int countUniqueWords(String content)
    {
        Set<String> uniqueWords = new HashSet<>(); // Множина для зберігання унікальних слів
        Scanner wordScanner = new Scanner(content);
        while (wordScanner.hasNext())
        {
            String word = cleanWord(wordScanner.next());
            if (!word.isEmpty())
            {
                uniqueWords.add(word);
            }
        }
        wordScanner.close();
        return uniqueWords.size();
    }

    private static int countSentences(String content)
    {
        Pattern sentencePattern = Pattern.compile("[^.!?]+[.!?]");
        Matcher matcher = sentencePattern.matcher(content);
        int count = 0;
        while (matcher.find())
        {
            count++;
        }
        return count;
    }

    private static int countPunctuation(String content)
    {
        int count = 0;
        for (char ch : content.toCharArray())
        {
            if (",.!?;:\"'()[]{}".indexOf(ch) != -1)
            {
                count++;
            }
        }
        return count;
    }

    private static double calculateAverageWordLength(String content)
    {
        int totalLength = 0, wordCount = 0;
        Scanner wordScanner = new Scanner(content);
        while (wordScanner.hasNext())
        {
            String word = cleanWord(wordScanner.next());
            totalLength += word.length();
            wordCount++;
        }
        wordScanner.close();
        return wordCount == 0 ? 0 : (double) totalLength / wordCount;
    }

    private static double calculateAverageSentenceLength(String content)
    {
        int totalWords = countWords(content);
        int sentenceCount = countSentences(content);
        return sentenceCount == 0 ? 0 : (double) totalWords / sentenceCount;
    }

    private static List<String> findMostFrequentWords(String content, int limit)
    {
        Map<String, Integer> wordFrequency = new HashMap<>(); // Множина для зберігання частоти слів
        Scanner wordScanner = new Scanner(content);
        while (wordScanner.hasNext())
        {
            String word = cleanWord(wordScanner.next());
            if (!word.isEmpty())
            {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1); // Збільшення частоти слова
            }
        }
        wordScanner.close();
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordFrequency.entrySet()); // Сортування слів за частотою
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<String> mostFrequentWords = new ArrayList<>(); // Список найбільш уживаних слів
        for (int i = 0; i < Math.min(limit, sortedEntries.size()); i++)
        {
            mostFrequentWords.add(sortedEntries.get(i).getKey());
        }
        return mostFrequentWords;
    }

    private static String cleanWord(String word)
    {
        return word.toLowerCase().replaceAll("[^\\p{L}]", "");
    }
}