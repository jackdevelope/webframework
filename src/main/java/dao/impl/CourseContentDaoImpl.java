package dao.impl;

import base.DateUtils;
import dao.CourseContentDao;
import dao.CourseDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import pojo.Course;
import pojo.Course_Lesson;
import pojo.Course_Section;
import utils.DruidUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * 课程内容管理 DAO层接口实现类
 */
public class CourseContentDaoImpl implements CourseContentDao {
    //根据课程ID查询课程信息
    //1.创建QueryRunner
    QueryRunner queryRunner = new QueryRunner(DruidUtils.getDataSource());

    @Override
    public List<Course_Section> findSectionAndLessonByCourseId(int courseId) {
        //2.编写SQL
        try {
            String sql = "select id,course_id,section_name,description,order_num,create_time,update_time,status from course_section where course_id = ?";
            List<Course_Section> sectionList = queryRunner.query(sql, new BeanListHandler<Course_Section>(Course_Section.class), courseId);
            //调用id章节查询方法
            for (Course_Section section : sectionList) {
                //调用方法,获取章节对应的课时
                List<Course_Lesson> lessonlist = findLessonBySectionId(section.getId());
                //将课时数据封装到章节对象中
                section.setLessonList(lessonlist);
            }
            return sectionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //根据章节ID查询章节信息
    @Override
    public List<Course_Lesson> findLessonBySectionId(int sectionId) {
        try {
            String sql = "select id,course_id,section_id,theme,duration,is_free,order_num,create_time,update_time,status from course_lesson where section_id = ?";
            List<Course_Lesson> lessonlist = queryRunner.query(sql, new BeanListHandler<Course_Lesson>(Course_Lesson.class), sectionId);
            return lessonlist;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Course findcourseByCourseId(int courseId) {
        try {
            String sql = "select id,course_name from course where id =?";
            Course course = null;
            course = queryRunner.query(sql, new BeanHandler<Course>(Course.class), courseId);
            return course;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //保存章节信息
    @Override
    public int saveSection(Course_Section section) {
        int sign = 0;
        try {
            String sql = "insert into course_section(course_id,section_name,description,order_num,status,create_time,update_time) values(?,?,?,?,?,?,?)";
            Object param[] = {section.getCourse_id(), section.getSection_name(), section.getDescription(), section.getOrderNum(), section.getStatus(), section.getCreate_time(), section.getUpdate_time()};
            sign = queryRunner.update(sql, param);
            return sign;
        } catch (SQLException e) {
            e.printStackTrace();
            return sign;
        }
    }

    //修改章节
    @Override
    public int updateSection(Course_Section section) {
        //设置标志位
        int result = 0;
        try {

            String sql = "update course_section set section_name = ?,description = ?,order_num = ?,update_time = ? where id = ?";
            Object[] param = {section.getSection_name(), section.getDescription(), section.getOrderNum(), section.getUpdate_time(), section.getId()};
            result = queryRunner.update(sql, param);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
    }

    @Override
    public int updateSectionStatus(int id, int status) {
        //设置标志位
        int sign = 0;
        try {
            String sql = "update course_section set status = ?,update_time = ? where id = ?;";
            Object param[] = {status, DateUtils.getDateFormart(), id};
            sign = queryRunner.update(sql, param);
            return sign;
        } catch (SQLException e) {
            e.printStackTrace();
            return sign;
        }
    }
}
