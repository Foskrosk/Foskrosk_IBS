package ru.appline.Servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ru.appline.Utils.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/calculate")
public class CalculatorServletPost extends HttpServlet {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getParameterMap().isEmpty()) {
            response.setContentType("application/json;charset=utf-8");

            JsonObject jObj = gson.fromJson(Utils.readRequest(request), JsonObject.class);
            PrintWriter pw = response.getWriter();

            Map<String, String> expression = Utils.parseCalculator(jObj, response, pw);
            if (expression == null)
                return;

            Double res = Utils.calculate(expression, jObj, response, pw, request);
            if (res == null)
                return;

            JsonObject jsonRes = new JsonObject();
            jsonRes.add("result", new JsonPrimitive(res));

            pw.print(jsonRes);
        } else {
            response.setContentType("text/html;charset=utf-8");

            PrintWriter pw = response.getWriter();

            Map<String, String> expression = Utils.parseCalculator(request, pw);
            if (expression == null)
                return;

            Double res = Utils.calculate(expression, null, response, pw, request);
            if (res == null)
                return;

            Utils.printResultOfCalculation(pw, res);
        }
    }
}
