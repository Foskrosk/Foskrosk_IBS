package ru.appline.Servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ru.appline.Utils.Utils;
import ru.appline.logic.Model;
import ru.appline.logic.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/post")
public class ServletPost extends HttpServlet {
    Model model = Model.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        User user;

        if (request.getParameterMap().isEmpty()) {
            response.setContentType("application/json;charset=utf-8");

            JsonObject jObj = gson.fromJson(Utils.readRequest(request), JsonObject.class);
            PrintWriter pw = response.getWriter();

            user = Utils.parseUser(jObj, response, pw, "Cannot create user with this data");
            if (user == null)
                return;
            model.add(user);

            pw.print(gson.toJson(model.getModel()));
        } else {
            response.setContentType("text/html;charset=utf-8");

            PrintWriter pw = response.getWriter();

            user = Utils.parseUser(request, pw);
            if (user == null)
                return;
            model.add(user);

            Utils.printCrateUserMsg(pw, user);
        }
    }


}
