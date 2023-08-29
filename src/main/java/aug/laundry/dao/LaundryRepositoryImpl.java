package aug.laundry.dao;

import aug.laundry.dao.Mapper.LaundryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LaundryRepositoryImpl implements LaundryRepository {

    private final LaundryMapper laundryMapper;

}
