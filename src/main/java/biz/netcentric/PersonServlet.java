package biz.netcentric;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * Created by kanke on 27/09/2015.
 */
@WebServlet(name = "PersonServlet", urlPatterns = {"/Sightly"})
public class PersonServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServlet.class);

    private String parameter;
    private String path;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        path = getURI(request);

        // Set response content type
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println(htmlParser());
        out.println(htmlExtractor());
        out.println(extractPersonFromScript());
        out.println(processScriptEngine(parameter));

        out.close();
    }


    /**
     * Gets the request URI including the query parameter value for lookup
     * Person.lookup(id)
     */
    public String getURI(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getHeader("Host");        // includes server name and server port
        String contextPath = request.getContextPath();  // includes leading forward slash
        parameter = request.getParameter("id");
        return scheme + "://" + host + contextPath;
    }


    /**
     * Parse Html using Jsoup library and return string
     */
    public String htmlParser() {
        Document document = getDocument();
        return document != null ? document.toString() : null;
    }


    /**
     * Get Document form request URI
     */

    public Document getDocument() {
        Document doc = null;
        try {
            doc = Jsoup.connect(path).get();
        } catch (IOException e) {
            LOGGER.error("Exception to get document from request URI" + path, e);
        }
        return doc;
    }

    /**
     * Parse Html (get body elements only) using Jsoup library and return string
     */
    public String htmlExtractor() {
        StringBuilder Elements = new StringBuilder();
        Document document = getDocument();
        Elements pElements = document.select("body");

        for (Element element : pElements) {
            Elements.append(element.text());
        }
        return Elements.toString();
    }

    /**
     * Checks if element is
     * <script type="server/javascript">
     */
    public boolean isServerScript(Element element) {
        String tagName = element.tagName();
        String attrType = element.attr("type");
        return "script".equals(tagName) && "server/javascript".equals(attrType);
    }


    public String extractPersonFromScript() {
        Document document = getDocument();

        Elements scripts = document.getElementsByTag("script");
        String person = null;
        for (Element element : scripts) {
            if (isServerScript(element)) {
                String scriptText = element.toString();

                /**
                 * Extract below from script tag for script engine
                 *
                 * var person=Person.lookup(id)
                 */
                BufferedReader br = null;
                try {
                    String sCurrentLine;
                    br = new BufferedReader(new StringReader(scriptText));
                    try {
                        while ((sCurrentLine = br.readLine()) != null) {
                            if (sCurrentLine.contains("var person")) {
                                person = sCurrentLine;
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error("Error reading form text:" + scriptText, e);
                    }
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException ex) {
                        LOGGER.error("Error closing BufferedReader" + br, ex);
                    }
                }

            }
        }

        return person != null ? person : "person";
    }


    /**
     * From servlet request, get request id
     * var id=request.getParameter("id")
     * var person=Person.lookup(id)
     */
    public String processScriptEngine(String id) {
        ScriptEngine engine = getScriptEngine();
        Object o = null;
        /**
         * split statement to get variable name and variable value
         * var person = Person.lookup()
         * "person" , "Person.lookup()"
         */

        engine.put("object", Person.lookup(id).toString());
        try {
            engine.eval("var person =  object ");
            o = engine.get("person");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return o != null ? o.toString() : null;
    }


    /**
     * Gets the script engine to be used for page rendering.
     * This servlet returns Java's built-in javascript engine.
     */
    public ScriptEngine getScriptEngine() {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        return engineManager.getEngineByName("JavaScript");
    }

}
