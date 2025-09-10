package tg.academia.school.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tg.academia.school.repository.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @Test
    void run_CreatesAllSampleData() throws Exception {
        when(teacherRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(schoolClassRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(gradeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(attendanceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        dataLoader.run();

        verify(teacherRepository, times(2)).save(any());
        verify(schoolClassRepository, times(2)).save(any());
        verify(studentRepository, times(2)).save(any());
        verify(gradeRepository, times(2)).save(any());
        verify(attendanceRepository, times(2)).save(any());
    }
}