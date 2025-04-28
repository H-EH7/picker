package picker.picker_backend.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import picker.picker_backend.test.model.entity.Test;

@Mapper
public interface TestMapper {
    @Select("SELECT * FROM test WHERE id = #{id}")
    Test findById(Long id);
}
