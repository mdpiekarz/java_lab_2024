

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ProductTest {
    @Test
    void priceIndexTest() {
        int year = 2011;
        int month = 5;
        int index = 16;
        Assertions.assertEquals(index,Product.priceIndex(year,month));
    }

    @Test
    void testPriceIndexTest() {
        Assertions.assertThrows(IndexOutOfBoundsException.class,()->Product.priceIndex(2030,1));
    }

    @ParameterizedTest
    @MethodSource("testPriceIdnexTest3Parameters")
    void testPriceIdnexTest3(int year, int month, int index){
        Assertions.assertEquals(index,Product.priceIndex(year,month));
    }

    static Stream<Arguments> testPriceIdnexTest3Parameters(){
        return Stream.of(
                Arguments.of(2012,12,35),
                Arguments.of(2022,3,146)
        );
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/dates.csv")
    void testPriceIndexTest4( String data, int year, int month, int index){
        Assertions.assertEquals(index,Product.priceIndex(year,month));
    }

}
