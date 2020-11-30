package web.servlet;

import base.Constants;
import base.DateUtils;
import base.UUIDUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import pojo.Course;
import service.CourseService;
import service.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/courseSalesInfo")
public class CourseSalesInfoServlet extends HttpServlet {
    /**
     * 保存课程营销信息
     * 收集表单数据,封装到course对象中,将图片上传到tomact服务器中
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //1.创建course对象
            Course course = new Course();
            //2.创建Map用来收集对象
            Map<String, Object> map = new HashMap<>();
            //3.创建文件磁盘对象工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //4.创建文件上传核心类
            ServletFileUpload servletFileUpload = new ServletFileUpload();
            //5.解析request,获取表单项集合
            List<FileItem> list = servletFileUpload.parseRequest(request);
            if (list != null) {
                //遍历集合,并判断哪个是普通表单项哪个是文件表单项
                for (FileItem fileItem : list) {
                    //判断当前表单项是否是普通表单
                    boolean formField = fileItem.isFormField();
                    if (formField) {
                        //普通表单项
                        String fieldName = fileItem.getFieldName();
                        String value = fileItem.getString("UTF-8");//设置编码
                        System.out.println("Filename:" + fieldName);
                        //存入Map
                        map.put(fieldName, value);
                    } else {
                        //文件上传项
                        //获取文件夹名
                        String name = fileItem.getName();
                        //使用新的文件名,保证UUID不重复
                        String newfilename = UUIDUtils.getUUID() + "_" + name;
                        //获取输入流
                        InputStream inputStream = fileItem.getInputStream();
                        //获取webapp目录路径
                        String realPath = this.getServletContext().getRealPath("/");
                        String webapppath = realPath.substring(0, realPath.indexOf("webframe_war"));
                        //创建输出流
                        FileOutputStream fileOutputStream = new FileOutputStream(webapppath + "/upload/" + newfilename);
                        //完成最终传输
                        IOUtils.copy(inputStream, fileOutputStream);
                        //关闭流
                        fileOutputStream.close();
                        inputStream.close();
                        //保存图片路径
                        map.put("couse_img_url", Constants.LOCAL_URL + "/upload/" + newfilename);
                    }
                }
                //使用BeanUtils封装实体类
                BeanUtils.populate(course, map);
                //时间提取
                String dateFormart = DateUtils.getDateFormart();
                //业务处理
                CourseService courseService = new CourseServiceImpl();
                //结果容器
                String result;
                if (map.get("id") != null) {
                    //修改操作
                    //补全信息
                    course.setUpdate_time(dateFormart);
                    result = courseService.updateCourseSalesInfo(course);
                    //响应结果
                    response.getWriter().print(result);
                } else {
                    //新建操作
                    //补全信息
                    map.put("create_time", dateFormart);//创建时间
                    map.put("update_time", dateFormart);//修改时间
                    course.setStatus(1);//上架
                    result = courseService.saveCurseSalesInfo(course);
                    //响应结果
                    response.getWriter().print(result);
                }
            } else {
                System.out.println("请联系管理员!!!!");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}