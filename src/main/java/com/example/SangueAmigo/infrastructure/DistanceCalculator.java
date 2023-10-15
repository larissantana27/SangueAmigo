package com.example.SangueAmigo.infrastructure;

import com.example.SangueAmigo.model.location.Coordinates;
import com.example.SangueAmigo.model.user.AddressInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DistanceCalculator {

    private final Logger logger = LoggerFactory.getLogger(DistanceCalculator.class);

    public double getDistance(AddressInformation userAddressInfo, AddressInformation bloodCenterAddressInfo) {
        try {
            logger.info("-Coordinates-");
            String userCoordinates = getCoordinatesByCep(userAddressInfo);
            logger.info("User Coordinates: {}", userCoordinates);
            String bloodCenterCoordinates = getCoordinatesByCep(bloodCenterAddressInfo);
            logger.info("BloodCenter Coordinates: {}", bloodCenterCoordinates);

            double distance = calculateDistance(userCoordinates, bloodCenterCoordinates);
            logger.info("CEPs Distance: {} Km", distance);
            logger.info("------------");

            return distance;
        } catch (Exception e) {
            logger.info("Catch this exception: ", e);
        }
        return 0;
    }

    private String getCoordinatesByCep(AddressInformation addressInfo) throws Exception {
        String address = addressInfo.getStreet() + "," + addressInfo.getNumber() + "," +
                addressInfo.getCity() + "," + addressInfo.getState() + ",Brazil";
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

        BufferedReader reader = getBufferedReader(encodedAddress, addressInfo.getId());
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    private BufferedReader getBufferedReader(String encodedAddress, int addressInfoId) throws IOException {
        String apiUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;
        logger.info("   {} - Api Url: {}", addressInfoId, apiUrl);

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private double calculateDistance(String coordinatesStringA, String coordinatesStringB) {
        Coordinates coordinatesA = extractCoordinates(coordinatesStringA);
        logger.info("coordinatesA: {}", coordinatesA);
        Coordinates coordinatesB = extractCoordinates(coordinatesStringB);
        logger.info("coordinatesB: {}", coordinatesB);

        return haversine(coordinatesA, coordinatesB);
    }

    private static Coordinates extractCoordinates(String input) {
        Coordinates coordinates = new Coordinates();

        String regex = "\"boundingbox\":\\[\"(-?\\d+\\.\\d+)\",\"(-?\\d+\\.\\d+)\",\"(-?\\d+\\.\\d+)\",\"(-?\\d+\\.\\d+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            double lat1 = Double.parseDouble(matcher.group(1));
            double lat2 = Double.parseDouble(matcher.group(2));
            double lon1 = Double.parseDouble(matcher.group(3));
            double lon2 = Double.parseDouble(matcher.group(4));

            List<Double> latitude = List.of(lat1, lat2);
            List<Double> longitude = List.of(lon1, lon2);

            coordinates = new Coordinates(latitude, longitude);
        }

        return coordinates;
    }

    public double haversine(Coordinates coordinateA, Coordinates coordinateB) {
        Double lat1 = Math.toRadians(coordinateA.getLatitude().get(0));
        Double lon1 = Math.toRadians(coordinateA.getLongitude().get(0));
        Double lat2 = Math.toRadians(coordinateB.getLatitude().get(0));
        Double lon2 = Math.toRadians(coordinateB.getLongitude().get(0));

        double distance = formula(lat1, lon1, lat2, lon2);

        return roundedValue(distance);
    }

    public double formula(Double lat1, Double lon1, Double lat2, Double lon2) {
        final double EARTH_RADIUS = 6371.0;

        double dLat = latitudeDistance(lat1, lat2);
        double dLon = longitudeDistance(lon1, lon2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    public double latitudeDistance(Double lat1, Double lat2) {
        return lat2 - lat1;
    }

    public double longitudeDistance(Double lon1, Double lon2) {
        return lon2 - lon1;
    }

    public double roundedValue(Double value) {
        BigDecimal roundedDistance = new BigDecimal(value).setScale(2, RoundingMode.UP);
        return roundedDistance.doubleValue();
    }
}