package dynamicBodyModifier;

import db.Database;
import httpMethods.DynamicBodyModifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import model.User;

public class UserListBodyModifier implements DynamicBodyModifier {
    public static final String NEW_LINE = "\n";

    public byte[] make(String allFile) throws IOException{
        return readUserListBody(allFile);
    }

    /**
     * 가입 유저 목록을 가져와 응답에 추가한다.
     *
     * @param allFile
     * @return
     * @throws IOException
     */
    public byte[] readUserListBody(String allFile) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(allFile));
        StringBuffer sb = new StringBuffer();
        String line;
        final String INSERT_INDEX = "<tbody>";

        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.contains(INSERT_INDEX)) {
                for (User u : Database.findAll()) {
                    sb.append("<tr>").append(NEW_LINE);
                    sb.append(String.format("<td>%s</td>", u.getUserId())).append(NEW_LINE);
                    sb.append(String.format("<td>%s</td>", u.getName())).append(NEW_LINE);
                    sb.append("</tr>").append(NEW_LINE);
                }
            }
        }
        return sb.toString().getBytes();
    }
}
