package ru.appline.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ru.appline.logic.Model;
import ru.appline.logic.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static String readRequest(HttpServletRequest request) {
        StringBuilder jb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
        return String.valueOf(jb);
    }

    public static Integer parseId(HttpServletRequest request, PrintWriter pw) {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            pw.print("<html>" +
                    "<h3>Не удалось распарсить id в " +
                    URLDecoder.decode(request.getParameterMap().keySet()
                                    .stream()
                                    .map(key -> key + "=" + Arrays.toString(request.getParameterMap().get(key)))
                                    .collect(Collectors.joining(", ", "{", "}")),
                            StandardCharsets.UTF_8) +
                    "</h3>" +
                    "<a href=\"index.jsp\">Домой</a>" +
                    "</html>");
            return null;
        }
        return id;
    }

    public static Integer parseId(HttpServletResponse response, JsonObject jObj, PrintWriter pw) {
        int id;
        try {
            id = jObj.get("id").getAsInt();
        } catch (NumberFormatException | NullPointerException e) {
            JsonObject error = new JsonObject();
            error.add("errorCode", new JsonPrimitive(400));
            error.add("errorDescription", new JsonPrimitive("Cannot parse id in " + jObj));
            response.setStatus(400);
            pw.print(error);
            return null;
        }
        return id;
    }

    public static User parseUser(JsonObject jObj, HttpServletResponse response, PrintWriter pw, String errorMsg) {
        User user;
        try {
            user = new User(
                    jObj.get("name").getAsString(),
                    jObj.get("surname").getAsString(),
                    jObj.get("salary").getAsDouble()
            );
        } catch (NumberFormatException | NullPointerException e) {
            JsonObject error = new JsonObject();
            error.add("errorCode", new JsonPrimitive(400));
            error.add("errorDescription", new JsonPrimitive(errorMsg + " " + jObj));
            response.setStatus(400);
            pw.print(error);
            return null;
        }
        return user;
    }

    public static User parseUser(HttpServletRequest request, PrintWriter pw) {
        User user;
        try {
            user = new User(
                    request.getParameter("name"),
                    request.getParameter("surname"),
                    Double.parseDouble(request.getParameter("salary"))
            );
        } catch (NumberFormatException | NullPointerException e) {
            pw.print("<html>" +
                    "<h3>Не удалось создать пользователя по вашим данным " +
                    URLDecoder.decode(request.getParameterMap().keySet()
                                    .stream()
                                    .map(key -> key + "=" + Arrays.toString(request.getParameterMap().get(key)))
                                    .collect(Collectors.joining(", ", "{", "}")),
                            StandardCharsets.UTF_8) +
                    "</h3>" +
                    "<a href=\"index.jsp\">Домой</a>" +
                    "</html>");
            return null;
        }
        return user;
    }

    public static void nullUserError(HttpServletResponse response, PrintWriter pw, String msg) {
        JsonObject error = new JsonObject();
        error.add("errorCode", new JsonPrimitive(404));
        error.add("errorDescription", new JsonPrimitive(msg));
        response.setStatus(400);
        pw.print(error);
    }

    public static void printAllUsers(PrintWriter pw, Model model) {
        pw.print("<html>" +
                "<h3> Доступные пользователи: </h3><br/>" +
                "ID пользователя: " +
                "<ul>");
        for (Map.Entry<Integer, User> entry : model.getModel().entrySet()) {
            pw.print("<li>" + entry.getKey() + "</li>" +
                    "<ul>" +
                    "<li>Имя: " + entry.getValue().getName() + "</li>" +
                    "<li>Фамилия: " + entry.getValue().getSurname() + "</li>" +
                    "<li>Зарплата: " + entry.getValue().getSalary() + "</li>" +
                    "</ul>");
        }
        pw.print("</ul>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static void printNullUser(PrintWriter pw, int id) {
        pw.print("<html>" +
                "<h3>Пользователя с id " + id + " нет :(</h3>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static void printUser(PrintWriter pw, Model model, int id) {
        pw.print("<html>" +
                "<h3>Запрошенный пользователь:</h3>" +
                "<br/>" +
                "Имя: " + model.getModel().get(id).getName() + "<br/>" +
                "Фамилия: " + model.getModel().get(id).getSurname() + "<br/>" +
                "Зарплата: " + model.getModel().get(id).getSalary() + "<br/>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static void printUserWithId(PrintWriter pw, User user, int id) {
        pw.print("<html>" +
                "<h3>Удаленный пользователь:</h3>" +
                "<br/>" +
                "ID: " + id + "<br/>" +
                "Имя: " + user.getName() + "<br/>" +
                "Фамилия: " + user.getSurname() + "<br/>" +
                "Зарплата: " + user.getSalary() + "<br/>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static void printCrateUserMsg(PrintWriter pw, User user) {
        pw.println("<html>" +
                "<h3>Пользователь " + user.getName() + " " + user.getSurname() + " с зарплатой= " + user.getSalary() + " успешно создан! :)</h3>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static void printEditUserMsg(Model model, PrintWriter pw, int id) {
        pw.println("<html>" +
                "<h3>Данные изменены " + model.getModel().get(id).getName() + " " + model.getModel().get(id).getSurname() +
                " с зарплатой= " + model.getModel().get(id).getSalary() + "</h3>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

    public static Map<String, String> parseCalculator(JsonObject jObj, HttpServletResponse response, PrintWriter pw) {
        Map<String, String> res = new HashMap<>();
        try {
            res.put("a", jObj.get("a").getAsString());
            res.put("b", jObj.get("b").getAsString());
            res.put("math", jObj.get("math").getAsString());
        } catch (NumberFormatException | NullPointerException e) {
            JsonObject error = new JsonObject();
            error.add("errorCode", new JsonPrimitive(400));
            error.add("errorDescription", new JsonPrimitive("Cannot calculate with this data" + jObj));
            response.setStatus(400);
            pw.print(error);
            return null;
        }
        return res;
    }

    public static Map<String, String> parseCalculator(HttpServletRequest request, PrintWriter pw) {
        Map<String, String> res = new HashMap<>();
        try {
            res.put("a", request.getParameter("a"));
            res.put("b", request.getParameter("b"));
            res.put("math", request.getParameter("math"));
        } catch (NumberFormatException | NullPointerException e) {
            pw.print("<html>" +
                    "<h3>Не удалось произвести расчет по этим данным " +
                    URLDecoder.decode(request.getParameterMap().keySet()
                                    .stream()
                                    .map(key -> key + "=" + Arrays.toString(request.getParameterMap().get(key)))
                                    .collect(Collectors.joining(", ", "{", "}")),
                            StandardCharsets.UTF_8) +
                    "</h3>" +
                    "<a href=\"index.jsp\">Домой</a>" +
                    "</html>");
            return null;
        }
        return res;
    }

    public static Double calculate(Map<String, String> expression, JsonObject jObj, HttpServletResponse response, PrintWriter pw, HttpServletRequest request) {
        double a;
        double b;
        String math;

        try {
            a = Double.parseDouble(expression.get("a"));
            b = Double.parseDouble(expression.get("b"));
            math = expression.get("math");
        } catch (NumberFormatException e) {
            if (jObj != null) {
                JsonObject error = new JsonObject();
                error.add("errorCode", new JsonPrimitive(400));
                error.add("errorDescription", new JsonPrimitive("Cannot parse this data " + jObj));
                response.setStatus(400);
                pw.print(error);
            } else {
                pw.print("<html>" +
                        "<h3>Не удалось произвести расчет по вашим данным " +
                        URLDecoder.decode(request.getParameterMap().keySet()
                                        .stream()
                                        .map(key -> key + "=" + Arrays.toString(request.getParameterMap().get(key)))
                                        .collect(Collectors.joining(", ", "{", "}")),
                                StandardCharsets.UTF_8) +
                        "</h3>" +
                        "<a href=\"index.jsp\">Домой</a>" +
                        "</html>");
            }
            return null;
        }

        switch (math) {
            case "*" -> {
                return a * b;
            }
            case "/" -> {
                return a / b;
            }
            case "+" -> {
                return a + b;
            }
            case "-" -> {
                return a - b;
            }
            default -> {
                if (jObj != null) {
                    JsonObject error = new JsonObject();
                    error.add("errorCode", new JsonPrimitive(400));
                    error.add("errorDescription", new JsonPrimitive("Unsupported math operation " + jObj.get("math")));
                    response.setStatus(400);
                    pw.print(error);
                } else {
                    pw.print("<html>" +
                            "<h3>Неподдерживаемая операция " + request.getParameter("math") +
                            "</h3>" +
                            "<a href=\"index.jsp\">Домой</a>" +
                            "</html>");
                }
                return null;
            }
        }
    }

    public static void printResultOfCalculation(PrintWriter pw, double res) {
        pw.print("<html>" +
                "<h3>Результат " + res +
                "</h3>" +
                "<a href=\"index.jsp\">Домой</a>" +
                "</html>");
    }

}
