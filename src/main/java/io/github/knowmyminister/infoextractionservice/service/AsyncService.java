package io.github.knowmyminister.infoextractionservice.service;

import io.github.knowmyminister.infoextractionservice.domain.Minister;
import io.github.knowmyminister.infoextractionservice.util.ExcelDataReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class AsyncService {

    Logger log = LoggerFactory.getLogger(this.getClass()
            .getName());

    @Async
    public Future<List<Minister>> process(String fileName) throws InterruptedException
    {
        log.info("###Start Processing with Thread id: " + Thread.currentThread()
                .getId());
        List<Minister> ministers = null;
        try
        {
            ministers = ExcelDataReader.read(fileName);
        }
        catch (IOException | InvalidFormatException e)
        {
            throw new InterruptedException(e.getMessage());
        }
        return new AsyncResult<>(ministers);
    }
}