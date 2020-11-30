package base;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //1.设置方法存储器
            String methodname = null;
            //2.获取Post请求的Content-type类型
            String contentType = request.getHeader("Content-Type");
            //3.判断传递的数据是不是JSON格式
            if ("application/json;charset=utf-8".equals(contentType)) {
                //是JSON格式,调用相应方法
                String postJSON = BaseServlet.getPostJSON(request);
                //将JSON格式的字符串转化为map
                Map<String, Object> resultmap = JSON.parseObject(postJSON, Map.class);
                //map集合中获取methodname
                methodname = (String) resultmap.get("methodName");
                //将获取到的数据,保存到request域对象中
                request.setAttribute("resultmap", resultmap);
            } else {
                methodname = request.getParameter("methodName");
            }
            if (methodname != null) {
                //0.反射开始
                //1.获取相应的字节码文件
                Class aClass = this.getClass();
                //2.根据传入的方法名,获取对应的方法对象
                Method method = aClass.getMethod(methodname, HttpServletRequest.class, HttpServletResponse.class);
                //3.调用相应的invoke来完成相应的功能
                method.invoke(this, request, response);
            } else {
                System.out.println("请联系管理员!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求的功能不存在!!!!");
        }
    }

    /**
     * 使用该方法进行JSON读取
     */
    public static String getPostJSON(HttpServletRequest request) {
        try {
            //1.从request中获取缓冲输入流对象
            BufferedReader reader = request.getReader();
            //2.创建StringBuffer 存储读取出的数据
            StringBuffer stringBuffer = new StringBuffer();
            //3.循环读取
            String line = null;
            while ((line = reader.readLine()) != null) {
                //将每次读取的数据追加到StringBuffer
                stringBuffer.append(line);
            }
            //4.返回结果
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
