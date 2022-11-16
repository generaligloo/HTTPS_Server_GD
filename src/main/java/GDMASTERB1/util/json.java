package GDMASTERB1.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
//import com.fasterxml.jackson.core.*;

import java.io.IOException;


public class json {
    private static final ObjectMapper myObjMapper = defaultObjMapper();
    private static ObjectMapper defaultObjMapper(){
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }
    public static JsonNode parse(String jsonSrc) throws IOException {
        return myObjMapper.readTree(jsonSrc);
    }
    public static <A> A fromJson(JsonNode node, Class<A> ClassA) throws JsonProcessingException {
        return myObjMapper.treeToValue(node, ClassA);
    }
    public static JsonNode toJson(Object obj)
    {
        return myObjMapper.valueToTree(obj);
    }
    public static String stringify(JsonNode node) throws JsonProcessingException
    {
        return genJson(node, false);
    }
    public static String stringifyIndent(JsonNode node) throws JsonProcessingException
    {
        return genJson(node, true);
    }
    private static String genJson(Object obj, boolean bool) throws JsonProcessingException
    {
        ObjectWriter objWr;
        objWr = myObjMapper.writer();
        if (bool)
        {
            objWr = objWr.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objWr.writeValueAsString(obj);
    }
}
