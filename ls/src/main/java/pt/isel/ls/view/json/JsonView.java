package pt.isel.ls.view.json;

import pt.isel.ls.exceptions.CommandIOException;
import pt.isel.ls.http.HttpContent;
import pt.isel.ls.model.DataTable;
import pt.isel.ls.result.Result;
import pt.isel.ls.view.View;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class JsonView extends View {

    //Main build method
    private JsonObject buildView(Result target) {
        try {
            Class resClass = target.getClass();

            Field [] fields = resClass.getDeclaredFields();

            JsonObject ret = new JsonObject();
            for(Field f : fields)
            {
                //in case field private
                f.setAccessible(true);
                Object val = f.get(target);

                //if is model entity
                if(val instanceof DataTable)
                {
                    JsonObject data = buildJsonObject(val);
                    JsonValue value = new JsonValue(data);
                    JsonPair pair = new JsonPair(
                            ((DataTable) val).getTableName() , value);
                    ret.addMember(pair);
                }

                //if is list
                else if(val instanceof List )
                {
                    JsonArray arr = buildJsonArray(val);
                    JsonValue value = new JsonValue(arr);
                    JsonPair pair = new JsonPair(f.getName() , value);
                    ret.addMember(pair);
                }

                //"simple" vals (int , string, etc...)
                else
                {
                    JsonValue value = new JsonValue(val);
                    JsonPair pair = new JsonPair(f.getName() , value);
                    ret.addMember(pair);
                }
            }


            return ret;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //build "complex" objects
    private JsonObject buildJsonObject(Object o)
    {
        try {
            Class c = o.getClass();
            Field[] fields = c.getFields();

            Object fval;

            JsonObject ret = new JsonObject();

            //for each field build a json value
            for (Field f : fields) {
                //
                f.setAccessible(true);
                fval = f.get(o);
                JsonValue val;

                //field is a list, need mapping to json array
                if(fval instanceof List)
                {
                   val =  new JsonValue(buildJsonArray(fval));
                }

                //if is model entity
                else if(fval instanceof DataTable)
                {
                    JsonObject data = buildJsonObject(fval);
                    val = new JsonValue(data);

                }
                else {
                    val = new JsonValue(fval);
                }

                //build the pair
                JsonPair p = new JsonPair(f.getName(), val);

                ret.addMember(p);
            }

            return ret;
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private JsonArray buildJsonArray(Object fval)
    {
        List list = (List) fval;
        JsonArray ret = new JsonArray();
        for(Object o : list)
        {
            JsonObject jo = buildJsonObject(o);
            ret.addValue(new JsonValue(jo));
        }

        return ret;
    }

    @Override
    public void show(Result target , Writer writer) throws CommandIOException
    {
        try {
            buildView(target).writeTo(writer);
        }catch (IOException e)
        {
            throw new CommandIOException(
                    String.format("Error writing json object to output %s", writer)
            );
        }
    }

    @Override
    public HttpContent getContent(Result target) {
        return buildView(target);
    }
}
