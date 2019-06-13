package pt.isel.ls.apps.http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServlet extends HttpServlet{


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        System.out.println("--New request was received --");
        System.out.println(req.getRequestURI());
        System.out.println(req.getMethod());
        System.out.println(req.getHeader("Accept"));

        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/plain; charset=%s",utf8.name()));

        String respBody = String.format("Current date and time is %s",new DateTime().toString());
        byte[] respBodyBytes = respBody.getBytes(utf8);
        resp.setStatus(200);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
    }
}
