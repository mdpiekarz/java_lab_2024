

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Map;

public class FoodProductTest {
    @Test
    void fromCsvNameTest(){
        FoodProduct product = FoodProduct.fromCsv(Path.of("src/test/resources/testowyplik.csv"));

        String nameProduct = product.getName();
        Assertions.assertEquals("maslo",nameProduct);
    }

    @Test
    void fromCsvPriceTest(){
        FoodProduct product = FoodProduct.fromCsv(Path.of("src/test/resources/testowyplik.csv"));

        Double productPrice = product.getPrice(2010, 2, "XXX");
        Assertions.assertEquals(6.78,productPrice);
    }
    @Test
    void fromCsvPriceTest2() throws NoSuchFieldException, IllegalAccessException {
        FoodProduct product = FoodProduct.fromCsv(Path.of("src/test/resources/testowyplik.csv"));
        Field pricesField = FoodProduct.class.getDeclaredField("prices");
        pricesField.setAccessible(true);
        Map<String,Double[]> prices = (Map)pricesField.get(product);
        Assertions.assertEquals(6.78,prices.get("XXX")[1]);
    }
}
