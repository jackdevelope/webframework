package dao;

import pojo.Course;

import java.util.List;

/**
 * 课程模块DAO层接口
 */
public interface CourseDao {
    //查询全部课程列表信息
    public List<Course> findCourselist();

    //根据条件查询课程信息
    public Course findByCourseNameAndStatus(String courseName, String status);

    //新建课程
    public int SaveCourseSalesInfo(Course course);

    //根据ID查询课程信息
    public Course findCourseById(int id);

    //修改课程营销信息
    public int updateCourseSalesInfo(Course course);

    //修改课程状态
    public int updateCourseStatus(Course course);
}
