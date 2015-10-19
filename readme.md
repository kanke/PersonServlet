
*Prior to this, I haven't used Jetty or Nashorn ot HTML5 template tag.

In AEM, I would have used a sling rewriter that implements the Transformer and TransformerFactory Interface.

#Parsers (Things I considered)

- I did research on parsing the HTML using an XML parser library
- Another option was to use Xalan-Java to respond to requests for XML documents by transforming those documents into HTML and serving them to web browsers. To respond to HTTP GET requests, all you need to do is overwrite the HttpServlet doGet() method with a procedure that instantiates a Transformer and uses it to perform a transformation. As the following example shows, you can generate a ResultStream that a PrintWriter writes to the HttpResponse OutputStream, returning the transformation output to the web browser.
- Serializing and de-seralizing
- Lonely storm
- Java Reflection api
- I also considered using HTML5 template tag to clone and replace contents and attributes of elements after eval from Nashorn as template doesn't provide data binding like mustache js or angular js. It will be creating client side JS.


#Test
- Run <code>mvn jetty: run</code> to start server
- Navigate to <code>http://localhost:8080/Sightly/person?id=2</code> to see results of servlets
- Change Id parameter to get different Person result e.g; <code>http://localhost:8080/Sightly/person?id=3</code>,<code>http://localhost:8080/Sightly/person?id=4</code> or no parameter at all <code>http://localhost:8080/Sightly/person</code> 


#TODO
Unfortunately, i didn't have enough time to research all options and go for a better approach due to the time constraints

- Send resulting html to browser in better format
- Support state in session?
- Checking for HTML parsing error and element tags like CDATA etc
- Evaluates and resolve data-var- attribute , $-expression, data-if and data-for-x


#Optional : Inclusion

- Yes it is possible to for one template to include another. I tried this but didn't and made some progress. Code was a bit here and there and that it why it wasn't included in this project. HTML5  <template> element allows developers to create client-side templates made up of chunks of reusable DOM. Because it is client side, templates are not visible in the browser until you activate them using Javascript.