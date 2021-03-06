package com.roman_musijowski.pgs_lessons.controllers;

import com.roman_musijowski.pgs_lessons.converters.LessoneToLessonForm;
import com.roman_musijowski.pgs_lessons.forms.LessonForm;
import com.roman_musijowski.pgs_lessons.forms.validators.LessonFormValidator;
import com.roman_musijowski.pgs_lessons.models.Lesson;
import com.roman_musijowski.pgs_lessons.services.LessonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class LessonControllerTest {

    @Mock
    LessonService lessonService;
    @InjectMocks
    LessonController controller;

    MockMvc mockMvc;

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new LessonController(new LessonFormValidator(),
                lessonService, new LessoneToLessonForm());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

        @Test
    void listLessons() throws Exception {

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson());
        lessons.add(new Lesson());

        //specific Mockito interaction, tell stub to return list of products
        when(lessonService.listAll()).thenReturn((List)lessons); //need to strip generics to keep Mockito happy.

        mockMvc.perform(get("/lesson/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/list"))
                .andExpect(model().attribute("lessons",hasSize(2)));
    }

    @Test
    void showLesson() throws Exception {
        Long id = 1L;

        when(lessonService.getById(id)).thenReturn(new Lesson());

        mockMvc.perform(get("/lesson/show/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/show"))
                .andExpect(model().attribute("lesson", instanceOf(Lesson.class)));
    }

    @Test
    void delete() throws Exception {
        Long id = 1L;
        String title = "Test title";
        String description = "Test description";
        String teacherInfo = "Test teacherInfo";
        LocalDateTime localDateTime = LocalDateTime.of(2025,05,15,18,30);


        Lesson returnLesson = new Lesson();
        returnLesson.setLessonId(id);
        returnLesson.setDescription(description);
        returnLesson.setTitle(title);
        returnLesson.setTeacherInfo(teacherInfo);
        returnLesson.setDate(localDateTime);

        when(lessonService.getById(id)).thenReturn(returnLesson);

        mockMvc.perform(get("/lesson/delete/1")
                .param("lesson_id", "1")
                .param("description", description)
                .param("title", title)
                .param("teacherInfo", teacherInfo)
                .param("date", "15-05-2025 18:30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson/list"));

        verify(lessonService, times(1)).getById(id);
        verify(lessonService, times(1)).deleteById(id);
    }

    @Test
    void newLesson() throws Exception {
        Long id = 1L;

        verifyZeroInteractions(lessonService);

        mockMvc.perform(get("/lesson/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/lessonForm"))
                .andExpect(model().attribute("lessonForm", instanceOf(LessonForm.class)));
    }

    @Test
    void saveOrUpdate() throws Exception {
        Long id = 1L;
        String title = "Test title";
        String description = "Test description";
        String teacherInfo = "Test teacherInfo";
        LocalDateTime localDateTime = LocalDateTime.of(2025,05,15,18,30);


        Lesson returnLesson = new Lesson();
        returnLesson.setLessonId(id);
        returnLesson.setDescription(description);
        returnLesson.setTitle(title);
        returnLesson.setTeacherInfo(teacherInfo);
        returnLesson.setDate(localDateTime);



        when(lessonService.saveOrUpdateLessonForm(any(LessonForm.class))).thenReturn(returnLesson);

        mockMvc.perform(post("/lesson")
                .param("lesson_id", "1")
                .param("description", description)
                .param("title", title)
                .param("teacherInfo", teacherInfo)
                .param("date", "15-05-2025 18:30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:lesson/show/1"));


        //verify properties of bound object
        ArgumentCaptor<LessonForm> boundLesson = ArgumentCaptor.forClass(LessonForm.class);
        verify(lessonService).saveOrUpdateLessonForm(boundLesson.capture());

        assertEquals(id, boundLesson.getValue().getLessonId());
        assertEquals(description, boundLesson.getValue().getDescription());
        assertEquals(title, boundLesson.getValue().getTitle());
        assertEquals(teacherInfo, boundLesson.getValue().getTeacherInfo());
        assertEquals(localDateTime, boundLesson.getValue().getDate());
    }

    @Test
    void edit() throws Exception {
        Long id = 1L;

        //Tell Mockito stub to return new product for ID 1
        when(lessonService.getById(id)).thenReturn(new Lesson());

        mockMvc.perform(get("/lesson/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lesson/lessonForm"))
                .andExpect(model().attribute("lessonForm", instanceOf(LessonForm.class)));
    }
}