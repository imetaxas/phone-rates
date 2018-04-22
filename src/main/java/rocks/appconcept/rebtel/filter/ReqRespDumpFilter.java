package rocks.appconcept.rebtel.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ReqRespDumpFilter implements Filter {

  private static final Logger logger = Logger.getLogger(ReqRespDumpFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    BasicConfigurator.configure();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      HttpServletResponse httpServletResponse = (HttpServletResponse) response;

      Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
      BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
      BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

      final StringBuilder logMessage = new StringBuilder("REST Request - ")
          .append("[HTTP METHOD:")
          .append(httpServletRequest.getMethod())
          .append("] [PATH INFO:")
          .append(httpServletRequest.getPathInfo())
          .append("] [REQUEST PARAMETERS:")
          .append(requestMap)
          .append("] [REQUEST BODY:")
          .append(bufferedRequest.getRequestBody())
          .append("] [REMOTE ADDRESS:")
          .append(httpServletRequest.getRemoteAddr())
          .append("]");

      chain.doFilter(bufferedRequest, bufferedResponse);
      logMessage.append(" [RESPONSE:").append(bufferedResponse.getContent()).append("]");
      logger.info(logMessage);
    } catch (Exception e) {
      logger.error(e);
    }
  }


  private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
    Map<String, String> typesafeRequestMap = new HashMap<String, String>();
    Enumeration<?> requestParamNames = request.getParameterNames();
    while (requestParamNames.hasMoreElements()) {
      String requestParamName = (String) requestParamNames.nextElement();
      String requestParamValue = request.getParameter(requestParamName);
      typesafeRequestMap.put(requestParamName, requestParamValue);
    }
    return typesafeRequestMap;
  }


  @Override
  public void destroy() {
    throw new UnsupportedOperationException();
  }


  private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private byte[] buffer = null;

    private BufferedRequestWrapper(HttpServletRequest req) throws IOException {
      super(req);
      // Read InputStream and store its content in a buffer.
      InputStream is = req.getInputStream();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int letti;
      while ((letti = is.read(buf)) > 0) {
        byteArrayOutputStream.write(buf, 0, letti);
      }
      this.buffer = byteArrayOutputStream.toByteArray();
    }


    @Override
    public ServletInputStream getInputStream() {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.buffer);
      return new BufferedServletInputStream(byteArrayInputStream);
    }

    String getRequestBody() throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
      String line;
      StringBuilder inputBuffer = new StringBuilder();
      do {
        line = reader.readLine();
        if (null != line) {
          inputBuffer.append(line.trim());
        }
      } while (line != null);
      reader.close();
      return inputBuffer.toString().trim();
    }

  }


  private static final class BufferedServletInputStream extends ServletInputStream {

    private ByteArrayInputStream bais;

    private BufferedServletInputStream(ByteArrayInputStream bais) {
      this.bais = bais;
    }

    @Override
    public int available() {
      return this.bais.available();
    }

    @Override
    public int read() {
      return this.bais.read();
    }

    @Override
    public int read(byte[] buf, int off, int len) {
      return this.bais.read(buf, off, len);
    }


  }

  private class TeeServletOutputStream extends ServletOutputStream {

    private final TeeOutputStream targetStream;

    private TeeServletOutputStream(OutputStream one, OutputStream two) {
      targetStream = new TeeOutputStream(one, two);
    }

    @Override
    public void write(int arg0) throws IOException {
      this.targetStream.write(arg0);
    }

    @Override
    public void flush() throws IOException {
      super.flush();
      this.targetStream.flush();
    }

    @Override
    public void close() throws IOException {
      super.close();
      this.targetStream.close();
    }
  }


  private class BufferedResponseWrapper implements HttpServletResponse {

    HttpServletResponse original;
    ByteArrayOutputStream bos;
    TeeServletOutputStream teeStream;

    PrintWriter teeWriter;

    private BufferedResponseWrapper(HttpServletResponse response) {
      original = response;
    }

    private String getContent() {
      if (bos != null) {
        return bos.toString();
      }
      return "";
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      if (this.teeWriter == null) {
        this.teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
      }
      return this.teeWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      if (teeStream == null) {
        bos = new ByteArrayOutputStream();
        teeStream = new TeeServletOutputStream(original.getOutputStream(), bos);
      }
      return teeStream;
    }

    @Override
    public String getCharacterEncoding() {
      return original.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
      return original.getContentType();
    }

    @Override
    public void setCharacterEncoding(String charset) {
      original.setCharacterEncoding(charset);
    }

    @Override
    public void setContentLength(int len) {
      original.setContentLength(len);
    }

    @Override
    public void setContentType(String type) {
      original.setContentType(type);
    }

    @Override
    public void setBufferSize(int size) {
      original.setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
      return original.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
      if (teeStream != null) {
        teeStream.flush();
      }
      if (this.teeWriter != null) {
        this.teeWriter.flush();
      }
    }

    @Override
    public void resetBuffer() {
      original.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
      return original.isCommitted();
    }

    @Override
    public void reset() {
      original.reset();
    }

    @Override
    public void setLocale(Locale loc) {
      original.setLocale(loc);
    }

    @Override
    public Locale getLocale() {
      return original.getLocale();
    }

    @Override
    public void addCookie(Cookie cookie) {
      original.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
      return original.containsHeader(name);
    }

    @Override
    public String encodeURL(String url) {
      return original.encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
      return original.encodeRedirectURL(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
      original.sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
      original.sendError(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
      original.sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
      original.setDateHeader(name, date);
    }

    @Override
    public void addDateHeader(String name, long date) {
      original.addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
      original.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
      original.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
      original.setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
      original.addIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
      original.setStatus(sc);
    }

    @Override
    public int getStatus() {
      return original.getStatus();
    }

    @Override
    public String getHeader(String s) {
      return original.getHeader(s);
    }

    @Override
    public Collection<String> getHeaders(String s) {
      return original.getHeaders(s);
    }

    @Override
    public Collection<String> getHeaderNames() {
      return original.getHeaderNames();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String sm) {
      original.setStatus(sc, sm);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeUrl(String url) {
      return original.encodeUrl(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeRedirectUrl(String url) {
      return original.encodeRedirectUrl(url);
    }
  }
}
