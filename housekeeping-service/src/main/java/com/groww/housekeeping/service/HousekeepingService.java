package com.groww.housekeeping.service;

import com.groww.housekeeping.model.StockUpdateRequest;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.Arrays;

@Service
public class HousekeepingService {

    @Autowired
    private RestTemplate restTemplate;

    public void processCSV(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                StockUpdateRequest request = mapCSVLineToStockUpdateRequest(line);
                updateStockInService(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    private StockUpdateRequest mapCSVLineToStockUpdateRequest(String[] line) {
        try {
            StockUpdateRequest request = new StockUpdateRequest();
            request.setSymbol(line[7]);
            request.setName(line[14]);
            request.setOpenPrice(parseDoubleSafely(line[15]));
            request.setHighPrice(parseDoubleSafely(line[16]));
            request.setLowPrice(parseDoubleSafely(line[17]));
            request.setClosePrice(parseDoubleSafely(line[18]));
            request.setLastTradedPrice(parseDoubleSafely(line[19]));
            request.setVolumeTraded(parseLongSafely(line[25]));
            request.setMarketType(line[2]);
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV line: " + Arrays.toString(line), e);
        }
    }

    private double parseDoubleSafely(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0; // Default value for invalid numbers
        }
    }

    private long parseLongSafely(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L; // Default value for invalid numbers
        }
    }


    private void updateStockInService(StockUpdateRequest request) {
        // REST call to Stock Service
        String url = "http://stock-service:8082/stocks";
        restTemplate.postForObject(url, request, String.class);
    }
}

