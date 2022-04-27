package jsonserializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JsonConverter
{
    public <T> String ObjectToString(T object) throws IllegalAccessException
    {
        StringBuilder stringToReturn = new StringBuilder("{");
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            stringToReturn.append(" \"").append(fields[i].getName()).append("\": ");

            //Check whether type is a number than it won't need ""
            Object propertyValue = object.getClass().getDeclaredFields()[i].get(object);
            if (IsNumber(propertyValue))
            {
                //If it's the last entry
                if(i == fields.length - 1)
                {
                    stringToReturn.append(propertyValue);
                }
                else
                {
                    stringToReturn.append(propertyValue).append(",");
                }
            }
            else
            {
                //If it's the last entry
                if(i == fields.length - 1)
                {
                    stringToReturn.append("\"").append(propertyValue).append("\"");
                }
                else
                {
                    stringToReturn.append("\"").append(propertyValue).append("\",");
                }
            }
        }

        stringToReturn.append(" }");

        return stringToReturn.toString();
    }

    public <T> T StringToObject(String string, T object) throws NoSuchFieldException, IllegalAccessException {
        //We need to convert a string into a model

        //String will be in the form
        /*{
          "Name" : "Byron",
          "Age" : 15
        }*/

        string = string.replaceAll("\\s+", "");
        int startingIndex = -1;
        char startingIndexValue = ' ';
        List<String> listOfAllFieldsAndValues = new ArrayList<>();
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).toList();

        for (int i = 0; i < string.length(); i++)
        {
            if(string.charAt(i) == '{')
            {
                startingIndex = i;
                startingIndexValue = '{';
            }
            else if(string.charAt(i) == ':')
            {
                if(startingIndexValue == '{' || startingIndexValue == ',')
                {
                    listOfAllFieldsAndValues.add(string.substring(startingIndex + 1, i));
                    startingIndex = i;
                    startingIndexValue = string.charAt(i);
                }
                else
                {
                    startingIndex = i;
                    startingIndexValue = string.charAt(i);
                }
            }
            else if(string.charAt(i) == ',')
            {
                if(startingIndexValue == ':')
                {
                    listOfAllFieldsAndValues.add(string.substring(startingIndex + 1, i));
                    startingIndex = i;
                    startingIndexValue = string.charAt(i);
                }
                else
                {
                    startingIndex = i;
                    startingIndexValue = string.charAt(i);
                }
            }
            else if(string.charAt(i) == '}')
            {
                listOfAllFieldsAndValues.add(string.substring(startingIndex + 1, i));
            }
        }

        Model model = new Model();
        for(int i = 0; i < (long) listOfAllFieldsAndValues.size(); i += 2)
        {
            String value = listOfAllFieldsAndValues.get(i + 1);
            if (CanConvertIntoNumber(value))
            {
                Field f1 = Model.class.getField(listOfAllFieldsAndValues.get(i));
                f1.set(model, Integer.valueOf(listOfAllFieldsAndValues.get(i + 1)));
            }
            else
            {
                Field f1 = Model.class.getField(listOfAllFieldsAndValues.get(i));
                f1.set(model, listOfAllFieldsAndValues.get(i + 1));
            }
        }

        return object;
    }

    public boolean CanConvertIntoNumber(String string)
    {
        try
        {
            var integer = Integer.valueOf(string);
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    public boolean IsNumber(Object o)
    {
        return Number.class.isAssignableFrom(o.getClass());
    }
}
