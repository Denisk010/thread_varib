import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger counter_len_3 = new AtomicInteger(0);
    public static AtomicInteger counter_len_4 = new AtomicInteger(0);
    public static AtomicInteger counter_len_5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text) && !isSameChar(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread sameChar = new Thread(() -> {
            for (String text : texts) {
                if (isSameChar(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread ascendingOrder = new Thread(() -> {
            for (String text : texts) {
                if (!isSameChar(text) && isAscendingOrder(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        palindrome.start();
        sameChar.start();
        ascendingOrder.start();


        ascendingOrder.join();
        sameChar.join();
        palindrome.join();

        System.out.println("Красивых слов с длиной 3: " + counter_len_3.get() + " шт \n"
                + "Красивых слов с длиной 4: " + counter_len_4.get() + " шт \n"
                + "Красивых слов с длиной 5: " + counter_len_5.get() + " шт \n");
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            counter_len_3.getAndIncrement();
        } else if (textLength == 4) {
            counter_len_4.getAndIncrement();
        } else if (textLength == 5) {
            counter_len_5.getAndIncrement();
        }
    }
    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean isSameChar(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }
    public static boolean isAscendingOrder(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }
}