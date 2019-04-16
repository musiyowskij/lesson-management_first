package com.roman_musijowski.pgs_lessons.converters;

import com.roman_musijowski.pgs_lessons.commands.LessonForm;
import com.roman_musijowski.pgs_lessons.models.Lesson;
import com.roman_musijowski.pgs_lessons.models.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class LessonFormToLesson implements Converter<LessonForm, Lesson> {

    @Override
    public Lesson convert(LessonForm lessonForm) {
        Lesson lesson = new Lesson();
        List<User> users =null;

        lesson.setLesson_id(lessonForm.getLesson_id());
        lesson.setTitle(lessonForm.getTitle());
        lesson.setDescription(lessonForm.getDescription());
        lesson.setTeacherInfo(lessonForm.getTeacherInfo());
        lesson.setUsers(users);

        //copy list of users

        return lesson;
    }
}
