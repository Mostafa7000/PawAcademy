package pawacademy.solution.unit.application;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pawacademy.solution.unit.application.dto.UnitDto;
import pawacademy.solution.unit.domain.Unit;
import pawacademy.solution.unit.domain.UnitRepository;
import pawacademy.solution.user.application.authentication.CurrentUser;
import pawacademy.solution.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitService {
    @Autowired
    UnitRepository unitRepository;

    public List<UnitDto> getUnits(@CurrentUser User user) {
        List<UnitDto> result = new ArrayList<>();
        for (Unit unit : unitRepository.findAll()) {
            UnitDto unitDto = new UnitDto();
            ModelMapper mapper = new ModelMapper();
            mapper.map(unit, unitDto);

            result.add(unitDto);
        }
        return result;
    }
}

