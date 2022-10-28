package ru.practicum.ewm.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.CommonMethods;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationStorage storage;
    private final CommonMethods commonMethods;

    @Autowired
    public CompilationServiceImpl(CompilationStorage storage, CommonMethods commonMethods) {
        this.storage = storage;
        this.commonMethods = commonMethods;
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = storage.findAllCompilations(pinned, pageable);
        } else {
            compilations = storage.findAllCompilations(pageable);
        }
        log.info("Найдены все подборки");
        return compilations;
    }

    @Override
    public Compilation getCompilation(long compId) {
        Compilation compilation = commonMethods.checkExistCompilation(compId);
        log.info("Подборка событий с id = " + compId + " найдена");
        return compilation;
    }

}
