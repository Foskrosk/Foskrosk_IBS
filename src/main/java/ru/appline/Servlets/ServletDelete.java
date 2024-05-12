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

@WebServlet(urlPatterns = "/delete")
public class ServletDelete extends HttpServlet {

    Model model = Model.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        PrintWriter pw = response.getWriter();
        Integer id = Utils.parseId(request, pw);
        if (id == null)
            return;

        User user = model.getModel().get(id);
        if (user == null) {
            Utils.printNullUser(pw, id);
        } else {
            model.getModel().remove(id);
            Utils.printUserWithId(pw, user, id);
        }

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();
        JsonObject jObj = gson.fromJson(Utils.readRequest(request), JsonObject.class);

        Integer id = Utils.parseId(response, jObj, pw);
        if (id == null)
            return;

        User user = model.getModel().get(id);
        if (user == null) {
            Utils.nullUserError(response, pw, "User with id " + id + " not found");
        } else {
            model.getModel().remove(id);
            pw.print(gson.toJson(user));
        }
    }
}
