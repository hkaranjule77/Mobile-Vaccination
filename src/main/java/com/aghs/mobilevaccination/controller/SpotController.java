package com.aghs.mobilevaccination.controller;

import com.aghs.mobilevaccination.data.dto.CityDto;
import com.aghs.mobilevaccination.data.dto.PinCodeDto;
import com.aghs.mobilevaccination.data.model.location.City;
import com.aghs.mobilevaccination.data.model.location.District;
import com.aghs.mobilevaccination.data.model.location.Spot;
import com.aghs.mobilevaccination.data.model.location.State;
import com.aghs.mobilevaccination.data.repository.location.CityRepository;
import com.aghs.mobilevaccination.data.repository.location.DistrictRepository;
import com.aghs.mobilevaccination.data.repository.location.SpotRepository;
import com.aghs.mobilevaccination.data.repository.location.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/spot")
public class SpotController {
    private final StateRepository stateRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final SpotRepository spotRepository;

    @Autowired
    public SpotController(StateRepository stateRepository,
                          DistrictRepository districtRepository,
                          CityRepository cityRepository,
                          SpotRepository spotRepository) {
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.cityRepository = cityRepository;
        this.spotRepository = spotRepository;
    }

    @GetMapping("/get-by-city")
    public String getByCity(Model model) {
        model.addAttribute("states", stateRepository.findAll());
        return "slot-by-city";
    }

    @PostMapping("/get-by-city")
    public String postByCity(Model model, @ModelAttribute("cityDto") CityDto cityDto) {
        List<String> messages = new ArrayList<>();
        model.addAttribute("states", stateRepository.findAll());
        model.addAttribute("cityDto", cityDto);
        model.addAttribute("messages", messages);
        System.out.println(cityDto);
        if(cityDto.getStateName() != null) {
            State selectedState = stateRepository.findByName(cityDto.getStateName());
            List<District> districts = districtRepository.findByState(selectedState);
            model.addAttribute("districts", districts);
            if (cityDto.getDistrictId() != null ) {
                District selectedDistrict = districtRepository.findByIdAndState(cityDto.getDistrictId(), selectedState);
                List<City> cities = cityRepository.findByDistrict(selectedDistrict);
                model.addAttribute("cities", cities);
                if (cityDto.getCityId() != null) {
                    City selectedCity = cityRepository.findByIdAndDistrict(cityDto.getCityId(), selectedDistrict);
                    List<Spot> spots = spotRepository.findByCity(selectedCity);
                    model.addAttribute("spots", spots.size()!=0 ? spots : null);
                }
            }
        }
        else {
            messages.add("Please select a state.");
        }
        return "slot-by-city";
    }

    @GetMapping("get-by-pin-code")
    public String getByPinCode(Model model) {
        return "slot-by-pin-code";
    }

    @PostMapping("get-by-pin-code")
    public String postByPinCode(Model model, @ModelAttribute("pinCodeDto") PinCodeDto pinCodeDto) {
        List<Spot> spots = spotRepository.findByPinCode(pinCodeDto.getPinCode());
        model.addAttribute("spots", spots.size()!=0 ? spots : null);
        return "slot-by-pin-code";
    }

    @ModelAttribute("cityDto")
    public CityDto getSpotDTO() {
        return new CityDto();
    }

    @ModelAttribute("pinCodeDto")
    public PinCodeDto getPinCodeDto() {
        return new PinCodeDto();
    }
}
