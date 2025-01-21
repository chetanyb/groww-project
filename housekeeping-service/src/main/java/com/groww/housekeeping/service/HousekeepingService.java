package com.groww.housekeeping.service;

import com.groww.housekeeping.model.StockUpdateRequest;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class HousekeepingService {

    @Autowired
    private RestTemplate restTemplate;

    public void processCSV(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Read the header line
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new RuntimeException("CSV file is empty");
            }

            // Map headers to their indices
            Map<String, Integer> headerIndexMap = getHeaderIndexMap(headers);

            String[] line;
            while ((line = reader.readNext()) != null) {
                StockUpdateRequest request = mapCSVLineToStockUpdateRequest(line, headerIndexMap);
                updateStockInService(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    private Map<String, Integer> getHeaderIndexMap(String[] headers) {
        Map<String, Integer> headerIndexMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerIndexMap.put(headers[i], i);
        }
        return headerIndexMap;
    }

    private StockUpdateRequest mapCSVLineToStockUpdateRequest(String[] line, Map<String, Integer> headerIndexMap) {
        try {
            StockUpdateRequest request = new StockUpdateRequest();
            request.setSymbol(getValue(line, headerIndexMap, "TckrSymb"));
            request.setName(getValue(line, headerIndexMap, "FinInstrmNm"));
            request.setOpenPrice(parseDoubleSafely(getValue(line, headerIndexMap, "OpnPric")));
            request.setHighPrice(parseDoubleSafely(getValue(line, headerIndexMap, "HghPric")));
            request.setLowPrice(parseDoubleSafely(getValue(line, headerIndexMap, "LwPric")));
            request.setClosePrice(parseDoubleSafely(getValue(line, headerIndexMap, "ClsPric")));
            request.setLastTradedPrice(parseDoubleSafely(getValue(line, headerIndexMap, "LastPric")));
            request.setVolumeTraded(parseLongSafely(getValue(line, headerIndexMap, "TtlTradgVol")));
            request.setMarketType(getValue(line, headerIndexMap, "Sgmt"));
            return request;
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV line: " + Arrays.toString(line), e);
        }
    }

    private String getValue(String[] line, Map<String, Integer> headerIndexMap, String header) {
        Integer index = headerIndexMap.get(header);
        return (index != null && index < line.length) ? line[index] : null;
    }

    private double parseDoubleSafely(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private long parseLongSafely(String value) {
        try {
            return value != null ? Long.parseLong(value) : 0L;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private void updateStockInService(StockUpdateRequest request) {
        // REST call to Stock Service
        String url = "http://stock-service:8082/stocks";
        restTemplate.postForObject(url, request, String.class);
    }
}
