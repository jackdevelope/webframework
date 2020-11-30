package dao.impl;


import dao.CourseDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import pojo.Course;
import utils.DruidUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 课程模块DAO层实现
 */
public class CourseDaoImpl implements CourseDao {
    //创建公有QueryRunner
    QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());

    //查询全部课程列表信息
    @Override
    public List<Course> findCourselist() {
        try {
            //编写sql
            String sql = "select id,course_name,price,sort_num,status from course where is_del=?";
            //在SQL中进行参数预处理之后,将结果集中的数据封装到Javabean
            List<Course> result = queryRunner.query(sql, new BeanListHandler<Course>(Course.class), 0);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据条件查询课程信息
    @Override
    public Course findByCourseNameAndStatus(String courseName, String status) {
        try {
            //使用sql存储容器方便动态拼接
            StringBuffer stringBuffer = new StringBuffer("select id,course_name,price,sort_num,status from course where 1=1 and is_del=?");
            //创建list集合
            List<Object> list = new ArrayList<>();
            list.add(0);

            //判断条件再决定是否拼接字符串
            if (courseName != null && courseName != "") {
                stringBuffer.append(" AND course_name LIKE ?");
                //like查询,需要拼接%
                courseName = "%" + courseName + "%";
                list.add(courseName);
            }
            if (status != null && status != "") {
                stringBuffer.append("AND STATUS=?");
                int stat = Integer.parseInt(status);
                list.add(stat);
            }
            //执行查询语句

            Course courseList = queryRunner.query(stringBuffer.toString(), new BeanHandler<Course>(Course.class), list.toArray());

            return courseList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //新建课程
    @Override
    public int SaveCourseSalesInfo(Course course) {
        int sign = 0;
        try {
            //编写sql
            String sql = "insert into course(couse_name,brief,teacher_name,teacher_info,preview_first_field,preview_second_field,discount,price,price_tag,share_image_title,share_title,share_description,course_description,course_img_url,status,create_time,update_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            //准备参数
            Object[] param = {course.getCourse_name(), course.getBrief(), course.getTeacher_name(), course.getTeacher_info(), course.getPreview_first_field(), course.getPreview_second_field(), course.getDiscounts(), course.getPrice(), course.getPrice_tag(), course.getShare_image_title(), course.getShare_title(), course.getShare_description(), course.getCourse_description(), course.getCourse_img_url(), course.getStatus(), course.getCreate_time(), course.getUpdate_time()};
            //执行插入操作
            sign = queryRunner.update(sql, param);
            return sign;
        } catch (SQLException e) {
            e.printStackTrace();
            return sign;
        }
    }

    //根据ID查询课程信息
    public Course findCourseById(int id) {
        try {
            String sql = "select id,course_name,brief,teacher_name,teacher_info,preview_first_field,preview_second_field,discounts,price,price_tag,course_img_url,share_image_title,share_title,share_description,course_description,status from course where id=?";
            Course mycourse = queryRunner.query(sql, new BeanHandler<Course>(Course.class), id);
            return mycourse;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    //修改课程营销信息
    @Override
    public int updateCourseSalesInfo(Course course) {
        try {
            String sql = "update course set course_name=?,brief=?,teacher_name=?,teacher_info=?,preview_first_field=?,preview_second_field=?,discounts=?,price=?,price_tag=?,share_image_title=?,share_title=?,share_description=?,course_description=?,course_img_url=?,update_time=? where id=?";
            Object[] param = {course.getCourse_name(), course.getBrief(), course.getTeacher_name(), course.getTeacher_info(), course.getPreview_first_field(), course.getPreview_second_field(), course.getDiscounts(), course.getPrice(), course.getPrice_tag(), course.getShare_image_title(), course.getShare_title(), course.getShare_description(), course.getCourse_description(), course.getCourse_img_url(), course.getUpdate_time(), course.getId()};
            int result = queryRunner.update(sql, param);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //修改课程状态
    @Override
    public int updateCourseStatus(Course course) {
        //设置标志器
        int sign = 0;
        try {
            //写入SQL语句
            String sql = "update course set status=?,update_time=? where id=?";
            //准备获得参数
            Object[] param = {course.getStatus(), course.getUpdate_time(), course.getId()};
            // System.out.println(course.getStatus()+course.getUpdate_time()+course.getId());
            sign = queryRunner.update(sql, param);
            return sign;
        } catch (SQLException e) {
            e.printStackTrace();
            return sign;
        }
    }

}
