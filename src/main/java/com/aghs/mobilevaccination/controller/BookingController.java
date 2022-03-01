package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.SpotDto;
import com.aghs.mobilevaccination.data.model.Member;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.vaccine.MemberVaccination;
import com.aghs.mobilevaccination.data.repository.MemberRepository;
import com.aghs.mobilevaccination.data.repository.vaccine.MemberVaccinationRepository;
import com.aghs.mobilevaccination.service.GeneralUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController extends DefaultController{
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberVaccinationRepository memberVaccinationRepository;
    @Autowired
    GeneralUserDetailService userService;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/user/member/show-slot")
    public String getSlotBookingPage(@ModelAttribute("userId") Long userId, Model model) {
        model.addAttribute("states", stateRepository.findAll());
        LocalDate today = LocalDate.now();
        model.addAttribute("selectedDate", today);
        model.addAttribute("remainingSlots", "N/A");
        return "book-slot-by-city";
    }

    @PostMapping("/user/member/book-slot")
    public String bookSlot(@ModelAttribute("userId") Long userId,
                           @ModelAttribute("selectedDate") String selectedDateString,
                           @ModelAttribute("spotDto") SpotDto spotDto,
                           Model model) {
        City city = new City();
        List<Spot> spots = new ArrayList<>();
        Member currentMember = memberRepository.findById(userId).orElse(null);
        LocalDate selectedDate = LocalDate.parse(selectedDateString, dateFormatter);
        List<String> messages = new ArrayList<>();
        model.addAttribute("messages", messages);
        model.addAttribute("remainingSlots", "Please select a city");
        Spot selectedSpot = getSpotWithSelectedCityAndItsSpots(spotDto, model, city, spots);
        // data checking
        if(spotDto.getCityId() != null) {
            Long bookedSlot = City.getRemainingSlots(memberVaccinationRepository, selectedDate, spots);
            Long remainingSlots = city.getAllotedSlotsPerDay() - bookedSlot;
            model.addAttribute("remainingSlots", remainingSlots);
            if(remainingSlots <= 0)
                messages.add("All slots for selected city.");
            else if(currentMember == null)
                messages.add("No such Member exist: " + String.valueOf(userId));
            else if (selectedSpot != null) {
                MemberVaccination memberVaccination = new MemberVaccination();
                memberVaccination.setSelectedDate(selectedDate);
                memberVaccination.setVaccinationSpot(selectedSpot);
                memberVaccination.setRegisteredAt(new Date());
                memberVaccination.setRecipient(currentMember);
                memberVaccinationRepository.save(memberVaccination);
                messages.add("Vaccination Slot Booked");
            }
        }
        return "book-slot-by-city";
    }

    @ModelAttribute("spotDto")
    public SpotDto getDefaultSpotDto() {
        return new SpotDto();
    }
}
