package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Function;

public class ThrowableReaderFunction implements Function<BufferedReader,String> {

    /*reader -> {
                    try {
                        return reader.readLine();   //BufferedReader -> String
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    }

    *///<==>

    private static String ioApply (BufferedReader reader){
        try{
            return reader.readLine();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String apply(BufferedReader reader) {
        return ioApply(reader);
    }
}
