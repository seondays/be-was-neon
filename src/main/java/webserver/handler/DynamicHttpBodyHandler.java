package webserver.handler;

import db.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import model.User;

public class DynamicHttpBodyHandler {
    public static final String USER_NAME_VIEW = "<p class=\"header__username\">안녕하세요 %s 님</p>";
    public static final String INSERT_INDEX = "<a href=\"/main\">";

    /**
     * body로 사용할 파일에 사용자 이름을 넣어 변경해준다.
     * todo : 따로 플래그를 심어서 그 부분에서 체크하는 것으로 변경해보기?
     * @param fileUrl
     * @return
     * @throws IOException
     */
    public byte[] readAddNameBody(String fileUrl, String name) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.contains(INSERT_INDEX)){
                sb.append(String.format((USER_NAME_VIEW),name)).append("\n");
            }
        }
        return sb.toString().getBytes();
    }

    public byte[] readUserListBody(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.contains("<tbody>")){
                for(User u: Database.findAll()) {
                    sb.append("<tr>").append("\n");
                    sb.append(String.format("<td>%s</td>", u.getUserId())).append("\n");
                    sb.append(String.format("<td>%s</td>", u.getName())).append("\n");
                    sb.append("</tr>").append("\n");
                }
            }
        }
        return sb.toString().getBytes();
    }
}