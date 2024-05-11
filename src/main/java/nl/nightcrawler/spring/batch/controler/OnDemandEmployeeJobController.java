package nl.nightcrawler.spring.batch.controler;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.nightcrawler.spring.batch.service.BatchJobService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;

@RestController
@Slf4j
@RequestMapping("/api/batch/bulk")
public class OnDemandEmployeeJobController {

    @Value("${inputFile.base.location}")
    private String inputFileBaseLocation;

    private final BatchJobService batchJobService;

    public OnDemandEmployeeJobController(BatchJobService batchJobService) {
        this.batchJobService = batchJobService;
    }

    @GetMapping("/runJob/{filename}")
    @SneakyThrows
    public BatchStatus load(@PathVariable(value = "filename") final String filename) {
        var inputFile = Paths.get(inputFileBaseLocation, filename);
        return batchJobService.run(inputFile.toAbsolutePath().toString()).getStatus();
    }


}
