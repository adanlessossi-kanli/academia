package tg.academia.administration.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tg.academia.administration.repository.*;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataLoader dataLoader;

    @Test
    void run_CreatesAllSampleData() throws Exception {
        when(userRepository.count()).thenReturn(0L);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        dataLoader.run();

        verify(userRepository, times(3)).save(any());
    }
}
