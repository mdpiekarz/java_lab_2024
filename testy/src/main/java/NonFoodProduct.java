

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class NonFoodProduct {
    Double[] prices;
    String name;

    private NonFoodProduct(String name, Double[] prices) {
        this.prices = prices;
    }

    public String getName(){
        return name;
    }

    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine();

            scanner.nextLine();
            prices = Arrays.stream(scanner.nextLine().split(";"))
                    .map(value -> value.replace(",","."))
                    .map(Double::valueOf)
                    .toArray(Double[]::new);

            scanner.close();

            return new NonFoodProduct(name, prices);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
