package dynamicBodyModifier;

import db.Database;
import httpMethods.DynamicBodyModifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import model.Article;
import model.Comment;
import model.Comments;
import utils.DynamicConstant;

public class ArticleBodyModifier implements DynamicBodyModifier {
    public static final String NEW_LINE = "\n";

    public byte[] make(String allFile) throws IOException {
        return addAllArticle(allFile);
    }

    /**
     * 작성된 모든 게시글을 읽어와서 응답에 추가한다.
     *
     * @param allFile
     * @return
     * @throws IOException
     */
    public byte[] addAllArticle(String allFile) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(allFile));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(NEW_LINE);
            if (line.contains("<!--            Article-->")) {
                for (Article article : Database.findAllArticle()) {
                    sb.append(DynamicConstant.articleHttpFormat(article.getTextBody(), article.getArticleID(), "../img/Ham.gif"));
                    readComments(article.getComments(), article.commentSize(), sb, article.getArticleID());
                    sb.append(DynamicConstant.navigatorHttpFormat(article.getArticleID()));
                }
            }
        }
        return sb.toString().getBytes();
    }

    /**
     * 작성된 게시글에 댓글이 있을 경우 읽어와서 응답에 추가한다.
     *
     * @param comments
     * @param commentSize
     * @param sb
     * @param articleId
     */
    private void readComments(Comments comments, int commentSize, StringBuffer sb, int articleId) {
        final String START_COMMENT = "        <ul class=\"comment\", article-id=\"%s\">";
        final String START_BUTTON = "            <button class=\"btn btn_ghost btn_size_m show-all-btn\" article-id=\"%s\">\n";
        final String INFO_ALL_COMMENTS = "                모든 댓글 보기(%s개)\n";
        final String END_BUTTON = "            </button>";
        final String END_COMMENT = "        </ul>";

        sb.append(String.format(START_COMMENT, articleId)).append(NEW_LINE);
        if (commentSize > 0) {
            for (Comment comment : comments) {
                sb.append(DynamicConstant.commentHttpFormat(comment.getCommentBody()));
            }
        }
        sb.append(String.format(START_BUTTON, articleId)).append(NEW_LINE);
        sb.append(String.format(INFO_ALL_COMMENTS, commentSize)).append(NEW_LINE);
        sb.append(END_BUTTON).append(NEW_LINE);
        sb.append(END_COMMENT).append(NEW_LINE);
    }
}