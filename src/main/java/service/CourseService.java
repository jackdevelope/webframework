package service;

import pojo.Course;

import java.util.List;
import java.util.Map;

/**
 * 课程管理service实现类
 */
public interface CourseService {
    //查询所有课程
    public List<Course> findCourselist();

    //查询有条件的课程
    public Course findByCourseNameAndStatus(String courseName, String status);

    //新建课程
    public String saveCurseSalesInfo(Course course);

    //通过ID查找课程
    public Course findCourseById(int id);

    //修改课程并保存
    public String updateCourseSalesInfo(Course course);

    //修改课程状态
    public Map<String, String> updateCourseStatus(Course course);

}
