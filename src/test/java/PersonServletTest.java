import biz.netcentric.PersonServlet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mockito.Spy;

import javax.script.ScriptEngine;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

/**
 * Created by kanke on 30/09/2015.
 */


public class PersonServletTest {

    @Spy
    private PersonServlet personServlet = mock(PersonServlet.class);


    @Test
    public void shouldGetUri() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String uri = "http://localhost:8080/Sightly/person?id=2";
        when(personServlet.getURI(request)).thenReturn(uri);

        assertEquals(personServlet.getURI(request), uri);

        verify(personServlet, times(1)).getURI(request);
    }

    @Test
    public void shouldVerifyUri() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        String uri = "http://localhost:8080/Sightly/person?id=2";

        when(request.getScheme()).thenReturn("http");
        when(request.getHeader("Host")).thenReturn("localhost:8080");
        when(request.getContextPath()).thenReturn("/Sightly/person?id=2");

        String request_uri = request.getScheme() + "://" + request.getHeader("Host") + request.getContextPath();

        assertEquals(uri, request_uri);
    }

    @Test
    public void shouldGetWongUri() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String uri = "http://localhost:8080/Sightly/person?id=2";
        when(personServlet.getURI(request)).thenReturn("http://localhost:8090/Slightly/personk");

        assertNotEquals(personServlet.getURI(request), uri);

        verify(personServlet, times(1)).getURI(request);
    }


    @Test
    public void shouldGetContentType() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getContentType()).thenReturn("text/html");

        assertEquals("text/html", response.getContentType());

    }

    @Test
    public void shouldGetWrongContentType() throws ServletException, IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getContentType()).thenReturn("script/html");

        assertNotEquals("text/html", response.getContentType());

    }


    @Test
    public void shouldGetDocument() throws IOException, ServletException {

        org.jsoup.nodes.Document doc = mock(org.jsoup.nodes.Document.class);
        when(personServlet.getDocument()).thenReturn(doc);

        assertEquals(personServlet.getDocument(), doc);

        verify(personServlet, times(1)).getDocument();
    }

    @Test
    public void shouldExtractPersonFromScript() throws IOException, ServletException {
        String script = "var person=Person.lookup(id)";

        when(personServlet.extractPersonFromScript()).thenReturn(script);

        assertEquals(personServlet.extractPersonFromScript(), script);

        verify(personServlet, times(1)).extractPersonFromScript();
    }

    @Test
    public void isServerSideScript() throws IOException, ServletException {
        Element element = mock(Element.class);
        String tagName = "script";
        String attrType = "server/javascript";

        when(element.tagName()).thenReturn("script");
        when(element.attr("server/javascript")).thenReturn("server/javascript");

        assertEquals(tagName, element.tagName());

        assertEquals(attrType, element.attr("server/javascript"));

    }

    @Test
    public void isNotServerSideScript() throws IOException, ServletException {
        Element element = mock(Element.class);
        String tagName = "script";
        String attrType = "sever/javascript";

        when(element.tagName()).thenReturn("h1");
        when(element.attr("text/javascript")).thenReturn("text/javascript");

        assertNotEquals(tagName, element.tagName());

        assertNotEquals(attrType, element.attr("server/javascript"));

    }


    @Test
    public void shouldExtractPersonFromHtml() throws IOException, ServletException {


        BufferedReader bufferedReader = mock(BufferedReader.class);

        when(bufferedReader.readLine()).thenReturn("script");

        when(personServlet.htmlExtractor()).thenReturn("script");

        assertEquals(personServlet.htmlExtractor(), bufferedReader.readLine());

    }

    @Test
    public void shouldParseHtml() throws IOException, ServletException {

        Document document = mock(Document.class);

        String mock = document.toString();

        when(personServlet.htmlParser()).thenReturn(mock);

        assertEquals(personServlet.htmlParser(), document.toString());
        verify(personServlet, times(1)).htmlParser();
    }


    @Test
    public void shouldReturnScriptEngine() throws IOException, ServletException {

        ScriptEngine engine = mock(ScriptEngine.class);
        when(personServlet.getScriptEngine()).thenReturn(engine);

        assertEquals(personServlet.getScriptEngine(), engine);

        verify(personServlet, times(1)).getScriptEngine();

    }
}


