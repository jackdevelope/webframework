package dao;

import pojo.Course;
import pojo.Course_Lesson;
import pojo.Course_Section;

import java.util.List;

/**
 * 课程内容管理 DAO层接口
 */
public interface CourseContentDao {
    //根据课程ID查询课程信息
    public List<Course_Section> findSectionAndLessonByCourseId(int courseId);

    //根据章节ID查询章节相关信息
    public List<Course_Lesson> findLessonBySectionId(int sectionId);

    //根据课程ID回显课程信息
    public Course findcourseByCourseId(int courseId);

    //保存章节信息
    public int saveSection(Course_Section section);

    //修改章节
    public int updateSection(Course_Section section);

    //修改章节状态
    public int updateSectionStatus(int id, int status);

}
