package utils;

public class DynamicConstant {

    public static String commentHttpFormat(String commentBody) {
        String comment = "            <li class=\"comment__item hidden\">\n"
                + "                <div class=\"comment__item__user\">\n"
                + "                    <img class=\"comment__item__user__img\"/>\n"
                + "                    <p class=\"comment__item__user__nickname\">account</p>\n"
                + "                </div>\n"
                + String.format("                <p class=\"comment__item__article\">%s</p>\n", commentBody)
                + "            </li>";
        return comment;
    }

    public static String articleHttpFormat(String articleBody, int articleId) {
        String article = "        <div class=\"post\">\n"
                + "<div class=\"post__account\">\n"
                + "                <img class=\"post__account__img\"/>\n"
                + "                <p class=\"post__account__nickname\">account</p>\n"
                + "            </div>\n"
                + "            <img class=\"post__img\"/>\n"
                + "            <div class=\"post__menu\">\n"
                + "                <ul class=\"post__menu__personal\">\n"
                + "                    <li>\n"
                + "                        <button class=\"post__menu__btn\">\n"
                + "                            <img src=\"../img/like.svg\"/>\n"
                + "                        </button>\n"
                + "                    </li>\n"
                + "                    <li>\n"
                + "                        <button class=\"post__menu__btn\">\n"
                + "                            <img src=\"../img/sendLink.svg\"/>\n"
                + "                        </button>\n"
                + "                    </li>\n"
                + "                </ul>\n"
                + "                <button class=\"post__menu__btn\">\n"
                + "                    <img src=\"../img/bookMark.svg\"/>\n"
                + "                </button>\n"
                + "            </div>\n"
                + String.format("            <p class=\"post__article\", article-id=\"%s\">\n", articleId)
                + String.format("%s\n", articleBody)
                + "            </p>\n"
                + "        </div>\n";
        return article;
    }

    public static String navigatorHttpFormat(int articleId) {
        String navi = "        <nav class=\"nav\">\n"
                + "            <ul class=\"nav__menu\">\n"
                + "                <li class=\"nav__menu__item\">\n"
                + "                    <a class=\"nav__menu__item__btn\" href=\"\">\n"
                + "                        <img\n"
                + "                                class=\"nav__menu__item__img\"\n"
                + "                                src=\"../img/ci_chevron-left.svg\"\n"
                + "                        />\n"
                + "                        이전 글\n"
                + "                    </a>\n"
                + "                </li>\n"
                + "                <li class=\"nav__menu__item\">\n"
                + "                    <!--                    add-articleID-->\n"
                + String.format(
                "                    <a class=\"btn btn_ghost btn_size_m\" href=\"/comment?article-id=%s\">댓글 작성</a>\n",
                articleId)
                + "                </li>\n"
                + "                <li class=\"nav__menu__item\">\n"
                + "                    <a class=\"nav__menu__item__btn\" href=\"\">\n"
                + "                        다음 글\n"
                + "                        <img\n"
                + "                                class=\"nav__menu__item__img\"\n"
                + "                                src=\"../img/ci_chevron-right.svg\"\n"
                + "                        />\n"
                + "                    </a>\n"
                + "                </li>\n"
                + "            </ul>\n"
                + "        </nav>\n";
        return navi;
    }
}