import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Read {

    private Path filepath;
    private Stream<String> stream;
    private List<String> list;
    private Stream<String> streams;

    public Read(String s)
    {
        filepath = new File(getClass().getResource(s).getFile()).toPath();
        run();
    }
    public  void run() {

        try (Stream<String> stream = Files.lines(filepath)) {
            list =  stream.collect(Collectors.toList());
            Collections.shuffle(list);


            streams = list.stream();
            streams.limit(10).forEach(word -> System.out.print(word + " "));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



