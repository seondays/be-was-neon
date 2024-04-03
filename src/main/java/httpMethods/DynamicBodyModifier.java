package httpMethods;

import java.io.IOException;

public interface DynamicBodyModifier {
    public byte[] make(String allFile) throws IOException;
}
