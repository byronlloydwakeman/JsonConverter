package jsonserializer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonConverterTest {

    private final JsonConverter converter = new JsonConverter();
    @Test
    void objectToString()
    {
        Model model = new Model();
        model.Age = 19;
        model.Name = "Byron";
        try {
            var actual = converter.ObjectToString(model);
            String expected = "{ \"Age\": 19, \"Name\": \"Byron\" }";
            Assertions.assertEquals(expected, actual);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        }
    }

    @Test
    void StringToObject() throws NoSuchFieldException, IllegalAccessException {
        String string = "{ Age: 19, Name: Byron }";
        var object = converter.StringToObject(string, new Model());
    }

    @Test
    void isNumber()
    {
        String value = "doawd";
        boolean expected = false;
        JsonConverter converter = new JsonConverter();
        boolean actual =  converter.IsNumber(value);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void CanConvertIntoNumber()
    {
        boolean expected = false;
        boolean actual = converter.CanConvertIntoNumber("a");
        Assertions.assertEquals(expected, actual);
    }
}