package web.servlet;

import base.BaseServlet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.apache.commons.beanutils.BeanUtils;
import pojo.Course;
import pojo.Course_Section;
import service.CourseContentService;
import service.impl.CourseContentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/courseContent")
public class CourseContentServlet extends BaseServlet {
    CourseContentServiceImpl contentService = new CourseContentServiceImpl();

    //展示对应章节与课程信息
    public void findSectionAndLessonByCourseId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        String course_id = request.getParameter("course_id");
        //业务处理
        List<Course_Section> list = contentService.findSectionAndLessonByCourseId(Integer.parseInt(course_id));
        String result = JSON.toJSONString(list);
        response.getWriter().print(result);
    }

    //根据课程ID 回显课程信息
    public void findCourseById(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取参数
            String course_id = request.getParameter("course_id");
            //2.业务处理
            Course course = contentService.findCourseByCourseId(Integer.parseInt(course_id));
            //3.返回JSON数据
            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Course.class, "id", "course_name");
            String result = JSON.toJSONString(course, filter);
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存&修改 章节信息
     */
    public void saveOrUpdateSection(HttpServletRequest request, HttpServletResponse response) {
        try {
            //0.设置结果存储器
            String result = null;
            //1.获取参数 从域对象中获取
            Map<String, Object> map = (Map) request.getAttribute("resultmap");
            //2.创建Course_Section
            Course_Section section = new Course_Section();
            //3.使用BeanUtils,将map中的数据封装到sectiom
            BeanUtils.populate(section, map);
            //4.业务处理
            //判断是否携带ID
            if (section.getId() == 0) {
                //新增模块
                result = contentService.saveSection(section);
                response.getWriter().print(result);
            } else {
                //修改操作
                result = contentService.updateSection(section);
                response.getWriter().print(result);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改章节状态
    public void updateSectionStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            //0.设置结果存储器
            String result = null;
            //1.获取参数
            int id = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            //2.业务处理
            result = contentService.updateSectionStatus(id, status);
            //3.返回结果
            response.getWriter().print(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
