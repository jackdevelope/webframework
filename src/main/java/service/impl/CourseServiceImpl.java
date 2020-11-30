package service.impl;

import base.DateUtils;
import base.StatusCode;
import dao.CourseDao;
import dao.impl.CourseDaoImpl;
import pojo.Course;
import service.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程管理service实现
 */
public class CourseServiceImpl implements CourseService {
    CourseDao courseDao = new CourseDaoImpl();

    //查询所有课程
    @Override
    public List<Course> findCourselist() {
        List<Course> courselist = courseDao.findCourselist();
        return courselist;
    }

    //查询有条件的课程
    @Override
    public Course findByCourseNameAndStatus(String course_name, String status) {
        Course byCourseNameAndStatus = courseDao.findByCourseNameAndStatus(course_name, status);
        return byCourseNameAndStatus;
    }

    //新建课程
    @Override
    public String saveCurseSalesInfo(Course course) {
        int result = 0;
        String aim = null;
        //1.信息插入
        String dateFormart = DateUtils.getDateFormart();
        course.setCreate_time(dateFormart);
        course.setUpdate_time(dateFormart);
        course.setStatus(1);
        //2.执行插入操作
        result = courseDao.SaveCourseSalesInfo(course);
        if (result > 0) {
            aim = StatusCode.SUCCESS.toString();
        } else {
            aim = StatusCode.FAIL.toString();
        }
        return aim;
    }

    //通过ID查找课程
    @Override
    public Course findCourseById(int id) {
        return courseDao.findCourseById(id);
    }

    //修改课程并保存
    @Override
    public String updateCourseSalesInfo(Course course) {
        //获得后台修改标志位
        int row = courseDao.updateCourseSalesInfo(course);
        //创建标准位容器
        String result;
        if (row > 0) {
            //保存成功
            result = StatusCode.SUCCESS.toString();
            return result;
        } else {
            //保存失败
            result = StatusCode.FAIL.toString();
            return result;
        }
    }

    //修改课程状态
    @Override
    public Map<String, String> updateCourseStatus(Course course) {
        //储存标志位
        int sign = 0;
        //储存状态存储器
        Map<String, String> statusmap = new HashMap<>();
        sign = courseDao.updateCourseStatus(course);
        if (sign > 0) {
            statusmap.put("status", "Edit successful");
        } else {
            statusmap.put("status", "Edit fail");
        }
        return statusmap;
    }
}
