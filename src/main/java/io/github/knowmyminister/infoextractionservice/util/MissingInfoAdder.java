package io.github.knowmyminister.infoextractionservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.knowmyminister.infoextractionservice.domain.Minister;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissingInfoAdder {
    public static void main(String[] args) throws IOException {
        List<Minister> ministers = new ObjectMapper().readValue(new File("ministersData.json"), new TypeReference<List<Minister>>() {
        });
        int idCount = 113;
        int profileUrlCount = 101;
        List<Minister> newList = new ArrayList<>(ministers.size());
        String replaced = null;
        for (Minister minister : ministers) {
            String fullName = minister.getFullName();
            String temp1 = String.valueOf(profileUrlCount++);
            if (Objects.nonNull(fullName)) {
                replaced = fullName.trim().replace(" ", "");
                minister.setProfileUrl("http://localhost:1337/assets/images/" + replaced + "_" + temp1 + ".jpg");
            }
            if (Objects.isNull(minister.getId())) {
                int temp2 = idCount++;
                minister.setId(String.valueOf(temp2));
            }
            if (!deleteMinisterIfNull(minister)) {
                if (Objects.isNull(minister.getPartyImageUrl())) {
                    minister.setPartyImageUrl("http://localhost:1337/assets/images/" + "{}" + ".jpg");
                }
                if (Objects.nonNull(minister.getHeight()) && minister.getHeight().equals("0")) {
                    minister.setHeight(null);
                }
                minister.setConstituency(null);
                minister.setAddress(null);
                newList.add(minister);
            }
        }
        /*for (Minister minister : newList) {
            System.out.println(minister.getId());
            System.out.println(minister.getProfileUrl());
            System.out.println(minister.getPartyImageUrl());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
        }
        System.out.println("Old List = " + ministers.size());
        System.out.println("New List = " + newList.size());*/
        new ObjectMapper().writeValue(new File("ministersData_Filtered.json"), newList);
    }

    private static boolean deleteMinisterIfNull(Minister minister) {
        if (Objects.isNull(minister.getBrief())
                && Objects.isNull(minister.getFullName())
                && Objects.isNull(minister.getTwitterUrl())
                && Objects.isNull(minister.getBorn())
                && Objects.isNull(minister.getCurrentDesignation())
                && Objects.isNull(minister.getFacebookUrl())
                && Objects.isNull(minister.getGooglePlusUrl())
                && Objects.isNull(minister.getInstagramUrl())
                && Objects.isNull(minister.getWikipediaUrl())
                && Objects.isNull(minister.getSpouse())
                && Objects.isNull(minister.getLinkedInUrl())
                && Objects.isNull(minister.getParty())
                && Objects.isNull(minister.getProfileUrl())
                && Objects.isNull(minister.getYoutubeUrl())
                && Objects.isNull(minister.getAddress())
                && Objects.isNull(minister.getConstituency())
                && Objects.isNull(minister.getEducation())
                && Objects.isNull(minister.getOfficialSite())
                && Objects.isNull(minister.getParents())
                && Objects.isNull(minister.getPartyImageUrl())
                && Objects.isNull(minister.getSalutation())
                && Objects.isNull(minister.getSpeechUrl())) {
            return true;
        }
        return false;
    }
}
