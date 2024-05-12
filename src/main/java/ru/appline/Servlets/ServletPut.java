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

@WebServlet(urlPatterns = "/put")
public class ServletPut extends HttpServlet {

    Model model = Model.getInstance();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();
        Integer id = Utils.parseId(request, pw);
        if (id == null)
            return;

        User user = model.getModel().get(id);
        if (user == null) {
            Utils.printNullUser(pw, id);
            return;
        }

        user = Utils.parseUser(request, pw);
        if (user == null)
            return;

        model.getModel().get(id).setName(user.getName());
        model.getModel().get(id).setSurname(user.getSurname());
        model.getModel().get(id).setSalary(user.getSalary());

        Utils.printEditUserMsg(model, pw, id);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        JsonObject jObj = gson.fromJson(Utils.readRequest(request), JsonObject.class);
        PrintWriter pw = response.getWriter();

        Integer id = Utils.parseId(response, jObj, pw);
        if (id == null)
            return;

        User user = model.getModel().get(id);
        if (user == null) {
            Utils.nullUserError(response, pw, "User with id " + id + " not found");
            return;
        }
        user = Utils.parseUser(jObj, response, pw, "Cannot update user. Wrong data");
        if (user == null)
            return;

        model.getModel().get(id).setName(user.getName());
        model.getModel().get(id).setSurname(user.getSurname());
        model.getModel().get(id).setSalary(user.getSalary());

        pw.print(gson.toJson(model.getModel().get(id)));

    }
}
