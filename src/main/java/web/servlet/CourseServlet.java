package web.servlet;

import base.BaseServlet;
import base.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import pojo.Course;
import service.CourseService;
import service.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/course")
public class CourseServlet extends BaseServlet {
    CourseService courseService = new CourseServiceImpl();

    //查询课程信息
    public void findCourseList(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.业务处理
            List<Course> courselist = courseService.findCourselist();
            //2.响应结果(并进行指定字段转换json，防止默认值转换)
            SimplePropertyPreFilter container = new SimplePropertyPreFilter(Course.class, "id", "course_name", "price", "sort_num", "status");
            String result = JSON.toJSONString(courselist, container);
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
            //}
        }
    }

    //根据条件查询信息
    public void findByCourseNameAndStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取参数
            String course_name = request.getParameter("course_name");
            String status = request.getParameter("status");
            Course courselist = courseService.findByCourseNameAndStatus(course_name, status);
            //2.响应结果(并进行指定字段转换json，防止默认值转换)
            SimplePropertyPreFilter container = new SimplePropertyPreFilter();
            container.getIncludes().add("id");
            container.getIncludes().add("course_name");
            container.getIncludes().add("price");
            container.getIncludes().add("sort_num");
            container.getIncludes().add("status");
            String result = JSON.toJSONString(courselist, container);
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据ID查询信息
    public void findCourseById(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.接收参数
            String id = request.getParameter("id");
            //2.业务处理
            Course courseById = courseService.findCourseById(Integer.parseInt(id));
            //3.返回结果
            String result = JSON.toJSONString(courseById);
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //修改课程状态
    public void updateCourseStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取参数
            String id = request.getParameter("id");
            //2.业务处理
            Course course = courseService.findCourseById(Integer.parseInt(id));
            //3.判断课程信息状态
            int status = course.getStatus();
            // System.out.println(status);
            if (status == 0) {
                //如果是0 设置为1
                course.setStatus(1);
            } else {
                //如果是1 设置为0
                course.setStatus(0);
            }
            //4.设置更新时间
            course.setUpdate_time(DateUtils.getDateFormart());
            //6.修改状态
            Map<String, String> newCourseSta = courseService.updateCourseStatus(course);
            //7.响应结果
            String result = JSON.toJSONString(newCourseSta);
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
