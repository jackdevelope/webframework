package service.impl;

import base.DateUtils;
import base.StatusCode;
import dao.CourseContentDao;
import dao.impl.CourseContentDaoImpl;
import pojo.Course;
import pojo.Course_Section;
import service.CourseContentService;

import java.util.List;

/**
 * 课程内容管理Service层接口实现
 */
public class CourseContentServiceImpl implements CourseContentService {
    CourseContentDao contentDao = new CourseContentDaoImpl();

    //根据课程ID查询课程内容
    @Override
    public List<Course_Section> findSectionAndLessonByCourseId(int courseId) {
        List<Course_Section> sections = contentDao.findSectionAndLessonByCourseId(courseId);
        return sections;
    }

    //根据ID查询课程信息
    @Override
    public Course findCourseByCourseId(int courseId) {
        Course course = contentDao.findcourseByCourseId(courseId);
        return course;
    }

    //保存章节信息
    @Override
    public String saveSection(Course_Section section) {
        int sign = 0;
        String result = null;
        //1.补全章节信息
        section.setStatus(2);//状态：0.隐藏，1.待更新,2.发布
        String date = DateUtils.getDateFormart();
        section.setCreate_time(date);
        section.setUpdate_time(date);
        //2.调用dao
        sign = contentDao.saveSection(section);
        //根据是否成功插入,来封装Bean信息
        if (sign > 0) {
            result = StatusCode.SUCCESS.toString();
            return result;
        } else {
            result = StatusCode.FAIL.toString();
            return result;
        }
    }

    //修改章节
    @Override
    public String updateSection(Course_Section section) {
        //0.设置标志器,结果器
        int sign = 0;
        String result = null;
        //1.修改内容
        String date = DateUtils.getDateFormart();
        section.setUpdate_time(date);
        //2.调用Dao
        sign = contentDao.updateSection(section);
        //判断是否进行插入
        if (sign > 0) {
            result = StatusCode.SUCCESS.toString();
            return result;
        } else {
            result = StatusCode.FAIL.toString();
            return result;
        }
    }

    //修改章节状态
    @Override
    public String updateSectionStatus(int id, int status) {
        //存储标志值,结果集
        int sign = 0;
        String result = null;
        sign = contentDao.updateSectionStatus(id, status);
        //判断是否修改成功
        if (sign > 0) {
            result = StatusCode.SUCCESS.toString();
            return result;
        } else {
            result = StatusCode.FAIL.toString();
            return result;
        }
    }

}
