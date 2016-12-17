import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Разработать сервлет, который показывает двухуровневый список.
 * Список загружается из текстового файла на сервере.
 * Элементов первого и второго уровня может быть произвольное количество.
 * Элементы списков второго уровня смещены на 4 пробела вправо.
 * Реализовать интерфейс для удобной работы со списком на javascript.
 * При выводе в виде html список верхнего уровня становится нумерованным.
 * Около каждого элемента первого уровня выводится символ, при нажатии на который
 * скрываются или показываются элементы соответствующего списка второго уровня.
 */
public class ListServlet extends HttpServlet {
    private ConcurrentHashMap<String, LinkedList<String>> list = new ConcurrentHashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            InputStream inputFile = new FileInputStream("C:\\Users\\Наташа\\Dropbox\\Учёба\\прога\\ТехПрог\\16\\src\\list.txt");
            Scanner input = new Scanner(inputFile);
            LinkedList<String> subList = new LinkedList<>();
            String first = "";

            while (input.hasNext()) {
                Scanner line = new Scanner(input.nextLine());
                if (line.findInLine("    ") != null) {
                    subList.add(line.next());
                } else {
                    first = line.next();
                    if (subList.size() != 0)
                        list.put(first, subList);
                    subList = new LinkedList<>();
                }
                list.put(first, subList);
            }
            System.out.println("All: " + list + list.size());
            inputFile.close();

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("error while closing the file");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        out.println("<html><body><link href=\"styleForList.css\" rel=\"stylesheet\" type=\"text/css\"> ");
        ConcurrentHashMap.KeySetView<String, LinkedList<String>> strings = list.keySet();

        out.println("<ul type=\"decimal\">");//начинаем нумерованный список
        int i = 1;
        for(String title : list.keySet()){
            out.println("<li><div id=\"item" + i + "\" class=\"menu\">");
            out.println("<span class=\"title\">" + title + "</span><ul>");
            for(String item : list.get(title)){
                out.println("<li>" + item + "</li>");
            }
            out.println("</ul></div></li>");
            i++;
        }
        out.println("</ul>");//закрыли нумерованный список
        out.println("<button type=\"button\" onclick=\"view()\">Add view</button>");

        out.println("<ul type=\"decimal\" id=\"demo\"></ul>");

        out.println("<script>");
        for(i = 1; i <= list.size(); i++){
            out.println("var menuElem" + i + " = document.getElementById(" + "'item" + i + "');");
            out.println("var titleElem" + i + " = menuElem" + i + ".querySelector('.title');");
            out.println("titleElem" + i + ".onclick = function () {");
            out.println("menuElem" + i + ".classList.toggle('open');");
            out.println("};");
        }
        out.println("</script>");
        out.println("<script src=\"extraScript.js\"></script>");

        out.println("</body></html>");
        out.close();

    }
}
