package com.teacher.config;

import com.teacher.entity.ArchiveType;
import com.teacher.entity.TeacherArchive;
import com.teacher.entity.User;
import com.teacher.mapper.ArchiveTypeMapper;
import com.teacher.mapper.TeacherArchiveMapper;
import com.teacher.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据初始化 - 创建默认管理员、测试教师和档案数据（如不存在）
 */
@Component
public class DataInit implements CommandLineRunner {

    private final UserMapper userMapper;
    private final TeacherArchiveMapper archiveMapper;
    private final ArchiveTypeMapper typeMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInit(UserMapper userMapper, TeacherArchiveMapper archiveMapper, ArchiveTypeMapper typeMapper) {
        this.userMapper = userMapper;
        this.archiveMapper = archiveMapper;
        this.typeMapper = typeMapper;
    }

    @Override
    public void run(String... args) {
        if (userMapper.selectCount(null) > 0) return;

        // 管理员 admin/123456，dept_id=1（计算机学院）
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setName("系统管理员");
        admin.setGender(1);
        admin.setPhone("13800000000");
        admin.setEmail("admin@school.edu.cn");
        admin.setRole("admin");
        admin.setDeptId(1L);
        userMapper.insert(admin);

        // 教师 teacher001-003/123456
        for (int i = 1; i <= 3; i++) {
            User teacher = new User();
            teacher.setUsername("teacher00" + i);
            teacher.setPassword(passwordEncoder.encode("123456"));
            teacher.setName(List.of("张伟", "李芳", "王强").get(i - 1));
            teacher.setGender(i == 2 ? 2 : 1);
            teacher.setPhone("1380013800" + i);
            teacher.setEmail(List.of("zhangwei", "lifang", "wangqiang").get(i - 1) + "@school.edu.cn");
            teacher.setRole("teacher");
            teacher.setDeptId(1L);
            userMapper.insert(teacher);
        }

        // 档案类型 type_id: research=1, teaching=2, award=3, training=4, promotion=5
        List<Object[]> archives = List.of(
                new Object[]{2L, 1L, "基于深度学习的图像识别研究", "发表SCI论文一篇，影响因子3.5", 2023},
                new Object[]{2L, 2L, "计算机组成原理课程改革", "获得校级教学成果二等奖", 2023},
                new Object[]{2L, 3L, "优秀教师称号", "2023年度校级优秀教师", 2023},
                new Object[]{3L, 2L, "高等数学教学方法创新", "学生满意度提升20%", 2023},
                new Object[]{3L, 4L, "教师培训结业证书", "参加教育部骨干教师培训", 2023},
                new Object[]{4L, 5L, "副教授职称晋升", "2023年通过副教授评审", 2023}
        );
        for (Object[] a : archives) {
            TeacherArchive archive = new TeacherArchive();
            archive.setTeacherId((Long) a[0]);
            archive.setTypeId((Long) a[1]);
            archive.setTitle((String) a[2]);
            archive.setDescription((String) a[3]);
            archive.setYear((Integer) a[4]);
            archive.setStatus(1);
            archiveMapper.insert(archive);
        }
    }
}
