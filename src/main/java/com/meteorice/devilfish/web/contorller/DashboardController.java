package com.meteorice.devilfish.web.contorller;


import com.meteorice.devilfish.pojo.CommResult;
import com.meteorice.devilfish.service.HostService;
import com.meteorice.devilfish.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    HostService hostService;

    @GetMapping(value = "getLog")
    public CommResult getLog() throws IOException {
        String fileName = "/Users/shish/work/githup/devilfish/src/main/java/com/meteorice/devilfish/web/contorller/data.json";
        StringBuffer sb = new StringBuffer(2048);
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(str -> sb.append(str));
        } catch (IOException e) {
            e.printStackTrace();

        }
        logger.debug("{}", sb.length());
        List list = JsonUtil.getJsonMapper().readValue(sb.toString(), List.class);

        return CommResult.SUCCESS(list);
    }
}
