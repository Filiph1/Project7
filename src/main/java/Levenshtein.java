import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Levenshtein
{
    private  Path filepath = new File(Levenshtein.class.getResource("words_alpha.txt").getFile()).toPath();
    private List<String> list;
    private Instant start;
    private Instant stop;
    private  Long time;


    public Levenshtein() {
        run();
    }


    public  void run()
    {
        regular(10);
        parallel(10);
        regular(20);
        parallel(20);
        regular(50);
        parallel(50);
        regular(100);
        parallel(100);
        regular(500);
        parallel(500);
        regular(1000);
        parallel(1000);
        regular(5000);
        parallel(5000);
    }

    public  void regular(int n)
    {

        start = Instant.now();

        try (Stream<String> stream = Files.lines(filepath)) {
            list= stream.sorted(randomOrder()).limit(n).collect(Collectors.toList());;

            for (int i = 0; i < list.size(); i++) {
                String word = list.get(i);
                for (int j = 0; j <= i ; j++) {
                    calculateEditDistance(word, list.get(j));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


         stop = Instant.now();
         time = Duration.between(start, stop).toMillis();
        System.out.println("Regular for " + n +" words "+ time + " ms");

    }
    public  void parallel(int n)
    {

         start = Instant.now();
        try (Stream<String> stream = Files.lines(filepath)) {
            list= stream.parallel().sorted(randomOrder()).limit(n).collect(Collectors.toList());;


            for (int i = 0; i < list.size(); i++) {
                String word = list.get(i);
                for (int j = 0; j <= i ; j++) {
                    calculateEditDistance(word, list.get(j));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


         stop = Instant.now();
         time = Duration.between(start, stop).toMillis();
        System.out.println("Parallel for " + n +" words "+ time+ " ms");

    }
    public  int calculateEditDistance(String x, String y) {
        int[][] dis = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dis[i][j] = j;
                }
                else if (j == 0) {
                    dis[i][j] = i;
                }
                else {
                    dis[i][j] = min(dis[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dis[i - 1][j] + 1,
                            dis[i][j - 1] + 1);
                }
            }
        }

        return dis[x.length()][y.length()];
    }

    private  int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private  int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    public  Comparator<String> randomOrder() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int x = r.nextInt(), y = r.nextInt();
        boolean b = r.nextBoolean();
        return Comparator.comparingInt((String s)->s.hashCode()^x)
                .thenComparingInt(s->s.length()^y)
                .thenComparing(b? Comparator.naturalOrder(): Comparator.reverseOrder());
    }


}
